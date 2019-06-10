package com.trading.brokergateway.Entity;

import com.sun.org.apache.xpath.internal.operations.Or;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "result")
public class Result implements Serializable{
    @Id
    @Column(nullable = false, name = "result_id")
    private String resultID;

    @Column(nullable = false, name = "order_id1")
    private String orderID1;

    @Column(nullable = false, name = "order_id2")
    private String orderID2;

    @Column(nullable = false, name = "future_id")
    private String futureID;

    @Column(nullable = false, name = "trader_id1")
    private String traderID1;

    @Column(nullable = false, name = "trader_id2")
    private String traderID2;

    @Column(nullable = false, name = "finish_time")
    private long finishTime;

    @Column(nullable = false, name = "price")
    private double price;

    @Column(nullable = false, name = "amount")
    private int amount;

    @Column(nullable = true,name ="commission")
    private double commission;

    public Result(){

    }
    public Result(String resultID, String orderID1,String orderID2,String traderID1, String traderID2,String futureID,long finishTime,double price,int amount){
        this.resultID =  resultID;
        this.orderID1 = orderID1;
        this.orderID2 = orderID2;
        this.traderID1 = traderID1;
        this.traderID2 = traderID2;
        this.futureID = futureID;
        this.finishTime = finishTime;
        this.price = price;
        this.amount = amount;
        this.commission = calcComission();
    }

    public void setResultID(String resultID) {
        this.resultID = resultID;
    }

    public String getResultID() {
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

    public void setOrderID1(String orderID1) {
        this.orderID1 = orderID1;
    }

    public String getOrderID1() {
        return orderID1;
    }

    public void setOrderID2(String orderID2) {
        this.orderID2 = orderID2;
    }

    public String getOrderID2() {
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

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    private double calcComission(){
        return 0.0;
    }
}
