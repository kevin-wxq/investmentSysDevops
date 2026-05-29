<template>
  <div class="app-container ops-page">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :model="queryParams"
      size="small"
      :inline="true"
      label-width="92px"
      class="ops-query-form"
    >
      <el-form-item label="姓名" prop="realName">
        <el-input v-model="queryParams.realName" class="query-control" placeholder="姓名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="工号" prop="employeeNo">
        <el-input v-model="queryParams.employeeNo" class="query-control" placeholder="工号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="所属小组" prop="team">
        <el-input v-model="queryParams.team" class="query-control" placeholder="所属小组" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="角色" prop="roleType">
        <el-select v-model="queryParams.roleType" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_team_role" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="在岗状态" prop="isOnJob">
        <el-select v-model="queryParams.isOnJob" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_on_job_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item class="query-actions">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['ops:team-member:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['ops:team-member:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['ops:team-member:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['ops:team-member:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="teamMemberList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="工号" align="center" prop="employeeNo" min-width="120" />
      <el-table-column label="姓名" align="center" prop="realName" min-width="110" />
      <el-table-column label="所属小组" align="center" prop="team" min-width="120" show-overflow-tooltip />
      <el-table-column label="角色" align="center" prop="roleType" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ops_team_role" :value="scope.row.roleType" />
        </template>
      </el-table-column>
      <el-table-column label="在岗状态" align="center" prop="isOnJob" width="110">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ops_on_job_status" :value="scope.row.isOnJob" />
        </template>
      </el-table-column>
      <el-table-column label="手机号" align="center" prop="mobile" min-width="130" />
      <el-table-column label="邮箱" align="left" prop="email" min-width="180" show-overflow-tooltip />
      <el-table-column label="技能标签" align="left" prop="skillTags" min-width="180" show-overflow-tooltip />
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['ops:team-member:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['ops:team-member:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="860px" append-to-body class="ops-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="104px">
        <el-divider content-position="left">成员信息</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="工号" prop="employeeNo">
              <el-input v-model="form.employeeNo" placeholder="系统自动生成" readonly>
                <el-button slot="append" icon="el-icon-refresh" :disabled="!!form.id" @click="loadNextEmployeeNo" />
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="realName">
              <el-input v-model="form.realName" placeholder="请输入姓名" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属小组" prop="team">
              <el-input v-model="form.team" placeholder="例如 运行组 / 数据库组" maxlength="40" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色" prop="roleType">
              <el-select v-model="form.roleType" placeholder="请选择角色" class="full-control">
                <el-option v-for="dict in dict.type.ops_team_role" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="在岗状态" prop="isOnJob">
              <el-select v-model="form.isOnJob" placeholder="请选择在岗状态" class="full-control">
                <el-option v-for="dict in dict.type.ops_on_job_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入职日期" prop="entryDate">
              <el-date-picker v-model="form.entryDate" clearable type="date" value-format="yyyy-MM-dd" placeholder="请选择入职日期" class="full-control" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">联系方式与能力</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="手机号" prop="mobile">
              <el-input v-model="form.mobile" placeholder="请输入手机号" maxlength="20" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="80" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="orderNum">
              <el-input-number v-model="form.orderNum" :min="0" :max="9999" controls-position="right" class="full-control" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="技能标签" prop="skillTags">
              <el-input v-model="form.skillTags" placeholder="逗号分隔，例如 Linux, MySQL" maxlength="120" />
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
import {
  listOpsTeamMember,
  getOpsTeamMember,
  delOpsTeamMember,
  addOpsTeamMember,
  updateOpsTeamMember,
  nextOpsTeamMemberNo
} from "@/api/devops/teamMember"

const validatePhone = (rule, value, callback) => {
  if (!value || /^1[3-9]\d{9}$|^\d{3,4}-?\d{7,8}$/.test(value)) {
    callback()
  } else {
    callback(new Error("请输入正确的联系电话"))
  }
}

export default {
  name: "OpsTeamMember",
  dicts: ["ops_team_role", "ops_on_job_status"],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      teamMemberList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        realName: null,
        employeeNo: null,
        team: null,
        roleType: null,
        isOnJob: null
      },
      form: {},
      rules: {
        realName: [{ required: true, message: "姓名不能为空", trigger: "blur" }],
        roleType: [{ required: true, message: "角色不能为空", trigger: "change" }],
        isOnJob: [{ required: true, message: "在岗状态不能为空", trigger: "change" }],
        mobile: [{ validator: validatePhone, trigger: "blur" }],
        email: [{ type: "email", message: "请输入正确的邮箱地址", trigger: "blur" }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listOpsTeamMember(this.queryParams).then(response => {
        this.teamMemberList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    loadNextEmployeeNo() {
      nextOpsTeamMemberNo().then(response => {
        this.form.employeeNo = response.data
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null,
        userId: null,
        realName: null,
        employeeNo: null,
        mobile: null,
        email: null,
        team: null,
        roleType: "3",
        skillTags: null,
        isOnJob: "1",
        entryDate: null,
        orderNum: 0,
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
      this.title = "新增团队成员"
      this.loadNextEmployeeNo()
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getOpsTeamMember(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改团队成员"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        if (this.form.id != null) {
          updateOpsTeamMember(this.form).then(() => {
            this.$modal.msgSuccess("修改成功")
            this.open = false
            this.getList()
          })
        } else {
          addOpsTeamMember(this.form).then(() => {
            this.$modal.msgSuccess("新增成功")
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm("确认删除选中的团队成员？").then(function() {
        return delOpsTeamMember(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download("ops/team-member/export", { ...this.queryParams }, "teamMember_" + new Date().getTime() + ".xlsx")
    }
  }
}
</script>

<style scoped>
.ops-query-form ::v-deep .el-form-item__label {
  white-space: nowrap;
}

.query-control {
  width: 210px;
}

.query-actions {
  margin-left: 4px;
}

.ops-dialog ::v-deep .el-divider__text {
  color: #606266;
  font-weight: 600;
}

.full-control {
  width: 100%;
}

.ops-dialog ::v-deep .el-form-item__label {
  white-space: nowrap;
}
</style>
