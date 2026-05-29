<template>
  <div class="app-container ops-page">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="92px" class="ops-query-form">
      <el-form-item label="关联系统" prop="systemId">
        <el-select v-model="queryParams.systemId" class="query-control" placeholder="全部" clearable filterable>
          <el-option v-for="item in systemOptions" :key="item.id" :label="formatSystem(item)" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="备份类型" prop="backupType">
        <el-select v-model="queryParams.backupType" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_backup_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="备份结果" prop="backupResult">
        <el-select v-model="queryParams.backupResult" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_backup_result" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="备份时间" prop="backupTime">
        <el-date-picker v-model="queryParams.backupTime" class="query-control" clearable type="date" value-format="yyyy-MM-dd" placeholder="备份时间" />
      </el-form-item>
      <el-form-item class="query-actions">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['ops:backup:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['ops:backup:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['ops:backup:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['ops:backup:export']">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="backupRecordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="关联系统" align="left" prop="systemId" min-width="180" show-overflow-tooltip>
        <template slot-scope="scope">{{ getSystemName(scope.row.systemId) }}</template>
      </el-table-column>
      <el-table-column label="备份类型" align="center" prop="backupType" width="110">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_backup_type" :value="scope.row.backupType" /></template>
      </el-table-column>
      <el-table-column label="备份时间" align="center" prop="backupTime" width="120">
        <template slot-scope="scope">{{ parseTime(scope.row.backupTime, "{y}-{m}-{d}") }}</template>
      </el-table-column>
      <el-table-column label="备份大小(MB)" align="right" prop="backupSizeMb" width="120" />
      <el-table-column label="备份结果" align="center" prop="backupResult" width="110">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_backup_result" :value="scope.row.backupResult" /></template>
      </el-table-column>
      <el-table-column label="恢复验证" align="center" prop="restoreTested" width="110">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_restore_tested" :value="scope.row.restoreTested" /></template>
      </el-table-column>
      <el-table-column label="执行人" align="center" prop="executorId" width="110">
        <template slot-scope="scope">{{ getTeamName(scope.row.executorId) }}</template>
      </el-table-column>
      <el-table-column label="备份路径" align="left" prop="backupPath" min-width="220" show-overflow-tooltip />
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['ops:backup:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['ops:backup:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body class="ops-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="112px">
        <el-divider content-position="left">备份对象</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="关联系统" prop="systemId">
              <el-select v-model="form.systemId" class="full-control" placeholder="请选择关联系统" clearable filterable>
                <el-option v-for="item in systemOptions" :key="item.id" :label="formatSystem(item)" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行人" prop="executorId">
              <el-select v-model="form.executorId" class="full-control" placeholder="请选择执行人" clearable filterable>
                <el-option v-for="item in teamOptions" :key="item.id" :label="item.realName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">备份信息</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="备份类型" prop="backupType">
              <el-select v-model="form.backupType" class="full-control" placeholder="请选择备份类型">
                <el-option v-for="dict in dict.type.ops_backup_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备份时间" prop="backupTime">
              <el-date-picker v-model="form.backupTime" class="full-control" clearable type="date" value-format="yyyy-MM-dd" placeholder="请选择备份时间" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备份大小(MB)" prop="backupSizeMb">
              <el-input-number v-model="form.backupSizeMb" class="full-control" :min="0" :max="999999999" controls-position="right" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备份结果" prop="backupResult">
              <el-select v-model="form.backupResult" class="full-control" placeholder="请选择备份结果">
                <el-option v-for="dict in dict.type.ops_backup_result" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="恢复验证" prop="restoreTested">
              <el-select v-model="form.restoreTested" class="full-control" placeholder="请选择恢复验证结果">
                <el-option v-for="dict in dict.type.ops_restore_tested" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备份路径" prop="backupPath">
              <el-input v-model="form.backupPath" placeholder="请输入备份路径" maxlength="200" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="错误信息" prop="errorMsg">
              <el-input v-model="form.errorMsg" type="textarea" :rows="3" placeholder="失败或部分成功时填写" maxlength="400" show-word-limit />
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
import { listBackupRecord, getBackupRecord, delBackupRecord, addBackupRecord, updateBackupRecord } from "@/api/devops/backupRecord"
import { listOpsSystemAsset } from "@/api/devops/systemAsset"
import { listOpsTeamMember } from "@/api/devops/teamMember"

export default {
  name: "BackupRecord",
  dicts: ["ops_backup_type", "ops_backup_result", "ops_restore_tested"],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      backupRecordList: [],
      systemOptions: [],
      teamOptions: [],
      title: "",
      open: false,
      queryParams: { pageNum: 1, pageSize: 10, systemId: null, backupType: null, backupTime: null, backupResult: null },
      form: {},
      rules: {
        systemId: [{ required: true, message: "关联系统不能为空", trigger: "change" }],
        backupType: [{ required: true, message: "备份类型不能为空", trigger: "change" }],
        backupTime: [{ required: true, message: "备份时间不能为空", trigger: "change" }],
        backupResult: [{ required: true, message: "备份结果不能为空", trigger: "change" }]
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
      listBackupRecord(this.queryParams).then(response => {
        this.backupRecordList = response.rows
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
      this.form = { id: null, systemId: null, backupType: "1", backupTime: null, backupSizeMb: 0, backupResult: "1", backupPath: null, restoreTested: "0", executorId: null, errorMsg: null, remark: null }
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
      this.title = "新增备份记录"
    },
    handleUpdate(row) {
      this.reset()
      getBackupRecord(row.id || this.ids).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改备份记录"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) return
        if (this.form.id != null) {
          updateBackupRecord(this.form).then(() => { this.$modal.msgSuccess("修改成功"); this.open = false; this.getList() })
        } else {
          addBackupRecord(this.form).then(() => { this.$modal.msgSuccess("新增成功"); this.open = false; this.getList() })
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm("确认删除选中的备份记录？").then(function() { return delBackupRecord(ids) }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download("ops/backup/export", { ...this.queryParams }, "backupRecord_" + new Date().getTime() + ".xlsx")
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
