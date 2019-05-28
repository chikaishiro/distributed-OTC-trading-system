package com.trading.brokergateway.Methods;

import java.io.Serializable;
import java.util.*;

import com.google.gson.Gson;
import com.trading.brokergateway.Entity.Order;
import com.trading.brokergateway.Util.FIX;

public class OrderControl implements Serializable {
    public static int PROCESSING = 0;
    public static int PROCESSED = 1;
    public static int FAILED = -1;

    public static int OK = 3;
    public static int PARTLY = 4;
    public static int NOK = 5;

    public static int CancelOrder(Order order,OrderQueue orderQueue,String futureID){
        if (order.getWay() == 'S'){
            PriorityQueue<Order> sellQueue = orderQueue.getSellQueue();
            Order temp = findOrder(order.getOrderID(),sellQueue);
            if(temp == null){
                return NOK;
            }
            else{
                sellQueue.remove(temp);
                orderQueue.setSellQueue(sellQueue);
                StoreUtil.SetQueue(orderQueue,futureID);
                int amountTarget = temp.getAmount();
                int amount = order.getAmount();
                if(amount == amountTarget){
                    return OK;
                }
                else{
                    return PARTLY;
                }
            }
        }
        else if (order.getWay() == 'B'){
            PriorityQueue<Order> buyQueue = orderQueue.getBuyQueue();
            Order temp = findOrder(order.getOrderID(),buyQueue);
            if(temp == null){
                return NOK;
            }
            else{
                buyQueue.remove(temp);
                orderQueue.setBuyQueue(buyQueue);
                StoreUtil.SetQueue(orderQueue,futureID);
                int amountTarget = temp.getAmount();
                int amount = order.getAmount();
                if(amount == amountTarget){
                    return OK;
                }
                else{
                    return PARTLY;
                }
            }
        }
        else{
            return NOK;
        }
    }

    public static Order findOrder(UUID orderID, PriorityQueue<Order> queue){
        for(Order temp:queue){
            if(temp.getOrderID().equals(orderID)){

                return temp;
            }
        }
        return null;
    }

    public static int OrderDeal(String OrderJSON){
        Order order = FIX.ParseFIX(OrderJSON);
        String futureID = order.getFutureID();
        OrderQueue orderQueue = StoreUtil.GetQueue(futureID);
        // Cancel Order 处理
        if (order.getType() == 'C'){
            // 第一个order不能是cancel
            if(orderQueue.getBuyQueue().size()==0 && orderQueue.getSellQueue().size()==0){
                return FAILED;
            }
            int ret = CancelOrder(order,orderQueue,futureID);
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




    public static void main(String[] args) {
        UUID uid = UUID.fromString("0d5df9af-08bc-45f0-9532-2333cac7c21");
        Order ord = new Order(uid, "SB", 'C', 'S', 2.1, 100, "2.4", Calendar.getInstance().getTimeInMillis(), "xxx");
        Gson gs = new Gson();
        String json = gs.toJson(ord);
        System.out.println(OrderDeal(json));
    }
}
