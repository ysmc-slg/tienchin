package top.zxqs.tienchin.framework.webSocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import top.zxqs.tienchin.common.utils.SecurityUtils;

/**
 * websocket 消息处理
 *
 * @author ruoyi
 */
@Component
@ServerEndpoint("/websocket/message")
public class WebSocketServer {
    /**
     * WebSocketServer 日志控制器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 默认最多允许同时在线人数100
     */
    public static int socketMaxOnlineCount = 100;

    private static Semaphore socketSemaphore = new Semaphore(socketMaxOnlineCount);
    public static Map<String, Session> USER_SESSIONS = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) throws Exception {
        System.out.println(session.getUserPrincipal());

        String username = SecurityUtils.getUsername();

        //从登录链接获取用户信息，记录下来
        USER_SESSIONS.remove(username);
        USER_SESSIONS.put(username, session);
        //给其他用户发送有新用户登录上来
        for (Map.Entry<String, Session> it : USER_SESSIONS.entrySet()) {
            if (!it.getKey().equals(username)) {
                it.getValue().getBasicRemote().sendText(username + "-登录");
            }
        }

        boolean semaphoreFlag = false;
        // 尝试获取信号量
        semaphoreFlag = SemaphoreUtils.tryAcquire(socketSemaphore);
        if (!semaphoreFlag) {
            // 未获取到信号量
            LOGGER.error("\n 当前在线人数超过限制数- {}", socketMaxOnlineCount);
            WebSocketUsers.sendMessageToUserByText(session, "当前在线人数超过限制数：" + socketMaxOnlineCount);
            session.close();
        } else {
            // 添加用户
            WebSocketUsers.put(session.getId(), session);
            LOGGER.info("\n 建立连接 - {}", session);
            LOGGER.info("\n 当前人数 - {}", WebSocketUsers.getUsers().size());
            WebSocketUsers.sendMessageToUserByText(session, "连接成功");
        }
    }

    /**
     * 连接关闭时处理
     */
    @OnClose
    public void onClose(Session session) {
        LOGGER.info("\n 关闭连接 - {}", session);
        // 移除用户
        WebSocketUsers.remove(session.getId());
        // 获取到信号量则需释放
        SemaphoreUtils.release(socketSemaphore);
    }

    /**
     * 抛出异常时处理
     */
    @OnError
    public void onError(Session session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            // 关闭连接
            session.close();
        }
        String sessionId = session.getId();
        LOGGER.info("\n 连接异常 - {}", sessionId);
        LOGGER.info("\n 异常信息 - {}", exception);
        // 移出用户
        WebSocketUsers.remove(sessionId);
        // 获取到信号量则需释放
        SemaphoreUtils.release(socketSemaphore);
    }

    /**
     * 服务器接收到客户端消息时调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        String msg = message.replace("你", "我").replace("吗", "");
        WebSocketUsers.sendMessageToUserByText(session, msg);
    }
}
