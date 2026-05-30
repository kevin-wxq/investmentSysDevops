import request from '@/utils/request'

export function listHtBug(query) {
  return request({
    url: '/ht/bug/list',
    method: 'get',
    params: query
  })
}

export function getHtBug(id) {
  return request({
    url: '/ht/bug/' + id,
    method: 'get'
  })
}

export function nextHtBugNo(systemId) {
  return request({
    url: '/ht/bug/next-bug-no',
    method: 'get',
    params: { systemId }
  })
}

export function addHtBug(data) {
  return request({
    url: '/ht/bug/add',
    method: 'post',
    data: data
  })
}

export function updateHtBug(data) {
  return request({
    url: '/ht/bug/edit',
    method: 'put',
    data: data
  })
}

export function delHtBug(id) {
  return request({
    url: '/ht/bug/' + id,
    method: 'delete'
  })
}
