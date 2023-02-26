package top.zxqs.tienchin.system.service;

/**
 * 短信发送service
 * @author: zxq
 * @create: 2023-02-24 22:59
 */
public interface SmsService {
    /**
     * 发送验证码
     * @param phoneNumber
     * @return
     */
    void send(String phoneNumber);
}
