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
      <el-form-item label="关联系统" prop="systemId">
        <el-select v-model="queryParams.systemId" class="query-control" placeholder="全部" clearable filterable>
          <el-option v-for="item in systemOptions" :key="item.id" :label="item.systemName" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="供应商" prop="vendorName">
        <el-input v-model="queryParams.vendorName" class="query-control" placeholder="供应商名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="联系人" prop="contactPerson">
        <el-input v-model="queryParams.contactPerson" class="query-control" placeholder="联系人" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="服务范围" prop="serviceScope">
        <el-select v-model="queryParams.serviceScope" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_service_scope" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="SLA等级" prop="slaLevel">
        <el-select v-model="queryParams.slaLevel" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_sla_level" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item class="query-actions">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['ops:vendor-contact:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['ops:vendor-contact:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['ops:vendor-contact:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['ops:vendor-contact:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="vendorContactList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="关联系统" align="left" prop="systemId" min-width="170" show-overflow-tooltip>
        <template slot-scope="scope">{{ getSystemName(scope.row.systemId) }}</template>
      </el-table-column>
      <el-table-column label="供应商名称" align="left" prop="vendorName" min-width="170" show-overflow-tooltip />
      <el-table-column label="联系人" align="center" prop="contactPerson" min-width="110" />
      <el-table-column label="联系电话" align="center" prop="contactPhone" min-width="130" />
      <el-table-column label="邮箱" align="left" prop="contactEmail" min-width="170" show-overflow-tooltip />
      <el-table-column label="服务范围" align="center" prop="serviceScope" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ops_service_scope" :value="scope.row.serviceScope" />
        </template>
      </el-table-column>
      <el-table-column label="SLA等级" align="center" prop="slaLevel" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ops_sla_level" :value="scope.row.slaLevel" />
        </template>
      </el-table-column>
      <el-table-column label="合同期限" align="center" min-width="190">
        <template slot-scope="scope">
          {{ parseTime(scope.row.contractStart, "{y}-{m}-{d}") || "-" }} 至 {{ parseTime(scope.row.contractEnd, "{y}-{m}-{d}") || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['ops:vendor-contact:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['ops:vendor-contact:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body class="ops-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="108px">
        <el-divider content-position="left">服务对象</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="关联系统" prop="systemId">
              <el-select v-model="form.systemId" placeholder="请选择关联系统" clearable filterable class="full-control">
                <el-option
                  v-for="item in systemOptions"
                  :key="item.id"
                  :label="`${item.systemCode || '-'} / ${item.systemName}`"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="服务范围" prop="serviceScope">
              <el-select v-model="form.serviceScope" placeholder="请选择服务范围" class="full-control">
                <el-option v-for="dict in dict.type.ops_service_scope" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="SLA等级" prop="slaLevel">
              <el-select v-model="form.slaLevel" placeholder="请选择SLA等级" class="full-control">
                <el-option v-for="dict in dict.type.ops_sla_level" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="orderNum">
              <el-input-number v-model="form.orderNum" :min="0" :max="9999" controls-position="right" class="full-control" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">供应商与联系人</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="供应商名称" prop="vendorName">
              <el-input v-model="form.vendorName" placeholder="请输入供应商名称" maxlength="80" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactPerson">
              <el-input v-model="form.contactPerson" placeholder="请输入联系人" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="form.contactPhone" placeholder="请输入联系电话" maxlength="20" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="contactEmail">
              <el-input v-model="form.contactEmail" placeholder="请输入邮箱" maxlength="80" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">合同信息</el-divider>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="合同编号" prop="contractNo">
              <el-input v-model="form.contractNo" placeholder="请输入合同编号" maxlength="80" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同开始" prop="contractStart">
              <el-date-picker v-model="form.contractStart" clearable type="date" value-format="yyyy-MM-dd" placeholder="请选择开始日期" class="full-control" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同结束" prop="contractEnd">
              <el-date-picker v-model="form.contractEnd" clearable type="date" value-format="yyyy-MM-dd" placeholder="请选择结束日期" class="full-control" />
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
  listOpsVendorContact,
  getOpsVendorContact,
  delOpsVendorContact,
  addOpsVendorContact,
  updateOpsVendorContact
} from "@/api/devops/vendorContact"
import { listOpsSystemAsset } from "@/api/devops/systemAsset"

const validatePhone = (rule, value, callback) => {
  if (!value || /^1[3-9]\d{9}$|^\d{3,4}-?\d{7,8}$/.test(value)) {
    callback()
  } else {
    callback(new Error("请输入正确的联系电话"))
  }
}

export default {
  name: "OpsVendorContact",
  dicts: ["ops_service_scope", "ops_sla_level"],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      vendorContactList: [],
      systemOptions: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        systemId: null,
        vendorName: null,
        contactPerson: null,
        serviceScope: null,
        slaLevel: null
      },
      form: {},
      rules: {
        systemId: [{ required: true, message: "关联系统不能为空", trigger: "change" }],
        vendorName: [{ required: true, message: "供应商名称不能为空", trigger: "blur" }],
        serviceScope: [{ required: true, message: "服务范围不能为空", trigger: "change" }],
        slaLevel: [{ required: true, message: "SLA等级不能为空", trigger: "change" }],
        contactPhone: [{ validator: validatePhone, trigger: "blur" }],
        contactEmail: [{ type: "email", message: "请输入正确的邮箱地址", trigger: "blur" }],
        contractEnd: [{ validator: (rule, value, callback) => this.validateContractEnd(value, callback), trigger: "change" }]
      }
    }
  },
  created() {
    this.getList()
    this.loadSystemOptions()
  },
  methods: {
    getList() {
      this.loading = true
      listOpsVendorContact(this.queryParams).then(response => {
        this.vendorContactList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    loadSystemOptions() {
      listOpsSystemAsset({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.systemOptions = response.rows || []
      })
    },
    getSystemName(id) {
      const item = this.systemOptions.find(option => option.id === id)
      return item ? `${item.systemCode || "-"} / ${item.systemName}` : "-"
    },
    validateContractEnd(value, callback) {
      if (!value || !this.form.contractStart || value >= this.form.contractStart) {
        callback()
      } else {
        callback(new Error("结束日期不能早于开始日期"))
      }
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null,
        systemId: null,
        vendorName: null,
        contactPerson: null,
        contactPhone: null,
        contactEmail: null,
        serviceScope: "2",
        contractNo: null,
        contractStart: null,
        contractEnd: null,
        slaLevel: "P2",
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
      this.title = "新增供应商联系人"
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getOpsVendorContact(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改供应商联系人"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        if (this.form.id != null) {
          updateOpsVendorContact(this.form).then(() => {
            this.$modal.msgSuccess("修改成功")
            this.open = false
            this.getList()
          })
        } else {
          addOpsVendorContact(this.form).then(() => {
            this.$modal.msgSuccess("新增成功")
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm("确认删除选中的供应商联系人？").then(function() {
        return delOpsVendorContact(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download("ops/vendor-contact/export", { ...this.queryParams }, "vendorContact_" + new Date().getTime() + ".xlsx")
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
