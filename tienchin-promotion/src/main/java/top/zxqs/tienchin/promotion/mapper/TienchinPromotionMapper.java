package top.zxqs.tienchin.promotion.mapper;

import top.zxqs.tienchin.promotion.domain.TienchinPromotion;

import java.util.Date;
import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author tienchin
 * @date 2023-01-08
 */
public interface TienchinPromotionMapper {


    /**
     * 获取所有促销活动信息
     * @param tienchinPromotion 促销活动信息
     * @return
     */
    public List<TienchinPromotion> selectTienchinPromotionList(TienchinPromotion tienchinPromotion);
    /**
     * 判断当前活动是否过期
     * @param endTime
     * @return
     */
    int updatePromotionByEndTime(Date endTime);

    int insertPromotion(TienchinPromotion promotion);
}