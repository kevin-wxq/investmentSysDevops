import request from '@/utils/request'

export function listWeeklyKeyOperationDetail(query) {
  return request({
    url: '/weekly/key-operation/list',
    method: 'get',
    params: query
  })
}

export function getWeeklyKeyOperationDetail(id) {
  return request({
    url: '/weekly/key-operation/' + id,
    method: 'get'
  })
}

export function addWeeklyKeyOperationDetail(data) {
  return request({
    url: '/weekly/key-operation/add',
    method: 'post',
    data: data
  })
}

export function updateWeeklyKeyOperationDetail(data) {
  return request({
    url: '/weekly/key-operation/edit',
    method: 'put',
    data: data
  })
}

export function delWeeklyKeyOperationDetail(id) {
  return request({
    url: '/weekly/key-operation/' + id,
    method: 'delete'
  })
}
