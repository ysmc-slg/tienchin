package top.zxqs.tienchin.web.controller.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.zxqs.tienchin.system.service.SmsService;

/**
 * 发送短信
 * @author: zxq
 * @create: 2023-02-24 23:28
 */
@RestController
public class SmsApicController {

    @Autowired
    private SmsService msmService;

    @GetMapping(value = "/send/{phoneNumber}")
    public void sendCode(@PathVariable String phoneNumber) {
        msmService.send(phoneNumber);
    }
}
