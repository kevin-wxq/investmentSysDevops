import request from '@/utils/request'

export function listWeeklyOperationTask(query) {
  return request({
    url: '/weekly/operation-task/list',
    method: 'get',
    params: query
  })
}

export function getWeeklyOperationTask(id) {
  return request({
    url: '/weekly/operation-task/' + id,
    method: 'get'
  })
}

export function addWeeklyOperationTask(data) {
  return request({
    url: '/weekly/operation-task/add',
    method: 'post',
    data: data
  })
}

export function updateWeeklyOperationTask(data) {
  return request({
    url: '/weekly/operation-task/edit',
    method: 'put',
    data: data
  })
}

export function delWeeklyOperationTask(id) {
  return request({
    url: '/weekly/operation-task/' + id,
    method: 'delete'
  })
}
