<template>
  <div class="app-container report-center">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="84px" class="report-query">
      <el-form-item label="统计周期">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          clearable
        />
      </el-form-item>
      <el-form-item label="事项类型" prop="itemType">
        <el-select v-model="queryParams.itemType" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_item_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="优先级" prop="priority">
        <el-select v-model="queryParams.priority" class="query-control" placeholder="全部" clearable>
          <el-option v-for="dict in dict.type.ops_item_priority" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['ops:report:export']">导出Excel</el-button>
        <el-button type="success" plain icon="el-icon-document" size="mini" @click="handleExportWord" v-hasPermi="['ops:report:export']">导出Word</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="16" class="overview-row">
      <el-col :xs="12" :sm="8" :md="4" v-for="item in overviewCards" :key="item.label">
        <div class="overview-card" :class="item.type">
          <div class="overview-value">{{ item.value }}</div>
          <div class="overview-label">{{ item.label }}</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="12">
        <div class="report-panel">
          <div class="panel-header">
            <span>按类型统计</span>
          </div>
          <el-table v-loading="loading" :data="typeStats" size="small" height="320">
            <el-table-column label="事项类型" align="center" min-width="120">
              <template slot-scope="scope"><dict-tag :options="dict.type.ops_item_type" :value="scope.row.type" /></template>
            </el-table-column>
            <el-table-column label="总数" prop="total" align="center" width="80" />
            <el-table-column label="已闭环" prop="closed" align="center" width="90" />
            <el-table-column label="未闭环" prop="unclosed" align="center" width="90" />
            <el-table-column label="闭环率" align="center" min-width="150">
              <template slot-scope="scope">
                <el-progress :percentage="scope.row.rate" :stroke-width="8" />
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>

      <el-col :xs="24" :lg="12">
        <div class="report-panel">
          <div class="panel-header">
            <span>按优先级统计</span>
          </div>
          <el-table v-loading="loading" :data="priorityStats" size="small" height="320">
            <el-table-column label="优先级" align="center" min-width="120">
              <template slot-scope="scope"><dict-tag :options="dict.type.ops_item_priority" :value="scope.row.priority" /></template>
            </el-table-column>
            <el-table-column label="总数" prop="total" align="center" width="80" />
            <el-table-column label="已闭环" prop="closed" align="center" width="90" />
            <el-table-column label="逾期" prop="overdue" align="center" width="90" />
          </el-table>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="summary-row">
      <el-col :xs="24" :lg="12">
        <div class="report-panel">
          <div class="panel-header">
            <span>逾期摘要</span>
            <el-tag type="danger" size="mini">{{ overdueSummary.total }} 项</el-tag>
          </div>
          <el-table v-loading="loading" :data="overdueSummary.items" size="small" height="300" empty-text="暂无逾期事项">
            <el-table-column label="事项编号" prop="itemNo" align="center" width="150" show-overflow-tooltip />
            <el-table-column label="事项标题" prop="title" min-width="180" show-overflow-tooltip />
            <el-table-column label="状态" align="center" width="110">
              <template slot-scope="scope"><dict-tag :options="dict.type.ops_item_status" :value="scope.row.status" /></template>
            </el-table-column>
            <el-table-column label="计划完成" align="center" width="120">
              <template slot-scope="scope">{{ parseTime(scope.row.planFinishTime, "{y}-{m}-{d}") }}</template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>

      <el-col :xs="24" :lg="12">
        <div class="report-panel">
          <div class="panel-header">
            <span>未闭环摘要</span>
            <el-tag type="warning" size="mini">{{ unclosedSummary.total }} 项</el-tag>
          </div>
          <el-table v-loading="loading" :data="unclosedSummary.items" size="small" height="300" empty-text="暂无未闭环事项">
            <el-table-column label="事项编号" prop="itemNo" align="center" width="150" show-overflow-tooltip />
            <el-table-column label="事项标题" prop="title" min-width="180" show-overflow-tooltip />
            <el-table-column label="优先级" align="center" width="100">
              <template slot-scope="scope"><dict-tag :options="dict.type.ops_item_priority" :value="scope.row.priority" /></template>
            </el-table-column>
            <el-table-column label="进度" align="center" width="120">
              <template slot-scope="scope"><el-progress :percentage="toPercent(scope.row.progress)" :stroke-width="8" /></template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getReportSummary, exportReportWord } from "@/api/devops/reportCenter"

export default {
  name: "OpsReportCenter",
  dicts: ["ops_item_type", "ops_item_priority", "ops_item_status"],
  data() {
    return {
      loading: false,
      dateRange: [],
      queryParams: {
        itemType: null,
        priority: null,
        beginTime: null,
        endTime: null
      },
      summary: {}
    }
  },
  computed: {
    overview() {
      return this.summary.overview || this.summary
    },
    overviewCards() {
      return [
        { label: "闭环总数", value: this.pickNumber(this.overview, ["total", "totalItems", "totalCount", "itemCount"]), type: "primary" },
        { label: "已闭环", value: this.pickNumber(this.overview, ["closed", "closedItems", "closedCount", "finishCount"]), type: "success" },
        { label: "未闭环", value: this.pickNumber(this.overview, ["unclosed", "unclosedItems", "unclosedCount", "openCount"]), type: "warning" },
        { label: "逾期", value: this.pickNumber(this.overview, ["overdue", "overdueItems", "overdueCount"]), type: "danger" },
        { label: "闭环率", value: this.pickPercent(this.overview, ["closeRate", "closedRate", "finishRate"]), type: "info" }
      ]
    },
    typeStats() {
      return this.normalizeStats(this.pickArray(this.summary, ["typeStats", "itemTypeStats", "byType", "typeList"]), "type")
    },
    priorityStats() {
      return this.normalizeStats(this.pickArray(this.summary, ["priorityStats", "byPriority", "priorityList"]), "priority")
    },
    overdueSummary() {
      return this.normalizeSummary(["overdueSummary", "overdue", "overdueItems"], ["overdueCount"])
    },
    unclosedSummary() {
      return this.normalizeSummary(["unclosedSummary", "unclosed", "unclosedItems", "openItems"], ["unclosedCount", "openCount"])
    }
  },
  created() {
    this.getSummary()
  },
  methods: {
    getSummary() {
      this.loading = true
      getReportSummary(this.buildQuery()).then(response => {
        this.summary = response.data || {}
        this.loading = false
      }).catch(() => {
        this.summary = {}
        this.loading = false
      })
    },
    buildQuery() {
      const params = { ...this.queryParams }
      if (this.dateRange && this.dateRange.length === 2) {
        params.beginTime = this.dateRange[0]
        params.endTime = this.dateRange[1]
      } else {
        params.beginTime = null
        params.endTime = null
      }
      return params
    },
    handleQuery() {
      this.getSummary()
    },
    resetQuery() {
      this.dateRange = []
      this.queryParams = { itemType: null, priority: null, beginTime: null, endTime: null }
      this.getSummary()
    },
    handleExport() {
      this.download("ops/report/work-item/export", this.buildQuery(), "ops_report_" + new Date().getTime() + ".xlsx")
    },
    handleExportWord() {
      exportReportWord(this.buildQuery()).then(response => {
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '运维报告_' + new Date().getTime() + '.docx'
        link.click()
        window.URL.revokeObjectURL(url)
      })
    },
    normalizeStats(rows, key) {
      return rows.map(item => {
        const total = this.pickNumber(item, ["total", "totalCount", "count"])
        const closed = this.pickNumber(item, ["closed", "closedCount", "finishCount"])
        const unclosed = this.pickNumber(item, ["unclosed", "unclosedCount", "openCount"], Math.max(total - closed, 0))
        return {
          type: item.type || item.itemType || item.name || item.code,
          priority: item.priority || item.name || item.code,
          total,
          closed,
          unclosed,
          overdue: this.pickNumber(item, ["overdue", "overdueCount"]),
          progress: this.toPercent(this.pickNumber(item, ["avgProgress", "averageProgress", "progress"])),
          rate: this.toPercent(this.pickNumber(item, ["closeRate", "closedRate", "finishRate"], total ? closed * 100 / total : 0))
        }
      })
    },
    normalizeSummary(itemKeys, totalKeys) {
      const value = this.pickValue(this.summary, itemKeys)
      const items = Array.isArray(value) ? value : (value && (value.items || value.list || value.rows)) || []
      const total = value && !Array.isArray(value)
        ? this.pickNumber(value, ["total", "count"], items.length)
        : this.pickNumber(this.overview, totalKeys, items.length)
      return { total, items }
    },
    pickArray(source, keys) {
      const value = this.pickValue(source, keys)
      return Array.isArray(value) ? value : []
    },
    pickValue(source, keys) {
      if (!source) return undefined
      return keys.map(key => source[key]).find(value => value !== undefined && value !== null)
    },
    pickNumber(source, keys, fallback = 0) {
      const value = this.pickValue(source, keys)
      const numberValue = Number(value)
      return Number.isFinite(numberValue) ? numberValue : fallback
    },
    pickPercent(source, keys) {
      return this.toPercent(this.pickNumber(source, keys)) + "%"
    },
    toPercent(value) {
      const numberValue = Number(value || 0)
      const percentValue = numberValue > 0 && numberValue <= 1 ? numberValue * 100 : numberValue
      return Math.max(0, Math.min(100, Math.round(percentValue)))
    }
  }
}
</script>

<style scoped>
.report-center { background: #f5f7fa; min-height: calc(100vh - 84px); }
.report-query {
  padding: 16px 16px 2px;
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 6px;
  margin-bottom: 16px;
}
.report-query ::v-deep .el-form-item__label { white-space: nowrap; }
.query-control { width: 180px; }
.overview-row { margin-bottom: 16px; }
.overview-card {
  height: 96px;
  border-radius: 6px;
  color: #fff;
  padding: 18px 16px;
  margin-bottom: 16px;
}
.overview-card.primary { background: #409eff; }
.overview-card.success { background: #67c23a; }
.overview-card.warning { background: #e6a23c; }
.overview-card.danger { background: #f56c6c; }
.overview-card.info { background: #909399; }
.overview-card.plain { background: #4f6f8f; }
.overview-value {
  font-size: 28px;
  font-weight: 700;
  line-height: 34px;
}
.overview-label {
  margin-top: 8px;
  font-size: 13px;
  opacity: 0.92;
}
.report-panel {
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 6px;
  padding: 14px 16px 16px;
  margin-bottom: 16px;
}
.panel-header {
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 15px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 10px;
}
.summary-row { margin-top: 0; }
</style>
