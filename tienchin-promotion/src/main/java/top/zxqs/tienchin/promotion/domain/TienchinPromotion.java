package top.zxqs.tienchin.promotion.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import top.zxqs.tienchin.common.annotation.Excel;
import top.zxqs.tienchin.common.core.domain.BaseEntity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 促销活动对象 tienchin_promotion
 *
 * @author tienchin
 * @date 2023-01-08
 */
public class TienchinPromotion extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 促销活动ID
     */
    private Long promotionId;

    /**
     * 活动名称
     */
    @Excel(name = "活动名称")
    @NotNull(message = "活动名称不能为空")
    private String name;

    /**
     * 渠道ID
     */
    @Excel(name = "渠道ID")
    @NotNull(message = "请选择渠道id")
    private Long channelId;

    @Excel(name = "渠道名称")
    private String channelName;

    /**
     * 活动简介
     */
    @NotNull(message = "活动简介不能为空")
    @Size(min = 0,max = 255,message = "活动简介最多255个字符")
    @Excel(name = "活动简介")
    private String info;

    /**
     * 活动类型 1 折扣券，2 代金券
     */
    @NotNull(message = "活动类型不能为空")
    @Max(value = 10,message = "折扣券值无效")
    @Min(value = 0,message = "折扣券值无效")
    @Excel(name = "活动类型 1 折扣券，2 代金券")
    private Integer type;

    /**
     * 折扣券
     */
    @Excel(name = "折扣券")
    private Double discount;

    /**
     * 代金券
     */
    @Excel(name = "代金券")
    @Min(value = 0,message = "代金券无效")
    private Double voucher;

    /**
     * 活动状态 0 过期，1正常
     */
    @Excel(name = "活动状态 0 过期，1正常")
    @Max(value = 1,message = "活动状态无效")
    @Min(value = 0,message = "活动状态无效")
    private Integer status;

    /**
     * 活动开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @NotNull(message = "活动开始时间不能为空")
    @Excel(name = "活动开始时间", width = 30, dateFormat = "yyyy-MM-dd hh:mm:ss")
    private Date beginTime;

    /**
     * 活动结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @NotNull(message = "活动结束时间不能为空")
    @Excel(name = "活动结束时间", width = 30, dateFormat = "yyyy-MM-dd hh:mm:ss")
    private Date endTime;

    /**
     * 逻辑删除 0正常 1删除
     */
    private Integer delFlag;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    public Long getPromotionId() {
        return promotionId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getVoucher() {
        return voucher;
    }

    public void setVoucher(Double voucher) {
        this.voucher = voucher;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return "TienchinPromotion{" +
                "promotionId=" + promotionId +
                ", name='" + name + '\'' +
                ", channelId=" + channelId +
                ", info='" + info + '\'' +
                ", type=" + type +
                ", discount=" + discount +
                ", voucher=" + voucher +
                ", status=" + status +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", delFlag=" + delFlag +
                '}';
    }
}