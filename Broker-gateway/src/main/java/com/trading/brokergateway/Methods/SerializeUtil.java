package com.trading.brokergateway.Methods;
import java.io.*;
import java.util.PriorityQueue;

import com.trading.brokergateway.Entity.Order;
import com.trading.brokergateway.Methods.*;
import redis.clients.jedis.Jedis;

/**
 * @author Administrator
 * 序列化对象工具类
 */
public class SerializeUtil {
    public static byte[] serialize(Object obj) {

        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;

        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);

            oos.writeObject(obj);
            byte[] byteArray = baos.toByteArray();
            return byteArray;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * 反序列化
     *
     * @param bytes
     * @return
     */
    public static Object unserialize(byte[] bytes) {

        ByteArrayInputStream bais = null;

        try {

            // 反序列化为对象
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
//            if(ois.readObject().toString().length() != 0){
//                System.out.println("xxadsa");
//            }
            return ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args){
        Order ord = new Order(3.4,"SB",'S');
        Jedis jedis = new Jedis("localhost");
        byte[] tp = jedis.get("order".getBytes());
        OrderQueue odq;



        System.out.println(tp);
        odq = (OrderQueue) SerializeUtil.unserialize(tp);


        odq.insertSell(ord);
        jedis.set("order".getBytes(), SerializeUtil.serialize(odq));

    }
}
