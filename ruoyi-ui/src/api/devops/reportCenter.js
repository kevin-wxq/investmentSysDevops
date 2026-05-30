import request from '@/utils/request'

export function getReportSummary(query) {
  return request({
    url: '/ops/report/summary',
    method: 'get',
    params: query
  })
}
