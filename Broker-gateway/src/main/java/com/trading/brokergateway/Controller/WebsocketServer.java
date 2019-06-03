package com.trading.brokergateway.Controller;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

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


@ServerEndpoint("/websocket")
@Component
public class WebsocketServer {
    // 日志记录
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 在线人数统计
    private AtomicInteger onlineCount = new AtomicInteger(0);

    // 在线session记录
    private Queue<Session> sessionQueue = new ConcurrentLinkedQueue<>();

    // Redis连接
    private Jedis jedis = new Jedis("localhost");


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
        logger.info("新连接加入，当前在线人数为：" + onlineCount);
    }

    @OnClose
    public void onClose(Session session) {
        sessionQueue.remove(session);
        onlineCount.getAndDecrement();
        logger.info("连接关闭，当前在线人数：" + onlineCount);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        sessionQueue.remove(session);
        logger.error("发生错误");
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(final Session session, String message) {
        //logger.info("来自客户端的消息：" + message);

        String[] params = message.split(",");
        if (params[0].equals("orderBook")) {
            deliverFuturesMarket(session, message);
        } else if (params[0].equals("trade")) {
            deliverTradeHistory(session, message);
        }

    }

    private void deliverTradeHistory(Session session, String message) {
        if (!sessionQueue.contains(session)) {
            logger.info("当前在线人数：" + onlineCount);
            return;
        }
        String[] params = message.split(",");
        String futureID = params[1];
        String brokerName = params[2];

        String key = "trade," + brokerName + "," + futureID;
        //System.out.println("key=" + key);
        String result = getTradeHistoryFromRedis(key);
        sendMessage(session, result);
        //logger.info("服务端返回: " + result);

        try {
            Thread.sleep(800);
        } catch (Exception e) {
            e.printStackTrace();
            onClose(session);
        }
    }

    private String getTradeHistoryFromRedis(String key) {
        String trades = (String)jedis.get(key);
        if (trades == null) return null;
        JSONArray jsonTrades = JSONArray.fromObject(trades);
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("type", "trade");
        jsonResult.put("data", jsonTrades);

        return jsonResult.toString();
    }

    private String getFuturesMarketFromRedis(String key) {
        String market = jedis.get(key);
        if (market == null) return null;
        JSONObject jsonMarket = JSONObject.fromObject(market);
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("type", "orderBook");
        jsonResult.put("data", jsonMarket);
        return jsonResult.toString();
    }

    private void deliverFuturesMarket(Session session, String message) {
        if (!sessionQueue.contains(session)) {
            logger.info("当前在线人数：" + onlineCount);
            return;
        }
        String[] params = message.split(",");
        String futureID = params[1];
        String brokerName = params[2];

        String key = "orderBook," + brokerName + "," + futureID;
        //System.out.println("key=" + key);
        String result = getFuturesMarketFromRedis(key);
        sendMessage(session, result);
        //logger.info("服务端返回: " + result);

        try {
            Thread.sleep(800);
        } catch (Exception e) {
            e.printStackTrace();
            onClose(session);
        }
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