<template>
  <div class="app-container ops-page">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="92px" class="ops-query-form">
      <el-form-item label="巡检日期" prop="inspectionDate">
        <el-date-picker v-model="queryParams.inspectionDate" class="query-control" clearable type="date" value-format="yyyy-MM-dd" placeholder="巡检日期" />
      </el-form-item>
      <el-form-item label="关联系统" prop="systemId">
        <el-select v-model="queryParams.systemId" class="query-control" placeholder="全部" clearable filterable>
          <el-option v-for="item in systemOptions" :key="item.id" :label="formatSystem(item)" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="巡检人" prop="inspectorName">
        <el-input v-model="queryParams.inspectorName" class="query-control" placeholder="巡检人" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="巡检结果" prop="inspectionResult">
        <el-select v-model="queryParams.inspectionResult" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_inspection_result" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item class="query-actions">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['daily:inspection:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['daily:inspection:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['daily:inspection:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['daily:inspection:export']">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="dailyInspectionMainList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="巡检日期" align="center" prop="inspectionDate" width="120">
        <template slot-scope="scope">{{ parseTime(scope.row.inspectionDate, "{y}-{m}-{d}") }}</template>
      </el-table-column>
      <el-table-column label="巡检时间" align="center" prop="inspectionTime" width="100" />
      <el-table-column label="关联系统" align="left" prop="systemName" min-width="170" show-overflow-tooltip />
      <el-table-column label="服务器IP" align="center" prop="serverIp" min-width="130" show-overflow-tooltip />
      <el-table-column label="CPU" align="center" prop="cpuUsage" width="90" />
      <el-table-column label="内存" align="center" prop="memoryUsage" width="90" />
      <el-table-column label="磁盘" align="center" prop="diskUsage" width="90" />
      <el-table-column label="网络" align="center" prop="networkStatus" width="100">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_health_status" :value="scope.row.networkStatus" /></template>
      </el-table-column>
      <el-table-column label="进程" align="center" prop="processStatus" width="100">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_health_status" :value="scope.row.processStatus" /></template>
      </el-table-column>
      <el-table-column label="巡检结果" align="center" prop="inspectionResult" width="110">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_inspection_result" :value="scope.row.inspectionResult" /></template>
      </el-table-column>
      <el-table-column label="巡检人" align="center" prop="inspectorName" width="110" />
      <el-table-column label="处理人" align="center" prop="handlerName" width="110" />
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['daily:inspection:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['daily:inspection:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="920px" append-to-body class="ops-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="108px">
        <el-divider content-position="left">巡检基础</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="巡检日期" prop="inspectionDate">
              <el-date-picker v-model="form.inspectionDate" class="full-control" clearable type="date" value-format="yyyy-MM-dd" placeholder="请选择巡检日期" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="巡检时间" prop="inspectionTime">
              <el-time-picker v-model="form.inspectionTime" class="full-control" value-format="HH:mm" format="HH:mm" placeholder="请选择巡检时间" />
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
            <el-form-item label="巡检人" prop="inspectorId">
              <el-select v-model="form.inspectorId" class="full-control" placeholder="请选择巡检人" clearable filterable @change="handleInspectorChange">
                <el-option v-for="item in teamOptions" :key="item.id" :label="item.realName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">资源与状态</el-divider>
        <el-row :gutter="18">
          <el-col :span="12"><el-form-item label="服务器IP" prop="serverIp"><el-input v-model="form.serverIp" placeholder="自动带出或手工补充" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="告警数量" prop="alarmCount"><el-input-number v-model="form.alarmCount" class="full-control" :min="0" :max="99999" controls-position="right" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="CPU使用率" prop="cpuUsage"><el-input v-model="form.cpuUsage" placeholder="例如 35%" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="内存使用率" prop="memoryUsage"><el-input v-model="form.memoryUsage" placeholder="例如 60%" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="磁盘使用率" prop="diskUsage"><el-input v-model="form.diskUsage" placeholder="例如 70%" /></el-form-item></el-col>
          <el-col :span="12">
            <el-form-item label="网络状态" prop="networkStatus">
              <el-select v-model="form.networkStatus" class="full-control" placeholder="请选择网络状态">
                <el-option v-for="dict in dict.type.ops_health_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="进程状态" prop="processStatus">
              <el-select v-model="form.processStatus" class="full-control" placeholder="请选择进程状态">
                <el-option v-for="dict in dict.type.ops_health_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24"><el-form-item label="告警信息" prop="alarmInfo"><el-input v-model="form.alarmInfo" type="textarea" :rows="3" placeholder="请输入告警信息" maxlength="400" show-word-limit /></el-form-item></el-col>
        </el-row>

        <el-divider content-position="left">处理结果</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="巡检结果" prop="inspectionResult">
              <el-select v-model="form.inspectionResult" class="full-control" placeholder="请选择巡检结果">
                <el-option v-for="dict in dict.type.ops_inspection_result" :key="dict.value" :label="dict.label" :value="dict.value" />
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
            <el-form-item label="处理时间" prop="handleTime">
              <el-date-picker v-model="form.handleTime" class="full-control" clearable type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择处理时间" />
            </el-form-item>
          </el-col>
          <el-col :span="24"><el-form-item label="处理措施" prop="handleMeasures"><el-input v-model="form.handleMeasures" type="textarea" :rows="3" placeholder="请输入处理措施" maxlength="400" show-word-limit /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="处理结果" prop="handleResult"><el-input v-model="form.handleResult" type="textarea" :rows="3" placeholder="请输入处理结果" maxlength="400" show-word-limit /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="备注" prop="remark"><el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入补充说明" maxlength="300" show-word-limit /></el-form-item></el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer"><el-button type="primary" @click="submitForm">确 定</el-button><el-button @click="cancel">取 消</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { listDailyInspectionMain, getDailyInspectionMain, delDailyInspectionMain, addDailyInspectionMain, updateDailyInspectionMain } from "@/api/devops/dailyInspectionMain"
import { listOpsSystemAsset } from "@/api/devops/systemAsset"
import { listOpsTeamMember } from "@/api/devops/teamMember"

export default {
  name: "DailyInspectionMain",
  dicts: ["ops_health_status", "ops_inspection_result"],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      dailyInspectionMainList: [],
      systemOptions: [],
      teamOptions: [],
      title: "",
      open: false,
      queryParams: { pageNum: 1, pageSize: 10, inspectionDate: null, systemId: null, inspectorName: null, inspectionResult: null },
      form: {},
      rules: {
        inspectionDate: [{ required: true, message: "巡检日期不能为空", trigger: "change" }],
        systemId: [{ required: true, message: "关联系统不能为空", trigger: "change" }],
        inspectorId: [{ required: true, message: "巡检人不能为空", trigger: "change" }],
        inspectionResult: [{ required: true, message: "巡检结果不能为空", trigger: "change" }]
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
      listDailyInspectionMain(this.queryParams).then(response => {
        this.dailyInspectionMainList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    loadOptions() {
      listOpsSystemAsset({ pageNum: 1, pageSize: 1000 }).then(response => { this.systemOptions = response.rows || [] })
      listOpsTeamMember({ pageNum: 1, pageSize: 1000, isOnJob: "1" }).then(response => { this.teamOptions = response.rows || [] })
    },
    formatSystem(item) { return `${item.systemCode || "-"} / ${item.systemName}` },
    handleSystemChange(id) {
      const item = this.systemOptions.find(option => option.id === id)
      this.form.systemName = item ? item.systemName : null
      this.form.serverIp = item ? item.serverIp : this.form.serverIp
    },
    handleInspectorChange(id) {
      const item = this.teamOptions.find(option => option.id === id)
      this.form.inspectorName = item ? item.realName : null
    },
    handleHandlerChange(id) {
      const item = this.teamOptions.find(option => option.id === id)
      this.form.handlerName = item ? item.realName : null
    },
    cancel() { this.open = false; this.reset() },
    reset() {
      this.form = {
        id: null, inspectionDate: null, inspectionTime: null, inspectorId: null, inspectorName: null, systemId: null, systemName: null,
        serverIp: null, cpuUsage: null, memoryUsage: null, diskUsage: null, networkStatus: "1", processStatus: "1",
        alarmInfo: null, alarmCount: 0, inspectionResult: "1", handleMeasures: null, handlerId: null, handlerName: null,
        handleTime: null, handleResult: null, remark: null
      }
      this.resetForm("form")
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery() },
    handleSelectionChange(selection) { this.ids = selection.map(item => item.id); this.single = selection.length !== 1; this.multiple = !selection.length },
    handleAdd() { this.reset(); this.open = true; this.title = "新增日常巡检" },
    handleUpdate(row) {
      this.reset()
      getDailyInspectionMain(row.id || this.ids).then(response => { this.form = response.data; this.open = true; this.title = "修改日常巡检" })
    },
    submitForm() {
      this.handleSystemChange(this.form.systemId); this.handleInspectorChange(this.form.inspectorId); this.handleHandlerChange(this.form.handlerId)
      this.$refs["form"].validate(valid => {
        if (!valid) return
        if (this.form.id != null) {
          updateDailyInspectionMain(this.form).then(() => { this.$modal.msgSuccess("修改成功"); this.open = false; this.getList() })
        } else {
          addDailyInspectionMain(this.form).then(() => { this.$modal.msgSuccess("新增成功"); this.open = false; this.getList() })
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm("确认删除选中的日常巡检？").then(function() { return delDailyInspectionMain(ids) }).then(() => { this.getList(); this.$modal.msgSuccess("删除成功") }).catch(() => {})
    },
    handleExport() { this.download("daily/inspection/export", { ...this.queryParams }, "dailyInspectionMain_" + new Date().getTime() + ".xlsx") }
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
