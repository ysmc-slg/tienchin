package top.zxqs.tienchin.common.service;

import java.util.Map;

/**
 * 短信发送service
 * @author: zxq
 * @create: 2023-02-24 22:59
 */
public interface MsmService {
    /**
     * 发送验证码
     * @param param
     * @param phone
     * @return
     */
    void send(Map<String, Object> param, String phone) throws Exception;
}
