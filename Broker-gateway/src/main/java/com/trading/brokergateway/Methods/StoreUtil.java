package com.trading.brokergateway.Methods;

import com.sun.org.apache.xpath.internal.operations.Or;
import com.trading.brokergateway.*;
import com.trading.brokergateway.Entity.Order;
import redis.clients.jedis.Jedis;

import java.util.HashSet;

public class StoreUtil {
    public static void SetQueue(OrderQueue orderQueue,String FutureID){
        Jedis jedis = new Jedis("localhost");
        byte[] byt = SerializeUtil.serialize(orderQueue);
        if(orderQueue != null){
            jedis.set(FutureID.getBytes(),byt);
        }
        jedis.close();

    }

    public static OrderQueue GetQueue(String FutureID){
        OrderQueue orderQueue;
        Jedis jedis = new Jedis("localhost");
        byte[] byt = jedis.get(FutureID.getBytes());
        if(byt != null){
            orderQueue = (OrderQueue) SerializeUtil.unserialize(byt);
        }
        else{
            orderQueue = new OrderQueue();
        }
        return orderQueue;
    }

    public static void SetSet(HashSet<String> set){
        Jedis jedis = new Jedis("localhost");
        try{
            byte[] byt = SerializeUtil.serialize(set);
            jedis.set("traders".getBytes(),byt);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            jedis.close();
        }
    }

    public static HashSet<String> GetSet(String key){
        Jedis jedis = new Jedis("localhost");
        HashSet<String> set;

        byte[] byt = jedis.get(key.getBytes());
        if(byt != null){
            set = (HashSet<String>) SerializeUtil.unserialize(byt);
        }
        else{
            set = new HashSet<String>();
        }

        jedis.close();
        return set;


    }
}
