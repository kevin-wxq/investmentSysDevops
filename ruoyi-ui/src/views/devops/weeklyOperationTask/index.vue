<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="92px"
      class="ops-query-form"
    >
      <el-form-item label="任务名称" prop="taskName">
        <el-input
          v-model="queryParams.taskName"
          class="query-control"
          placeholder="请输入任务名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="执行人" prop="executor">
        <el-input
          v-model="queryParams.executor"
          class="query-control"
          placeholder="请输入执行人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务状态" prop="taskStatus">
        <el-select
          v-model="queryParams.taskStatus"
          class="query-control"
          placeholder="全部"
          clearable
        >
          <el-option v-for="dict in dict.type.ops_task_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="计划日期" prop="plannedDate">
        <el-date-picker
          clearable
          v-model="queryParams.plannedDate"
          class="query-control"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择计划日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item class="query-actions">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['weekly:operation-task:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['weekly:operation-task:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['weekly:operation-task:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['weekly:operation-task:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="weeklyOperationTaskList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="任务名称" align="left" prop="taskName" min-width="180" show-overflow-tooltip />
      <el-table-column label="执行人" align="center" prop="executor" width="120" show-overflow-tooltip />
      <el-table-column label="需求方" align="center" prop="requester" width="130" show-overflow-tooltip />
      <el-table-column label="计划日期" align="center" prop="plannedDate" width="120" />
      <el-table-column label="任务状态" align="center" prop="taskStatus" width="110">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ops_task_status" :value="scope.row.taskStatus" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="left" prop="remark" min-width="160" show-overflow-tooltip />
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['weekly:operation-task:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['weekly:operation-task:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body class="ops-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="108px">
        <el-divider content-position="left">基础信息</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="任务名称" prop="taskName">
              <el-input v-model="form.taskName" placeholder="请输入任务名称" maxlength="100" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务状态" prop="taskStatus">
              <el-select v-model="form.taskStatus" placeholder="请选择任务状态" clearable class="full-control">
                <el-option v-for="dict in dict.type.ops_task_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行人" prop="executor">
              <el-select v-model="form.executor" placeholder="请选择执行人" clearable filterable class="full-control">
                <el-option v-for="item in teamOptions" :key="item.id" :label="item.realName" :value="item.realName" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="需求方" prop="requester">
              <el-input v-model="form.requester" placeholder="请输入需求方" maxlength="60" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划日期" prop="plannedDate">
              <el-date-picker clearable v-model="form.plannedDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择计划日期" class="full-control" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">任务进展</el-divider>
        <el-row :gutter="18">
          <el-col :span="24">
            <el-form-item label="需求描述" prop="requirementDesc">
              <el-input v-model="form.requirementDesc" type="textarea" :rows="3" placeholder="请输入需求描述" maxlength="500" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="本周进展" prop="thisWeekProgress">
              <el-input v-model="form.thisWeekProgress" type="textarea" :rows="3" placeholder="请输入本周进展" maxlength="500" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="下周计划" prop="nextWeekPlan">
              <el-input v-model="form.nextWeekPlan" type="textarea" :rows="3" placeholder="请输入下周计划" maxlength="500" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">风险与备注</el-divider>
        <el-row :gutter="18">
          <el-col :span="24">
            <el-form-item label="问题/风险" prop="issueRisk">
              <el-input v-model="form.issueRisk" type="textarea" :rows="3" placeholder="请输入问题/风险" maxlength="500" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入内容" maxlength="300" show-word-limit />
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
import { listWeeklyOperationTask, getWeeklyOperationTask, delWeeklyOperationTask, addWeeklyOperationTask, updateWeeklyOperationTask } from "@/api/devops/weeklyOperationTask"
import { listOpsTeamMember } from "@/api/devops/teamMember"

export default {
  name: "WeeklyOperationTask",
  dicts: ["ops_task_status"],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      weeklyOperationTaskList: [],
      teamOptions: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskName: null,
        executor: null,
        taskStatus: null,
        plannedDate: null,
      },
      form: {},
      rules: {
        taskName: [{ required: true, message: "任务名称不能为空", trigger: "blur" }],
        taskStatus: [{ required: true, message: "任务状态不能为空", trigger: "change" }]
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
      listWeeklyOperationTask(this.queryParams).then(response => {
        this.weeklyOperationTaskList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    loadTeamOptions() {
      listOpsTeamMember({ pageNum: 1, pageSize: 1000, isOnJob: "1" }).then(response => {
        this.teamOptions = response.rows || []
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null,
        reportId: null,
        taskName: null,
        executor: null,
        requester: null,
        plannedDate: null,
        taskStatus: "1",
        requirementDesc: null,
        thisWeekProgress: null,
        nextWeekPlan: null,
        issueRisk: null,
        delFlag: null,
        createTime: null,
        createBy: null,
        updateBy: null,
        updateTime: null,
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
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.form.reportId = null
      this.open = true
      this.title = "添加周运维任务"
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getWeeklyOperationTask(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改周运维任务"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWeeklyOperationTask(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addWeeklyOperationTask(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除周运维任务编号为"' + ids + '"的数据项？').then(function() {
        return delWeeklyOperationTask(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('weekly/operation-task/export', {
        ...this.queryParams
      }, `weeklyOperationTask_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.ops-query-form .el-form-item {
  margin-right: 12px;
}

.ops-query-form ::v-deep .el-form-item__label {
  white-space: nowrap;
}

.query-control {
  width: 220px;
}

.query-actions {
  white-space: nowrap;
}

.full-control {
  width: 100%;
}

.ops-dialog ::v-deep .el-dialog__body {
  padding-top: 8px;
}

.ops-dialog ::v-deep .el-divider--horizontal {
  margin: 12px 0 22px;
}

.ops-dialog ::v-deep .el-divider__text {
  color: #606266;
  font-weight: 600;
}

.ops-dialog ::v-deep .el-form-item__label {
  white-space: nowrap;
}
</style>
