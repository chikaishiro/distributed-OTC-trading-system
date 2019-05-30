package com.trading.brokergateway.Entity;

import com.sun.org.apache.xpath.internal.operations.Or;


import java.io.Serializable;
import java.util.UUID;

public class Result implements Serializable{
    private UUID resultID;
    private UUID orderID;
    private long finishTime;
    private String traderID;
    private String futureID;
    private char way;
    private char type;
    private int amount;

    public Result(){

    }
    public Result(UUID resultid, long finishTime, Order order,int amount){
        this.resultID = resultid;
        this.finishTime = finishTime;
        this.orderID = order.getOrderID();
        this.amount = amount;
        this.way = order.getWay();
        this.type = order.getType();
        this.traderID = order.getTraderId();
        this.futureID = order.getFutureID();
    }

    public char getWay() {
        return way;
    }

    public void setWay(char way) {
        this.way = way;
    }

    public String getFutureID() {
        return futureID;
    }

    public void setFutureID(String futureID) {
        this.futureID = futureID;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }

    public UUID getResultID() {
        return resultID;
    }

    public void setResultID(UUID resultID) {
        this.resultID = resultID;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTraderID() {
        return traderID;
    }

    public void setTraderID(String traderID) {
        this.traderID = traderID;
    }
}
