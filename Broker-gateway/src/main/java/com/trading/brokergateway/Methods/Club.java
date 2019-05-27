package com.trading.brokergateway.Methods;

import com.trading.brokergateway.Entity.Order;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;

public class Club implements Serializable {

    /**
     * 其实序列化的作用是能转化成Byte流，然后又能反序列化成原始的类。能
     * 在网络进行传输，也可以保存在磁盘中，
     * 有了SUID之后，那么如果序列化的类已经保存了在本地中，
     * 中途你更改了类后，SUID变了，那么反序列化的时候就不会变成原始的类了，
     * 还会抛异常，主要就是用于版本控制。
     */

    private PriorityQueue<Order> sq;
    private PriorityQueue<Order> bq;
    public Club(){
        this.sq =  new PriorityQueue<Order>(new SmallComparator());
        this.bq = new PriorityQueue<Order>(new BigComparator());
    }

    public double get(){
        return 2.11;
    }
}