package top.zxqs.tienchin.channel.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import top.zxqs.tienchin.common.annotation.Excel;
import top.zxqs.tienchin.common.core.domain.BaseEntity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
    @NotEmpty(message = "渠道名称不能为空")
    private String channelName;

    /**
     * 渠道状态 0启动 1停止
     */
    @Excel(name = "渠道状态",prompt="请选择",combo="正常,禁用",readConverterExp="0=禁用,1=正常")
    @NotNull(message = "渠道状态不能为空")
    @Max(value = 1,message = "渠道状态最大为1")
    @Min(value = 0,message = "渠道状态最小为0")
    private String status;

    /**
     * 渠道类型 1线上渠道，2线下渠道
     */
    @Excel(name = "渠道类型",prompt="请选择",combo="线上渠道,线下渠道",readConverterExp="1=线上渠道,2=线下渠道")
    @NotNull(message = "渠道类型不能为空")
    @Max(value = 2,message = "渠道类型最大为2")
    @Min(value = 1,message = "渠道类型最小为1")
    private Integer type;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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