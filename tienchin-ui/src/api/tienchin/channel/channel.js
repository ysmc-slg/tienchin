import request from '@/utils/request'

// 查询渠道列表
export function listChannel(query) {
  return request({
    url: '/tienchin/channel/channelList',
    method: 'get',
    params: query
  })
}

