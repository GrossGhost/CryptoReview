package com.example.nodav.cryptoreview.model;

import io.realm.RealmObject;

public class UserHoldings extends RealmObject {

    private String id;
    private String price;
    private double holding;
    private double holdingCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getHolding() {
        return holding;
    }

    public void setHolding(double holding) {
        this.holding = holding;
    }

    public double getHoldingCount() {
        return holdingCount;
    }

    public void setHoldingCount(double holdingCount) {
        this.holdingCount = holdingCount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public double getProfit(double priceUsd) {

       return (priceUsd - holding ) * holdingCount;
    }

    public double getProfitPercent(double priceUsd) {
       return priceUsd / holding * 100 - 100;
    }

}
