import request from '@/utils/request'

export function getReportSummary(query) {
  return request({
    url: '/ops/report/summary',
    method: 'get',
    params: query
  })
}

export function exportReportWord(query) {
  return request({
    url: '/ops/report/work-item/export-word',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}
