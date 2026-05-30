<template>
  <div class="app-container ops-page">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="92px" class="ops-query-form">
      <el-form-item label="变更编号" prop="changeNo">
        <el-input v-model="queryParams.changeNo" class="query-control" placeholder="变更编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="变更标题" prop="changeTitle">
        <el-input v-model="queryParams.changeTitle" class="query-control" placeholder="变更标题" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="变更类型" prop="changeType">
        <el-select v-model="queryParams.changeType" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_change_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="风险等级" prop="riskLevel">
        <el-select v-model="queryParams.riskLevel" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_risk_level" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="变更状态" prop="status">
        <el-select v-model="queryParams.status" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_change_status" :key="dict.value" :label="dict.label" :value="dict.value" />
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
      <el-table-column label="变更编号" align="center" prop="changeNo" width="170" show-overflow-tooltip />
      <el-table-column label="变更标题" align="left" prop="changeTitle" min-width="200" show-overflow-tooltip />
      <el-table-column label="变更类型" align="center" prop="changeType" width="110">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_change_type" :value="scope.row.changeType" /></template>
      </el-table-column>
      <el-table-column label="风险等级" align="center" prop="riskLevel" width="100">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_risk_level" :value="scope.row.riskLevel" /></template>
      </el-table-column>
      <el-table-column label="变更状态" align="center" prop="status" width="100">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_change_status" :value="scope.row.status" /></template>
      </el-table-column>
      <el-table-column label="执行人" align="center" prop="executorName" width="100" show-overflow-tooltip />
      <el-table-column label="补丁号" align="center" prop="patchNo" width="140" show-overflow-tooltip />
      <el-table-column label="变更时间" align="center" prop="changeTime" width="120">
        <template slot-scope="scope">{{ parseTime(scope.row.changeTime, "{y}-{m}-{d}") }}</template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['ops:change:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['ops:change:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="980px" append-to-body class="ops-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="118px">
        <el-divider content-position="left">变更概况</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="变更编号" prop="changeNo">
              <el-input v-model="form.changeNo" placeholder="系统自动生成" readonly>
                <el-button slot="append" icon="el-icon-refresh" :disabled="!!form.id" @click="loadNextNo" />
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="变更状态" prop="status">
              <el-select v-model="form.status" class="full-control" placeholder="请选择变更状态">
                <el-option v-for="dict in dict.type.ops_change_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="变更标题" prop="changeTitle">
              <el-input v-model="form.changeTitle" placeholder="请输入变更标题" maxlength="100" />
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
            <el-form-item label="关联系统" prop="systemId">
              <el-select v-model="form.systemId" class="full-control" placeholder="请选择关联系统" clearable filterable @change="handleSystemChange">
                <el-option v-for="item in systemOptions" :key="item.id" :label="formatSystem(item)" :value="item.id" />
              </el-select>
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
          <el-col :span="24">
            <el-form-item label="变更描述" prop="changeDesc">
              <el-input v-model="form.changeDesc" type="textarea" :rows="3" placeholder="请输入变更描述" maxlength="400" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">人员与方案</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="执行人" prop="executorId">
              <el-select v-model="form.executorId" class="full-control" placeholder="请选择执行人" clearable filterable @change="handleExecutorChange">
                <el-option v-for="item in teamOptions" :key="item.id" :label="item.realName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审批人" prop="approverId">
              <el-select v-model="form.approverId" class="full-control" placeholder="请选择审批人" clearable filterable @change="handleApproverChange">
                <el-option v-for="item in teamOptions" :key="item.id" :label="item.realName" :value="item.id" />
              </el-select>
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
        </el-row>

        <el-divider content-position="left">验证闭环</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="补丁号" prop="patchNo">
              <el-input v-model="form.patchNo" placeholder="请输入补丁号" maxlength="60" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="验证结果" prop="verifyResult">
              <el-select v-model="form.verifyResult" class="full-control" placeholder="请选择验证结果" clearable>
                <el-option v-for="dict in dict.type.ht_verify_result" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="验证说明" prop="verifyDetail">
              <el-input v-model="form.verifyDetail" type="textarea" :rows="3" placeholder="请输入验证说明" maxlength="800" show-word-limit />
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

    <el-drawer :title="detailTitle" :visible.sync="detailOpen" size="750px" append-to-body class="change-detail-drawer">
      <div class="detail-body" v-loading="detailLoading">
        <el-descriptions :column="2" size="small" border>
          <el-descriptions-item label="变更编号">{{ detail.changeNo }}</el-descriptions-item>
          <el-descriptions-item label="变更类型"><dict-tag :options="dict.type.ops_change_type" :value="detail.changeType" /></el-descriptions-item>
          <el-descriptions-item label="变更标题" :span="2">{{ detail.changeTitle }}</el-descriptions-item>
          <el-descriptions-item label="关联系统">{{ detail.systemName }}</el-descriptions-item>
          <el-descriptions-item label="风险等级"><dict-tag :options="dict.type.ops_risk_level" :value="detail.riskLevel" /></el-descriptions-item>
          <el-descriptions-item label="状态"><dict-tag :options="dict.type.ops_change_status" :value="detail.status" /></el-descriptions-item>
          <el-descriptions-item label="变更结果"><dict-tag v-if="detail.changeResult" :options="dict.type.ops_change_result" :value="detail.changeResult" /><span v-else>-</span></el-descriptions-item>
          <el-descriptions-item label="执行人">{{ detail.executorName }}</el-descriptions-item>
          <el-descriptions-item label="审批人">{{ detail.approverName }}</el-descriptions-item>
          <el-descriptions-item label="变更时间">{{ parseTime(detail.changeTime, "{y}-{m}-{d} {h}:{i}") }}</el-descriptions-item>
          <el-descriptions-item label="补丁号">{{ detail.patchNo || '-' }}</el-descriptions-item>
          <el-descriptions-item label="变更描述" :span="2">{{ detail.changeDesc || '-' }}</el-descriptions-item>
          <el-descriptions-item label="变更内容" :span="2">{{ detail.changeContent || '-' }}</el-descriptions-item>
          <el-descriptions-item label="回退方案" :span="2">{{ detail.rollbackPlan || '-' }}</el-descriptions-item>
          <el-descriptions-item label="验证结果"><dict-tag v-if="detail.verifyResult" :options="dict.type.ops_change_result" :value="detail.verifyResult" /><span v-else>-</span></el-descriptions-item>
          <el-descriptions-item label="验证说明">{{ detail.verifyDetail || '-' }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { listChangeRecord, getChangeRecord, delChangeRecord, addChangeRecord, updateChangeRecord, nextChangeNo } from "@/api/devops/changeRecord"
import { listOpsSystemAsset } from "@/api/devops/systemAsset"
import { listOpsTeamMember } from "@/api/devops/teamMember"

export default {
  name: "ChangeRecord",
  dicts: ["ops_change_type", "ops_risk_level", "ops_change_result", "ops_change_status", "ht_verify_result"],
  data() {
    return {
      loading: true,
      detailLoading: false,
      ids: [],
      detailOpen: false,
      detailTitle: "变更详情",
      detail: {},
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      changeRecordList: [],
      systemOptions: [],
      teamOptions: [],
      title: "",
      open: false,
      queryParams: { pageNum: 1, pageSize: 10, changeNo: null, systemId: null, changeType: null, changeTitle: null, riskLevel: null, status: null, changeResult: null },
      form: {},
      rules: {
        systemId: [{ required: true, message: "关联系统不能为空", trigger: "change" }],
        changeType: [{ required: true, message: "变更类型不能为空", trigger: "change" }],
        changeTitle: [{ required: true, message: "变更标题不能为空", trigger: "blur" }],
        riskLevel: [{ required: true, message: "风险等级不能为空", trigger: "change" }],
        status: [{ required: true, message: "变更状态不能为空", trigger: "change" }]
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
        this.changeRecordList = response.rows || []
        this.total = response.total || 0
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
    loadNextNo() {
      nextChangeNo().then(response => { this.form.changeNo = response.data })
    },
    handleSystemChange(id) {
      const item = this.systemOptions.find(option => option.id === id)
      this.form.systemName = item ? item.systemName : null
    },
    handleExecutorChange(id) {
      const item = this.teamOptions.find(option => option.id === id)
      this.form.executorName = item ? item.realName : null
    },
    handleApproverChange(id) {
      const item = this.teamOptions.find(option => option.id === id)
      this.form.approverName = item ? item.realName : null
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null,
        changeNo: null,
        systemId: null,
        systemName: null,
        changeType: "1",
        changeTitle: null,
        changeDesc: null,
        riskLevel: "1",
        changeContent: null,
        rollbackPlan: null,
        changeTime: null,
        executorId: null,
        executorName: null,
        changeResult: "1",
        approverId: null,
        approverName: null,
        status: "APPLYING",
        workItemId: null,
        patchNo: null,
        verifyResult: null,
        verifyDetail: null,
        remark: null
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
      this.title = "新增变更记录"
      this.loadNextNo()
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getChangeRecord(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改变更记录"
      })
    },
    submitForm() {
      this.handleSystemChange(this.form.systemId)
      this.handleExecutorChange(this.form.executorId)
      this.handleApproverChange(this.form.approverId)
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
    },
    handleDetail(row) {
      this.detail = { ...row }
      this.detailTitle = row.changeNo ? `变更详情：${row.changeNo}` : "变更详情"
      this.detailOpen = true
      this.detailLoading = true
      getChangeRecord(row.id).then(response => {
        this.detail = response.data || row
        this.detailLoading = false
      }).catch(() => { this.detailLoading = false })
    }
  }
}
</script>

<style scoped>
.ops-query-form ::v-deep .el-form-item__label { white-space: nowrap; }
.query-control { width: 210px; }
.query-actions { margin-left: 4px; }
.ops-dialog ::v-deep .el-dialog { max-width: calc(100vw - 32px); }
.ops-dialog ::v-deep .el-divider__text { color: #606266; font-weight: 600; }
.ops-dialog ::v-deep .el-form-item__label { white-space: nowrap; }
.full-control { width: 100%; }
.change-detail-drawer ::v-deep .el-drawer { max-width: 100vw; }
.change-detail-drawer ::v-deep .el-descriptions-item__label { white-space: nowrap; }
.detail-body { padding: 0 20px 24px; }
@media (max-width: 900px) {
  .ops-dialog ::v-deep .el-col-12 { width: 100%; }
  .change-detail-drawer ::v-deep .el-drawer { width: 100% !important; }
}
</style>
