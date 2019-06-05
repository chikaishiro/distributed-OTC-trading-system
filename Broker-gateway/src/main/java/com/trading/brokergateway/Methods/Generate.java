package com.trading.brokergateway.Methods;

import com.google.gson.Gson;
import com.trading.brokergateway.Entity.Order;
import org.apache.catalina.Store;

import java.util.*;
import java.util.stream.Collectors;

public class Generate {
    public static void test1(){
        Order ord1 = new Order(UUID.randomUUID(), "SB", 'M', 'S', 8.1, 100, "2.4",
                Calendar.getInstance().getTimeInMillis(), "xxx");
        Order ord2 = new Order(UUID.randomUUID(), "SB", 'M', 'S', 7.1, 100, "2.4",
                Calendar.getInstance().getTimeInMillis(), "xxx");
        Order ord3 = new Order(UUID.randomUUID(), "SB", 'M', 'S', 6.1, 100, "2.4",
                Calendar.getInstance().getTimeInMillis(), "xxx");
        Order ord4 = new Order(UUID.randomUUID(), "SB", 'M', 'B', 5.1, 100, "2.4",
                Calendar.getInstance().getTimeInMillis(), "xxx");
        Order ord5 = new Order(UUID.randomUUID(), "SB", 'M', 'B', 4.1, 100, "2.4",
                Calendar.getInstance().getTimeInMillis(), "xxx");
        Order ord6 = new Order(UUID.randomUUID(), "SB", 'M', 'B', 3.1, 100, "2.4",
                Calendar.getInstance().getTimeInMillis(), "xxx");
        OrderQueue orderQueue = StoreUtil.GetQueue("SB");
        orderQueue.insertSell(ord1);
        orderQueue.insertSell(ord2);
        orderQueue.insertSell(ord3);
        orderQueue.insertBuy(ord4);
        orderQueue.insertBuy(ord5);
        orderQueue.insertBuy(ord6);
        StoreUtil.SetQueue(orderQueue,"SB");
    }
    public static void test2(){
        Order ord1 = new Order(UUID.randomUUID(), "SB", 'L', 'S', 8.1, 100, "2.4",
                Calendar.getInstance().getTimeInMillis(), "xxx");
        Order ord2 = new Order(UUID.randomUUID(), "SB", 'L', 'S', 6.1, 100, "2.4",
                2, "xxx");
        Order ord3 = new Order(UUID.randomUUID(), "SB", 'L', 'S', 6.1, 100, "2.4",
                1, "xxx");
        Order ord4 = new Order(UUID.randomUUID(), "SB", 'L', 'B', 5.1, 100, "2.4",
                2, "xxx");
        Order ord5 = new Order(UUID.randomUUID(), "SB", 'L', 'B', 5.1, 100, "2.4",
                1, "xxx");
        Order ord6 = new Order(UUID.randomUUID(), "SB", 'L', 'B', 3.1, 100, "2.4",
                Calendar.getInstance().getTimeInMillis(), "xxx");
        OrderQueue orderQueue = StoreUtil.GetQueue("SB");
        orderQueue.insertSell(ord1);
        orderQueue.insertSell(ord2);
        orderQueue.insertSell(ord3);
        orderQueue.insertBuy(ord4);
        orderQueue.insertBuy(ord5);
        orderQueue.insertBuy(ord6);
        StoreUtil.SetQueue(orderQueue,"SB");
    }
    public static void main(String[] args) {
        test2();


    }
}
