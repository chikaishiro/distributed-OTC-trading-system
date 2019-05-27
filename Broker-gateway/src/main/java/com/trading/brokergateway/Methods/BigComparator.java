package com.trading.brokergateway.Methods;

import com.trading.brokergateway.Entity.Order;

import java.io.Serializable;
import java.util.Comparator;

public class BigComparator implements Comparator<Order>,Serializable{
    @Override
    public int compare(Order o1,Order o2){
        if (o2.getPrice() > o1.getPrice()){
            return 1;
        }
        else{
            return -1;
        }
    }
}
