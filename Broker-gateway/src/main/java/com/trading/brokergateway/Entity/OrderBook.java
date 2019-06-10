package com.trading.brokergateway.Entity;
import com.trading.brokergateway.Methods.OrderQueue;
import com.trading.brokergateway.Util.SortUtil;

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
        PriorityQueue<Order> buyQueue = odq.getBuyQueue();
        PriorityQueue<Order> sellQueue = odq.getSellQueue();
        List<Order> bList = SortUtil.sortQueue(odq.getBuyQueue(),false);
        List<Order> sList = SortUtil.sortQueue(odq.getSellQueue(),true);
        for(int i =0;i<bList.size();i++){
            Order temp = bList.get(i);
            this.buyList.add(temp);
        }
        for(int i =sList.size()-1;i>=0;i--){
            Order temp = sList.get(i);
            this.sellList.add(temp);
        }
    }
}
