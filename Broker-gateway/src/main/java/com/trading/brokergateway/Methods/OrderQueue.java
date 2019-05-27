package com.trading.brokergateway.Methods;

import com.trading.brokergateway.Entity.Order;

import java.io.Serializable;
import java.util.*;
public class OrderQueue implements Serializable {
    private PriorityQueue<Order> SellQueue;
    private PriorityQueue<Order> BuyQueue;
    public OrderQueue(){
        this.SellQueue =  new PriorityQueue<Order>(new SmallComparator());
        this.BuyQueue = new PriorityQueue<Order>(new BigComparator());
    }

    public void insertBuy(Order order){
        this.BuyQueue.add(order);
    }

    public void insertSell(Order order){
        this.SellQueue.add(order);
    }

    public PriorityQueue<Order> getBuyQueue() {
        return this.BuyQueue;
    }
    public PriorityQueue<Order> getSellQueue(){
        return this.SellQueue;
    }

//    public static void main(String[] args){
//        queueTest();
//    }

//    public static void queueTest(){
//        OrderQueue q = new OrderQueue();
//        Order o1 = new Order(2.11,"SB",'S');
//        Order o2 = new Order(2.22,"SB",'S');
//        Order o3 = new Order(2.33,"SB",'S');
//        q.insertBuy(o1);
//        q.insertSell(o1);
//        q.insertBuy(o2);
//        q.insertSell(o2);
//        q.insertBuy(o3);
//        q.insertSell(o3);
//        PriorityQueue<Order> q1 = q.getBuyQueue();
//        PriorityQueue<Order> q2 = q.getSellQueue();
//        System.out.println(q1.size());
//        System.out.println(q2.size());
//
//        while(q1.size() > 0 ){
//            Order temp = q1.poll();
//            System.out.println(temp.getPrice());
//
//        }
//        while(q2.size() > 0 ){
//            Order temp = q2.poll();
//            System.out.println(temp.getPrice());
//        }
//    }
}
