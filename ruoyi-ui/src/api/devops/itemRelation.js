import request from '@/utils/request'

export function listOpsItemRelation(query) {
  return request({
    url: '/ops/item-relation/list',
    method: 'get',
    params: query
  })
}

export function listOpsItemRelationBySource(sourceType, sourceId) {
  return request({
    url: '/ops/item-relation/list',
    method: 'get',
    params: { sourceType, sourceId, pageNum: 1, pageSize: 1000 }
  })
}
