<template>
  <div class="app-container ops-page">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="92px" class="ops-query-form">
      <el-form-item label="问题编号" prop="issueNo">
        <el-input v-model="queryParams.issueNo" class="query-control" placeholder="问题编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="问题标题" prop="issueTitle">
        <el-input v-model="queryParams.issueTitle" class="query-control" placeholder="问题标题" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="关联系统" prop="systemId">
        <el-select v-model="queryParams.systemId" class="query-control" placeholder="全部" clearable filterable>
          <el-option v-for="item in systemOptions" :key="item.id" :label="formatSystem(item)" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="问题类型" prop="issueType">
        <el-select v-model="queryParams.issueType" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_issue_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_issue_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item class="query-actions">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="opsIssueList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="问题编号" align="center" prop="issueNo" width="190" show-overflow-tooltip />
      <el-table-column label="问题标题" align="left" prop="issueTitle" min-width="220" show-overflow-tooltip />
      <el-table-column label="问题类型" align="center" prop="issueType" width="120">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_issue_type" :value="scope.row.issueType" /></template>
      </el-table-column>
      <el-table-column label="关联系统" align="left" prop="systemName" min-width="150" show-overflow-tooltip />
      <el-table-column label="所属模块" align="center" prop="moduleCode" width="120">
        <template slot-scope="scope"><dict-tag :options="dict.type.ht_req_module" :value="scope.row.moduleCode" /></template>
      </el-table-column>
      <el-table-column label="优先级" align="center" prop="priority" width="90">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_item_priority" :value="scope.row.priority" /></template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="110">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_issue_status" :value="scope.row.status" /></template>
      </el-table-column>
      <el-table-column label="负责人" align="center" prop="ownerName" width="110" show-overflow-tooltip />
      <el-table-column label="发现时间" align="center" prop="foundTime" width="160">
        <template slot-scope="scope">{{ parseTime(scope.row.foundTime, "{y}-{m}-{d} {h}:{i}") }}</template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="300" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-connection" @click="handleConvertToBug(scope.row)">转Bug</el-button>
          <el-button size="mini" type="text" icon="el-icon-document-add" @click="handleConvertToRequirement(scope.row)">转需求</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="980px" append-to-body class="ops-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="118px">
        <el-divider content-position="left">基础信息</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="问题编号" prop="issueNo">
              <el-input v-model="form.issueNo" placeholder="系统自动生成" readonly>
                <el-button slot="append" icon="el-icon-refresh" :disabled="!!form.id" @click="loadNextNo" />
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="问题标题" prop="issueTitle">
              <el-input v-model="form.issueTitle" placeholder="请输入问题标题" maxlength="120" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="问题类型" prop="issueType">
              <el-select v-model="form.issueType" class="full-control" placeholder="请选择问题类型">
                <el-option v-for="dict in dict.type.ops_issue_type" :key="dict.value" :label="dict.label" :value="dict.value" />
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
            <el-form-item label="所属模块" prop="moduleCode">
              <el-select v-model="form.moduleCode" class="full-control" placeholder="请选择所属模块" clearable filterable>
                <el-option v-for="dict in dict.type.ht_req_module" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-select v-model="form.priority" class="full-control" placeholder="请选择优先级">
                <el-option v-for="dict in dict.type.ops_item_priority" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="问题描述" prop="issueDesc">
              <el-input v-model="form.issueDesc" type="textarea" :rows="3" placeholder="请输入问题描述" maxlength="1000" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">发现与影响</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="发现来源" prop="sourceType">
              <el-input v-model="form.sourceType" placeholder="请输入发现来源" maxlength="60" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="影响范围" prop="impactScope">
              <el-input v-model="form.impactScope" placeholder="请输入影响范围" maxlength="120" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发现人" prop="founder">
              <el-input v-model="form.founder" placeholder="请输入发现人" maxlength="40" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发现时间" prop="foundTime">
              <el-date-picker v-model="form.foundTime" class="full-control" clearable type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择发现时间" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="ownerId">
              <el-select v-model="form.ownerId" class="full-control" placeholder="请选择负责人" clearable filterable @change="handleOwnerChange">
                <el-option v-for="item in teamOptions" :key="item.id" :label="item.realName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="当前状态" prop="status">
              <el-select v-model="form.status" class="full-control" placeholder="请选择当前状态">
                <el-option v-for="dict in dict.type.ops_issue_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">处理闭环</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="处理方式" prop="handleMethod">
              <el-select v-model="form.handleMethod" class="full-control" placeholder="请选择处理方式" clearable>
                <el-option v-for="dict in dict.type.ops_handle_method" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="根因分类" prop="rootCause">
              <el-select v-model="form.rootCause" class="full-control" placeholder="请选择根因分类" clearable>
                <el-option v-for="dict in dict.type.ops_root_cause" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="处理结果" prop="handleResult">
              <el-input v-model="form.handleResult" type="textarea" :rows="3" placeholder="请输入处理结果" maxlength="800" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关联Bug编号" prop="relatedBugNo">
              <el-input v-model="form.relatedBugNo" placeholder="转Bug后回写" readonly />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关联需求编号" prop="relatedReqNo">
              <el-input v-model="form.relatedReqNo" placeholder="转需求后回写" readonly />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关联变更编号" prop="relatedChangeNo">
              <el-input v-model="form.relatedChangeNo" placeholder="变更后回写" readonly />
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

    <el-drawer :title="detailTitle" :visible.sync="detailOpen" size="720px" append-to-body class="ops-detail-drawer">
      <div class="detail-body" v-loading="relationLoading">
        <el-descriptions :column="2" size="small" border>
          <el-descriptions-item label="问题编号">{{ detail.issueNo }}</el-descriptions-item>
          <el-descriptions-item label="问题类型"><dict-tag :options="dict.type.ops_issue_type" :value="detail.issueType" /></el-descriptions-item>
          <el-descriptions-item label="问题标题" :span="2">{{ detail.issueTitle }}</el-descriptions-item>
          <el-descriptions-item label="关联系统">{{ detail.systemName }}</el-descriptions-item>
          <el-descriptions-item label="所属模块"><dict-tag :options="dict.type.ht_req_module" :value="detail.moduleCode" /></el-descriptions-item>
          <el-descriptions-item label="优先级"><dict-tag :options="dict.type.ops_item_priority" :value="detail.priority" /></el-descriptions-item>
          <el-descriptions-item label="当前状态"><dict-tag :options="dict.type.ops_issue_status" :value="detail.status" /></el-descriptions-item>
          <el-descriptions-item label="发现人">{{ detail.founder }}</el-descriptions-item>
          <el-descriptions-item label="发现时间">{{ parseTime(detail.foundTime, "{y}-{m}-{d} {h}:{i}") }}</el-descriptions-item>
          <el-descriptions-item label="负责人">{{ detail.ownerName }}</el-descriptions-item>
          <el-descriptions-item label="处理方式"><dict-tag :options="dict.type.ops_handle_method" :value="detail.handleMethod" /></el-descriptions-item>
          <el-descriptions-item label="根因分类"><dict-tag :options="dict.type.ops_root_cause" :value="detail.rootCause" /></el-descriptions-item>
          <el-descriptions-item label="问题描述" :span="2">{{ detail.issueDesc }}</el-descriptions-item>
          <el-descriptions-item label="处理结果" :span="2">{{ detail.handleResult }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">关联链路</el-divider>
        <el-table :data="relationList" size="small" border>
          <el-table-column label="来源类型" prop="sourceType" min-width="110" show-overflow-tooltip />
          <el-table-column label="来源编号" prop="sourceNo" min-width="140" show-overflow-tooltip />
          <el-table-column label="目标类型" prop="targetType" min-width="110" show-overflow-tooltip />
          <el-table-column label="目标编号" prop="targetNo" min-width="140" show-overflow-tooltip />
          <el-table-column label="关系类型" prop="relationType" min-width="110" show-overflow-tooltip />
          <el-table-column label="关系说明" prop="relationDesc" min-width="160" show-overflow-tooltip />
        </el-table>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import {
  listOpsIssue,
  getOpsIssue,
  delOpsIssue,
  addOpsIssue,
  updateOpsIssue,
  nextIssueNo,
  convertToBug,
  convertToRequirement
} from "@/api/devops/opsIssue"
import { listOpsItemRelation, listOpsItemRelationBySource } from "@/api/devops/itemRelation"
import { listOpsSystemAsset } from "@/api/devops/systemAsset"
import { listOpsTeamMember } from "@/api/devops/teamMember"

export default {
  name: "OpsIssue",
  dicts: ["ops_issue_type", "ops_issue_status", "ops_handle_method", "ops_root_cause", "ops_item_priority", "ht_req_module"],
  data() {
    return {
      loading: true,
      relationLoading: false,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      opsIssueList: [],
      relationList: [],
      systemOptions: [],
      teamOptions: [],
      title: "",
      detailTitle: "运维问题详情",
      open: false,
      detailOpen: false,
      detail: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        issueNo: null,
        issueTitle: null,
        systemId: null,
        issueType: null,
        status: null
      },
      form: {},
      rules: {
        issueType: [{ required: true, message: "问题类型不能为空", trigger: "change" }],
        systemId: [{ required: true, message: "关联系统不能为空", trigger: "change" }],
        issueTitle: [{ required: true, message: "问题标题不能为空", trigger: "blur" }],
        priority: [{ required: true, message: "优先级不能为空", trigger: "change" }],
        status: [{ required: true, message: "当前状态不能为空", trigger: "change" }],
        foundTime: [{ required: true, message: "发现时间不能为空", trigger: "change" }]
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
      listOpsIssue(this.queryParams).then(response => {
        this.opsIssueList = response.rows || []
        this.total = response.total || 0
        this.loading = false
      }).catch(() => {
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
      nextIssueNo().then(response => { this.form.issueNo = response.data })
    },
    handleSystemChange(id) {
      const item = this.systemOptions.find(option => option.id === id)
      this.form.systemName = item ? item.systemName : null
    },
    handleOwnerChange(id) {
      const item = this.teamOptions.find(option => option.id === id)
      this.form.ownerName = item ? item.realName : null
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null,
        issueNo: null,
        issueType: "FUNCTION",
        systemId: null,
        systemName: null,
        moduleCode: null,
        issueTitle: null,
        issueDesc: null,
        sourceType: null,
        impactScope: null,
        priority: "P2",
        founder: null,
        foundTime: this.parseTime(new Date(), "{y}-{m}-{d} {h}:{i}:{s}"),
        ownerId: null,
        ownerName: null,
        status: "PENDING",
        handleMethod: null,
        handleResult: null,
        rootCause: null,
        workItemId: null,
        relatedBugNo: null,
        relatedReqNo: null,
        relatedChangeNo: null,
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
      this.title = "新增运维问题"
      this.loadNextNo()
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getOpsIssue(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改运维问题"
      })
    },
    submitForm() {
      this.handleSystemChange(this.form.systemId)
      this.handleOwnerChange(this.form.ownerId)
      this.$refs["form"].validate(valid => {
        if (!valid) return
        if (this.form.id != null) {
          updateOpsIssue(this.form).then(() => { this.$modal.msgSuccess("修改成功"); this.open = false; this.getList() })
        } else {
          addOpsIssue(this.form).then(() => { this.$modal.msgSuccess("新增成功"); this.open = false; this.getList() })
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm("确认删除选中的运维问题？").then(function() { return delOpsIssue(ids) }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download("ops/issue/export", { ...this.queryParams }, "opsIssue_" + new Date().getTime() + ".xlsx")
    },
    handleConvertToBug(row) {
      this.$modal.confirm("确认将该运维问题转为Bug？").then(() => convertToBug(row.id)).then(() => {
        this.$modal.msgSuccess("转Bug成功")
        this.getList()
      }).catch(() => {})
    },
    handleConvertToRequirement(row) {
      this.$modal.confirm("确认将该运维问题转为需求？").then(() => convertToRequirement(row.id)).then(() => {
        this.$modal.msgSuccess("转需求成功")
        this.getList()
      }).catch(() => {})
    },
    handleDetail(row) {
      const id = row.id
      this.detail = { ...row }
      this.detailTitle = row.issueNo ? `运维问题详情：${row.issueNo}` : "运维问题详情"
      this.detailOpen = true
      getOpsIssue(id).then(response => {
        this.detail = response.data || row
      })
      this.loadRelations(id)
    },
    loadRelations(id) {
      this.relationLoading = true
      listOpsItemRelationBySource("OPS_ISSUE", id).then(response => {
        this.relationList = response.rows || response.data || []
        this.relationLoading = false
      }).catch(() => {
        listOpsItemRelation({ sourceType: "OPS_ISSUE", sourceId: id, pageNum: 1, pageSize: 1000 }).then(response => {
          this.relationList = response.rows || response.data || []
          this.relationLoading = false
        }).catch(() => {
          this.relationList = []
          this.relationLoading = false
        })
      })
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
.ops-detail-drawer ::v-deep .el-drawer { max-width: 100vw; }
.ops-detail-drawer ::v-deep .el-descriptions-item__label { white-space: nowrap; }
.detail-body { padding: 0 20px 24px; }
.full-control { width: 100%; }

@media (max-width: 900px) {
  .ops-dialog ::v-deep .el-col-12 {
    width: 100%;
  }
}

@media (max-width: 760px) {
  .ops-detail-drawer ::v-deep .el-drawer {
    width: 100% !important;
  }
}
</style>
