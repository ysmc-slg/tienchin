package top.zxqs.tienchin.channel.service;

import top.zxqs.tienchin.channel.domain.TienchinChannel;

import java.util.List;

/**
 * 渠道管理Service接口
 *
 * @author tienchin
 * @date 2023-01-01
 */
public interface ITienchinChannelService {


    /**
     * 查询渠道管理列表
     *
     * @param tienchinChannel 渠道管理
     * @return 渠道管理集合
     */
    public List<TienchinChannel> selectTienchinChannelList(TienchinChannel tienchinChannel);

    /**
     * 新增渠道管理
     * @param channel
     * @return
     */
    int addChannel(TienchinChannel channel);
    /**
     * 根据id查询渠道信息
     * @param channelId
     * @return
     */
    TienchinChannel selectTienchinChannelById(Long channelId);
    /**
     * 修改渠道
     * @param channel
     * @return
     */
    int updateChannel(TienchinChannel channel);
    /**
     * 根据id删除
     * @param channelIds
     * @return
     */
    void removeChannel(Long[] channelIds);

    /**
     *
     * @param channelList
     * @param updateSupport
     * @param operName
     * @return
     */
    String importChannel(List<TienchinChannel> channelList, boolean updateSupport, String operName);
}