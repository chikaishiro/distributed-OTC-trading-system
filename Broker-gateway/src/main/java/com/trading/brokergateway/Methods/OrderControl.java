package com.trading.brokergateway.Methods;

import java.io.Serializable;
import java.util.*;
import com.trading.brokergateway.Entity.Order;
import com.trading.brokergateway.Protocol.FIX;

public class OrderControl implements Serializable {
    public enum RetStat{
        PROCESSING,
        PROCESSED,
        FAILED
    }
    public static int OrderDeal(String OrderJSON){
        Order order = FIX.ParseFIX(OrderJSON);
        return 1;
    }




    public static void main(String[] args){

    }
}
