package top.zxqs.tienchin.promotion.service;

import top.zxqs.tienchin.promotion.domain.TienchinPromotion;

import java.util.List;

/**
 * 促销活动 Service接口
 *
 * @author tienchin
 * @date 2023-01-08
 */
public interface ITienchinPromotionService {


    /**
     * 查询【请填写功能名称】列表
     *
     * @param tienchinPromotion 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TienchinPromotion> selectTienchinPromotionList(TienchinPromotion tienchinPromotion);


}