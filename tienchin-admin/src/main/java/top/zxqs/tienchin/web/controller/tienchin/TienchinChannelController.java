package top.zxqs.tienchin.web.controller.tienchin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.zxqs.tienchin.channel.domain.TienchinChannel;
import top.zxqs.tienchin.channel.service.ITienchinChannelService;
import top.zxqs.tienchin.common.annotation.Log;
import top.zxqs.tienchin.common.core.controller.BaseController;
import top.zxqs.tienchin.common.core.domain.AjaxResult;
import top.zxqs.tienchin.common.core.domain.entity.SysRole;
import top.zxqs.tienchin.common.core.domain.entity.SysUser;
import top.zxqs.tienchin.common.core.page.TableDataInfo;
import top.zxqs.tienchin.common.enums.BusinessType;
import top.zxqs.tienchin.common.utils.poi.ExcelUtil;

import javax.servlet.http.HttpServletResponse;
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

    /**
     * 新增渠道管理
     * @param channel
     * @return
     */
    @PostMapping("/add")
    @Log(title = "渠道管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasPermission('tienchin:channel:add')")
    public AjaxResult addChannel(@Validated @RequestBody TienchinChannel channel){
        channel.setCreateBy(getUsername());
        int result = channelService.addChannel(channel);

        return toAjax(result);
    }

    /**
     * 根据id查询渠道信息
     * @param channelId
     * @return
     */
    @GetMapping("/getChannel/{channelId}")
    @PreAuthorize("hasPermission('tienchin:channel:query')")
    public AjaxResult getChannelById(@PathVariable Long channelId){

        return AjaxResult.success(channelService.selectTienchinChannelById(channelId));

    }

    /**
     * 修改渠道
     * @param channel
     * @return
     */
    @PutMapping("/updateChannel")
    @Log(title = "渠道管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasPermission('tienchin:channel:edit')")
    public AjaxResult updateChannel(@Validated @RequestBody TienchinChannel channel){
        channel.setUpdateBy(getUsername());
        return toAjax(channelService.updateChannel(channel));
    }

    /**
     * 根据id删除
     * @param channelIds
     * @return
     */
    @DeleteMapping("/removeChannel/{channelIds}")
    @Log(title = "渠道管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasPermission('tienchin:channel:delete')")
    public AjaxResult removeChannel(@PathVariable Long[] channelIds){
        channelService.removeChannel(channelIds);

        return success();

    }

    /**
     * 上传Excel文件
     * @param file
     * @param updateSupport
     * @return
     * @throws Exception
     */
    @Log(title = "渠道管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("hasPermission('tienchin:channel:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<TienchinChannel> util = new ExcelUtil<TienchinChannel>(TienchinChannel.class);
        List<TienchinChannel> channelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = channelService.importChannel(channelList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @Log(title = "渠道管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("hasPermission('tienchin:channel:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, TienchinChannel channel) {
        List<TienchinChannel> list = channelService.selectTienchinChannelList(channel);
        ExcelUtil<TienchinChannel> util = new ExcelUtil<TienchinChannel>(TienchinChannel.class);
        util.exportExcel(response, list, "渠道数据");
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<TienchinChannel> util = new ExcelUtil<TienchinChannel>(TienchinChannel.class);
        util.importTemplateExcel(response, "渠道数据");
    }
}