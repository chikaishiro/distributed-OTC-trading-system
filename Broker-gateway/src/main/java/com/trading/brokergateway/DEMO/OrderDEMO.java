package com.trading.brokergateway.DEMO;

import com.google.gson.Gson;
import com.trading.brokergateway.Controller.OrderControl;
import com.trading.brokergateway.Entity.Order;
import com.trading.brokergateway.Methods.OrderQueue;
import com.trading.brokergateway.Methods.StoreUtil;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class OrderDEMO {
    public static void generateBig(String futureID) throws Exception{
        List<Double> sellPrices = new LinkedList<Double>(){{
            add(6.1);
            add(6.1);
            add(6.1);
            add(6.4);
            add(6.4);
            add(6.5);
            add(6.6);
            add(6.9);
            add(7.1);
            add(7.2);
            add(7.5);
            add(7.5);
            add(8.1);
            add(8.3);
            add(8.6);
        }};
        List<Integer> Amounts = new LinkedList<Integer>(){{
            add(200);
            add(300);
            add(200);
            add(500);
            add(600);
            add(400);
            add(700);
            add(200);
            add(300);
            add(500);
            add(600);
            add(700);
            add(400);
            add(500);
            add(600);
        }};
        List<Double> buyPrices = new LinkedList<Double>(){{
            add(3.1);
            add(3.2);
            add(3.6);
            add(3.8);
            add(4.2);
            add(4.6);
            add(4.8);
            add(4.9);
            add(5.1);
            add(5.2);
            add(5.4);
            add(5.4);
            add(5.5);
            add(5.6);
            add(5.6);
        }};
        OrderQueue orderQueue = StoreUtil.GetQueue(futureID);
        for(int i=0;i<15;i++){
            Order ord1 = new Order(UUID.randomUUID(), futureID, 'L', 'S', sellPrices.get(i), Amounts.get(i), "localhost",
                    Calendar.getInstance().getTimeInMillis(), "seller"+String.valueOf(i));
            orderQueue.insertSell(ord1);
            Thread.sleep(100);
        }
        for(int i=0;i<15;i++){
            Order ord1 = new Order(UUID.randomUUID(), futureID, 'L', 'B', buyPrices.get(i), Amounts.get(i), "localhost",
                    Calendar.getInstance().getTimeInMillis(), "buyer"+String.valueOf(i));
            orderQueue.insertBuy(ord1);
        }
        StoreUtil.SetQueue(orderQueue,futureID);
    }

    public static void doTrade(String futureID){
        List<Order> orders = new LinkedList<Order>(){{
            add(new Order(UUID.randomUUID(), futureID, 'M', 'B',2.1,100, "localhost",
                    Calendar.getInstance().getTimeInMillis(), "trader"+String.valueOf(UUID.randomUUID())));

        }};
        for(Order order:orders){
            trade(order);
        }
    }

    public static void trade(Order order){
        Gson gson = new Gson();
        OrderControl.OrderDeal(gson.toJson(order));
    }
    public static void main(String[] args) throws Exception {

        generateBig("SB");
        generateBig("U");
        //doTrade("SB");

    }
}
