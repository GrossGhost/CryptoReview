package com.example.nodav.cryptoreview.presenter;

import android.widget.Toast;

import com.example.nodav.cryptoreview.App;
import com.example.nodav.cryptoreview.model.CryptoResponse;
import com.example.nodav.cryptoreview.network.Service;
import com.example.nodav.cryptoreview.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivityPresenter {
    private MainActivity view;
    private Realm model;
    private Service service;
    private boolean isFirstTimeStartActivity;

    public MainActivityPresenter(Realm model, Service service) {
        this.model = model;
        this.service = service;
        isFirstTimeStartActivity = true;
    }

    public void attachView(MainActivity usersActivity) {
        view = usersActivity;
        if (isFirstTimeStartActivity){
            onRefreshData();
            isFirstTimeStartActivity = false;
        }
    }

    public void detachView() {
        view = null;
    }


    public void viewIsReady() {
        RealmResults<CryptoResponse> data = model.where(CryptoResponse.class).findAll();
        view.showCrypto(data);
    }

    public void onCryptoDelete(String id, int position){
        model.beginTransaction();
        model.where(CryptoResponse.class).equalTo("id", id).findFirst().deleteFromRealm();
        model.commitTransaction();
        view.getCryptoAdapter().notifyItemRemoved(position);

    }

    public void onCryptoAdd(String id, int position){
        CryptoResponse crypto = new CryptoResponse();
        crypto.setId(id);
        model.beginTransaction();
        model.insert(crypto);
        model.commitTransaction();
        view.getCryptoAdapter().notifyItemChanged(position);

        updateUsersCrypto(id, position);

    }

    public List<String> getUsersCryptoList(){
        List<String> list = new ArrayList<>();
        RealmResults<CryptoResponse> usersCrypto = model.where(CryptoResponse.class).findAll();
        for (CryptoResponse crypto : usersCrypto){
            list.add(crypto.getId());
        }
        return list;
    }

    private void onErrorResponse(){
        if (view != null) {
            view.hideProgressbar();
            Toast.makeText(view, "Error", Toast.LENGTH_SHORT).show();
        }
    }
    private void onResponse(){
        view.hideProgressbar();
        view.getCryptoAdapter().notifyDataSetChanged();
        Toast.makeText(view, "Update", Toast.LENGTH_SHORT).show();
    }

    private void onResponse(int position){
        view.hideProgressbar();
        view.getCryptoAdapter().notifyItemChanged(position);
        Toast.makeText(view, "Update", Toast.LENGTH_SHORT).show();
    }

    public void getCryptoTitles(){
        view.showProgressbar();
        service.getCryptoTitles(new Service.GetCryptoTitlesCallback() {
            @Override
            public void onSuccess(List<String> titles) {
                view.showCryptoDialog(titles);
                view.hideProgressbar();
                App.getInstance().setTitles(titles);
            }

            @Override
            public void onError() {
                onErrorResponse();
            }
        });
    }

    private void updateUsersCrypto(String id, int position) {
        view.showProgressbar();
        service.updateUsersCrypto(id, position, new Service.UpdateCryptoCallback() {
            @Override
            public void onSuccess(CryptoResponse response, int position) {
                model.beginTransaction();
                model.copyToRealmOrUpdate(response);
                model.commitTransaction();

                onResponse(position);
            }

            @Override
            public void onError() {
                onErrorResponse();
            }
        });
    }

    public void onRefreshData(){
        view.showProgressbar();
        List<CryptoResponse> list = model.copyFromRealm(model.where(CryptoResponse.class).findAll());
        service.updateUsersCryptos(list, new Service.UpdateCryptosCallback() {
            @Override
            public void onSuccess(List<CryptoResponse> response) {
                model.beginTransaction();
                model.copyToRealmOrUpdate(response);
                model.commitTransaction();

                onResponse();
            }

            @Override
            public void onError() {
                onErrorResponse();
            }
        });
    }

}
