package top.zxqs.tienchin.channel.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zxqs.tienchin.channel.domain.TienchinChannel;
import top.zxqs.tienchin.channel.mapper.TienchinChannelMapper;
import top.zxqs.tienchin.channel.service.ITienchinChannelService;
import top.zxqs.tienchin.common.core.domain.BaseEntity;
import top.zxqs.tienchin.common.core.domain.entity.SysUser;
import top.zxqs.tienchin.common.exception.ServiceException;
import top.zxqs.tienchin.common.exception.base.BaseException;
import top.zxqs.tienchin.common.utils.DateUtils;
import top.zxqs.tienchin.common.utils.SecurityUtils;
import top.zxqs.tienchin.common.utils.StringUtils;
import top.zxqs.tienchin.common.utils.bean.BeanValidators;

import javax.validation.Validator;
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
    @Autowired
    protected Validator validator;

    private static final Logger log = LoggerFactory.getLogger(TienchinChannelServiceImpl.class);
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


        return tienchinChannelMapper.updateChannel(channel);
    }

    /**
     * 根据id删除
     * @param channelIds
     * @return
     */
    @Override
    public void removeChannel(Long[] channelIds) {
        for(Long channelId : channelIds){
            tienchinChannelMapper.removeChannel(channelId);
        }
    }
    /**
     * 上传Excel文件
     * @param channelList
     * @param updateSupport
     * @return
     * @throws Exception
     */
    @Override
    public String importChannel(List<TienchinChannel> channelList, boolean updateSupport, String operName) {
        if (StringUtils.isNull(channelList) || channelList.size() == 0) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for(TienchinChannel channel: channelList){

            try {
                // 判断渠道是否已经存在
                TienchinChannel c = tienchinChannelMapper.selectTienchinChannelByName(channel.getChannelName());
                // 为 true 表示没有存在的渠道名
                if(StringUtils.isNull(c)){
                    // 校验Excel表中的字段是否符合实体类字段上加的注解
                    BeanValidators.validateWithException(validator, channel);
                    channel.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserName());
                    addChannel(channel);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、渠道信息 " + channel.getChannelName() + " 导入成功");
                }else if (updateSupport){
                    // 前端是否勾选了更新已经存在的渠道数据
                    // 校验Excel表中的字段是否符合实体类字段上加的注解
                    BeanValidators.validateWithException(validator, channel);
                    channel.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserName());
                    channel.setChannelId(c.getChannelId());
                    updateChannel(channel);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、渠道信息 " + channel.getChannelName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、渠道信息 " + channel.getChannelName() + " 已存在");
                }
            } catch (Exception e){
                failureNum++;
                String msg = "<br/>" + failureNum + "、渠道信息 " + channel.getChannelName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }

        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

}