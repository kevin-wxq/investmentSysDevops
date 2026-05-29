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
      <el-form-item label="运维名称" prop="operationName">
        <el-input
          v-model="queryParams.operationName"
          class="query-control"
          placeholder="请输入运维名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="系统名称" prop="systemName">
        <el-select
          v-model="queryParams.systemName"
          class="query-control"
          placeholder="全部"
          clearable
          filterable
        >
          <el-option v-for="item in systemOptions" :key="item.id" :label="item.systemName" :value="item.systemName" />
        </el-select>
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
          v-hasPermi="['weekly:key-operation:add']"
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
          v-hasPermi="['weekly:key-operation:edit']"
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
          v-hasPermi="['weekly:key-operation:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['weekly:key-operation:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="weeklyKeyOperationDetailList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="所属任务" align="left" prop="taskId" min-width="180" show-overflow-tooltip>
        <template slot-scope="scope">{{ getTaskName(scope.row.taskId) }}</template>
      </el-table-column>
      <el-table-column label="运维类型" align="center" prop="operationType" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ops_operation_type" :value="scope.row.operationType" />
        </template>
      </el-table-column>
      <el-table-column label="运维名称" align="left" prop="operationName" min-width="180" show-overflow-tooltip />
      <el-table-column label="系统名称" align="left" prop="systemName" min-width="160" show-overflow-tooltip />
      <el-table-column label="执行人" align="center" prop="executor" width="120" show-overflow-tooltip />
      <el-table-column label="开始时间" align="center" prop="startTime" width="160" />
      <el-table-column label="结束时间" align="center" prop="endTime" width="160" />
      <el-table-column label="运维结果" align="center" prop="operationResult" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ops_operation_result" :value="scope.row.operationResult" />
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
            v-hasPermi="['weekly:key-operation:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['weekly:key-operation:remove']"
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
            <el-form-item label="所属任务" prop="taskId">
              <el-select v-model="form.taskId" placeholder="请选择所属任务" clearable filterable class="full-control">
                <el-option v-for="item in taskOptions" :key="item.id" :label="item.taskName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="运维类型" prop="operationType">
              <el-select v-model="form.operationType" placeholder="请选择运维类型" clearable class="full-control">
                <el-option v-for="dict in dict.type.ops_operation_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="运维名称" prop="operationName">
              <el-input v-model="form.operationName" placeholder="请输入运维名称" maxlength="100" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="系统名称" prop="systemName">
              <el-select v-model="form.systemName" placeholder="请选择系统名称" clearable filterable class="full-control">
                <el-option v-for="item in systemOptions" :key="item.id" :label="item.systemName" :value="item.systemName" />
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
            <el-form-item label="运维结果" prop="operationResult">
              <el-select v-model="form.operationResult" placeholder="请选择运维结果" clearable class="full-control">
                <el-option v-for="dict in dict.type.ops_operation_result" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startTime">
              <el-date-picker clearable v-model="form.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择开始时间" class="full-control" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-date-picker clearable v-model="form.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择结束时间" class="full-control" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">运维内容</el-divider>
        <el-row :gutter="18">
          <el-col :span="24">
            <el-form-item label="运维内容" prop="operationContent">
              <el-input v-model="form.operationContent" type="textarea" :rows="3" placeholder="请输入运维内容" maxlength="500" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="影响系统" prop="affectedSystems">
              <el-select
                v-model="affectedSystemNames"
                multiple
                clearable
                filterable
                allow-create
                default-first-option
                placeholder="请选择影响系统"
                class="full-control"
                @change="handleAffectedSystemsChange"
              >
                <el-option v-for="item in systemOptions" :key="item.id" :label="item.systemName" :value="item.systemName" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">验证与问题</el-divider>
        <el-row :gutter="18">
          <el-col :span="24">
            <el-form-item label="验证方法" prop="verifyMethod">
              <el-input v-model="form.verifyMethod" type="textarea" :rows="3" placeholder="请输入验证方法" maxlength="500" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="验证结果" prop="verifyResult">
              <el-input v-model="form.verifyResult" type="textarea" :rows="3" placeholder="请输入验证结果" maxlength="500" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="发现的问题" prop="issuesFound">
              <el-input v-model="form.issuesFound" type="textarea" :rows="3" placeholder="请输入发现的问题" maxlength="500" show-word-limit />
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
import { listWeeklyKeyOperationDetail, getWeeklyKeyOperationDetail, delWeeklyKeyOperationDetail, addWeeklyKeyOperationDetail, updateWeeklyKeyOperationDetail } from "@/api/devops/weeklyKeyOperationDetail"
import { listWeeklyOperationTask } from "@/api/devops/weeklyOperationTask"
import { listOpsSystemAsset } from "@/api/devops/systemAsset"
import { listOpsTeamMember } from "@/api/devops/teamMember"

export default {
  name: "WeeklyKeyOperationDetail",
  dicts: ["ops_operation_type", "ops_operation_result"],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      weeklyKeyOperationDetailList: [],
      taskOptions: [],
      systemOptions: [],
      teamOptions: [],
      affectedSystemNames: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        operationName: null,
        systemName: null,
        executor: null,
      },
      form: {},
      rules: {
        taskId: [{ required: true, message: "所属任务不能为空", trigger: "change" }],
        operationName: [{ required: true, message: "运维名称不能为空", trigger: "blur" }],
        operationType: [{ required: true, message: "运维类型不能为空", trigger: "change" }],
        operationResult: [{ required: true, message: "运维结果不能为空", trigger: "change" }]
      }
    }
  },
  created() {
    this.getList()
    this.loadTaskOptions()
    this.loadSystemOptions()
    this.loadTeamOptions()
  },
  methods: {
    getList() {
      this.loading = true
      listWeeklyKeyOperationDetail(this.queryParams).then(response => {
        this.weeklyKeyOperationDetailList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    loadTaskOptions() {
      listWeeklyOperationTask({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.taskOptions = response.rows || []
      })
    },
    loadSystemOptions() {
      listOpsSystemAsset({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.systemOptions = response.rows || []
      })
    },
    loadTeamOptions() {
      listOpsTeamMember({ pageNum: 1, pageSize: 1000, isOnJob: "1" }).then(response => {
        this.teamOptions = response.rows || []
      })
    },
    getTaskName(id) {
      const item = this.taskOptions.find(option => String(option.id) === String(id))
      return item ? item.taskName : "-"
    },
    parseSystemNames(value) {
      if (!value) {
        return []
      }
      return String(value).split(/[、,，]/).map(item => item.trim()).filter(Boolean)
    },
    handleAffectedSystemsChange(value) {
      this.form.affectedSystems = value.join("、")
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null,
        taskId: null,
        operationType: "1",
        operationName: null,
        systemName: null,
        operationContent: null,
        executor: null,
        startTime: null,
        endTime: null,
        operationResult: "1",
        affectedSystems: null,
        verifyMethod: null,
        verifyResult: null,
        issuesFound: null,
        delFlag: null,
        createTime: null,
        createBy: null,
        updateBy: null,
        updateTime: null,
        remark: null
      }
      this.affectedSystemNames = []
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
      this.open = true
      this.title = "添加周关键运维明细"
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getWeeklyKeyOperationDetail(id).then(response => {
        this.form = response.data
        this.affectedSystemNames = this.parseSystemNames(this.form.affectedSystems)
        this.open = true
        this.title = "修改周关键运维明细"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.affectedSystems = this.affectedSystemNames.join("、")
          if (this.form.id != null) {
            updateWeeklyKeyOperationDetail(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addWeeklyKeyOperationDetail(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除周关键运维明细编号为"' + ids + '"的数据项？').then(function() {
        return delWeeklyKeyOperationDetail(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('weekly/key-operation/export', {
        ...this.queryParams
      }, `weeklyKeyOperationDetail_${new Date().getTime()}.xlsx`)
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
