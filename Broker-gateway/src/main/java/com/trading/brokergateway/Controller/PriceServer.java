package com.trading.brokergateway.Controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.*;
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
            try {
            while(true){

                    Thread.sleep(1500);
                    String i = new Gson().toJson(getRecentPrice(futureID));
                    i = i+";"+calcVWAP(futureID)+";"+calcTWAP(futureID);
                    sendMessage(session,i);
                }


            }
            catch (Exception e){
                return;
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

    public LinkedList<Double> getRecentPrice(String futureID) throws Exception{
        LinkedList<Double> res = new LinkedList<>();
        JDBCConn link = new JDBCConn();
        String target = "'"+futureID+"'";
        ResultSet rs = link.getRs("select price,finish_time from result where future_id = "+ target + "order by finish_time desc limit 20");
        while (rs.next()){
            double price = rs.getDouble("price");
            res.add(price);
        }
        LinkedList<Double> res2 = new LinkedList<>();
        for(int i=19;i>=0;i--){
            res2.add(res.get(i));
        }
        link.close();
        return res2;
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

    public static String calcVWAP(String futureID) throws Exception{
        double totalAmount = 0;
        int totalVolume = 0;
        LinkedList<Double> res = new LinkedList<>();
        JDBCConn link = new JDBCConn();
        String target = "'"+futureID+"'";
        ResultSet rs = link.getRs("select price,amount from result where future_id = "+ target + "order by finish_time desc limit 15");
        while (rs.next()){
            double price = rs.getDouble("price");
            int amount = rs.getInt("amount");
            totalAmount += price * amount;
            totalVolume += amount;
            res.add(price);
        }
        DecimalFormat df = new DecimalFormat("#.000");
        double ret = totalAmount/(double)totalVolume;
        link.close();
        return df.format(ret);
    }

    public static String calcTWAP(String futureID) throws Exception{
        long step = 3600*100;
        double sum = 0;
        int cnt = 0;
        LinkedList<Double> res = new LinkedList<>();
        JDBCConn link = new JDBCConn();
        String target = "'"+futureID+"'";
        Long now = Calendar.getInstance().getTimeInMillis();
        Long start = now - 10*step;
        Long temp = now-step;
        double MAX =0;
        double MIN = 0;
        ResultSet rss = link.getRs("select Max(price) as max,Min(price) as min from result" +
                " where future_id = " + target + " and finish_time>" + String.valueOf(now-(step/10)) + " and finish_time<" + String.valueOf(now));
        while(rss.next()){
            MAX = rss.getDouble("max");
            MIN = rss.getDouble("min");
        }
        while(temp>=start) {
            ResultSet rs = link.getRs("select Max(price) as max,Min(price) as min from result" +
                    " where future_id = " + target + " and finish_time>" + String.valueOf(temp) + " and finish_time<" + String.valueOf(now));
            while (rs.next()) {
                double max = rs.getDouble("max");
                double min = rs.getDouble("min");
                double avg = (max + min) / 2;
                //System.out.println(avg);
                if(avg != 0){
                    sum += avg;
                    cnt++;
                }
            }
            temp -= step;
        }
        DecimalFormat df = new DecimalFormat("#.000");
        link.close();
        if(cnt == 0){
            return "一小时内暂无成交记录";
        }
        return df.format(sum/cnt)+";"+String.valueOf(MAX)+"/"+String.valueOf(MIN);
    }


    public static void main(String args[]) throws Exception{
        System.out.println(PriceServer.calcTWAP("SB"));
    }


}