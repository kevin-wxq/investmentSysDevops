<template>
  <div class="app-container ops-page">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="92px" class="ops-query-form">
      <el-form-item label="关联系统" prop="systemId">
        <el-select v-model="queryParams.systemId" class="query-control" placeholder="全部" clearable filterable>
          <el-option v-for="item in systemOptions" :key="item.id" :label="formatSystem(item)" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="标题" prop="title">
        <el-input v-model="queryParams.title" class="query-control" placeholder="标题" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="分类" prop="category">
        <el-select v-model="queryParams.category" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_knowledge_category" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="发布状态" prop="isPublished">
        <el-select v-model="queryParams.isPublished" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_publish_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item class="query-actions">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['ops:knowledge:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['ops:knowledge:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['ops:knowledge:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['ops:knowledge:export']">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="knowledgeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="标题" align="left" prop="title" min-width="220" show-overflow-tooltip />
      <el-table-column label="关联系统" align="left" prop="systemId" min-width="170" show-overflow-tooltip>
        <template slot-scope="scope">{{ getSystemName(scope.row.systemId) }}</template>
      </el-table-column>
      <el-table-column label="分类" align="center" prop="category" width="120">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_knowledge_category" :value="scope.row.category" /></template>
      </el-table-column>
      <el-table-column label="标签" align="left" prop="tags" min-width="160" show-overflow-tooltip />
      <el-table-column label="作者" align="center" prop="authorId" width="110">
        <template slot-scope="scope">{{ getTeamName(scope.row.authorId) }}</template>
      </el-table-column>
      <el-table-column label="浏览次数" align="right" prop="viewCount" width="100" />
      <el-table-column label="发布状态" align="center" prop="isPublished" width="110">
        <template slot-scope="scope"><dict-tag :options="dict.type.ops_publish_status" :value="scope.row.isPublished" /></template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['ops:knowledge:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['ops:knowledge:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="920px" append-to-body class="ops-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="108px">
        <el-divider content-position="left">知识信息</el-divider>
        <el-row :gutter="18">
          <el-col :span="12"><el-form-item label="标题" prop="title"><el-input v-model="form.title" placeholder="请输入标题" maxlength="100" /></el-form-item></el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="category">
              <el-select v-model="form.category" class="full-control" placeholder="请选择分类">
                <el-option v-for="dict in dict.type.ops_knowledge_category" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关联系统" prop="systemId">
              <el-select v-model="form.systemId" class="full-control" placeholder="请选择关联系统" clearable filterable>
                <el-option v-for="item in systemOptions" :key="item.id" :label="formatSystem(item)" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关联故障" prop="faultId">
              <el-select v-model="form.faultId" class="full-control" placeholder="可选择关联故障" clearable filterable>
                <el-option v-for="item in faultOptions" :key="item.id" :label="item.faultTitle" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="作者" prop="authorId">
              <el-select v-model="form.authorId" class="full-control" placeholder="请选择作者" clearable filterable>
                <el-option v-for="item in teamOptions" :key="item.id" :label="item.realName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发布状态" prop="isPublished">
              <el-select v-model="form.isPublished" class="full-control" placeholder="请选择发布状态">
                <el-option v-for="dict in dict.type.ops_publish_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12"><el-form-item label="浏览次数" prop="viewCount"><el-input-number v-model="form.viewCount" class="full-control" :min="0" :max="999999" controls-position="right" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="标签" prop="tags"><el-input v-model="form.tags" placeholder="逗号分隔，例如 MySQL,发布" maxlength="120" /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="适用环境" prop="applicableEnv"><el-input v-model="form.applicableEnv" placeholder="例如 生产 / 测试 / 灾备" maxlength="120" /></el-form-item></el-col>
        </el-row>

      <el-divider content-position="left">内容沉淀</el-divider>
      <el-row :gutter="18">
        <el-col :span="24"><el-form-item label="问题描述" prop="problemDesc"><el-input v-model="form.problemDesc" type="textarea" :rows="4" placeholder="请输入问题描述" maxlength="800" show-word-limit /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="解决方案" prop="solutionDesc"><el-input v-model="form.solutionDesc" type="textarea" :rows="5" placeholder="请输入解决方案" maxlength="1000" show-word-limit /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="附件上传" prop="fileList"><file-upload v-model="form.fileList" :fileSize="20" :limit="10" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="备注" prop="remark"><el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入补充说明" maxlength="300" show-word-limit /></el-form-item></el-col>
      </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer"><el-button type="primary" @click="submitForm">确 定</el-button><el-button @click="cancel">取 消</el-button></div>
    </el-dialog>

    <el-drawer :title="detailTitle" :visible.sync="detailOpen" size="750px" append-to-body class="knowledge-detail-drawer">
      <div class="detail-body" v-loading="detailLoading">
        <el-descriptions :column="2" size="small" border>
          <el-descriptions-item label="标题" :span="2">{{ detail.title }}</el-descriptions-item>
          <el-descriptions-item label="分类"><dict-tag :options="dict.type.ops_knowledge_category" :value="detail.category" /></el-descriptions-item>
          <el-descriptions-item label="发布状态"><dict-tag :options="dict.type.ops_publish_status" :value="detail.isPublished" /></el-descriptions-item>
          <el-descriptions-item label="关联系统">{{ getSystemName(detail.systemId) }}</el-descriptions-item>
          <el-descriptions-item label="作者">{{ getTeamName(detail.authorId) }}</el-descriptions-item>
          <el-descriptions-item label="浏览次数">{{ detail.viewCount || 0 }}</el-descriptions-item>
          <el-descriptions-item label="标签">{{ detail.tags || '-' }}</el-descriptions-item>
          <el-descriptions-item label="适用环境" :span="2">{{ detail.applicableEnv || '-' }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">问题描述</el-divider>
        <div class="detail-text">{{ detail.problemDesc || '暂无描述' }}</div>

        <el-divider content-position="left">解决方案</el-divider>
        <div class="detail-text">{{ detail.solutionDesc || '暂无方案' }}</div>

        <el-divider content-position="left">附件列表</el-divider>
        <div v-if="detailAttachList.length > 0" class="attach-list">
          <div v-for="file in detailAttachList" :key="file.id" class="attach-item">
            <i class="el-icon-document"></i>
            <a :href="getBaseUrl() + file.filePath" target="_blank">{{ file.fileName }}</a>
            <span class="attach-size">{{ formatSize(file.fileSize) }}</span>
          </div>
        </div>
        <div v-else class="empty-text">暂无附件</div>

        <el-divider content-position="left">备注</el-divider>
        <div class="detail-text">{{ detail.remark || '暂无备注' }}</div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { listKnowledge, getKnowledge, delKnowledge, addKnowledge, updateKnowledge } from "@/api/devops/knowledge"
import { listAttachByKnowledgeId, addAttach, delAttach } from "@/api/devops/knowledgeAttach"
import { listOpsSystemAsset } from "@/api/devops/systemAsset"
import { listOpsTeamMember } from "@/api/devops/teamMember"
import { listFaultRecord } from "@/api/devops/faultRecord"

export default {
  name: "Knowledge",
  dicts: ["ops_knowledge_category", "ops_publish_status"],
  data() {
    return {
      loading: true, detailLoading: false, ids: [], single: true, multiple: true, showSearch: true, total: 0,
      knowledgeList: [], systemOptions: [], teamOptions: [], faultOptions: [], title: "", open: false,
      detailOpen: false, detailTitle: "知识详情", detail: {}, detailAttachList: [],
      queryParams: { pageNum: 1, pageSize: 10, systemId: null, title: null, category: null, isPublished: null },
      form: {},
      rules: {
        title: [{ required: true, message: "标题不能为空", trigger: "blur" }],
        category: [{ required: true, message: "分类不能为空", trigger: "change" }],
        isPublished: [{ required: true, message: "发布状态不能为空", trigger: "change" }]
      }
    }
  },
  created() { this.getList(); this.loadOptions() },
  methods: {
    getList() {
      this.loading = true
      listKnowledge(this.queryParams).then(response => { this.knowledgeList = response.rows; this.total = response.total; this.loading = false })
    },
    loadOptions() {
      listOpsSystemAsset({ pageNum: 1, pageSize: 1000 }).then(response => { this.systemOptions = response.rows || [] })
      listOpsTeamMember({ pageNum: 1, pageSize: 1000, isOnJob: "1" }).then(response => { this.teamOptions = response.rows || [] })
      listFaultRecord({ pageNum: 1, pageSize: 1000 }).then(response => { this.faultOptions = response.rows || [] })
    },
    formatSystem(item) { return `${item.systemCode || "-"} / ${item.systemName}` },
    getSystemName(id) { const item = this.systemOptions.find(option => option.id === id); return item ? this.formatSystem(item) : "-" },
    getTeamName(id) { const item = this.teamOptions.find(option => option.id === id); return item ? item.realName : "-" },
    cancel() { this.open = false; this.reset() },
    reset() {
      this.form = { id: null, systemId: null, faultId: null, title: null, category: "1", tags: null, problemDesc: null, solutionDesc: null, applicableEnv: null, authorId: null, viewCount: 0, isPublished: "0", remark: null, fileList: [] }
      this.resetForm("form")
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery() },
    handleSelectionChange(selection) { this.ids = selection.map(item => item.id); this.single = selection.length !== 1; this.multiple = !selection.length },
    handleAdd() { this.reset(); this.open = true; this.title = "新增运维知识" },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getKnowledge(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改运维知识"
        listAttachByKnowledgeId(id).then(res => {
          this.form.fileList = (res.data || []).map(item => ({ name: item.fileName, url: item.filePath }))
        })
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) return
        const submitData = { ...this.form }
        const fileList = submitData.fileList || []
        delete submitData.fileList
        if (submitData.id != null) {
          updateKnowledge(submitData).then(() => { this.$modal.msgSuccess("修改成功"); this.open = false; this.getList() })
        } else {
          addKnowledge(submitData).then(response => {
            const knowledgeId = response.data || response.id
            if (knowledgeId && fileList.length > 0) {
              fileList.forEach(file => {
                addAttach({ knowledgeId: knowledgeId, fileName: file.name, filePath: file.url })
              })
            }
            this.$modal.msgSuccess("新增成功"); this.open = false; this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm("确认删除选中的运维知识？").then(function() { return delKnowledge(ids) }).then(() => { this.getList(); this.$modal.msgSuccess("删除成功") }).catch(() => {})
    },
    handleExport() { this.download("ops/knowledge/export", { ...this.queryParams }, "knowledge_" + new Date().getTime() + ".xlsx") },
    handleDetail(row) {
      this.detail = { ...row }
      this.detailTitle = row.title ? `知识详情：${row.title}` : "知识详情"
      this.detailOpen = true
      this.detailLoading = true
      getKnowledge(row.id).then(response => {
        this.detail = response.data || row
        this.detailLoading = false
      }).catch(() => { this.detailLoading = false })
      listAttachByKnowledgeId(row.id).then(res => {
        this.detailAttachList = res.data || []
      }).catch(() => { this.detailAttachList = [] })
    },
    getBaseUrl() { return process.env.VUE_APP_BASE_API || '' },
    formatSize(bytes) {
      if (!bytes) return ''
      if (bytes < 1024) return bytes + ' B'
      if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB'
      return (bytes / 1024 / 1024).toFixed(1) + ' MB'
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
.ops-dialog ::v-deep .el-form-item__label { white-space: nowrap; }
.knowledge-detail-drawer ::v-deep .el-drawer { max-width: 100vw; }
.knowledge-detail-drawer ::v-deep .el-descriptions-item__label { white-space: nowrap; }
.detail-body { padding: 0 20px 24px; }
.detail-text { padding: 10px 12px; background: #f5f7fa; border-radius: 4px; color: #606266; line-height: 1.8; white-space: pre-wrap; min-height: 40px; }
.empty-text { color: #909399; font-size: 13px; padding: 10px 0; }
.attach-list { display: flex; flex-direction: column; gap: 8px; }
.attach-item { display: flex; align-items: center; gap: 8px; padding: 8px 12px; background: #f5f7fa; border-radius: 4px; }
.attach-item i { color: #409EFF; }
.attach-item a { color: #409EFF; text-decoration: none; }
.attach-item a:hover { text-decoration: underline; }
.attach-size { color: #909399; font-size: 12px; margin-left: auto; }
@media (max-width: 760px) {
  .knowledge-detail-drawer ::v-deep .el-drawer { width: 100% !important; }
}
</style>
