import request from '@/utils/request'

// 查询日常运维明细列表
export function listDaily_operation(query) {
  return request({
    url: '/dailyOperation/list',
    method: 'get',
    params: query
  })
}

// 查询日常运维明细详细
export function getDaily_operation(id) {
  return request({
    url: '/dailyOperation/' + id,
    method: 'get'
  })
}

// 新增日常运维明细
export function addDaily_operation(data) {
  return request({
    url: '/dailyOperation/add',
    method: 'post',
    data: data
  })
}

// 修改日常运维明细
export function updateDaily_operation(data) {
  return request({
    url: '/dailyOperation/edit',
    method: 'put',
    data: data
  })
}

// 删除日常运维明细
export function delDaily_operation(id) {
  return request({
    url: '/dailyOperation/' + id,
    method: 'delete'
  })
}
