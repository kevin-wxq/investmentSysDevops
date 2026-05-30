import request from '@/utils/request'
export function listOpsSystemAsset(query) { return request({ url:'/ops/system-asset/list', method:'get', params:query }) }
export function getOpsSystemAsset(id) { return request({ url:'/ops/system-asset/'+id, method:'get' }) }
export function nextOpsSystemAssetCode(systemType) { return request({ url:'/ops/system-asset/next-code', method:'get', params:{ systemType } }) }
export function addOpsSystemAsset(data) { return request({ url:'/ops/system-asset/add', method:'post', data }) }
export function updateOpsSystemAsset(data) { return request({ url:'/ops/system-asset/edit', method:'put', data }) }
export function delOpsSystemAsset(id) { return request({ url:'/ops/system-asset/'+id, method:'delete' }) }
export function getLinkedItems(id) { return request({ url:'/ops/system-asset/'+id+'/linked-items', method:'get' }) }
