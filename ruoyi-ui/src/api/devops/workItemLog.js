import request from '@/utils/request'

export function listWorkItemLog(query) {
  return request({
    url: '/ops/work-item-log/list',
    method: 'get',
    params: query
  })
}
