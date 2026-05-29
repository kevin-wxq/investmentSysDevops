import request from '@/utils/request'
export function listOpsTeamMember(query) { return request({ url:'/ops/team-member/list', method:'get', params:query }) }
export function getOpsTeamMember(id) { return request({ url:'/ops/team-member/'+id, method:'get' }) }
export function nextOpsTeamMemberNo() { return request({ url:'/ops/team-member/next-employee-no', method:'get' }) }
export function addOpsTeamMember(data) { return request({ url:'/ops/team-member/add', method:'post', data }) }
export function updateOpsTeamMember(data) { return request({ url:'/ops/team-member/edit', method:'put', data }) }
export function delOpsTeamMember(id) { return request({ url:'/ops/team-member/'+id, method:'delete' }) }
