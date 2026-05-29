<template>
  <div class="app-container ops-page">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="92px" class="ops-query-form">
      <el-form-item label="所属班次" prop="shiftId">
        <el-select v-model="queryParams.shiftId" class="query-control" placeholder="全部" clearable filterable>
          <el-option v-for="item in shiftOptions" :key="item.id" :label="formatShift(item)" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="日志类型" prop="logType">
        <el-select v-model="queryParams.logType" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_log_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="严重程度" prop="severity">
        <el-select v-model="queryParams.severity" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_severity" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="处理人" prop="handlerName">
        <el-input v-model="queryParams.handlerName" class="query-control" placeholder="处理人" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="日志时间" prop="logTime">
        <el-date-picker v-model="queryParams.logTime" class="query-control" clearable type="date" value-format="yyyy-MM-dd" placeholder="日志时间" />
      </el-form-item>
      <el-form-item class="query-actions">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['ops:shift-log:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['ops:shift-log:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['ops:shift-log:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['ops:shift-log:export']">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="shiftLogList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="所属班次" align="left" prop="shiftId" min-width="180" show-overflow-tooltip>
        <template slot-scope="scope">{{ getShiftName(scope.row.shiftId) }}</template>
      </el-table-column>
      <el-table-column label="日志时间" align="center" prop="logTime" width="120">
        <template slot-scope="scope">{{ parseTime(scope.row.logTime, "{y}-{m}-{d}") }}</template>
      </el-table-column>
      <el-table-column label="日志类型" align="center" prop="logType" width="110">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_log_type" :value="scope.row.logType" /></template>
      </el-table-column>
      <el-table-column label="严重程度" align="center" prop="severity" width="110">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_severity" :value="scope.row.severity" /></template>
      </el-table-column>
      <el-table-column label="日志内容" align="left" prop="logContent" min-width="220" show-overflow-tooltip />
      <el-table-column label="处理人" align="center" prop="handlerName" min-width="110" />
      <el-table-column label="是否升级" align="center" prop="isEscalated" width="110">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_yes_no" :value="scope.row.isEscalated" /></template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['ops:shift-log:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['ops:shift-log:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="880px" append-to-body class="ops-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="104px">
        <el-divider content-position="left">日志信息</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="所属班次" prop="shiftId">
              <el-select v-model="form.shiftId" class="full-control" placeholder="请选择班次" clearable filterable>
                <el-option v-for="item in shiftOptions" :key="item.id" :label="formatShift(item)" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="日志时间" prop="logTime">
              <el-date-picker v-model="form.logTime" class="full-control" clearable type="date" value-format="yyyy-MM-dd" placeholder="请选择日志时间" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="日志类型" prop="logType">
              <el-select v-model="form.logType" class="full-control" placeholder="请选择日志类型">
                <el-option v-for="dict in dict.type.ops_log_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="严重程度" prop="severity">
              <el-select v-model="form.severity" class="full-control" placeholder="请选择严重程度">
                <el-option v-for="dict in dict.type.ops_severity" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="日志内容" prop="logContent">
              <el-input v-model="form.logContent" type="textarea" :rows="4" placeholder="请输入日志内容" maxlength="500" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">处理信息</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="处理人" prop="handlerId">
              <el-select v-model="form.handlerId" class="full-control" placeholder="请选择处理人" clearable filterable @change="handleHandlerChange">
                <el-option v-for="item in teamOptions" :key="item.id" :label="item.realName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否升级" prop="isEscalated">
              <el-select v-model="form.isEscalated" class="full-control" placeholder="请选择是否升级">
                <el-option v-for="dict in dict.type.ops_yes_no" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="处理结果" prop="handlingResult">
              <el-input v-model="form.handlingResult" type="textarea" :rows="3" placeholder="请输入处理结果" maxlength="400" show-word-limit />
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
import { listShiftLog, getShiftLog, delShiftLog, addShiftLog, updateShiftLog } from "@/api/devops/shiftLog"
import { listDutyShift } from "@/api/devops/dutyShift"
import { listOpsTeamMember } from "@/api/devops/teamMember"

export default {
  name: "ShiftLog",
  dicts: ["ops_log_type", "ops_severity", "ops_yes_no"],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      shiftLogList: [],
      shiftOptions: [],
      teamOptions: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        shiftId: null,
        logType: null,
        severity: null,
        handlerName: null,
        logTime: null
      },
      form: {},
      rules: {
        shiftId: [{ required: true, message: "所属班次不能为空", trigger: "change" }],
        logTime: [{ required: true, message: "日志时间不能为空", trigger: "change" }],
        logType: [{ required: true, message: "日志类型不能为空", trigger: "change" }],
        severity: [{ required: true, message: "严重程度不能为空", trigger: "change" }],
        logContent: [{ required: true, message: "日志内容不能为空", trigger: "blur" }]
      }
    }
  },
  created() {
    this.getList()
    this.loadShiftOptions()
    this.loadTeamOptions()
  },
  methods: {
    getList() {
      this.loading = true
      listShiftLog(this.queryParams).then(response => {
        this.shiftLogList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    loadShiftOptions() {
      listDutyShift({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.shiftOptions = response.rows || []
      })
    },
    loadTeamOptions() {
      listOpsTeamMember({ pageNum: 1, pageSize: 1000, isOnJob: "1" }).then(response => {
        this.teamOptions = response.rows || []
      })
    },
    formatShift(item) {
      const date = this.parseTime(item.shiftDate, "{y}-{m}-{d}") || "-"
      return `${date} / ${item.shiftName}`
    },
    getShiftName(id) {
      const item = this.shiftOptions.find(option => option.id === id)
      return item ? this.formatShift(item) : "-"
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
        id: null,
        shiftId: null,
        logTime: null,
        logType: "1",
        logContent: null,
        severity: "1",
        handlerId: null,
        handlerName: null,
        handlingResult: null,
        isEscalated: "0",
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
      this.title = "新增值班日志"
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getShiftLog(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改值班日志"
      })
    },
    submitForm() {
      this.handleHandlerChange(this.form.handlerId)
      this.$refs["form"].validate(valid => {
        if (!valid) return
        if (this.form.id != null) {
          updateShiftLog(this.form).then(() => {
            this.$modal.msgSuccess("修改成功")
            this.open = false
            this.getList()
          })
        } else {
          addShiftLog(this.form).then(() => {
            this.$modal.msgSuccess("新增成功")
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm("确认删除选中的值班日志？").then(function() {
        return delShiftLog(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download("ops/shift-log/export", { ...this.queryParams }, "shiftLog_" + new Date().getTime() + ".xlsx")
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
