package top.zxqs.tienchin.promotion.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zxqs.tienchin.common.utils.DateUtils;
import top.zxqs.tienchin.common.utils.SecurityUtils;
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
        expireActivity(tienchinPromotion);
        return tienchinPromotionMapper.selectTienchinPromotionList(tienchinPromotion);
    }
    /**
     * 添加促销活动
     * @param promotion
     * @return
     */
    @Override
    public int insertPromotion(TienchinPromotion promotion) {
        promotion.setCreateBy(SecurityUtils.getUsername());
        promotion.setCreateTime(DateUtils.getNowDate());
        promotion.setStatus(1);

        return tienchinPromotionMapper.insertPromotion(promotion);
    }


    /**
     * 将过期活动设置为过期
     * @param tienchinPromotion
     * @return
     */
    public void expireActivity(TienchinPromotion tienchinPromotion){
        tienchinPromotionMapper.updatePromotionByEndTime(tienchinPromotion.getEndTime());
    }


}