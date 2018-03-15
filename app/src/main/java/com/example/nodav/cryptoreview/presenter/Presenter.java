package com.example.nodav.cryptoreview.presenter;


import android.util.Log;

import com.example.nodav.cryptoreview.App;
import com.example.nodav.cryptoreview.model.CryptoResponse;
import com.example.nodav.cryptoreview.view.MainActivity;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class Presenter implements RealmChangeListener {
    private MainActivity view;
    private Realm model;

    public Presenter(Realm model) {
        this.model = model;
        Log.d("PRESENTER", "Presenter create");
    }

    public void attachView(MainActivity usersActivity) {
        view = usersActivity;
    }

    public void detachView() {
        view = null;
    }


    public void viewIsReady() {
        RealmResults<CryptoResponse> data = model.where(CryptoResponse.class).findAll();
        data.addChangeListener(this);
        view.showCrypto(data);
    }

    @Override
    public void onChange(Object o) {
        view.updateCrypto();
    }

    public void onRefreshData(){
        App.getInstance().updateUsersCrypto(model.where(CryptoResponse.class).findAll());
    }
}
