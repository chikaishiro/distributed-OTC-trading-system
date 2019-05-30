package com.trading.tradergateway.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "future")
public class Future implements Serializable {
    @Id
    @Column(nullable = false, name = "futureId")
    private String futureID;
    @Column(nullable = false, name = "futureName")
    private String futureName;
    @Column(nullable = false, name = "category")
    private String category;
    @Column(nullable = false, name = "listTime")
    private String listTime;
    @Column(nullable = false, name = "expired")
    private String expired;


    public Future() {}

    public Future(String futureID, String futureName, String category, String listTime, String expired) {
        this.futureID = futureID;
        this.futureName = futureName;
        this.category = category;
        this.listTime = listTime;
        this.expired = expired;
    }

    public String getFutureID() {
        return futureID;
    }

    public void setFutureID(String futureID) {
        this.futureID = futureID;
    }

    public String getFutureName() {
        return futureName;
    }

    public void setFutureName(String futureName) {
        this.futureName = futureName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getListTime() {
        return listTime;
    }

    public void setListTime(String listTime) {
        this.listTime = listTime;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }


}
