<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <div class="stat-card primary"><div class="stat-value">{{stats.systemCount}}</div><div class="stat-label">系统资产总数</div></div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card success"><div class="stat-value">{{stats.faultCount}}</div><div class="stat-label">本月故障数</div></div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card warning"><div class="stat-value">{{stats.inspectionRate}}%</div><div class="stat-label">巡检完成率</div></div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card danger"><div class="stat-value">{{stats.pendingChanges}}</div><div class="stat-label">待审批变更</div></div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-title">系统类型分布</div>
          <div ref="systemTypeChart" style="height:300px"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-title">月度故障趋势</div>
          <div ref="faultTrendChart" style="height:300px"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-title">巡检统计</div>
          <div ref="inspectionChart" style="height:300px"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-title">变更类型分布</div>
          <div ref="changeTypeChart" style="height:300px"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-title">最新故障记录</div>
          <el-table :data="recentFaults" size="small" max-height="280">
            <el-table-column prop="faultTitle" label="标题" show-overflow-tooltip />
            <el-table-column prop="systemName" label="系统" width="120" />
            <el-table-column prop="faultLevel" label="等级" width="80"><template slot-scope="s">{{ ['','一般','严重','紧急'][s.row.faultLevel] }}</template></el-table-column>
            <el-table-column prop="faultStatus" label="状态" width="80"><template slot-scope="s">{{ ['','发现','处理中','已恢复','已关闭'][s.row.faultStatus] }}</template></el-table-column>
            <el-table-column prop="occurTime" label="时间" width="100" />
          </el-table>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-title">最新变更记录</div>
          <el-table :data="recentChanges" size="small" max-height="280">
            <el-table-column prop="changeTitle" label="标题" show-overflow-tooltip />
            <el-table-column prop="changeType" label="类型" width="80"><template slot-scope="s">{{ ['','版本升级','配置变更','数据库变更','网络变更','其他'][s.row.changeType] }}</template></el-table-column>
            <el-table-column prop="changeResult" label="结果" width="80"><template slot-scope="s">{{ ['','成功','失败','部分成功'][s.row.changeResult] }}</template></el-table-column>
            <el-table-column prop="changeTime" label="时间" width="100" />
          </el-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import request from '@/utils/request'

export default {
  name: 'OpsDashboard',
  data() {
    return {
      stats: { systemCount: 0, faultCount: 0, inspectionRate: 0, pendingChanges: 0 },
      recentFaults: [],
      recentChanges: []
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      request({ url: '/ops/dashboard/stats', method: 'get' }).then(r => {
        this.stats = r.data
        this.recentFaults = r.data.recentFaults || []
        this.recentChanges = r.data.recentChanges || []
        this.$nextTick(() => {
          this.initSystemTypeChart(r.data.systemTypeData || [])
          this.initFaultTrendChart(r.data.faultTrend || [])
          this.initInspectionChart(r.data.inspectionData || [])
          this.initChangeTypeChart(r.data.changeTypeData || [])
        })
      }).catch(() => {
        // Fallback demo data
        this.initSystemTypeChart([{value:35,name:'核心业务'},{value:25,name:'支撑系统'},{value:20,name:'基础设施'}])
        this.initFaultTrendChart([5,8,6,9,7,4,3,6,5,8,4,7])
        this.initInspectionChart([85,90,78,92,88,95])
        this.initChangeTypeChart([{value:15,name:'版本升级'},{value:25,name:'配置变更'},{value:20,name:'数据库变更'},{value:10,name:'网络变更'},{value:5,name:'其他'}])
      })
    },
    initSystemTypeChart(data) {
      const chart = echarts.init(this.$refs.systemTypeChart)
      chart.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 0 },
        series: [{ type: 'pie', radius: ['40%','70%'], avoidLabelOverlap: false,
          label: { show: true, position: 'outside', formatter: '{b}\n{d}%' }, data }]
      })
    },
    initFaultTrendChart(data) {
      const chart = echarts.init(this.$refs.faultTrendChart)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'] },
        yAxis: { type: 'value', name: '故障数' },
        series: [{ type: 'line', smooth: true, data, areaStyle: { color: 'rgba(245,108,108,0.2)' },
          itemStyle: { color: '#f56c6c' } }]
      })
    },
    initInspectionChart(data) {
      const chart = echarts.init(this.$refs.inspectionChart)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: ['周一','周二','周三','周四','周五','周六'] },
        yAxis: { type: 'value', name: '完成率(%)', max: 100 },
        series: [{ type: 'bar', data, itemStyle: { color: '#67c23a' }, barWidth: '50%' }]
      })
    },
    initChangeTypeChart(data) {
      const chart = echarts.init(this.$refs.changeTypeChart)
      chart.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 0 },
        series: [{ type: 'pie', radius: '65%', center: ['50%','45%'], data }]
      })
    }
  }
}
</script>

<style scoped>
.dashboard-container { padding: 10px; }
.stat-card { border-radius: 8px; padding: 20px; color: #fff; text-align: center; }
.stat-card.primary { background: linear-gradient(135deg, #409eff, #337ecc); }
.stat-card.success { background: linear-gradient(135deg, #67c23a, #529b2e); }
.stat-card.warning { background: linear-gradient(135deg, #e6a23c, #c98a2c); }
.stat-card.danger { background: linear-gradient(135deg, #f56c6c, #d94d4d); }
.stat-value { font-size: 32px; font-weight: bold; margin-bottom: 5px; }
.stat-label { font-size: 14px; opacity: 0.9; }
.chart-card { background: #fff; border-radius: 8px; padding: 16px; box-shadow: 0 2px 8px rgba(0,0,0,0.08); }
.chart-title { font-size: 16px; font-weight: 600; margin-bottom: 12px; color: #303133; }

.ops-dialog ::v-deep .el-form-item__label {
  white-space: nowrap;
}
</style>
