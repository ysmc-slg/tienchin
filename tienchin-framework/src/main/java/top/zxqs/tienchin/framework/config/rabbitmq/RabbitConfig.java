package top.zxqs.tienchin.framework.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.zxqs.tienchin.common.constant.RabbitConstant;
import top.zxqs.tienchin.system.service.ISendLogService;

import javax.annotation.PostConstruct;

/**
 * @author: zxq
 * @create: 2023-02-25 13:49
 */
@Configuration
public class RabbitConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ISendLogService sendLogService;
    /**
     * 短信发送的队列
     * @return
     */
    @Bean
    public Queue msmQueue(){
        return new Queue(RabbitConstant.MSM_QUEUE,true,false,false);
    }

    @Bean
    public DirectExchange msmDirectExchange(){
        return new DirectExchange(RabbitConstant.MSM_EXCHANGE,true,false);
    }

    @Bean
    public Binding msmBinding(){
        return BindingBuilder.bind(msmQueue())
                .to(msmDirectExchange())
                .with(RabbitConstant.MSM_ROUTING_KEY);
    }

    @PostConstruct
    public void initRabbitTemplate() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 消息到达交换机的回调
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if(ack){
            String msgId = correlationData.getId();
            // 消息到达交换机，将 send_log 中的 status 设置为1（发送成功）
            sendLogService.updateSendLogStatusByMsgId(msgId);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {

    }

}
