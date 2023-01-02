package top.zxqs.tienchin.channel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zxqs.tienchin.channel.domain.TienchinChannel;
import top.zxqs.tienchin.channel.mapper.TienchinChannelMapper;
import top.zxqs.tienchin.channel.service.ITienchinChannelService;
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

}