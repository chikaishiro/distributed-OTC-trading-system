package com.trading.brokergateway.Protocol;
import com.google.gson.Gson;
import com.trading.brokergateway.Entity.Order;

public class FIX {
    public static Order ParseFIX(String fix){
        Gson gson = new Gson();
        Order order =gson.fromJson(fix, Order.class);
        return order;
    }

    public static String toFIX(Order order){
        Gson gson = new Gson();
        String fix = gson.toJson(order);
        return fix;
    }

    public static void main(String[] args){

//        FIX fix = new FIX();
//        Order order = new Order(1,"SB",'S');
//        String i = fix.toFIX(order);
//        System.out.println(i);
//        Order to = fix.ParseFIX(i);
//        System.out.println(to.getPrice());
//        System.out.println(to.getWay());
//        System.out.println(to.getFutureID());
    }
}
