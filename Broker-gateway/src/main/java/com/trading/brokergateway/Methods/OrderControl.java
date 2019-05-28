package com.trading.brokergateway.Methods;

import java.io.Serializable;
import java.util.*;
import com.trading.brokergateway.Entity.Order;
import com.trading.brokergateway.Protocol.FIX;
import org.apache.catalina.Store;

public class OrderControl implements Serializable {
    public static int PROCESSING = 0;
    public static int PROCESSED = 1;
    public static int FAILED = -1;

    public static int OK = 3;
    public static int PARTLY = 4;
    public static int NOK = 5;

    public static int CancelOrder(Order order,OrderQueue orderQueue){
        if (order.getWay() == 'S'){
            PriorityQueue<Order> sellQueue = orderQueue.getSellQueue();
        }
        else if (order.getWay() == 'B'){

        }
        else{
            return NOK;
        }
    }

    public Order findOrder(UUID orderID, PriorityQueue<Order> queue){
        for(Order temp:queue){

        }
    }
    public static int OrderDeal(String OrderJSON){
        Order order = FIX.ParseFIX(OrderJSON);
        String futureID = order.getFutureID();
        OrderQueue orderQueue = StoreUtil.GetQueue(futureID);
        // Cancel Order
        if (order.getType() == 'C'){
            int ret = CancelOrder(order,orderQueue);
            return ret;
        }
        // 该期货的第一个订单
        if(orderQueue.getBuyQueue().size()==0 && orderQueue.getSellQueue().size()==0){
            if(order.getWay() == 'S'){
                orderQueue.insertSell(order);
                StoreUtil.SetQueue(orderQueue,futureID);
                return PROCESSING;
            }
            else if(order.getWay() == 'B'){
                orderQueue.insertBuy(order);
                StoreUtil.SetQueue(orderQueue,futureID);
                return PROCESSING;
            }
            else{
                return FAILED;
            }
        }
        // 该期货已经有订单
        else{
            char way = order.getWay();
            //  卖订单
            if(way == 'S'){
                PriorityQueue<Order> buyQueue = orderQueue.getBuyQueue();
                // 还没有人买
                if(buyQueue.size()==0){
                    orderQueue.insertSell(order);
                    StoreUtil.SetQueue(orderQueue,futureID);
                    return PROCESSING;
                }
                // 去买的队列里做匹配
                else{

                }
            }
            // 买订单
            else if(way == 'B') {
                PriorityQueue<Order> sellQueue = orderQueue.getSellQueue();
                //还没有人卖
                if(sellQueue.size()==0){
                    orderQueue.insertBuy(order);
                    StoreUtil.SetQueue(orderQueue,futureID);
                    return PROCESSING;
                }
                // 去卖的队列里匹配
                else{

                }
            }
            else{
                return FAILED;
            }

        }

        return FAILED;
    }




    public static void main(String[] args){

    }
}
