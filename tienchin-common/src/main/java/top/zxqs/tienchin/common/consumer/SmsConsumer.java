package top.zxqs.tienchin.common.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import top.zxqs.tienchin.common.constant.RabbitConstant;
import top.zxqs.tienchin.common.core.redis.RedisCache;
import top.zxqs.tienchin.common.utils.sms.SmsUtil;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: zxq
 * @create: 2023-02-25 14:12
 */
@Component
public class SmsConsumer {
    @Autowired
    private RedisCache redisCache;

    @RabbitListener(queues = RabbitConstant.MSM_QUEUE)
    public void onMessage(Message message, Channel channel) {
        MessageHeaders headers = message.getHeaders();
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);

        Map<String, Object> payload = (Map<String, Object>) message.getPayload();
        try {
            // 发送短信验证码
            SmsUtil.send(payload);
            cache(payload);
            basicAck(channel,tag);
        } catch (Exception e){
            basicAck(channel,tag);
            e.printStackTrace();
        }
    }


    /**
     * 手动 ack
     * @param channel
     * @param tag
     */
    public void basicAck(Channel channel,Long tag){
        try {
            channel.basicAck(tag,false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 缓存 短信验证码
     * @param map
     */
    public void cache(Map<String,Object> map){

        String phoneNumber = ((String) map.get("phoneNumber"));
        String code = ((String) map.get("code"));

        String key = getSmsKey(phoneNumber);
        // 将验证码保存到redis，有效期 3 分钟
        redisCache.setCacheObject(key,code, 3, TimeUnit.MINUTES);
    }

    /**
     * 获取保存在redis的验证码的 key
     * @return
     */
    public String getSmsKey(String phoneNumber){
        return RabbitConstant.SMS_KEY + phoneNumber;
    }

}
