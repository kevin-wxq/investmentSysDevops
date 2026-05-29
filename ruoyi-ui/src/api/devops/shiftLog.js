import request from '@/utils/request'

// 查询值班日志列表
export function listShiftLog(query) {
  return request({
    url: '/ops/shift-log/list',
    method: 'get',
    params: query
  })
}

// 查询值班日志详细
export function getShiftLog(id) {
  return request({
    url: '/ops/shift-log/' + id,
    method: 'get'
  })
}

// 新增值班日志
export function addShiftLog(data) {
  return request({
    url: '/ops/shift-log/add',
    method: 'post',
    data: data
  })
}

// 修改值班日志
export function updateShiftLog(data) {
  return request({
    url: '/ops/shift-log/edit',
    method: 'put',
    data: data
  })
}

// 删除值班日志
export function delShiftLog(id) {
  return request({
    url: '/ops/shift-log/' + id,
    method: 'delete'
  })
}
