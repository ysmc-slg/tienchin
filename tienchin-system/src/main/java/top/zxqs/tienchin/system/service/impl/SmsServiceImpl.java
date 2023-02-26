package top.zxqs.tienchin.system.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zxqs.tienchin.common.constant.RabbitConstant;
import top.zxqs.tienchin.common.core.redis.RedisCache;
import top.zxqs.tienchin.common.utils.RandomUtil;
import top.zxqs.tienchin.common.utils.sms.SmsUtil;
import top.zxqs.tienchin.system.domain.SendLog;
import top.zxqs.tienchin.system.service.ISendLogService;
import top.zxqs.tienchin.system.service.SmsService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ISendLogService sendLogService;

    /**
     * 发送验证码，通过rabbitmq发送
     * @param phoneNumber     手机号
     * @return
     */
    @Override
    public void send(String phoneNumber){
        Map<String,Object> map = new HashMap<>();

        // 检查手机号是否合规
        SmsUtil.checkPhoneNumber(phoneNumber);
        // 获取验证码
        String code = RandomUtil.getFourBitRandom();
        // 消息唯一编码
        String msgId = UUID.randomUUID().toString();

        map.put("phoneNumber",phoneNumber);
        map.put("code",code);

        // 通过rabbitmq发送手机号和验证码，由消费者去发送短信
        rabbitTemplate.convertAndSend(RabbitConstant.MSM_EXCHANGE,RabbitConstant.MSM_ROUTING_KEY, map,new CorrelationData(msgId));

        SendLog sendLog = sendLog(msgId,map);
        // 将发送的消息保存到日志，如果发送失败可以重新发送
        int result = sendLogService.insertSendLog(sendLog);
    }


    /**
     * 封装 sendLog
     * @param msgId
     * @param message
     * @return
     */
    public SendLog sendLog(String msgId, Map<String,Object> message){
        SendLog sendLog = new SendLog();

        sendLog.setName("手机短息验证码登录");
        sendLog.setCount(0);
        sendLog.setMsgId(msgId);
        sendLog.setMessage(JSON.toJSONString(message));
        sendLog.setRouteKey(RabbitConstant.MSM_ROUTING_KEY);
        sendLog.setExchange(RabbitConstant.MSM_EXCHANGE);
        // 重试时间要比当前时间晚一分钟，防止发送消息还未完成，定时任务就扫描到重新发送。
        sendLog.setTryTime(new Date(System.currentTimeMillis() + 1000 * 60 * RabbitConstant.MSG_TIMEOUT));
        sendLog.setCreateTime(new Date());

        return sendLog;
    }

}