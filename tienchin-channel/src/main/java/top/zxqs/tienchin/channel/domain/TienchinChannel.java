package top.zxqs.tienchin.channel.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import top.zxqs.tienchin.common.annotation.Excel;
import top.zxqs.tienchin.common.core.domain.BaseEntity;

/**
 * 渠道管理对象 tienchin_channel
 *
 * @author tienchin
 * @date 2023-01-01
 */
public class TienchinChannel extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long channelId;

    /**
     * 渠道名称
     */
    @Excel(name = "渠道名称")
    private String channelName;

    /**
     * 渠道状态 0启动 1停止
     */
    @Excel(name = "渠道状态 0启动 1停止")
    private String status;

    /**
     * 渠道类型 1线上渠道，2线下渠道
     */
    @Excel(name = "渠道类型 1线上渠道，2线下渠道")
    private Long type;

    /**
     * $column.columnComment
     */
    private Integer delFlag;

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getType() {
        return type;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("channelId", getChannelId())
                .append("channelName", getChannelName())
                .append("status", getStatus())
                .append("remark", getRemark())
                .append("type", getType())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("updateBy", getUpdateBy())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}