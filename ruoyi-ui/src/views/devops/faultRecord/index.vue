<template>
  <div class="app-container ops-page">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="92px" class="ops-query-form">
      <el-form-item label="关联系统" prop="systemId">
        <el-select v-model="queryParams.systemId" class="query-control" placeholder="全部" clearable filterable>
          <el-option v-for="item in systemOptions" :key="item.id" :label="formatSystem(item)" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="故障标题" prop="faultTitle">
        <el-input v-model="queryParams.faultTitle" class="query-control" placeholder="故障标题" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="故障级别" prop="faultLevel">
        <el-select v-model="queryParams.faultLevel" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_fault_level" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="故障状态" prop="faultStatus">
        <el-select v-model="queryParams.faultStatus" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_fault_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="处理人" prop="handlerName">
        <el-input v-model="queryParams.handlerName" class="query-control" placeholder="处理人" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item class="query-actions">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['ops:fault:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['ops:fault:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['ops:fault:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['ops:fault:export']">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="faultRecordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="关联系统" align="left" prop="systemName" min-width="170" show-overflow-tooltip />
      <el-table-column label="故障标题" align="left" prop="faultTitle" min-width="220" show-overflow-tooltip />
      <el-table-column label="故障级别" align="center" prop="faultLevel" width="110">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_fault_level" :value="scope.row.faultLevel" /></template>
      </el-table-column>
      <el-table-column label="故障状态" align="center" prop="faultStatus" width="110">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_fault_status" :value="scope.row.faultStatus" /></template>
      </el-table-column>
      <el-table-column label="发生时间" align="center" prop="occurTime" width="120">
        <template slot-scope="scope">{{ parseTime(scope.row.occurTime, "{y}-{m}-{d}") }}</template>
      </el-table-column>
      <el-table-column label="发现人" align="center" prop="discovererName" width="110" />
      <el-table-column label="处理人" align="center" prop="handlerName" width="110" />
      <el-table-column label="持续时长(分钟)" align="right" prop="durationMinutes" width="140" />
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['ops:fault:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['ops:fault:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="920px" append-to-body class="ops-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="112px">
        <el-divider content-position="left">故障概况</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="关联系统" prop="systemId">
              <el-select v-model="form.systemId" class="full-control" placeholder="请选择关联系统" clearable filterable @change="handleSystemChange">
                <el-option v-for="item in systemOptions" :key="item.id" :label="formatSystem(item)" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="故障标题" prop="faultTitle">
              <el-input v-model="form.faultTitle" placeholder="请输入故障标题" maxlength="100" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="故障级别" prop="faultLevel">
              <el-select v-model="form.faultLevel" class="full-control" placeholder="请选择故障级别">
                <el-option v-for="dict in dict.type.ops_fault_level" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="故障状态" prop="faultStatus">
              <el-select v-model="form.faultStatus" class="full-control" placeholder="请选择故障状态">
                <el-option v-for="dict in dict.type.ops_fault_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发生时间" prop="occurTime">
              <el-date-picker v-model="form.occurTime" class="full-control" clearable type="date" value-format="yyyy-MM-dd" placeholder="请选择发生时间" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="恢复时间" prop="recoverTime">
              <el-date-picker v-model="form.recoverTime" class="full-control" clearable type="date" value-format="yyyy-MM-dd" placeholder="请选择恢复时间" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="故障描述" prop="faultDesc">
              <el-input v-model="form.faultDesc" type="textarea" :rows="3" placeholder="请输入故障描述" maxlength="500" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">人员与处理</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="发现人" prop="discovererId">
              <el-select v-model="form.discovererId" class="full-control" placeholder="请选择发现人" clearable filterable @change="handleDiscovererChange">
                <el-option v-for="item in teamOptions" :key="item.id" :label="item.realName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="处理人" prop="handlerId">
              <el-select v-model="form.handlerId" class="full-control" placeholder="请选择处理人" clearable filterable @change="handleHandlerChange">
                <el-option v-for="item in teamOptions" :key="item.id" :label="item.realName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="持续时长" prop="durationMinutes">
              <el-input-number v-model="form.durationMinutes" class="full-control" :min="0" :max="999999" controls-position="right" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="根因分析" prop="rootCause">
              <el-input v-model="form.rootCause" type="textarea" :rows="3" placeholder="请输入根因分析" maxlength="500" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="解决方案" prop="solution">
              <el-input v-model="form.solution" type="textarea" :rows="3" placeholder="请输入解决方案" maxlength="500" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="预防措施" prop="preventiveMeasures">
              <el-input v-model="form.preventiveMeasures" type="textarea" :rows="3" placeholder="请输入预防措施" maxlength="500" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="影响范围" prop="affectedScope">
              <el-input v-model="form.affectedScope" type="textarea" :rows="3" placeholder="请输入影响范围" maxlength="500" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入补充说明" maxlength="300" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFaultRecord, getFaultRecord, delFaultRecord, addFaultRecord, updateFaultRecord } from "@/api/devops/faultRecord"
import { listOpsSystemAsset } from "@/api/devops/systemAsset"
import { listOpsTeamMember } from "@/api/devops/teamMember"

export default {
  name: "FaultRecord",
  dicts: ["ops_fault_level", "ops_fault_status"],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      faultRecordList: [],
      systemOptions: [],
      teamOptions: [],
      title: "",
      open: false,
      queryParams: { pageNum: 1, pageSize: 10, systemId: null, faultTitle: null, faultLevel: null, faultStatus: null, handlerName: null },
      form: {},
      rules: {
        systemId: [{ required: true, message: "关联系统不能为空", trigger: "change" }],
        faultTitle: [{ required: true, message: "故障标题不能为空", trigger: "blur" }],
        faultLevel: [{ required: true, message: "故障级别不能为空", trigger: "change" }],
        faultStatus: [{ required: true, message: "故障状态不能为空", trigger: "change" }],
        occurTime: [{ required: true, message: "发生时间不能为空", trigger: "change" }]
      }
    }
  },
  created() {
    this.getList()
    this.loadOptions()
  },
  methods: {
    getList() {
      this.loading = true
      listFaultRecord(this.queryParams).then(response => {
        this.faultRecordList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    loadOptions() {
      listOpsSystemAsset({ pageNum: 1, pageSize: 1000 }).then(response => { this.systemOptions = response.rows || [] })
      listOpsTeamMember({ pageNum: 1, pageSize: 1000, isOnJob: "1" }).then(response => { this.teamOptions = response.rows || [] })
    },
    formatSystem(item) {
      return `${item.systemCode || "-"} / ${item.systemName}`
    },
    handleSystemChange(id) {
      const item = this.systemOptions.find(option => option.id === id)
      this.form.systemName = item ? item.systemName : null
    },
    handleDiscovererChange(id) {
      const item = this.teamOptions.find(option => option.id === id)
      this.form.discovererName = item ? item.realName : null
    },
    handleHandlerChange(id) {
      const item = this.teamOptions.find(option => option.id === id)
      this.form.handlerName = item ? item.realName : null
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null, systemId: null, systemName: null, faultTitle: null, faultDesc: null, faultLevel: "1", faultStatus: "1",
        occurTime: null, discovererId: null, discovererName: null, handlerId: null, handlerName: null, recoverTime: null,
        durationMinutes: 0, rootCause: null, solution: null, preventiveMeasures: null, affectedScope: null, remark: null
      }
      this.resetForm("form")
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "新增故障记录"
    },
    handleUpdate(row) {
      this.reset()
      getFaultRecord(row.id || this.ids).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改故障记录"
      })
    },
    submitForm() {
      this.handleSystemChange(this.form.systemId)
      this.handleDiscovererChange(this.form.discovererId)
      this.handleHandlerChange(this.form.handlerId)
      this.$refs["form"].validate(valid => {
        if (!valid) return
        if (this.form.id != null) {
          updateFaultRecord(this.form).then(() => { this.$modal.msgSuccess("修改成功"); this.open = false; this.getList() })
        } else {
          addFaultRecord(this.form).then(() => { this.$modal.msgSuccess("新增成功"); this.open = false; this.getList() })
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm("确认删除选中的故障记录？").then(function() { return delFaultRecord(ids) }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download("ops/fault/export", { ...this.queryParams }, "faultRecord_" + new Date().getTime() + ".xlsx")
    }
  }
}
</script>

<style scoped>
.ops-query-form ::v-deep .el-form-item__label { white-space: nowrap; }
.query-control { width: 210px; }
.query-actions { margin-left: 4px; }
.ops-dialog ::v-deep .el-divider__text { color: #606266; font-weight: 600; }
.full-control { width: 100%; }

.ops-dialog ::v-deep .el-form-item__label {
  white-space: nowrap;
}
</style>
