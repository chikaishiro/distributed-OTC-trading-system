package com.trading.tradergateway.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order implements Serializable {
    @Id
    @Column(nullable = false, name = "order_id")
    private String orderID;

    @Column(nullable = false, name = "future_id")
    private String futureID;

    @Column(nullable = false, name = "type")
    private char type;

    @Column(nullable = false, name = "way")
    private char way;

    @Column(nullable = true, name = "price")
    private Double price;

    @Column(nullable = false, name = "amount")
    private int amount;

    @Column(nullable = true, name = "broker_ip")
    private String brokerIp;

    @Column(nullable = false, name = "time_stamp")
    private Long timeStamp;

    @Column(nullable = true, name = "trader_id")
    private String traderId;

    @Column(nullable = true, name = "status")
    private String status;

    public Order() {}

    public Order(String orderID, String futureID, char type, char way, Double price,
                 int amount, String brokerIp, Long timeStamp, String traderId, String status) {
        this.orderID = orderID;
        this.futureID = futureID;
        this.type = type;
        this.way = way;
        this.price = price;
        this.amount = amount;
        this.brokerIp = brokerIp;
        this.timeStamp = timeStamp;
        this.traderId = traderId;
        this.status = status;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
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

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTraderId(){
        return traderId;
    }

    public void setTraderId(String traderId){
        this.traderId = traderId;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

}
