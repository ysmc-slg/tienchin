import request from '@/utils/request'

// 查询渠道列表
export function listChannel(query) {
  return request({
    url: '/tienchin/channel/channelList',
    method: 'get',
    params: query
  })
}

// 添加渠道管理
export function addChannel(data) {
  return request({
    url: '/tienchin/channel/add',
    method: 'post',
    data: data
  })
}

// 根据id查询
export function getChannel(channelId) {
  return request({
    url: '/tienchin/channel/getChannel/'+channelId,
    method: 'get'
  })
}

// 修改渠道
export function updateChannel(data) {
  return request({
    url: '/tienchin/channel/updateChannel',
    method: 'post',
    data: data
  })
}

