package com.trading.brokergateway.Methods;

import com.trading.brokergateway.Entity.Order;

import java.util.*;
public class OrderQueue {
    private PriorityQueue<Order> SellQueue;
    private PriorityQueue<Order> BuyQueue;
    public OrderQueue(){
        this.SellQueue =  new PriorityQueue<Order>(new Comparator<Order>(){ //小顶堆
            @Override
            public int compare(Order o1,Order o2){
                if (o2.getPrice() > o1.getPrice()){
                    return -1;
                }
                else{
                    return 1;
                }
            }
        });
        this.BuyQueue = new PriorityQueue<Order>(new Comparator<Order>(){ //大顶堆
            @Override
            public int compare(Order o1,Order o2){
                if (o2.getPrice() > o1.getPrice()){
                    return 1;
                }
                else{
                    return -1;
                }
            }
        });
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

    public static void main(String[] args){

    }

    public void queueTest(){
//        OrderQueue q = new OrderQueue();
//        Order o1 = new Order(2.11);
//        Order o2 = new Order(2.22);
//        Order o3 = new Order(2.33);
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
    }
}
