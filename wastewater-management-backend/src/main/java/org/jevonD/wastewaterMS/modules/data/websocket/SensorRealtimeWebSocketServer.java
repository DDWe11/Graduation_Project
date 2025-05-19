package org.jevonD.wastewaterMS.modules.data.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 传感器实时数据 WebSocket 服务端
 */
@Component
@ServerEndpoint("/ws/realtime-data")
public class SensorRealtimeWebSocketServer {

    // 保存所有会话（线程安全）
    private static final Set<Session> SESSIONS = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        SESSIONS.add(session);
        System.out.println("WebSocket连接建立: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        SESSIONS.remove(session);
        System.out.println("WebSocket连接关闭: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        SESSIONS.remove(session);
        System.err.println("WebSocket发生错误: " + session.getId() + " " + throwable.getMessage());
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        // 可根据 message 处理客户端自定义请求，如订阅哪些传感器等
        System.out.println("收到消息: " + message);
    }

    /**
     * 对所有客户端广播消息
     */
    public static void broadcast(String jsonMsg) {
        for (Session session : SESSIONS) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(jsonMsg);
                } catch (IOException e) {
                    System.err.println("WebSocket推送失败: " + e.getMessage());
                }
            }
        }
    }
}