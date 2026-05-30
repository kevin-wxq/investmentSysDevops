import request from '@/utils/request'

// 查询变更记录列表
export function listChangeRecord(query) {
  return request({
    url: '/ops/change/list',
    method: 'get',
    params: query
  })
}

// 查询变更记录详细
export function getChangeRecord(id) {
  return request({
    url: '/ops/change/' + id,
    method: 'get'
  })
}

// 新增变更记录
export function addChangeRecord(data) {
  return request({
    url: '/ops/change/add',
    method: 'post',
    data: data
  })
}

// 修改变更记录
export function updateChangeRecord(data) {
  return request({
    url: '/ops/change/edit',
    method: 'put',
    data: data
  })
}

// 下一个变更编号
export function nextChangeNo() {
  return request({
    url: '/ops/change/next-change-no',
    method: 'get'
  })
}

// 删除变更记录
export function delChangeRecord(id) {
  return request({
    url: '/ops/change/' + id,
    method: 'delete'
  })
}
