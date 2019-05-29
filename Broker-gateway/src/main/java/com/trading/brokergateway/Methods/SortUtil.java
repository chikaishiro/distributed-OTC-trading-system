package com.trading.brokergateway.Methods;

import com.google.gson.Gson;
import com.trading.brokergateway.Entity.Order;

import java.util.*;
import java.util.stream.Collectors;

public class SortUtil {
    public static List<Order> sortQueue(PriorityQueue<Order> orderQueue,boolean small2big){
        int prevPrice = -1;
        int i;
        if(small2big){
            i= 1;
        }
        else{
            i = -1;
        }
        List<Order> result = orderQueue.stream()
                .sorted(Comparator.comparing((Order p) -> i*p.getPrice())
                        .thenComparing(Order::getTimeStamp)).collect(Collectors.toList());
//        Gson gs = new Gson();
//        for (Order order :result){
//            System.out.println(gs.toJson(order));
//        }
        return result;

    }

    public static void main(String[] args) {
        OrderQueue orderQueue = new OrderQueue();
        Order ord1 = new Order(UUID.randomUUID(), "SB", 'M', 'S', 8.1, 100, "2.4",
                6, "xxx");
        Order ord2 = new Order(UUID.randomUUID(), "SB", 'M', 'S', 8.1, 100, "2.4",
                8, "xxx");
        Order ord3 = new Order(UUID.randomUUID(), "SB", 'M', 'S', 6.1, 100, "2.4",
                Calendar.getInstance().getTimeInMillis(), "xxx");
        Order ord4 = new Order(UUID.randomUUID(), "SB", 'M', 'B', 5.1, 100, "2.4",
                Calendar.getInstance().getTimeInMillis(), "xxx");
        Order ord5 = new Order(UUID.randomUUID(), "SB", 'M', 'B', 4.1, 100, "2.4",
                6, "xxx");
        Order ord6 = new Order(UUID.randomUUID(), "SB", 'M', 'B', 4.1, 100, "2.4",
                5, "xxx");
        orderQueue.insertSell(ord1);
        orderQueue.insertSell(ord2);
        orderQueue.insertSell(ord3);
        orderQueue.insertBuy(ord4);
        orderQueue.insertBuy(ord5);
        orderQueue.insertBuy(ord6);
        sortQueue(orderQueue.getBuyQueue(),false);
        sortQueue(orderQueue.getSellQueue(),true);
    }
}
