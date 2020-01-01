package com.lian.threaddemo.controller;

import com.lian.threaddemo.config.MyEndpointConfigure;
import com.lian.threaddemo.service.ExecService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@RestController
@RequestMapping("/pages/thread/")
@ServerEndpoint(value = "/pages/thread/log", configurator = MyEndpointConfigure.class)
public class LoggingExecService {

    private final static Logger log = LogManager.getLogger(LoggingExecService.class);



    @Autowired
    ExecService execService;

    private static Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();


    @OnOpen
    public void onOpen(Session session){

        sessionMap.put(session.getId(), session);
        new Thread(() -> {
            log.info("------onOpen--------");
            while (sessionMap.get(session.getId()) != null) {
                try {
                    execService.sendLog(session);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        //从集合中删除

    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 服务器接收到客户端消息时调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {

    }

    /**
     * 封装一个send方法，发送消息到前端
     */
    private void send(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
