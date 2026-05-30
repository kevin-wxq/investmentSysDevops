<template>
  <div class="app-container ht-page">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="92px" class="ht-query-form">
      <el-form-item label="Bug编号" prop="bugNo">
        <el-input v-model="queryParams.bugNo" class="query-control" placeholder="Bug编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="Bug标题" prop="bugTitle">
        <el-input v-model="queryParams.bugTitle" class="query-control" placeholder="Bug标题" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="关联系统" prop="systemId">
        <el-select v-model="queryParams.systemId" class="query-control" placeholder="全部" clearable filterable>
          <el-option v-for="item in systemOptions" :key="item.id" :label="formatSystem(item)" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="严重级别" prop="severity">
        <el-select v-model="queryParams.severity" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ht_bug_severity" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ht_bug_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item class="query-actions">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['ht:bug:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['ht:bug:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['ht:bug:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['ht:bug:export']">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="htBugList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="Bug编号" align="center" prop="bugNo" width="170" show-overflow-tooltip />
      <el-table-column label="Bug标题" align="left" prop="bugTitle" min-width="220" show-overflow-tooltip />
      <el-table-column label="关联需求" align="center" prop="requirementNo" width="180" show-overflow-tooltip />
      <el-table-column label="关联系统" align="left" prop="systemName" min-width="150" show-overflow-tooltip />
      <el-table-column label="严重级别" align="center" prop="severity" width="110">
        <template slot-scope="scope"><dict-tag :options="dict.type.ht_bug_severity" :value="scope.row.severity" /></template>
      </el-table-column>
      <el-table-column label="优先级" align="center" prop="priority" width="90">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_item_priority" :value="scope.row.priority" /></template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="110">
        <template slot-scope="scope"><dict-tag :options="dict.type.ht_bug_status" :value="scope.row.status" /></template>
      </el-table-column>
      <el-table-column label="发现人" align="center" prop="founder" width="110" show-overflow-tooltip />
      <el-table-column label="负责人" align="center" prop="ownerName" width="110" show-overflow-tooltip />
      <el-table-column label="发现时间" align="center" prop="foundTime" width="160">
        <template slot-scope="scope">{{ parseTime(scope.row.foundTime, "{y}-{m}-{d} {h}:{i}") }}</template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['ht:bug:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['ht:bug:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="980px" append-to-body class="ht-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="118px">
        <el-divider content-position="left">基础信息</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="Bug编号" prop="bugNo">
              <el-input v-model="form.bugNo" placeholder="系统自动生成" readonly>
                <el-button slot="append" icon="el-icon-refresh" :disabled="!!form.id" @click="loadNextNo" />
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Bug标题" prop="bugTitle">
              <el-input v-model="form.bugTitle" placeholder="请输入Bug标题" maxlength="120" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关联需求" prop="requirementId">
              <el-select v-model="form.requirementId" class="full-control" placeholder="请选择关联需求" clearable filterable @change="handleRequirementChange">
                <el-option v-for="item in requirementOptions" :key="item.id" :label="formatRequirement(item)" :value="item.id" />
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
            <el-form-item label="严重级别" prop="severity">
              <el-select v-model="form.severity" class="full-control" placeholder="请选择严重级别">
                <el-option v-for="dict in dict.type.ht_bug_severity" :key="dict.value" :label="dict.label" :value="dict.value" />
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
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" class="full-control" placeholder="请选择状态">
                <el-option v-for="dict in dict.type.ht_bug_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发现时间" prop="foundTime">
              <el-date-picker v-model="form.foundTime" class="full-control" clearable type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择发现时间" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Bug描述" prop="bugDesc">
              <el-input v-model="form.bugDesc" type="textarea" :rows="3" placeholder="请输入Bug描述" maxlength="1000" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">处理安排</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="发现人" prop="founder">
              <el-input v-model="form.founder" placeholder="请输入发现人" maxlength="40" />
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
            <el-form-item label="计划修复时间" prop="planFixTime">
              <el-date-picker v-model="form.planFixTime" class="full-control" clearable type="date" value-format="yyyy-MM-dd" placeholder="请选择计划修复时间" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="实际修复时间" prop="actualFixTime">
              <el-date-picker v-model="form.actualFixTime" class="full-control" clearable type="date" value-format="yyyy-MM-dd" placeholder="请选择实际修复时间" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="修复计划" prop="fixPlan">
              <el-input v-model="form.fixPlan" type="textarea" :rows="3" placeholder="请输入修复计划" maxlength="800" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="修复结果" prop="fixResult">
              <el-input v-model="form.fixResult" type="textarea" :rows="3" placeholder="请输入修复结果" maxlength="800" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">测试与关闭</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="测试人" prop="tester">
              <el-input v-model="form.tester" placeholder="请输入测试人" maxlength="40" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="验收结果" prop="testResult">
              <el-select v-model="form.testResult" class="full-control" placeholder="请选择验收结果" clearable>
                <el-option v-for="dict in dict.type.ht_acceptance_result" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关闭时间" prop="closeTime">
              <el-date-picker v-model="form.closeTime" class="full-control" clearable type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择关闭时间" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="关闭说明" prop="closeDesc">
              <el-input v-model="form.closeDesc" type="textarea" :rows="3" placeholder="请输入关闭说明" maxlength="500" show-word-limit />
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
import { listHtBug, getHtBug, delHtBug, addHtBug, updateHtBug, nextHtBugNo } from "@/api/devops/htBug"
import { listHtRequirement } from "@/api/devops/htRequirement"
import { listOpsSystemAsset } from "@/api/devops/systemAsset"
import { listOpsTeamMember } from "@/api/devops/teamMember"

export default {
  name: "HtBug",
  dicts: ["ht_bug_severity", "ops_item_priority", "ht_bug_status", "ht_acceptance_result"],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      htBugList: [],
      requirementOptions: [],
      systemOptions: [],
      teamOptions: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bugNo: null,
        bugTitle: null,
        systemId: null,
        severity: null,
        status: null
      },
      form: {},
      rules: {
        bugTitle: [{ required: true, message: "Bug标题不能为空", trigger: "blur" }],
        severity: [{ required: true, message: "严重级别不能为空", trigger: "change" }],
        priority: [{ required: true, message: "优先级不能为空", trigger: "change" }],
        status: [{ required: true, message: "状态不能为空", trigger: "change" }],
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
      listHtBug(this.queryParams).then(response => {
        this.htBugList = response.rows || []
        this.total = response.total || 0
        this.loading = false
      })
    },
    loadOptions() {
      listHtRequirement({ pageNum: 1, pageSize: 1000 }).then(response => { this.requirementOptions = response.rows || [] })
      listOpsSystemAsset({ pageNum: 1, pageSize: 1000 }).then(response => { this.systemOptions = response.rows || [] })
      listOpsTeamMember({ pageNum: 1, pageSize: 1000, isOnJob: "1" }).then(response => { this.teamOptions = response.rows || [] })
    },
    formatSystem(item) {
      return `${item.systemCode || "-"} / ${item.systemName}`
    },
    formatRequirement(item) {
      return `${item.reqNo || "-"} / ${item.reqName}`
    },
    loadNextNo() {
      nextHtBugNo(this.form.systemId).then(response => { this.form.bugNo = response.data })
    },
    handleRequirementChange(id) {
      const item = this.requirementOptions.find(option => option.id === id)
      this.form.requirementNo = item ? item.reqNo : null
      if (item && !this.form.systemId) {
        this.form.systemId = item.systemId
        this.form.systemName = item.systemName
      }
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
        bugNo: null,
        requirementId: null,
        requirementNo: null,
        systemId: null,
        systemName: null,
        bugTitle: null,
        bugDesc: null,
        severity: "S2",
        priority: "P2",
        status: "WAIT_CONFIRM",
        founder: null,
        foundTime: this.parseTime(new Date(), "{y}-{m}-{d} {h}:{i}:{s}"),
        ownerId: null,
        ownerName: null,
        fixPlan: null,
        fixResult: null,
        planFixTime: null,
        actualFixTime: null,
        tester: null,
        testResult: null,
        closeTime: null,
        closeDesc: null,
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
      this.title = "新增Bug记录"
      this.loadNextNo()
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getHtBug(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改Bug记录"
      })
    },
    submitForm() {
      this.handleRequirementChange(this.form.requirementId)
      this.handleSystemChange(this.form.systemId)
      this.handleOwnerChange(this.form.ownerId)
      this.$refs["form"].validate(valid => {
        if (!valid) return
        if (this.form.id != null) {
          updateHtBug(this.form).then(() => { this.$modal.msgSuccess("修改成功"); this.open = false; this.getList() })
        } else {
          addHtBug(this.form).then(() => { this.$modal.msgSuccess("新增成功"); this.open = false; this.getList() })
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm("确认删除选中的Bug记录？").then(function() { return delHtBug(ids) }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download("ht/bug/export", { ...this.queryParams }, "htBug_" + new Date().getTime() + ".xlsx")
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

@media (max-width: 900px) {
  .ht-dialog ::v-deep .el-col-12 {
    width: 100%;
  }
}
</style>
