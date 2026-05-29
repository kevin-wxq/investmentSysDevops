import request from '@/utils/request'

// 查询故障记录列表
export function listFaultRecord(query) {
  return request({
    url: '/ops/fault/list',
    method: 'get',
    params: query
  })
}

// 查询故障记录详细
export function getFaultRecord(id) {
  return request({
    url: '/ops/fault/' + id,
    method: 'get'
  })
}

// 新增故障记录
export function addFaultRecord(data) {
  return request({
    url: '/ops/fault/add',
    method: 'post',
    data: data
  })
}

// 修改故障记录
export function updateFaultRecord(data) {
  return request({
    url: '/ops/fault/edit',
    method: 'put',
    data: data
  })
}

// 删除故障记录
export function delFaultRecord(id) {
  return request({
    url: '/ops/fault/' + id,
    method: 'delete'
  })
}
