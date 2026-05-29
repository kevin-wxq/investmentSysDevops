<template>
  <div class="app-container ops-page">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="92px" class="ops-query-form">
      <el-form-item label="班次名称" prop="shiftName">
        <el-input v-model="queryParams.shiftName" class="query-control" placeholder="班次名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="班次日期" prop="shiftDate">
        <el-date-picker v-model="queryParams.shiftDate" class="query-control" clearable type="date" value-format="yyyy-MM-dd" placeholder="班次日期" />
      </el-form-item>
      <el-form-item label="班次类型" prop="shiftType">
        <el-select v-model="queryParams.shiftType" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_shift_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="班次状态" prop="shiftStatus">
        <el-select v-model="queryParams.shiftStatus" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_shift_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="带班领导" prop="leaderName">
        <el-input v-model="queryParams.leaderName" class="query-control" placeholder="带班领导" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item class="query-actions">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['ops:duty-shift:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['ops:duty-shift:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['ops:duty-shift:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['ops:duty-shift:export']">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="dutyShiftList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="班次名称" align="left" prop="shiftName" min-width="150" show-overflow-tooltip />
      <el-table-column label="班次日期" align="center" prop="shiftDate" width="120">
        <template slot-scope="scope">{{ parseTime(scope.row.shiftDate, "{y}-{m}-{d}") }}</template>
      </el-table-column>
      <el-table-column label="班次类型" align="center" prop="shiftType" width="110">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_shift_type" :value="scope.row.shiftType" /></template>
      </el-table-column>
      <el-table-column label="带班领导" align="center" prop="leaderName" min-width="120" />
      <el-table-column label="值班成员" align="left" prop="memberIds" min-width="180" show-overflow-tooltip>
        <template slot-scope="scope">{{ getMemberNames(scope.row.memberIds) }}</template>
      </el-table-column>
      <el-table-column label="班次状态" align="center" prop="shiftStatus" width="110">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_shift_status" :value="scope.row.shiftStatus" /></template>
      </el-table-column>
      <el-table-column label="交接备注" align="left" prop="handoverNotes" min-width="180" show-overflow-tooltip />
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['ops:duty-shift:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['ops:duty-shift:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="860px" append-to-body class="ops-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="104px">
        <el-divider content-position="left">班次信息</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="班次名称" prop="shiftName">
              <el-input v-model="form.shiftName" placeholder="请输入班次名称" maxlength="60" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="班次日期" prop="shiftDate">
              <el-date-picker v-model="form.shiftDate" class="full-control" clearable type="date" value-format="yyyy-MM-dd" placeholder="请选择班次日期" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="班次类型" prop="shiftType">
              <el-select v-model="form.shiftType" class="full-control" placeholder="请选择班次类型">
                <el-option v-for="dict in dict.type.ops_shift_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="班次状态" prop="shiftStatus">
              <el-select v-model="form.shiftStatus" class="full-control" placeholder="请选择班次状态">
                <el-option v-for="dict in dict.type.ops_shift_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">人员安排</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="带班领导" prop="leaderId">
              <el-select v-model="form.leaderId" class="full-control" placeholder="请选择带班领导" clearable filterable @change="handleLeaderChange">
                <el-option v-for="item in teamOptions" :key="item.id" :label="item.realName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="值班成员" prop="memberIds">
              <el-select v-model="memberIdsValue" class="full-control" placeholder="请选择值班成员" multiple clearable filterable>
                <el-option v-for="item in teamOptions" :key="item.id" :label="item.realName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="交接备注" prop="handoverNotes">
              <el-input v-model="form.handoverNotes" type="textarea" :rows="3" placeholder="请输入交接备注" maxlength="300" show-word-limit />
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
import { listDutyShift, getDutyShift, delDutyShift, addDutyShift, updateDutyShift } from "@/api/devops/dutyShift"
import { listOpsTeamMember } from "@/api/devops/teamMember"

export default {
  name: "DutyShift",
  dicts: ["ops_shift_type", "ops_shift_status"],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      dutyShiftList: [],
      teamOptions: [],
      memberIdsValue: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        shiftName: null,
        shiftDate: null,
        shiftType: null,
        leaderName: null,
        shiftStatus: null
      },
      form: {},
      rules: {
        shiftName: [{ required: true, message: "班次名称不能为空", trigger: "blur" }],
        shiftDate: [{ required: true, message: "班次日期不能为空", trigger: "change" }],
        shiftType: [{ required: true, message: "班次类型不能为空", trigger: "change" }],
        shiftStatus: [{ required: true, message: "班次状态不能为空", trigger: "change" }]
      }
    }
  },
  created() {
    this.getList()
    this.loadTeamOptions()
  },
  methods: {
    getList() {
      this.loading = true
      listDutyShift(this.queryParams).then(response => {
        this.dutyShiftList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    loadTeamOptions() {
      listOpsTeamMember({ pageNum: 1, pageSize: 1000, isOnJob: "1" }).then(response => {
        this.teamOptions = response.rows || []
      })
    },
    getMemberNames(memberIds) {
      if (!memberIds) return "-"
      const ids = String(memberIds).split(",").map(id => Number(id))
      return ids.map(id => {
        const item = this.teamOptions.find(option => option.id === id)
        return item ? item.realName : id
      }).join("、")
    },
    handleLeaderChange(id) {
      const item = this.teamOptions.find(option => option.id === id)
      this.form.leaderName = item ? item.realName : null
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null,
        shiftName: null,
        shiftDate: null,
        shiftType: "1",
        leaderId: null,
        leaderName: null,
        memberIds: null,
        handoverNotes: null,
        shiftStatus: "1",
        remark: null
      }
      this.memberIdsValue = []
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
      this.title = "新增值班班次"
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getDutyShift(id).then(response => {
        this.form = response.data
        this.memberIdsValue = this.form.memberIds ? String(this.form.memberIds).split(",").map(item => Number(item)) : []
        this.open = true
        this.title = "修改值班班次"
      })
    },
    submitForm() {
      this.form.memberIds = this.memberIdsValue.join(",")
      this.handleLeaderChange(this.form.leaderId)
      this.$refs["form"].validate(valid => {
        if (!valid) return
        if (this.form.id != null) {
          updateDutyShift(this.form).then(() => {
            this.$modal.msgSuccess("修改成功")
            this.open = false
            this.getList()
          })
        } else {
          addDutyShift(this.form).then(() => {
            this.$modal.msgSuccess("新增成功")
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm("确认删除选中的值班班次？").then(function() {
        return delDutyShift(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download("ops/duty-shift/export", { ...this.queryParams }, "dutyShift_" + new Date().getTime() + ".xlsx")
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
