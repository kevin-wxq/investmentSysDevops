import request from '@/utils/request'

// 查询值班班次列表
export function listDutyShift(query) {
  return request({
    url: '/ops/duty-shift/list',
    method: 'get',
    params: query
  })
}

// 查询值班班次详细
export function getDutyShift(id) {
  return request({
    url: '/ops/duty-shift/' + id,
    method: 'get'
  })
}

// 新增值班班次
export function addDutyShift(data) {
  return request({
    url: '/ops/duty-shift/add',
    method: 'post',
    data: data
  })
}

// 修改值班班次
export function updateDutyShift(data) {
  return request({
    url: '/ops/duty-shift/edit',
    method: 'put',
    data: data
  })
}

// 删除值班班次
export function delDutyShift(id) {
  return request({
    url: '/ops/duty-shift/' + id,
    method: 'delete'
  })
}
