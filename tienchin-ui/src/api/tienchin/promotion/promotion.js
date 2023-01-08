import request from '@/utils/request'

// 查询渠道列表
export function listPromotion(query) {
  return request({
    url: '/tienchin/promotion/list',
    method: 'get',
    params: query
  })
}


