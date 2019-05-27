package com.trading.brokergateway.Methods;

import com.sun.org.apache.xpath.internal.operations.Or;
import com.trading.brokergateway.*;
import com.trading.brokergateway.Entity.Order;
import redis.clients.jedis.Jedis;

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
}
