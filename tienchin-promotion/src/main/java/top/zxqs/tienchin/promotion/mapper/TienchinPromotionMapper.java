package top.zxqs.tienchin.promotion.mapper;

import top.zxqs.tienchin.promotion.domain.TienchinPromotion;

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

}