package com.trading.brokergateway.Methods;

import java.io.Serializable;
import java.util.*;

import com.google.gson.Gson;
import com.trading.brokergateway.DAO.ResultSaving;
import com.trading.brokergateway.Entity.Order;
import com.trading.brokergateway.Entity.Result;
import com.trading.brokergateway.Util.FIX;
import com.trading.brokergateway.Util.SortUtil;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;

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
                if(order.getType() == 'M'){
                    return NOK;
                }
                orderQueue.insertSell(order);
                StoreUtil.SetQueue(orderQueue,futureID);
                return PROCESSING;
            }
            else if(order.getWay() == 'B'){
                if(order.getType() == 'M'){
                    return NOK;
                }
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
                    if(order.getType() == 'M'){
                        return NOK;
                    }
                    orderQueue.insertSell(order);
                    StoreUtil.SetQueue(orderQueue,futureID);
                    return PROCESSING;
                }
                // 去买的队列里做匹配
                // TODO
                else{
                    char type = order.getType();
                    int ret;
                    if(type=='M'){
                        // Sell MarketOrder
                        List<Order> buyList = SortUtil.sortQueue(buyQueue,false);
                        int amount = order.getAmount();
                        for(Order tempOrder:buyList){
                            if(amount == 0){
                                break;
                            }
                            buyList.remove(tempOrder);
                            int tempAmount = tempOrder.getAmount();
                            if(tempAmount>amount){
                                tempAmount = tempAmount - amount;
                                tempOrder.setAmount(tempAmount);
                                buyList.add(tempOrder);
                                buyQueue.clear();
                                for(Order temp:buyList){
                                    orderQueue.insertBuy(temp);
                                }
                                StoreUtil.SetQueue(orderQueue,futureID);
                                StoreUtil.setOrderStat(order.getOrderID());
                                record(order,tempOrder,tempOrder.getPrice(),amount,futureID);
                                return PROCESSED;
                            }
                            if(tempAmount == amount){
                                buyQueue.clear();
                                for(Order temp:buyList){
                                    orderQueue.insertBuy(temp);
                                }
                                StoreUtil.SetQueue(orderQueue,futureID);
                                StoreUtil.setOrderStat(order.getOrderID());
                                StoreUtil.setOrderStat(tempOrder.getOrderID());
                                record(order,tempOrder,tempOrder.getPrice(),amount,futureID);
                                return PROCESSED;
                            }
                            else{
                                amount = amount -tempAmount;
                                StoreUtil.setOrderStat(tempOrder.getOrderID());
                                record(order,tempOrder,tempOrder.getPrice(),tempAmount,futureID);
                                continue;
                            }

                        }
                        buyQueue.clear();
                        orderQueue.setBuyQueue(buyQueue);
                        StoreUtil.SetQueue(orderQueue,futureID);
                        return PARTLY;

                    }
                    else if(type == 'L'){
                        // Limit Order
                        double price = order.getPrice();
                        int amount = order.getAmount();
                        PriorityQueue<Order> backup = new PriorityQueue<>(buyQueue);
                        List<Order> buyList = SortUtil.sortQueueEqualsPrice(backup,price);
                        for(Order tempOrder:buyList){
                            if(amount == 0){
                                break;
                            }
                            buyQueue.remove(tempOrder);
                            int tempAmount = tempOrder.getAmount();
                            if(tempAmount> amount){
                                tempAmount = tempAmount - amount;
                                tempOrder.setAmount(tempAmount);
                                buyQueue.add(tempOrder);
                                orderQueue.setBuyQueue(buyQueue);
                                StoreUtil.setOrderStat(order.getOrderID());
                                StoreUtil.SetQueue(orderQueue,futureID);
                                record(order,tempOrder,price,amount,futureID);
                                return PROCESSED;
                            }
                            if(tempAmount == amount){
                                orderQueue.setBuyQueue(buyQueue);
                                StoreUtil.setOrderStat(order.getOrderID());
                                StoreUtil.setOrderStat(tempOrder.getOrderID());
                                StoreUtil.SetQueue(orderQueue,futureID);
                                record(order,tempOrder,price,amount,futureID);
                                return PROCESSED;
                            }
                            else{
                                amount = amount -tempAmount;
                                StoreUtil.setOrderStat(tempOrder.getOrderID());
                                record(order,tempOrder,price,tempAmount,futureID);
                                continue;
                            }
                        }
                        order.setAmount(amount);
                        orderQueue.insertSell(order);
                        orderQueue.setBuyQueue(buyQueue);
                        StoreUtil.SetQueue(orderQueue,futureID);
                        return PROCESSING;
                    }
                    else if (type == 'S'){
                        // 卖 StopOrder
                        double toPrice = order.getPrice();
                        int amount = order.getAmount();
                        PriorityQueue<Order> backup = new PriorityQueue<Order>(buyQueue);
                        List<Order> buyList = SortUtil.sortBuyEqualsPrice(backup,toPrice);
                        for(Order tempOrder:buyList){
                            if(amount == 0){
                                break;
                            }
                            buyQueue.remove(tempOrder);
                            int tempAmount = tempOrder.getAmount();
                            if(tempAmount> amount){
                                tempAmount = tempAmount - amount;
                                tempOrder.setAmount(tempAmount);
                                buyQueue.add(tempOrder);
                                orderQueue.setBuyQueue(buyQueue);
                                StoreUtil.setOrderStat(order.getOrderID());
                                StoreUtil.SetQueue(orderQueue,futureID);
                                record(order,tempOrder,tempOrder.getPrice(),amount,futureID);
                                return PROCESSED;
                            }
                            if(tempAmount == amount){
                                orderQueue.setBuyQueue(buyQueue);
                                StoreUtil.setOrderStat(order.getOrderID());
                                StoreUtil.setOrderStat(tempOrder.getOrderID());
                                StoreUtil.SetQueue(orderQueue,futureID);
                                record(order,tempOrder,tempOrder.getPrice(),amount,futureID);
                                return PROCESSED;
                            }
                            else{
                                amount = amount -tempAmount;
                                StoreUtil.setOrderStat(tempOrder.getOrderID());
                                record(order,tempOrder,tempOrder.getPrice(),tempAmount,futureID);
                                continue;
                            }
                        }
                        order.setAmount(amount);
                        //orderQueue.insertSell(order);
                        orderQueue.setBuyQueue(buyQueue);
                        StoreUtil.SetQueue(orderQueue,futureID);
                        return PROCESSING;
                    }
                    else{
                        return FAILED;
                    }

                }
            }
            // 买订单
            else if(way == 'B') {
                PriorityQueue<Order> sellQueue = orderQueue.getSellQueue();
                //还没有人卖
                if(sellQueue.size()==0){
                    if(order.getType() == 'M'){
                        return NOK;
                    }
                    orderQueue.insertBuy(order);
                    StoreUtil.SetQueue(orderQueue,futureID);
                    return PROCESSING;
                }
                // 去卖的队列里匹配
                // TODO
                else{
                    char type = order.getType();

                    if(type=='M'){
                        // MarketOrder
                        List<Order> sellList = SortUtil.sortQueue(sellQueue,true);
                        int amount = order.getAmount();
                        for(Order tempOrder:sellList){
                            if(amount == 0){
                                break;
                            }
                            sellList.remove(tempOrder);
                            int tempAmount = tempOrder.getAmount();
                            if(tempAmount>amount){
                                tempAmount = tempAmount - amount;
                                tempOrder.setAmount(tempAmount);
                                sellList.add(tempOrder);
                                sellQueue.clear();
                                for(Order temp:sellList){
                                    orderQueue.insertSell(temp);
                                }
                                StoreUtil.setOrderStat(order.getOrderID());
                                StoreUtil.SetQueue(orderQueue,futureID);
                                record(order,tempOrder,tempOrder.getPrice(),amount,futureID);
                                return PROCESSED;
                            }
                            if(tempAmount == amount){
                                sellQueue.clear();
                                for(Order temp:sellList){
                                    orderQueue.insertSell(temp);
                                }
                                StoreUtil.setOrderStat(order.getOrderID());
                                StoreUtil.setOrderStat(tempOrder.getOrderID());
                                StoreUtil.SetQueue(orderQueue,futureID);
                                record(order,tempOrder,tempOrder.getPrice(),amount,futureID);
                                return PROCESSED;
                            }
                            else{
                                amount = amount -tempAmount;
                                StoreUtil.setOrderStat(tempOrder.getOrderID());
                                record(order,tempOrder,tempOrder.getPrice(),tempAmount,futureID);
                                continue;
                            }

                        }
                        sellQueue.clear();
                        orderQueue.setSellQueue(sellQueue);
                        StoreUtil.SetQueue(orderQueue,futureID);
                        return PARTLY;

                    }
                    else if(type == 'L'){
                        // Limit Order
                        double price = order.getPrice();
                        int amount = order.getAmount();

                        PriorityQueue<Order> backup = new PriorityQueue<>(sellQueue);
                        List<Order> sellList = SortUtil.sortQueueEqualsPrice(backup,price);
                        for(Order tempOrder:sellList){
                            if(amount == 0){
                                break;
                            }
                            sellQueue.remove(tempOrder);
                            int tempAmount = tempOrder.getAmount();
                            if(tempAmount> amount){
                                tempAmount = tempAmount - amount;
                                tempOrder.setAmount(tempAmount);
                                sellQueue.add(tempOrder);
                                orderQueue.setSellQueue(sellQueue);
                                StoreUtil.setOrderStat(order.getOrderID());
                                StoreUtil.SetQueue(orderQueue,futureID);
                                record(order,tempOrder,price,amount,futureID);
                                return PROCESSED;
                            }
                            if(tempAmount == amount){
                                orderQueue.setSellQueue(sellQueue);
                                StoreUtil.setOrderStat(order.getOrderID());
                                StoreUtil.setOrderStat(tempOrder.getOrderID());
                                StoreUtil.SetQueue(orderQueue,futureID);
                                record(order,tempOrder,price,amount,futureID);
                                return PROCESSED;
                            }
                            else{
                                amount = amount -tempAmount;
                                StoreUtil.setOrderStat(tempOrder.getOrderID());
                                record(order,tempOrder,price,tempAmount,futureID);
                                continue;
                            }
                        }
                        order.setAmount(amount);
                        orderQueue.insertBuy(order);
                        orderQueue.setSellQueue(sellQueue);
                        StoreUtil.SetQueue(orderQueue,futureID);
                        return PROCESSING;

                    }
                    else if (type == 'S'){
                        // 买 Stop Order
                        double toPrice = order.getPrice();
                        int amount = order.getAmount();
                        PriorityQueue<Order> backup = new PriorityQueue<Order>(sellQueue);
                        List<Order> sellList = SortUtil.sortSellEqualsPrice(backup,toPrice);
                        for(Order tempOrder:sellList){
                            if(amount == 0){
                                break;
                            }
                            sellQueue.remove(tempOrder);
                            int tempAmount = tempOrder.getAmount();
                            if(tempAmount> amount){
                                tempAmount = tempAmount - amount;
                                tempOrder.setAmount(tempAmount);
                                sellQueue.add(tempOrder);
                                orderQueue.setSellQueue(sellQueue);
                                StoreUtil.setOrderStat(order.getOrderID());
                                StoreUtil.SetQueue(orderQueue,futureID);
                                record(order,tempOrder,tempOrder.getPrice(),amount,futureID);
                                return PROCESSED;
                            }
                            if(tempAmount == amount){
                                orderQueue.setSellQueue(sellQueue);
                                StoreUtil.setOrderStat(order.getOrderID());
                                StoreUtil.setOrderStat(tempOrder.getOrderID());
                                StoreUtil.SetQueue(orderQueue,futureID);
                                record(order,tempOrder,tempOrder.getPrice(),amount,futureID);
                                return PROCESSED;
                            }
                            else{
                                amount = amount - tempAmount;
                                StoreUtil.setOrderStat(tempOrder.getOrderID());
                                record(order,tempOrder,tempOrder.getPrice(),tempAmount,futureID);
                                continue;
                            }
                        }
                        order.setAmount(amount);
                        //orderQueue.insertBuy(order);
                        orderQueue.setSellQueue(sellQueue);
                        StoreUtil.SetQueue(orderQueue,futureID);
                        return PROCESSING;

                    }
                    else{
                        return FAILED;
                    }
                }
            }
            else{
                return FAILED;
            }

        }
    }

    public static void record(Order order1,Order order2,double price,int amount,String futureID){
        UUID orderID1 = order1.getOrderID();
        UUID orderID2 = order2.getOrderID();
        String traderID1 = order1.getTraderId();
        String traderID2 = order2.getTraderId();
        UUID uid = UUID.randomUUID();
        long finishTime = Calendar.getInstance().getTimeInMillis();
        Result result = new Result(uid,orderID1,orderID2,traderID1,traderID2,futureID,finishTime,price,amount);
        ResultSaving.saveTradeResult(result);

    }

    public static String getStrFromStat(int i){
        switch(i){
            case 0:
                return "PROCESSING";
            case 1:
                return "PROCESSED";
            case -1:
                return "FAILED";
            case 3:
                return "OK";
            case 4:
                return "PARTLY";
            case 5:
                return "NOK";
        }
        return null;
    }

    public static void main(String[] args) {
        UUID uid = UUID.randomUUID();
        System.out.println(uid);
        Order ord1 = new Order(uid, "SB", 'S', 'S', 3.2, 200, "2.4",
                Calendar.getInstance().getTimeInMillis(), "xxx");
        Gson gs = new Gson();

        int i = OrderDeal(gs.toJson(ord1));
        System.out.println(i);
    }
}
