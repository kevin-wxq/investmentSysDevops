import request from '@/utils/request'

// 查询备份记录列表
export function listBackupRecord(query) {
  return request({
    url: '/ops/backup/list',
    method: 'get',
    params: query
  })
}

// 查询备份记录详细
export function getBackupRecord(id) {
  return request({
    url: '/ops/backup/' + id,
    method: 'get'
  })
}

// 新增备份记录
export function addBackupRecord(data) {
  return request({
    url: '/ops/backup/add',
    method: 'post',
    data: data
  })
}

// 修改备份记录
export function updateBackupRecord(data) {
  return request({
    url: '/ops/backup/edit',
    method: 'put',
    data: data
  })
}

// 删除备份记录
export function delBackupRecord(id) {
  return request({
    url: '/ops/backup/' + id,
    method: 'delete'
  })
}
