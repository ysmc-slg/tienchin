package top.zxqs.tienchin.web.controller.tienchin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.zxqs.tienchin.common.annotation.Log;
import top.zxqs.tienchin.common.core.controller.BaseController;
import top.zxqs.tienchin.common.core.domain.AjaxResult;
import top.zxqs.tienchin.common.core.page.TableDataInfo;
import top.zxqs.tienchin.common.enums.BusinessType;
import top.zxqs.tienchin.promotion.domain.TienchinPromotion;
import top.zxqs.tienchin.promotion.service.ITienchinPromotionService;

import java.util.List;

/**
 * 促销活动
 * @author: zxq
 * @create: 2023-01-08 16:54
 */
@RestController
@RequestMapping("/tienchin/promotion")
public class TienchinPromotionController extends BaseController {

    @Autowired
    private ITienchinPromotionService promotionService;

    @GetMapping("/list")
    @PreAuthorize("hasPermission('tienchin:promotion:list')")
    public TableDataInfo list(TienchinPromotion promotion){
        startPage();
        List<TienchinPromotion> tienchinPromotions = promotionService.selectTienchinPromotionList(promotion);
        return getDataTable(tienchinPromotions);
    }

    /**
     * 添加促销活动
     * @param promotion
     * @return
     */
    @PostMapping("/add")
    @Log(title = "促销活动", businessType = BusinessType.INSERT)
    @PreAuthorize("hasPermission('tienchin:promotion:add')")
    public AjaxResult addPromotion(@Validated @RequestBody TienchinPromotion promotion){
        int rows = promotionService.insertPromotion(promotion);

        return toAjax(rows);
    }



}
