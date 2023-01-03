package top.zxqs.tienchin.channel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zxqs.tienchin.channel.domain.TienchinChannel;
import top.zxqs.tienchin.channel.mapper.TienchinChannelMapper;
import top.zxqs.tienchin.channel.service.ITienchinChannelService;
import top.zxqs.tienchin.common.core.domain.BaseEntity;
import top.zxqs.tienchin.common.core.domain.entity.SysUser;
import top.zxqs.tienchin.common.exception.base.BaseException;
import top.zxqs.tienchin.common.utils.DateUtils;

import java.util.List;

/**
 * 渠道管理Service业务层处理
 *
 * @author tienchin
 * @date 2023-01-01
 */
@Service
public class TienchinChannelServiceImpl implements ITienchinChannelService {
    @Autowired
    private TienchinChannelMapper tienchinChannelMapper;



    /**
     * 查询渠道管理列表
     *
     * @param tienchinChannel 渠道管理
     * @return 渠道管理
     */
    @Override
    public List<TienchinChannel> selectTienchinChannelList(TienchinChannel tienchinChannel) {
        return tienchinChannelMapper.selectTienchinChannelList(tienchinChannel);
    }
    /**
     * 新增渠道管理
     * @param channel
     * @return
     */
    @Override
    @Transactional
    public int addChannel(TienchinChannel channel) {
        TienchinChannel tienchinChannel = tienchinChannelMapper.selectTienchinChannelByName(channel.getChannelName());
        if(tienchinChannel != null){
            throw  new BaseException("渠道名称已存在");
        }

        return tienchinChannelMapper.addChannel(channel);
    }
    /**
     * 根据id查询渠道信息
     * @param channelId
     * @return
     */
    @Override
    public TienchinChannel selectTienchinChannelById(Long channelId) {
        return tienchinChannelMapper.selectTienchinChannelById(channelId);
    }
    /**
     * 修改渠道
     * @param channel
     * @return
     */
    @Override
    public int updateChannel(TienchinChannel channel) {
        TienchinChannel tienchinChannel = tienchinChannelMapper.selectTienchinChannelByName(channel.getChannelName());
        if(tienchinChannel != null){
            throw  new BaseException("渠道名称已存在");
        }

        return tienchinChannelMapper.updateChannel(channel);
    }

}