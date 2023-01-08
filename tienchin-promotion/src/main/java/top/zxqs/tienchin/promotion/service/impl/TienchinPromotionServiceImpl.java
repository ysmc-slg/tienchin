package top.zxqs.tienchin.promotion.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zxqs.tienchin.promotion.domain.TienchinPromotion;
import top.zxqs.tienchin.promotion.mapper.TienchinPromotionMapper;
import top.zxqs.tienchin.promotion.service.ITienchinPromotionService;

import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author tienchin
 * @date 2023-01-08
 */
@Service
public class TienchinPromotionServiceImpl implements ITienchinPromotionService {
    @Autowired
    private TienchinPromotionMapper tienchinPromotionMapper;


    /**
     * 获取所有促销活动信息
     * @param tienchinPromotion 促销活动信息
     * @return
     */
    @Override
    public List<TienchinPromotion> selectTienchinPromotionList(TienchinPromotion tienchinPromotion) {
        return tienchinPromotionMapper.selectTienchinPromotionList(tienchinPromotion);
    }


}