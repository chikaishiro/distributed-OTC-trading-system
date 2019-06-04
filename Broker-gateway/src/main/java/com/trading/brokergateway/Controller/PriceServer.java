package com.trading.brokergateway.Controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.google.gson.Gson;
import com.trading.brokergateway.Methods.StoreUtil;
import com.trading.brokergateway.Util.JDBCConn;
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


@ServerEndpoint("/price")
@Component
public class PriceServer {
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

    public class broadCastPrice implements Runnable {
        private String futureID;
        private Session session;

        public broadCastPrice(String futureID,Session session) {
            this.futureID = futureID;
            this.session = session;
        }

        public void run() {
            while(true){
                try {
                    Thread.sleep(1000);
                    String i = new Gson().toJson(getRecentPrice(futureID));
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
        broadCastPrice bcp = new broadCastPrice(futureID,session);
        bcp.run();


    }

    public LinkedList<String> getRecentPrice(String futureID) throws Exception{
        LinkedList<String> res = new LinkedList<>();
        JDBCConn link = new JDBCConn();
        String target = "'"+futureID+"'";
        ResultSet rs = link.getRs("select price,finish_time from result where future_id = "+ target + "order by finish_time desc limit 10");
        while (rs.next()){
            double price = rs.getDouble("price");
            res.add(String.valueOf(price));
        }
        return res;
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