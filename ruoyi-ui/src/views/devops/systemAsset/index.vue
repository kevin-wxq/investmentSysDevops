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
      <el-form-item label="系统编码" prop="systemCode">
        <el-input v-model="queryParams.systemCode" class="query-control" placeholder="系统编码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="系统名称" prop="systemName">
        <el-input v-model="queryParams.systemName" class="query-control" placeholder="系统名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="系统类型" prop="systemType">
        <el-select v-model="queryParams.systemType" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_system_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="重要等级" prop="importanceLevel">
        <el-select v-model="queryParams.importanceLevel" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_importance_level" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="systemStatus">
        <el-select v-model="queryParams.systemStatus" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_system_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item class="query-actions">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['ops:system-asset:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['ops:system-asset:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['ops:system-asset:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['ops:system-asset:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="systemAssetList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="系统编码" align="center" prop="systemCode" min-width="120" />
      <el-table-column label="系统名称" align="left" prop="systemName" min-width="170" show-overflow-tooltip />
      <el-table-column label="系统类型" align="center" prop="systemType" width="110">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ops_system_type" :value="scope.row.systemType" />
        </template>
      </el-table-column>
      <el-table-column label="重要等级" align="center" prop="importanceLevel" width="110">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ops_importance_level" :value="scope.row.importanceLevel" />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="systemStatus" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ops_system_status" :value="scope.row.systemStatus" />
        </template>
      </el-table-column>
      <el-table-column label="所属部门" align="center" prop="department" min-width="120" show-overflow-tooltip />
      <el-table-column label="部署IP" align="center" prop="serverIp" min-width="130" show-overflow-tooltip />
      <el-table-column label="技术负责人" align="center" prop="techOwnerId" min-width="120" show-overflow-tooltip>
        <template slot-scope="scope">{{ getTeamName(scope.row.techOwnerId) }}</template>
      </el-table-column>
      <el-table-column label="访问地址" align="left" prop="systemUrl" min-width="180" show-overflow-tooltip />
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['ops:system-asset:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['ops:system-asset:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="920px" append-to-body class="ops-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="108px">
        <el-divider content-position="left">基础信息</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="系统编码" prop="systemCode">
              <el-input v-model="form.systemCode" placeholder="系统自动生成" readonly>
                <el-button slot="append" icon="el-icon-refresh" :disabled="!!form.id" @click="loadNextCode" />
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="系统名称" prop="systemName">
              <el-input v-model="form.systemName" placeholder="请输入系统名称" maxlength="80" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="系统类型" prop="systemType">
              <el-select v-model="form.systemType" placeholder="请选择系统类型" class="full-control" @change="handleSystemTypeChange">
                <el-option v-for="dict in dict.type.ops_system_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="重要等级" prop="importanceLevel">
              <el-select v-model="form.importanceLevel" placeholder="请选择重要等级" class="full-control">
                <el-option v-for="dict in dict.type.ops_importance_level" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="运行状态" prop="systemStatus">
              <el-select v-model="form.systemStatus" placeholder="请选择运行状态" class="full-control">
                <el-option v-for="dict in dict.type.ops_system_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上线日期" prop="goLiveDate">
              <el-date-picker v-model="form.goLiveDate" clearable type="date" value-format="yyyy-MM-dd" placeholder="请选择上线日期" class="full-control" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">归属与技术</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="所属部门" prop="department">
              <el-input v-model="form.department" placeholder="请输入所属部门" maxlength="60" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="业务负责人" prop="businessOwner">
              <el-input v-model="form.businessOwner" placeholder="请输入业务负责人" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="技术负责人" prop="techOwnerId">
              <el-select v-model="form.techOwnerId" placeholder="请选择技术负责人" clearable filterable class="full-control">
                <el-option v-for="item in teamOptions" :key="item.id" :label="item.realName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="orderNum">
              <el-input-number v-model="form.orderNum" :min="0" :max="9999" controls-position="right" class="full-control" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部署IP" prop="serverIp">
              <el-input v-model="form.serverIp" placeholder="例如 10.1.10.12" maxlength="64" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="访问地址" prop="systemUrl">
              <el-input v-model="form.systemUrl" placeholder="https:// 或 http:// 地址" maxlength="200" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库类型" prop="dbType">
              <el-input v-model="form.dbType" placeholder="例如 MySQL / Oracle" maxlength="40" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开发语言" prop="devLang">
              <el-input v-model="form.devLang" placeholder="例如 Java / Vue" maxlength="60" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">备注</el-divider>
        <el-row :gutter="18">
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
  listOpsSystemAsset,
  getOpsSystemAsset,
  delOpsSystemAsset,
  addOpsSystemAsset,
  updateOpsSystemAsset,
  nextOpsSystemAssetCode
} from "@/api/devops/systemAsset"
import { listOpsTeamMember } from "@/api/devops/teamMember"

export default {
  name: "OpsSystemAsset",
  dicts: ["ops_system_type", "ops_importance_level", "ops_system_status"],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      systemAssetList: [],
      teamOptions: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        systemCode: null,
        systemName: null,
        systemType: null,
        importanceLevel: null,
        systemStatus: null
      },
      form: {},
      rules: {
        systemName: [{ required: true, message: "系统名称不能为空", trigger: "blur" }],
        systemType: [{ required: true, message: "系统类型不能为空", trigger: "change" }],
        importanceLevel: [{ required: true, message: "重要等级不能为空", trigger: "change" }],
        systemStatus: [{ required: true, message: "运行状态不能为空", trigger: "change" }],
        systemUrl: [{ type: "url", message: "请输入正确的访问地址", trigger: "blur" }]
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
      listOpsSystemAsset(this.queryParams).then(response => {
        this.systemAssetList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    loadTeamOptions() {
      listOpsTeamMember({ pageNum: 1, pageSize: 1000, isOnJob: "1" }).then(response => {
        this.teamOptions = response.rows || []
      })
    },
    getTeamName(id) {
      const item = this.teamOptions.find(option => option.id === id)
      return item ? item.realName : "-"
    },
    loadNextCode() {
      nextOpsSystemAssetCode(this.form.systemType).then(response => {
        this.form.systemCode = response.data
      })
    },
    handleSystemTypeChange() {
      if (!this.form.id) {
        this.loadNextCode()
      }
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null,
        systemCode: null,
        systemName: null,
        systemType: "1",
        importanceLevel: "2",
        systemUrl: null,
        serverIp: null,
        dbType: null,
        devLang: null,
        department: null,
        businessOwner: null,
        techOwnerId: null,
        goLiveDate: null,
        systemStatus: "1",
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
      this.title = "新增系统资产"
      this.loadNextCode()
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getOpsSystemAsset(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改系统资产"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        if (this.form.id != null) {
          updateOpsSystemAsset(this.form).then(() => {
            this.$modal.msgSuccess("修改成功")
            this.open = false
            this.getList()
          })
        } else {
          addOpsSystemAsset(this.form).then(() => {
            this.$modal.msgSuccess("新增成功")
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm("确认删除选中的系统资产？").then(function() {
        return delOpsSystemAsset(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download("ops/system-asset/export", { ...this.queryParams }, "systemAsset_" + new Date().getTime() + ".xlsx")
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
