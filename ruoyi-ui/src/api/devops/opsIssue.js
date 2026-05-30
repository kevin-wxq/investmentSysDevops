import request from '@/utils/request'

export function listOpsIssue(query) {
  return request({
    url: '/ops/issue/list',
    method: 'get',
    params: query
  })
}

export function getOpsIssue(id) {
  return request({
    url: '/ops/issue/' + id,
    method: 'get'
  })
}

export function nextIssueNo() {
  return request({
    url: '/ops/issue/next-issue-no',
    method: 'get'
  })
}

export function addOpsIssue(data) {
  return request({
    url: '/ops/issue/add',
    method: 'post',
    data: data
  })
}

export function updateOpsIssue(data) {
  return request({
    url: '/ops/issue/edit',
    method: 'put',
    data: data
  })
}

export function delOpsIssue(id) {
  return request({
    url: '/ops/issue/' + id,
    method: 'delete'
  })
}

export function convertToBug(id) {
  return request({
    url: '/ops/issue/' + id + '/convert/bug',
    method: 'post'
  })
}

export function convertToRequirement(id) {
  return request({
    url: '/ops/issue/' + id + '/convert/requirement',
    method: 'post'
  })
}
