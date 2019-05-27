package com.trading.tradergateway.Entity;

import javax.persistence.*;


@Entity
@Table(name = "Broker")
public class Broker {

    private String brokerID;
    private String brokerName;
    private String brokerIp;

    public Broker() {}

    public Broker(String brokerID, String brokerName, String brokerIp) {
        this.brokerID = brokerID;
        this.brokerName = brokerName;
        this.brokerIp = brokerIp;
    }

    public String getBrokerID() {
        return brokerID;
    }

    public void setBrokerID(String brokerID) {
        this.brokerID = brokerID;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public String getBrokerIp() {
        return brokerIp;
    }

    public void setBrokerHttp(String brokerHttp) {
        this.brokerIp = brokerIp;
    }

}
