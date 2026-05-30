<template>
  <div class="app-container ops-page">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="92px" class="ops-query-form">
      <el-form-item label="事项编号" prop="itemNo">
        <el-input v-model="queryParams.itemNo" class="query-control" placeholder="事项编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="事项标题" prop="title">
        <el-input v-model="queryParams.title" class="query-control" placeholder="事项标题" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="事项类型" prop="itemType">
        <el-select v-model="queryParams.itemType" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_item_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="优先级" prop="priority">
        <el-select v-model="queryParams.priority" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_item_priority" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="当前状态" prop="status">
        <el-select v-model="queryParams.status" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_item_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item class="query-actions">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['ops:work-item:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['ops:work-item:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['ops:work-item:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['ops:work-item:export']">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="workItemList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="事项编号" align="center" prop="itemNo" width="170" show-overflow-tooltip />
      <el-table-column label="事项标题" align="left" prop="title" min-width="220" show-overflow-tooltip />
      <el-table-column label="类型" align="center" prop="itemType" width="100">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_item_type" :value="scope.row.itemType" /></template>
      </el-table-column>
      <el-table-column label="系统来源" align="left" min-width="150" show-overflow-tooltip>
        <template slot-scope="scope">{{ formatSource(scope.row) }}</template>
      </el-table-column>
      <el-table-column label="优先级" align="center" prop="priority" width="90">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_item_priority" :value="scope.row.priority" /></template>
      </el-table-column>
      <el-table-column label="当前状态" align="center" prop="status" width="120">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_item_status" :value="scope.row.status" /></template>
      </el-table-column>
      <el-table-column label="处理进展" align="left" prop="progress" min-width="180" show-overflow-tooltip />
      <el-table-column label="负责人" align="center" prop="ownerName" width="110" show-overflow-tooltip />
      <el-table-column label="计划完成" align="center" prop="planFinishTime" width="120">
        <template slot-scope="scope">{{ parseTime(scope.row.planFinishTime, "{y}-{m}-{d}") }}</template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="150">
        <template slot-scope="scope">{{ parseTime(scope.row.updateTime || scope.row.createTime, "{y}-{m}-{d} {h}:{i}") }}</template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="210" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleLog(scope.row)">日志</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['ops:work-item:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['ops:work-item:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="960px" append-to-body class="ops-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="116px">
        <div class="form-section">
          <div class="section-title">基础信息</div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="事项编号" prop="itemNo">
                <el-input v-model="form.itemNo" placeholder="系统自动生成" readonly>
                  <el-button slot="append" icon="el-icon-refresh" :disabled="!!form.id" @click="loadNextNo" />
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="事项类型" prop="itemType">
                <el-select v-model="form.itemType" class="full-control" placeholder="请选择事项类型" @change="handleItemTypeChange">
                  <el-option v-for="dict in dict.type.ops_item_type" :key="dict.value" :label="dict.label" :value="dict.value" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="事项标题" prop="title">
                <el-input v-model="form.title" placeholder="请输入事项标题" maxlength="120" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="系统来源">
                <el-input :value="formatSource(form)" placeholder="系统自动带入" readonly />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="来源模块">
                <el-input v-model="form.sourceModule" placeholder="系统自动带入" readonly />
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
              <el-form-item label="负责人" prop="ownerId">
                <el-select v-model="form.ownerId" class="full-control" placeholder="请选择负责人" clearable filterable @change="handleOwnerChange">
                  <el-option v-for="item in teamOptions" :key="item.id" :label="item.realName" :value="item.id" />
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
                <el-date-picker v-model="form.submitTime" class="full-control" clearable type="date" value-format="yyyy-MM-dd" placeholder="请选择提出时间" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <div class="form-section">
          <div class="section-title">处理闭环</div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="当前状态" prop="status">
                <el-select v-model="form.status" class="full-control" placeholder="请选择当前状态">
                  <el-option v-for="dict in dict.type.ops_item_status" :key="dict.value" :label="dict.label" :value="dict.value" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="计划完成" prop="planFinishTime">
                <el-date-picker v-model="form.planFinishTime" class="full-control" clearable type="date" value-format="yyyy-MM-dd" placeholder="请选择计划完成时间" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="实际完成" prop="actualFinishTime">
                <el-date-picker v-model="form.actualFinishTime" class="full-control" clearable type="date" value-format="yyyy-MM-dd" placeholder="请选择实际完成时间" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="验收结果" prop="acceptanceResult">
                <el-select v-model="form.acceptanceResult" class="full-control" placeholder="请选择验收结果" clearable>
                  <el-option v-for="dict in dict.type.ops_acceptance_result" :key="dict.value" :label="dict.label" :value="dict.value" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="处理进展" prop="progress">
                <el-input v-model="form.progress" type="textarea" :rows="3" placeholder="请输入最新处理进展、阻塞事项或下一步计划" maxlength="800" show-word-limit />
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
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-drawer title="事项流转日志" :visible.sync="logOpen" size="620px" append-to-body>
      <div class="log-drawer">
        <el-alert v-if="currentLogItem.itemNo" :title="currentLogItem.itemNo + ' / ' + currentLogItem.title" type="info" :closable="false" show-icon />
        <el-table v-loading="logLoading" :data="logList" class="log-table">
          <el-table-column label="原状态" align="center" prop="fromStatus" width="100">
            <template slot-scope="scope"><dict-tag :options="dict.type.ops_item_status" :value="scope.row.fromStatus" /></template>
          </el-table-column>
          <el-table-column label="新状态" align="center" prop="toStatus" width="100">
            <template slot-scope="scope"><dict-tag :options="dict.type.ops_item_status" :value="scope.row.toStatus" /></template>
          </el-table-column>
          <el-table-column label="操作人" align="center" prop="operatorName" width="100" show-overflow-tooltip />
          <el-table-column label="操作时间" align="center" prop="actionTime" width="150">
            <template slot-scope="scope">{{ parseTime(scope.row.actionTime, "{y}-{m}-{d} {h}:{i}") }}</template>
          </el-table-column>
          <el-table-column label="说明" align="left" prop="actionRemark" min-width="160" show-overflow-tooltip />
        </el-table>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { listWorkItem, getWorkItem, delWorkItem, addWorkItem, updateWorkItem, nextWorkItemNo } from "@/api/devops/workItem"
import { listWorkItemLog } from "@/api/devops/workItemLog"
import { listOpsTeamMember } from "@/api/devops/teamMember"

export default {
  name: "OpsWorkItem",
  dicts: ["ops_item_type", "ops_item_priority", "ops_item_status", "ops_acceptance_result"],
  data() {
    return {
      loading: true,
      logLoading: false,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      workItemList: [],
      logList: [],
      teamOptions: [],
      currentLogItem: {},
      title: "",
      open: false,
      logOpen: false,
      queryParams: { pageNum: 1, pageSize: 10, itemNo: null, title: null, itemType: null, priority: null, status: null },
      form: {},
      rules: {
        title: [{ required: true, message: "事项标题不能为空", trigger: "blur" }],
        itemType: [{ required: true, message: "事项类型不能为空", trigger: "change" }],
        priority: [{ required: true, message: "优先级不能为空", trigger: "change" }],
        status: [{ required: true, message: "当前状态不能为空", trigger: "change" }],
        progress: [{ required: true, message: "处理进展不能为空", trigger: "blur" }],
        ownerId: [{ required: true, message: "负责人不能为空", trigger: "change" }]
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
      listWorkItem(this.queryParams).then(response => {
        this.workItemList = response.rows || []
        this.total = response.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    loadOptions() {
      listOpsTeamMember({ pageNum: 1, pageSize: 1000, isOnJob: "1" }).then(response => { this.teamOptions = response.rows || [] })
    },
    formatSource(row) {
      if (!row) return "-"
      return row.systemName || row.sourceName || row.sourceModule || "-"
    },
    formatProgress(value) {
      const numberValue = Number(value || 0)
      return Math.max(0, Math.min(100, Math.round(numberValue)))
    },
    loadNextNo() {
      nextWorkItemNo(this.form.itemType).then(response => { this.form.itemNo = response.data })
    },
    handleItemTypeChange() {
      if (!this.form.id) this.loadNextNo()
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
        id: null, itemNo: null, itemType: "REQ", title: null, sourceModule: "手工录入", sourceName: null,
        priority: "P2", status: "PENDING", progress: null, ownerId: null, ownerName: null, submitter: null, submitTime: null,
        planFinishTime: null, actualFinishTime: null, acceptanceResult: null, closeDesc: null, remark: null
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
      this.title = "新增闭环事项"
      this.loadNextNo()
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getWorkItem(id).then(response => {
        this.form = { ...this.form, ...(response.data || {}) }
        this.open = true
        this.title = "修改闭环事项"
      })
    },
    submitForm() {
      this.handleOwnerChange(this.form.ownerId)
      this.$refs["form"].validate(valid => {
        if (!valid) return
        if (this.form.id != null) {
          updateWorkItem(this.form).then(() => { this.$modal.msgSuccess("修改成功"); this.open = false; this.getList() })
        } else {
          addWorkItem(this.form).then(() => { this.$modal.msgSuccess("新增成功"); this.open = false; this.getList() })
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm("确认删除选中的闭环事项？").then(function() { return delWorkItem(ids) }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleLog(row) {
      this.currentLogItem = row
      this.logOpen = true
      this.logLoading = true
      listWorkItemLog({ workItemId: row.id }).then(response => {
        this.logList = response.rows || []
        this.logLoading = false
      }).catch(() => {
        this.logLoading = false
      })
    },
    handleExport() {
      this.download("ops/work-item/export", { ...this.queryParams }, "workItem_" + new Date().getTime() + ".xlsx")
    }
  }
}
</script>

<style scoped>
.ops-query-form ::v-deep .el-form-item__label { white-space: nowrap; }
.query-control { width: 210px; }
.query-actions { margin-left: 4px; }
.ops-dialog ::v-deep .el-dialog { max-width: calc(100vw - 32px); }
.ops-dialog ::v-deep .el-dialog__body { padding-top: 14px; }
.ops-dialog ::v-deep .el-form-item__label { white-space: nowrap; }
.form-section {
  border: 1px solid #ebeef5;
  border-radius: 6px;
  padding: 16px 18px 2px;
  margin-bottom: 16px;
  background: #fff;
}
.section-title {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 14px;
}
.full-control { width: 100%; }
.log-drawer { padding: 0 20px 20px; }
.log-table { margin-top: 14px; }

@media (max-width: 900px) {
  .ops-dialog ::v-deep .el-col-12 {
    width: 100%;
  }
}
</style>
