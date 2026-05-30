import request from '@/utils/request'

export function listHtRequirement(query) {
  return request({
    url: '/ht/requirement/list',
    method: 'get',
    params: query
  })
}

export function getHtRequirement(id) {
  return request({
    url: '/ht/requirement/' + id,
    method: 'get'
  })
}

export function nextHtRequirementNo(deptCode, proposeDate) {
  return request({
    url: '/ht/requirement/next-req-no',
    method: 'get',
    params: { deptCode, proposeDate }
  })
}

export function addHtRequirement(data) {
  return request({
    url: '/ht/requirement/add',
    method: 'post',
    data: data
  })
}

export function updateHtRequirement(data) {
  return request({
    url: '/ht/requirement/edit',
    method: 'put',
    data: data
  })
}

export function delHtRequirement(id) {
  return request({
    url: '/ht/requirement/' + id,
    method: 'delete'
  })
}
