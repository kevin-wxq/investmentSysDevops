<template>
  <div class="app-container ht-page">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="92px" class="ht-query-form">
      <el-form-item label="需求编号" prop="reqNo">
        <el-input v-model="queryParams.reqNo" class="query-control" placeholder="需求编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="需求名称" prop="reqName">
        <el-input v-model="queryParams.reqName" class="query-control" placeholder="需求名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="提出部门" prop="deptCode">
        <el-select v-model="queryParams.deptCode" class="query-control" placeholder="全部" clearable filterable>
          <el-option v-for="dict in dict.type.ht_dept_code" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="优先级" prop="priority">
        <el-select v-model="queryParams.priority" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_item_priority" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="当前状态" prop="status">
        <el-select v-model="queryParams.status" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ht_req_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item class="query-actions">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['ht:requirement:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['ht:requirement:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['ht:requirement:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['ht:requirement:export']">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="htRequirementList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="需求编号" align="center" prop="reqNo" width="190" show-overflow-tooltip />
      <el-table-column label="需求名称" align="left" prop="reqName" min-width="220" show-overflow-tooltip />
      <el-table-column label="提出部门" align="center" prop="deptCode" width="120">
        <template slot-scope="scope"><dict-tag :options="dict.type.ht_dept_code" :value="scope.row.deptCode" /></template>
      </el-table-column>
      <el-table-column label="所属模块" align="center" prop="moduleCode" width="120">
        <template slot-scope="scope"><dict-tag :options="dict.type.ht_req_module" :value="scope.row.moduleCode" /></template>
      </el-table-column>
      <el-table-column label="关联系统" align="left" prop="systemName" min-width="150" show-overflow-tooltip />
      <el-table-column label="优先级" align="center" prop="priority" width="90">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_item_priority" :value="scope.row.priority" /></template>
      </el-table-column>
      <el-table-column label="当前状态" align="center" prop="status" width="120">
        <template slot-scope="scope"><dict-tag :options="dict.type.ht_req_status" :value="scope.row.status" /></template>
      </el-table-column>
      <el-table-column label="提出人" align="center" prop="submitter" width="110" show-overflow-tooltip />
      <el-table-column label="提出时间" align="center" prop="submitTime" width="160">
        <template slot-scope="scope">{{ parseTime(scope.row.submitTime, "{y}-{m}-{d} {h}:{i}") }}</template>
      </el-table-column>
      <el-table-column label="预计上线" align="center" prop="expectedOnlineTime" width="120">
        <template slot-scope="scope">{{ parseTime(scope.row.expectedOnlineTime, "{y}-{m}-{d}") }}</template>
      </el-table-column>
      <el-table-column label="补丁号" align="center" prop="patchNo" width="140" show-overflow-tooltip />
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['ht:requirement:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['ht:requirement:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="980px" append-to-body class="ht-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="118px">
        <el-divider content-position="left">基础信息</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="需求编号" prop="reqNo">
              <el-input v-model="form.reqNo" placeholder="系统自动生成" readonly>
                <el-button slot="append" icon="el-icon-refresh" :disabled="!!form.id" @click="loadNextNo" />
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="需求名称" prop="reqName">
              <el-input v-model="form.reqName" placeholder="请输入需求名称" maxlength="120" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提出部门" prop="deptCode">
              <el-select v-model="form.deptCode" class="full-control" placeholder="请选择提出部门" filterable @change="handleNoFactorChange">
                <el-option v-for="dict in dict.type.ht_dept_code" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属模块" prop="moduleCode">
              <el-select v-model="form.moduleCode" class="full-control" placeholder="请选择所属模块" filterable clearable>
                <el-option v-for="dict in dict.type.ht_req_module" :key="dict.value" :label="dict.label" :value="dict.value" />
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
            <el-form-item label="优先级" prop="priority">
              <el-select v-model="form.priority" class="full-control" placeholder="请选择优先级">
                <el-option v-for="dict in dict.type.ops_item_priority" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提出人" prop="submitter">
              <el-input v-model="form.submitter" placeholder="请输入提出人" maxlength="40" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提出时间" prop="submitTime">
              <el-date-picker v-model="form.submitTime" class="full-control" clearable type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择提出时间" @change="handleNoFactorChange" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">业务与分析</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="是否涉及商务" prop="businessFlag">
              <el-select v-model="form.businessFlag" class="full-control" placeholder="请选择是否涉及商务">
                <el-option v-for="dict in dict.type.ht_business_flag" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="厂商分析人" prop="vendorAnalyst">
              <el-input v-model="form.vendorAnalyst" placeholder="请输入厂商分析人" maxlength="40" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分析结果" prop="analysisResult">
              <el-select v-model="form.analysisResult" class="full-control" placeholder="请选择分析结果" clearable>
                <el-option v-for="dict in dict.type.ht_analysis_result" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="分析详情" prop="analysisDetail">
              <el-input v-model="form.analysisDetail" type="textarea" :rows="3" placeholder="请输入分析详情" maxlength="1000" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划排期时间" prop="planScheduleTime">
              <el-date-picker v-model="form.planScheduleTime" class="full-control" clearable type="date" value-format="yyyy-MM-dd" placeholder="请选择计划排期时间" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="需求描述" prop="reqDesc">
              <el-input v-model="form.reqDesc" type="textarea" :rows="3" placeholder="请输入需求描述" maxlength="1000" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="业务价值" prop="businessValue">
              <el-input v-model="form.businessValue" type="textarea" :rows="3" placeholder="请输入业务价值" maxlength="800" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">验收与上线</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="当前状态" prop="status">
              <el-select v-model="form.status" class="full-control" placeholder="请选择当前状态">
                <el-option v-for="dict in dict.type.ht_req_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="验收人" prop="acceptor">
              <el-select v-model="form.acceptor" class="full-control" placeholder="请选择验收人" filterable clearable>
                <el-option v-for="dict in dict.type.ht_acceptor" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="验收结果" prop="acceptanceResult">
              <el-select v-model="form.acceptanceResult" class="full-control" placeholder="请选择验收结果" clearable>
                <el-option v-for="dict in dict.type.ht_acceptance_result" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="验收详情" prop="acceptanceDetail">
              <el-input v-model="form.acceptanceDetail" type="textarea" :rows="3" placeholder="请输入验收详情" maxlength="800" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预计上线时间" prop="expectedOnlineTime">
              <el-date-picker v-model="form.expectedOnlineTime" class="full-control" clearable type="date" value-format="yyyy-MM-dd" placeholder="请选择预计上线时间" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开发完成时间" prop="devFinishTime">
              <el-date-picker v-model="form.devFinishTime" class="full-control" clearable type="date" value-format="yyyy-MM-dd" placeholder="请选择开发完成时间" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上线时间" prop="onlineTime">
              <el-date-picker v-model="form.onlineTime" class="full-control" clearable type="date" value-format="yyyy-MM-dd" placeholder="请选择上线时间" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="补丁号" prop="patchNo">
              <el-input v-model="form.patchNo" placeholder="请输入补丁号" maxlength="60" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="处理进展" prop="progress">
              <el-input v-model="form.progress" type="textarea" :rows="3" placeholder="请输入处理进展" maxlength="800" show-word-limit />
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

    <el-drawer :title="detailTitle" :visible.sync="detailOpen" size="780px" append-to-body class="req-detail-drawer">
      <div class="detail-body" v-loading="detailLoading">
        <el-descriptions :column="2" size="small" border>
          <el-descriptions-item label="需求编号">{{ detail.reqNo }}</el-descriptions-item>
          <el-descriptions-item label="提出部门"><dict-tag :options="dict.type.ops_dept_code" :value="detail.deptCode" /></el-descriptions-item>
          <el-descriptions-item label="需求名称" :span="2">{{ detail.reqName }}</el-descriptions-item>
          <el-descriptions-item label="优先级"><dict-tag :options="dict.type.ops_item_priority" :value="detail.priority" /></el-descriptions-item>
          <el-descriptions-item label="所属模块"><dict-tag :options="dict.type.ht_req_module" :value="detail.moduleCode" /></el-descriptions-item>
          <el-descriptions-item label="关联系统">{{ detail.systemName }}</el-descriptions-item>
          <el-descriptions-item label="状态"><dict-tag :options="dict.type.ht_req_status" :value="detail.status" /></el-descriptions-item>
          <el-descriptions-item label="提出人">{{ detail.submitter }}</el-descriptions-item>
          <el-descriptions-item label="提出时间">{{ parseTime(detail.submitTime, "{y}-{m}-{d} {h}:{i}") }}</el-descriptions-item>
          <el-descriptions-item label="需求描述" :span="2">{{ detail.reqDesc || '-' }}</el-descriptions-item>
          <el-descriptions-item label="业务价值" :span="2">{{ detail.businessValue || '-' }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">分析与排期</el-divider>
        <el-descriptions :column="2" size="small" border>
          <el-descriptions-item label="厂商分析人">{{ detail.vendorAnalyst || '-' }}</el-descriptions-item>
          <el-descriptions-item label="分析结果"><dict-tag v-if="detail.analysisResult" :options="dict.type.ht_analysis_result" :value="detail.analysisResult" /><span v-else>-</span></el-descriptions-item>
          <el-descriptions-item label="分析说明" :span="2">{{ detail.analysisDetail || '-' }}</el-descriptions-item>
          <el-descriptions-item label="计划排期">{{ parseTime(detail.planScheduleTime, "{y}-{m}-{d}") }}</el-descriptions-item>
          <el-descriptions-item label="开发完成">{{ parseTime(detail.devFinishTime, "{y}-{m}-{d}") }}</el-descriptions-item>
          <el-descriptions-item label="补丁号">{{ detail.patchNo || '-' }}</el-descriptions-item>
          <el-descriptions-item label="预计上线">{{ parseTime(detail.expectedOnlineTime, "{y}-{m}-{d}") }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">验收与上线</el-divider>
        <el-descriptions :column="2" size="small" border>
          <el-descriptions-item label="验收人">{{ detail.acceptor || '-' }}</el-descriptions-item>
          <el-descriptions-item label="验收结果"><dict-tag v-if="detail.acceptanceResult" :options="dict.type.ht_acceptance_result" :value="detail.acceptanceResult" /><span v-else>-</span></el-descriptions-item>
          <el-descriptions-item label="验收说明" :span="2">{{ detail.acceptanceDetail || '-' }}</el-descriptions-item>
          <el-descriptions-item label="上线时间">{{ parseTime(detail.onlineTime, "{y}-{m}-{d}") }}</el-descriptions-item>
          <el-descriptions-item label="备注">{{ detail.remark || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { listHtRequirement, getHtRequirement, delHtRequirement, addHtRequirement, updateHtRequirement, nextHtRequirementNo } from "@/api/devops/htRequirement"
import { listOpsSystemAsset } from "@/api/devops/systemAsset"

export default {
  name: "HtRequirement",
  dicts: ["ht_dept_code", "ht_req_module", "ops_item_priority", "ht_req_status", "ht_business_flag", "ht_analysis_result", "ht_acceptor", "ht_acceptance_result"],
  data() {
    return {
      loading: true,
      detailLoading: false,
      ids: [],
      detailOpen: false,
      detailTitle: "需求详情",
      detail: {},
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      htRequirementList: [],
      systemOptions: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        reqNo: null,
        reqName: null,
        deptCode: null,
        priority: null,
        status: null
      },
      form: {},
      rules: {
        reqName: [{ required: true, message: "需求名称不能为空", trigger: "blur" }],
        deptCode: [{ required: true, message: "提出部门不能为空", trigger: "change" }],
        priority: [{ required: true, message: "优先级不能为空", trigger: "change" }],
        status: [{ required: true, message: "当前状态不能为空", trigger: "change" }],
        businessFlag: [{ required: true, message: "是否涉及商务不能为空", trigger: "change" }],
        submitTime: [{ required: true, message: "提出时间不能为空", trigger: "change" }]
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
      listHtRequirement(this.queryParams).then(response => {
        this.htRequirementList = response.rows || []
        this.total = response.total || 0
        this.loading = false
      })
    },
    loadOptions() {
      listOpsSystemAsset({ pageNum: 1, pageSize: 1000 }).then(response => { this.systemOptions = response.rows || [] })
    },
    formatSystem(item) {
      return `${item.systemCode || "-"} / ${item.systemName}`
    },
    formatDateParam(value) {
      return value ? String(value).slice(0, 10) : null
    },
    loadNextNo() {
      if (!this.form.deptCode) return
      nextHtRequirementNo(this.form.deptCode, this.formatDateParam(this.form.submitTime)).then(response => { this.form.reqNo = response.data })
    },
    handleNoFactorChange() {
      if (!this.form.id) this.loadNextNo()
    },
    handleSystemChange(id) {
      const item = this.systemOptions.find(option => option.id === id)
      this.form.systemName = item ? item.systemName : null
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null,
        reqNo: null,
        deptCode: "FI",
        reqName: null,
        priority: "P2",
        moduleCode: null,
        systemId: null,
        systemName: null,
        reqDesc: null,
        businessValue: null,
        businessFlag: "0",
        submitter: null,
        submitTime: this.parseTime(new Date(), "{y}-{m}-{d} {h}:{i}:{s}"),
        expectedOnlineTime: null,
        vendorAnalyst: null,
        analysisResult: null,
        planScheduleTime: null,
        devFinishTime: null,
        acceptor: null,
        acceptanceResult: null,
        onlineTime: null,
        patchNo: null,
        analysisDetail: null,
        acceptanceDetail: null,
        status: "WAIT_ANALYSIS",
        progress: null,
        workItemId: null,
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
      this.title = "新增衡泰需求"
      this.loadNextNo()
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getHtRequirement(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改衡泰需求"
      })
    },
    submitForm() {
      this.handleSystemChange(this.form.systemId)
      this.$refs["form"].validate(valid => {
        if (!valid) return
        if (this.form.id != null) {
          updateHtRequirement(this.form).then(() => { this.$modal.msgSuccess("修改成功"); this.open = false; this.getList() })
        } else {
          addHtRequirement(this.form).then(() => { this.$modal.msgSuccess("新增成功"); this.open = false; this.getList() })
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm("确认删除选中的衡泰需求？").then(function() { return delHtRequirement(ids) }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download("ht/requirement/export", { ...this.queryParams }, "htRequirement_" + new Date().getTime() + ".xlsx")
    },
    handleDetail(row) {
      this.detail = { ...row }
      this.detailTitle = row.reqNo ? `需求详情：${row.reqNo}` : "需求详情"
      this.detailOpen = true
      this.detailLoading = true
      getHtRequirement(row.id).then(response => {
        this.detail = response.data || row
        this.detailLoading = false
      }).catch(() => { this.detailLoading = false })
    }
  }
}
</script>

<style scoped>
.ht-query-form ::v-deep .el-form-item__label { white-space: nowrap; }
.query-control { width: 210px; }
.query-actions { margin-left: 4px; }
.ht-dialog ::v-deep .el-dialog { max-width: calc(100vw - 32px); }
.ht-dialog ::v-deep .el-divider__text { color: #606266; font-weight: 600; }
.ht-dialog ::v-deep .el-form-item__label { white-space: nowrap; }
.full-control { width: 100%; }
.req-detail-drawer ::v-deep .el-drawer { max-width: 100vw; }
.req-detail-drawer ::v-deep .el-descriptions-item__label { white-space: nowrap; }
.detail-body { padding: 0 20px 24px; }
@media (max-width: 900px) {
  .ht-dialog ::v-deep .el-col-12 { width: 100%; }
  .req-detail-drawer ::v-deep .el-drawer { width: 100% !important; }
}
</style>
