package top.zxqs.tienchin.web.controller.tienchin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zxqs.tienchin.channel.domain.TienchinChannel;
import top.zxqs.tienchin.channel.service.ITienchinChannelService;
import top.zxqs.tienchin.common.core.controller.BaseController;
import top.zxqs.tienchin.common.core.page.TableDataInfo;

import java.util.List;

/**
 * 渠道管理Controller
 *
 * @author tienchin
 * @date 2023-01-01
 */
@RestController
@RequestMapping("/tienchin/channel")
public class TienchinChannelController extends BaseController {

    @Autowired
    private ITienchinChannelService channelService;

    /**
     * 获取所有渠道信息
     * @param channel
     * @return
     */
    @GetMapping("/channelList")
    @PreAuthorize("hasPermission('tienchin:channel:list')")
    public TableDataInfo channelList(TienchinChannel channel){
        startPage();

        List<TienchinChannel> channelList = channelService.selectTienchinChannelList(channel);

        return getDataTable(channelList);
    }
}