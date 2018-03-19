package com.example.nodav.cryptoreview.presenter;

import android.widget.Toast;

import com.example.nodav.cryptoreview.App;
import com.example.nodav.cryptoreview.model.CryptoResponse;
import com.example.nodav.cryptoreview.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivityPresenter implements RealmChangeListener {
    private MainActivity view;
    private Realm model;

    public MainActivityPresenter(Realm model) {
        this.model = model;
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
        view.getCryptoAdapter().notifyDataSetChanged();
    }

    public void onRefreshData(){
        App.getInstance().updateUsersCrypto(model.where(CryptoResponse.class).findAll());
    }

    public void onCryptoDelete(String id){
        model.beginTransaction();
        model.where(CryptoResponse.class).equalTo("id", id).findFirst().deleteFromRealm();
        model.commitTransaction();
    }

    public void onCryptoAdd(String id){
        CryptoResponse crypto = new CryptoResponse();
        crypto.setId(id);
        model.beginTransaction();
        model.insert(crypto);
        model.commitTransaction();

        App.getInstance().updateUsersCrypto(id);
    }

    public List<String> getUsersCryptoList(){
        List<String> list = new ArrayList<>();
        RealmResults<CryptoResponse> usersCrypto = model.where(CryptoResponse.class).findAll();
        for (CryptoResponse crypto : usersCrypto){
            list.add(crypto.getId());
        }
        return list;
    }

    public void onRequestStart(){
        if (view != null)
            view.showProgressbar();
    }

    public void onErrorResponse(){
        if (view != null) {
            view.hideProgressbar();
            Toast.makeText(view, "Error", Toast.LENGTH_SHORT).show();
        }
    }
    public void onResponse(){
        view.hideProgressbar();
        Toast.makeText(view, "Update", Toast.LENGTH_SHORT).show();
    }
}
