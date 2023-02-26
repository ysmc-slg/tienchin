package top.zxqs.tienchin.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import top.zxqs.tienchin.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * @author: zxq
 * @create: 2023-02-25 21:46
 */
public class SendLog extends BaseEntity {
    private String msgId;
    private String name;
    /**
     * 0 消息投递中   1 投递成功   2投递失败
     */
    private Integer status;
    private String routeKey;
    private String exchange;
    private Integer count;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date tryTime;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getTryTime() {
        return tryTime;
    }

    public void setTryTime(Date tryTime) {
        this.tryTime = tryTime;
    }
}
