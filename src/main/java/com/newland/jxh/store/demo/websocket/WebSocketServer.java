package com.newland.jxh.store.demo.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author qyw
 * @Description websocket 简易聊天室
 * @Date Created in 14:35 2018/8/19
 */
@ServerEndpoint("/chat-room/{username}")
@Component
@Slf4j
public class WebSocketServer {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int                                  onlineCount  = 0;

    private static Map<String, Session> livingSessions = new ConcurrentHashMap<String, Session>();

    @OnOpen
    public void openSession(@PathParam("username") String username, Session session) {

        String sessionId = session.getId();

        livingSessions.put(sessionId, session);

        addOnlineCount();

        log.info("有新窗口开始监听:{},当前在线人数为:{}" ,username,getOnlineCount());

        sendTextAll("欢迎用户[" + username + "] 来到聊天室！");

    }

    @OnMessage
    public void onMessage(@PathParam("username") String username, Session session, String message) {

//        sendText(session, "用户[" + username + "] : " + message);

        sendTextAll("用户[" + username + "] : " + message);
    }

    private void sendTextAll(String message) {

        livingSessions.forEach((sessionId, session) -> {
            sendText(session,message);
        });
    }

    @OnClose
    public void onClose(@PathParam("username") String username, Session session) {

        String sessionId = session.getId();

        //当前的Session 移除
        livingSessions.remove(sessionId);

        subOnlineCount();           //在线数减1

        log.info("有一连接关闭！当前在线人数为:{}", getOnlineCount());

        //并且通知其他人当前用户已经离开聊天室了
        sendTextAll("用户[" + username + "] 已经离开聊天室了！");
    }


    private void sendText(Session session, String message) {

        RemoteEndpoint.Basic basic = session.getBasicRemote();

        try {
            basic.sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
