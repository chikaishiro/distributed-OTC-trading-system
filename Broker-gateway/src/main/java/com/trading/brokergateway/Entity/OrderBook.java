package com.trading.brokergateway.Entity;
import com.trading.brokergateway.Methods.OrderQueue;

import java.io.Serializable;
import java.util.*;
public class OrderBook implements Serializable {
    private LinkedList<Order> sellList;
    private LinkedList<Order> buyList;
    public OrderBook(){
        sellList = new LinkedList<Order>();
        buyList = new LinkedList<Order>();
    }

    public OrderBook(OrderQueue odq){
        sellList = new LinkedList<Order>();
        buyList = new LinkedList<Order>();
        PriorityQueue<Order> buyQueue = odq.getBuyQueue();
        PriorityQueue<Order> sellQueue = odq.getSellQueue();
        while(sellQueue.size()!=0){
            Order temp = sellQueue.poll();
            this.sellList.add(temp);
        }
        while(buyQueue.size()!=0){
            Order temp = buyQueue.poll();
            this.buyList.add(temp);
        }
    }

    public OrderBook(OrderQueue odq,boolean Display){
        sellList = new LinkedList<Order>();
        buyList = new LinkedList<Order>();
        LinkedList<Order> tempSell = new LinkedList<>();
        PriorityQueue<Order> buyQueue = odq.getBuyQueue();
        PriorityQueue<Order> sellQueue = odq.getSellQueue();
        while(sellQueue.size()!=0){
            Order temp = sellQueue.poll();
            tempSell.add(temp);
        }
        for(int i = tempSell.size()-1;i>=0;i--){
            Order temp = tempSell.get(i);
            this.sellList.add(temp);
        }
        while(buyQueue.size()!=0){
            Order temp = buyQueue.poll();
            this.buyList.add(temp);
        }
    }
}
