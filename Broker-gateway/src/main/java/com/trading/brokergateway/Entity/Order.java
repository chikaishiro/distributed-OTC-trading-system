package com.trading.brokergateway.Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;


public class Order implements Serializable {
    private UUID orderID;

    private String futureID;

    private char type;

    private char way;

    private Double price;

    private int amount;

    private String brokerIp;

    private long timeStamp;

    private String traderId;

    public Order() {}

    public Order(UUID orderID, String futureID, char type, char way,
                 Double price, int amount, String brokerIp, long timeStamp, String traderId) {
        this.orderID = orderID;
        this.futureID = futureID;
        this.type = type;
        this.way = way;
        this.price = price;
        this.amount = amount;
        this.brokerIp = brokerIp;
        this.timeStamp = timeStamp;
        this.traderId = traderId;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
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

    public char getWay() {
        return way;
    }

    public void setWay(char way) {
        this.way = way;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getBrokerIp() {
        return brokerIp;
    }

    public void setBrokerIp(String brokerIp) {
        this.brokerIp = brokerIp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTraderId(){
        return traderId;
    }

    public void setTraderId(String traderId){
        this.traderId = traderId;
    }
}
