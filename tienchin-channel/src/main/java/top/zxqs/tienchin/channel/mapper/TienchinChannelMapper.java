package top.zxqs.tienchin.channel.mapper;

import top.zxqs.tienchin.channel.domain.TienchinChannel;

import java.util.List;

/**
 * 渠道管理Mapper接口
 *
 * @author tienchin
 * @date 2023-01-01
 */
public interface TienchinChannelMapper {


    /**
     * 查询渠道管理列表
     *
     * @param tienchinChannel 渠道管理
     * @return 渠道管理集合
     */
    public List<TienchinChannel> selectTienchinChannelList(TienchinChannel tienchinChannel);

    /**
     * 根据渠道名称查询
     * @param channelName
     * @return
     */
    TienchinChannel selectTienchinChannelByName(String channelName);

    /**
     * 新增渠道
     * @param channel
     * @return
     */
    int addChannel(TienchinChannel channel);

    TienchinChannel selectTienchinChannelById(Long channelId);
    /**
     * 修改渠道
     * @param channel
     * @return
     */
    int updateChannel(TienchinChannel channel);
    /**
     * 根据id删除
     * @param channelId
     * @return
     */
    void removeChannel(Long channelId);
}