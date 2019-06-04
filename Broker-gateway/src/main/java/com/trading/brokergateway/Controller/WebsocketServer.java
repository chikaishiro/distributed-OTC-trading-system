package com.trading.brokergateway.Controller;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.google.gson.Gson;
import com.trading.brokergateway.Methods.StoreUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;


@ServerEndpoint("/orderbook")
@Component
public class WebsocketServer {
    // 日志记录
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 在线人数统计
    private AtomicInteger onlineCount = new AtomicInteger(0);

    // 在线session记录
    private Queue<Session> sessionQueue = new ConcurrentLinkedQueue<>();





    public Queue<Session> getSessionQueue() {
        return sessionQueue;
    }

    public AtomicInteger getOnlineCount() {
        return onlineCount;
    }

    @OnOpen
    public void onOpen(Session session) {
        sessionQueue.add(session);
        onlineCount.getAndIncrement();
        System.out.println("新连接加入，当前在线人数为：" + onlineCount);
    }

    @OnClose
    public void onClose(Session session) {
        sessionQueue.remove(session);
        onlineCount.getAndDecrement();
        System.out.println("连接关闭，当前在线人数：" + onlineCount);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        sessionQueue.remove(session);
        logger.error("发生错误");
        throwable.printStackTrace();
    }

    public class broadCastODB implements Runnable {
        private String futureID;
        private Session session;

        public broadCastODB(String futureID,Session session) {
            this.futureID = futureID;
            this.session = session;
        }

        public void run() {
            while(true){
                try {
                    Thread.sleep(1000);
                    Gson gson = new Gson();
                    String i = gson.toJson(StoreUtil.GetQueue(futureID));
                    sendMessage(session,i);
                }
                catch (Exception e){
                    return;
                }

            }
        }
    }
    @OnMessage
    public void onMessage(final Session session, String message) {
        System.out.println("来自客户端的消息：" + message);
        String futureID = message;
        broadCastODB bodb = new broadCastODB(futureID,session);
        bodb.run();
//        String[] params = message.split(",");
//        if (params[0].equals("orderBook")) {
//            deliverFuturesMarket(session, message);
//        } else if (params[0].equals("trade")) {
//            deliverTradeHistory(session, message);
//        }

    }




    private void sendMessage(Session session, String msg) {
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcastAll(String msg) {
        for(Session s : sessionQueue) {
            sendMessage(s, msg);
        }
    }

}