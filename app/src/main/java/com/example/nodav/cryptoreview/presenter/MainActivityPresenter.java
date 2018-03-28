package com.example.nodav.cryptoreview.presenter;

import android.widget.Toast;

import com.example.nodav.cryptoreview.App;
import com.example.nodav.cryptoreview.model.CryptoResponse;
import com.example.nodav.cryptoreview.model.UserHoldings;
import com.example.nodav.cryptoreview.network.NetworkError;
import com.example.nodav.cryptoreview.network.Service;
import com.example.nodav.cryptoreview.view.ChangeHoldingDialog;
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
            countTotalValue();
            isFirstTimeStartActivity = false;
        }
    }

    public void detachView() {
        view = null;
    }


    public void viewIsReady() {
        RealmResults<CryptoResponse> data = model.where(CryptoResponse.class).findAll();
        RealmResults<UserHoldings> holdingsData = model.where(UserHoldings.class).findAll();
        view.showCrypto(data, holdingsData);
    }

    public void onCryptoDelete(String id, int position){
        model.beginTransaction();
        model.where(CryptoResponse.class).equalTo("id", id).findFirst().deleteFromRealm();
        model.where(UserHoldings.class).equalTo("id", id).findFirst().deleteFromRealm();
        model.commitTransaction();

        view.getCryptoAdapter().notifyItemRemoved(position);
        countTotalValue();
    }

    public void onCryptoAdd(String id, int position){
        CryptoResponse crypto = new CryptoResponse();
        crypto.setId(id);
        UserHoldings holding = new UserHoldings();
        holding.setId(id);

        model.beginTransaction();
        model.insert(crypto);
        model.insert(holding);
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

    private void onErrorResponse(int stringId){
        if (view != null) {
            view.hideProgressbar();
            Toast.makeText(view, stringId, Toast.LENGTH_SHORT).show();
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
            public void onError(NetworkError error) {
                onErrorResponse(error.getStringErrorId());
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
            public void onError(NetworkError error) {
                onErrorResponse(error.getStringErrorId());
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
                countTotalValue();
            }

            @Override
            public void onError(NetworkError error) {
                onErrorResponse(error.getStringErrorId());
            }
        });
    }

    public void onCryptoClick(String id, int position){
        UserHoldings holding = model.where(UserHoldings.class).contains("id", id).findFirst();
        ChangeHoldingDialog dialog = new ChangeHoldingDialog(view, id, position, this, holding.getHolding(), holding.getHoldingCount());
        dialog.show();
    }

    public void onHoldingChanged(String id, int position, Double holding, Double count){
        UserHoldings userHoldings = model.where(UserHoldings.class).equalTo("id", id).findFirst();

        model.beginTransaction();
        userHoldings.setHolding(holding);
        userHoldings.setHoldingCount(count);
        model.commitTransaction();
        view.getCryptoAdapter().notifyItemChanged(position);

        countTotalValue();
    }

    private void countTotalValue(){
        double totalValue = 0;
        double totalChange;
        double holdingValue = 0;
        RealmResults<CryptoResponse> data = model.where(CryptoResponse.class).findAll();
        RealmResults<UserHoldings> holdingsData = model.where(UserHoldings.class).findAll();

        for (CryptoResponse crypto : data){
            UserHoldings holding = holdingsData.where().contains("id", crypto.getId()).findFirst();
            if (crypto.getPriceUsd() != null){
                double price = Double.parseDouble(crypto.getPriceUsd());
                totalValue += price * holding.getHoldingCount();
                holdingValue += holding.getHolding() * holding.getHoldingCount();
            }

        }
        totalChange = totalValue / holdingValue * 100 - 100;
        view.updateTotalStats(totalValue, totalChange);
    }

}
