package com.example.nodav.cryptoreview.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;



public class UsersCryptoList extends RealmObject {

    @PrimaryKey
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
