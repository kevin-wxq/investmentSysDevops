import request from '@/utils/request'

export function listWorkItem(query) {
  return request({
    url: '/ops/work-item/list',
    method: 'get',
    params: query
  })
}

export function getWorkItem(id) {
  return request({
    url: '/ops/work-item/' + id,
    method: 'get'
  })
}

export function nextWorkItemNo(itemType) {
  return request({
    url: '/ops/work-item/next-item-no',
    method: 'get',
    params: { itemType }
  })
}

export function addWorkItem(data) {
  return request({
    url: '/ops/work-item/add',
    method: 'post',
    data: data
  })
}

export function updateWorkItem(data) {
  return request({
    url: '/ops/work-item/edit',
    method: 'put',
    data: data
  })
}

export function delWorkItem(id) {
  return request({
    url: '/ops/work-item/' + id,
    method: 'delete'
  })
}
