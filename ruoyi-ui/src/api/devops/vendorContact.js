import request from '@/utils/request'
export function listOpsVendorContact(query) { return request({ url:'/ops/vendor-contact/list', method:'get', params:query }) }
export function getOpsVendorContact(id) { return request({ url:'/ops/vendor-contact/'+id, method:'get' }) }
export function addOpsVendorContact(data) { return request({ url:'/ops/vendor-contact/add', method:'post', data }) }
export function updateOpsVendorContact(data) { return request({ url:'/ops/vendor-contact/edit', method:'put', data }) }
export function delOpsVendorContact(id) { return request({ url:'/ops/vendor-contact/'+id, method:'delete' }) }
