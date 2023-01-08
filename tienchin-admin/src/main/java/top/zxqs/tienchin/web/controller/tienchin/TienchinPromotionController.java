package top.zxqs.tienchin.web.controller.tienchin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zxqs.tienchin.common.core.controller.BaseController;
import top.zxqs.tienchin.common.core.page.TableDataInfo;
import top.zxqs.tienchin.promotion.domain.TienchinPromotion;
import top.zxqs.tienchin.promotion.service.ITienchinPromotionService;

import java.util.List;

/**
 * @author: zxq
 * @create: 2023-01-08 16:54
 */
@RestController
@RequestMapping("/tienchin/promotion")
public class TienchinPromotionController extends BaseController {

    @Autowired
    private ITienchinPromotionService promotionService;

    @GetMapping("/list")
    public TableDataInfo list(TienchinPromotion promotion){
        startPage();
        List<TienchinPromotion> tienchinPromotions = promotionService.selectTienchinPromotionList(promotion);
        return getDataTable(tienchinPromotions);
    }

}
