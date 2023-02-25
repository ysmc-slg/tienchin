package top.zxqs.tienchin.web.controller.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.zxqs.tienchin.common.service.MsmService;
import top.zxqs.tienchin.common.utils.RandomUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 发送短信
 * @author: zxq
 * @create: 2023-02-24 23:28
 */
@RestController
public class MsmApicController {

    @Autowired
    private MsmService msmService;

    @GetMapping(value = "/send/{phone}")
    public void sendCode(@PathVariable String phone) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        map.put("code", RandomUtil.getFourBitRandom());
        msmService.send(map,phone);
    }
}
