package com.trading.brokergateway.Methods;

import java.io.Serializable;
import java.util.*;
import com.trading.brokergateway.Entity.Order;
public class OrderControl implements Serializable {
    private HashMap<String,OrderQueue> OrderMap;
    public OrderControl(){
        this.OrderMap = new HashMap<>();
    }

    public void OrderDeal(Order order){
        String futureid = order.getFutureID();
        OrderQueue odq  = this.OrderMap.get(futureid);
        if(odq == null){
            OrderQueue temp = new OrderQueue();
            char way = order.getWay();
            if(way == 'S'){
                temp.insertSell(order);
            }
            else if(way == 'B'){
                temp.insertBuy(order);
            }
            this.OrderMap.put(futureid,temp);
        }
        else{
            /* Order logic */
        }

    }

    public void setOrderMap(HashMap<String, OrderQueue> orderMap) {
        this.OrderMap = orderMap;
    }

    public HashMap<String, OrderQueue> getOrderMap() {
        return this.OrderMap;
    }


    public static void main(String[] args){
        OrderControl odc = new OrderControl();
        Order order = new Order(2.11,"SB",'S');
        String futureid = order.getFutureID();
        OrderQueue odq  = odc.getOrderMap().get(futureid);
        if(odq == null){
            System.out.println("xx");
        }
    }
}
