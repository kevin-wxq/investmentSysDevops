import request from '@/utils/request'

export function listAttachByKnowledgeId(knowledgeId) {
  return request({ url: '/ops/knowledge-attach/list/' + knowledgeId, method: 'get' })
}

export function addAttach(data) {
  return request({ url: '/ops/knowledge-attach/add', method: 'post', data })
}

export function delAttach(id) {
  return request({ url: '/ops/knowledge-attach/' + id, method: 'delete' })
}
