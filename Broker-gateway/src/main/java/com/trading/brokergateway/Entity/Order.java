package com.trading.brokergateway.Entity;

import java.io.*;

public class Order implements Serializable {
    private double Price;
    private String FutureID;
    private char way;
    /*

     */
    public Order(double price,String futureID,char way){
        this.Price = price;
        this.FutureID = futureID;
        this.way = way;
    }
    public void setPrice(double price){
        this.Price = price;
    }
    public double getPrice(){
        return this.Price;
    }

    public void setFutureID(String futureID) {
        FutureID = futureID;
    }

    public String getFutureID() {
        return FutureID;
    }

    public void setWay(char way) {
        this.way = way;
    }

    public char getWay() {
        return way;
    }
}
