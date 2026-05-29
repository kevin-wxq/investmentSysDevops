import request from '@/utils/request'

// 查询运维知识库列表
export function listKnowledge(query) {
  return request({
    url: '/ops/knowledge/list',
    method: 'get',
    params: query
  })
}

// 查询运维知识库详细
export function getKnowledge(id) {
  return request({
    url: '/ops/knowledge/' + id,
    method: 'get'
  })
}

// 新增运维知识库
export function addKnowledge(data) {
  return request({
    url: '/ops/knowledge/add',
    method: 'post',
    data: data
  })
}

// 修改运维知识库
export function updateKnowledge(data) {
  return request({
    url: '/ops/knowledge/edit',
    method: 'put',
    data: data
  })
}

// 删除运维知识库
export function delKnowledge(id) {
  return request({
    url: '/ops/knowledge/' + id,
    method: 'delete'
  })
}
