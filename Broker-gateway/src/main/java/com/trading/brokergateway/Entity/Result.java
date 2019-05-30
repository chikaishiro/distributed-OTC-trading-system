package com.trading.brokergateway.Entity;

import com.sun.org.apache.xpath.internal.operations.Or;


import java.io.Serializable;
import java.util.UUID;

public class Result implements Serializable{
    private UUID resultID;
    private UUID orderID1;
    private UUID orderID2;
    private String futureID;
    private String traderID1;
    private String traderID2;
    private long finishTime;
    private double price;
    private int amount;

    public Result(){

    }
    public Result(UUID resultID, UUID orderID1,UUID orderID2,String traderID1, String traderID2,String futureID,long finishTime,double price,int amount){
        this.resultID = resultID;
        this.orderID1 = orderID1;
        this.orderID2 = orderID2;
        this.traderID1 = traderID1;
        this.traderID2 = traderID2;
        this.futureID = futureID;
        this.finishTime = finishTime;
        this.price = price;
        this.amount = amount;
    }

    public void setResultID(UUID resultID) {
        this.resultID = resultID;
    }

    public UUID getResultID() {
        return resultID;
    }

    public void setFutureID(String futureID) {
        this.futureID = futureID;
    }

    public String getFutureID() {
        return futureID;
    }

    public String getTraderID1() {
        return traderID1;
    }

    public void setTraderID1(String traderID1) {
        this.traderID1 = traderID1;
    }

    public void setTraderID2(String traderID2) {
        this.traderID2 = traderID2;
    }

    public String getTraderID2() {
        return traderID2;
    }

    public void setOrderID1(UUID orderID1) {
        this.orderID1 = orderID1;
    }

    public UUID getOrderID1() {
        return orderID1;
    }

    public void setOrderID2(UUID orderID2) {
        this.orderID2 = orderID2;
    }

    public UUID getOrderID2() {
        return orderID2;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public long getFinishTime() {
        return finishTime;
    }
}
