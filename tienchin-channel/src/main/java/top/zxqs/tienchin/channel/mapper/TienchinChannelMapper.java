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


}