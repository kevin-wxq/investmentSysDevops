<template>
  <div class="app-container ops-page">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="92px" class="ops-query-form">
      <el-form-item label="关联系统" prop="systemId">
        <el-select v-model="queryParams.systemId" class="query-control" placeholder="全部" clearable filterable>
          <el-option v-for="item in systemOptions" :key="item.id" :label="formatSystem(item)" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="变更类型" prop="changeType">
        <el-select v-model="queryParams.changeType" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_change_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="变更标题" prop="changeTitle">
        <el-input v-model="queryParams.changeTitle" class="query-control" placeholder="变更标题" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="风险等级" prop="riskLevel">
        <el-select v-model="queryParams.riskLevel" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_risk_level" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="变更结果" prop="changeResult">
        <el-select v-model="queryParams.changeResult" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_change_result" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item class="query-actions">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['ops:change:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['ops:change:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['ops:change:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['ops:change:export']">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="changeRecordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="关联系统" align="left" prop="systemId" min-width="180" show-overflow-tooltip>
        <template slot-scope="scope">{{ getSystemName(scope.row.systemId) }}</template>
      </el-table-column>
      <el-table-column label="变更标题" align="left" prop="changeTitle" min-width="220" show-overflow-tooltip />
      <el-table-column label="变更类型" align="center" prop="changeType" width="120">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_change_type" :value="scope.row.changeType" /></template>
      </el-table-column>
      <el-table-column label="风险等级" align="center" prop="riskLevel" width="110">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_risk_level" :value="scope.row.riskLevel" /></template>
      </el-table-column>
      <el-table-column label="变更时间" align="center" prop="changeTime" width="120">
        <template slot-scope="scope">{{ parseTime(scope.row.changeTime, "{y}-{m}-{d}") }}</template>
      </el-table-column>
      <el-table-column label="执行人" align="center" prop="executorId" width="110">
        <template slot-scope="scope">{{ getTeamName(scope.row.executorId) }}</template>
      </el-table-column>
      <el-table-column label="审批人" align="center" prop="approverId" width="110">
        <template slot-scope="scope">{{ getTeamName(scope.row.approverId) }}</template>
      </el-table-column>
      <el-table-column label="变更结果" align="center" prop="changeResult" width="110">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_change_result" :value="scope.row.changeResult" /></template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['ops:change:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['ops:change:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="920px" append-to-body class="ops-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="108px">
        <el-divider content-position="left">变更概况</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="关联系统" prop="systemId">
              <el-select v-model="form.systemId" class="full-control" placeholder="请选择关联系统" clearable filterable>
                <el-option v-for="item in systemOptions" :key="item.id" :label="formatSystem(item)" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="变更类型" prop="changeType">
              <el-select v-model="form.changeType" class="full-control" placeholder="请选择变更类型">
                <el-option v-for="dict in dict.type.ops_change_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="变更标题" prop="changeTitle">
              <el-input v-model="form.changeTitle" placeholder="请输入变更标题" maxlength="100" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="form.riskLevel" class="full-control" placeholder="请选择风险等级">
                <el-option v-for="dict in dict.type.ops_risk_level" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="变更时间" prop="changeTime">
              <el-date-picker v-model="form.changeTime" class="full-control" clearable type="date" value-format="yyyy-MM-dd" placeholder="请选择变更时间" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="变更结果" prop="changeResult">
              <el-select v-model="form.changeResult" class="full-control" placeholder="请选择变更结果">
                <el-option v-for="dict in dict.type.ops_change_result" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">人员与方案</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="执行人" prop="executorId">
              <el-select v-model="form.executorId" class="full-control" placeholder="请选择执行人" clearable filterable>
                <el-option v-for="item in teamOptions" :key="item.id" :label="item.realName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审批人" prop="approverId">
              <el-select v-model="form.approverId" class="full-control" placeholder="请选择审批人" clearable filterable>
                <el-option v-for="item in teamOptions" :key="item.id" :label="item.realName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="变更描述" prop="changeDesc">
              <el-input v-model="form.changeDesc" type="textarea" :rows="3" placeholder="请输入变更描述" maxlength="400" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="变更内容" prop="changeContent">
              <el-input v-model="form.changeContent" type="textarea" :rows="4" placeholder="请输入变更内容" maxlength="600" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="回滚方案" prop="rollbackPlan">
              <el-input v-model="form.rollbackPlan" type="textarea" :rows="3" placeholder="请输入回滚方案" maxlength="500" show-word-limit />
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
import { listChangeRecord, getChangeRecord, delChangeRecord, addChangeRecord, updateChangeRecord } from "@/api/devops/changeRecord"
import { listOpsSystemAsset } from "@/api/devops/systemAsset"
import { listOpsTeamMember } from "@/api/devops/teamMember"

export default {
  name: "ChangeRecord",
  dicts: ["ops_change_type", "ops_risk_level", "ops_change_result"],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      changeRecordList: [],
      systemOptions: [],
      teamOptions: [],
      title: "",
      open: false,
      queryParams: { pageNum: 1, pageSize: 10, systemId: null, changeType: null, changeTitle: null, riskLevel: null, changeResult: null },
      form: {},
      rules: {
        systemId: [{ required: true, message: "关联系统不能为空", trigger: "change" }],
        changeType: [{ required: true, message: "变更类型不能为空", trigger: "change" }],
        changeTitle: [{ required: true, message: "变更标题不能为空", trigger: "blur" }],
        riskLevel: [{ required: true, message: "风险等级不能为空", trigger: "change" }]
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
      listChangeRecord(this.queryParams).then(response => {
        this.changeRecordList = response.rows
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
    getSystemName(id) {
      const item = this.systemOptions.find(option => option.id === id)
      return item ? this.formatSystem(item) : "-"
    },
    getTeamName(id) {
      const item = this.teamOptions.find(option => option.id === id)
      return item ? item.realName : "-"
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = { id: null, systemId: null, changeType: "1", changeTitle: null, changeDesc: null, riskLevel: "1", changeContent: null, rollbackPlan: null, changeTime: null, executorId: null, changeResult: "1", approverId: null, remark: null }
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
      this.title = "新增变更记录"
    },
    handleUpdate(row) {
      this.reset()
      getChangeRecord(row.id || this.ids).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改变更记录"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) return
        if (this.form.id != null) {
          updateChangeRecord(this.form).then(() => { this.$modal.msgSuccess("修改成功"); this.open = false; this.getList() })
        } else {
          addChangeRecord(this.form).then(() => { this.$modal.msgSuccess("新增成功"); this.open = false; this.getList() })
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm("确认删除选中的变更记录？").then(function() { return delChangeRecord(ids) }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download("ops/change/export", { ...this.queryParams }, "changeRecord_" + new Date().getTime() + ".xlsx")
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
