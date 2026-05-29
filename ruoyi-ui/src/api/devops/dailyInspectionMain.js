import request from '@/utils/request'

export function listDailyInspectionMain(query) {
  return request({
    url: '/daily/inspection/list',
    method: 'get',
    params: query
  })
}

export function getDailyInspectionMain(id) {
  return request({
    url: '/daily/inspection/' + id,
    method: 'get'
  })
}

export function addDailyInspectionMain(data) {
  return request({
    url: '/daily/inspection/add',
    method: 'post',
    data: data
  })
}

export function updateDailyInspectionMain(data) {
  return request({
    url: '/daily/inspection/edit',
    method: 'put',
    data: data
  })
}

export function delDailyInspectionMain(id) {
  return request({
    url: '/daily/inspection/' + id,
    method: 'delete'
  })
}
