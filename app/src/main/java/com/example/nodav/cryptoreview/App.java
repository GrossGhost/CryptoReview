package com.example.nodav.cryptoreview;

import android.app.Application;

import com.example.nodav.cryptoreview.dagger.component.AppComponent;

import com.example.nodav.cryptoreview.dagger.component.DaggerAppComponent;
import com.example.nodav.cryptoreview.dagger.module.NetModule;
import com.example.nodav.cryptoreview.dagger.module.PresenterModule;
import com.example.nodav.cryptoreview.dagger.module.RealmModule;
import com.example.nodav.cryptoreview.model.CryptoResponse;
import com.example.nodav.cryptoreview.network.ApiService;
import com.example.nodav.cryptoreview.presenter.MainActivityPresenter;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Retrofit;


public class App extends Application {

    private static App instance = null;

    @Inject
    Retrofit retrofit;
    @Inject
    Realm realm;
    @Inject
    MainActivityPresenter presenter;

    private AppComponent appComponent;
    private List<String> titles = new ArrayList<>();
    private ApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appComponent = DaggerAppComponent.builder()
                .netModule(new NetModule())
                .realmModule(new RealmModule(this))
                .presenterModule(new PresenterModule())
                .build();

        appComponent.inject(this);

        apiService = retrofit.create(ApiService.class);

        RealmResults<CryptoResponse> usersCryptos = realm.where(CryptoResponse.class).findAll();
        if (usersCryptos.size() > 0) {
            updateUsersCrypto(usersCryptos);
        }
        getCryptoTitles();

    }

    public static App getInstance() {
        return instance;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public List<String> getTitles() {
        return titles;
    }

    private void getCryptoTitles() {

        Observable<List<CryptoResponse>> observable = apiService.getCryptos();
        observable.subscribeOn(Schedulers.newThread())
                .flatMap(Observable::fromIterable)
                .doOnNext(response -> titles.add(response.getId()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cryptoResponse -> { }, throwable ->
                        presenter.onErrorResponse());

    }

    public void updateUsersCrypto(RealmResults<CryptoResponse> cryptos) {
        presenter.onRequestStart();
        Observable<List<CryptoResponse>> observable;

        for (int i = 0; i < cryptos.size(); i++ ) {
            final int pos = i+1;
            observable = apiService.getCrypto(cryptos.get(i).getId());
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete(() -> {if (pos == cryptos.size()){ presenter.onResponse();}})
                    .subscribe(responseData ->
                                    realm.executeTransaction(realm ->
                                            realm.copyToRealmOrUpdate(responseData.get(0))
                                    ),
                            throwable -> {if (pos == cryptos.size()){ presenter.onErrorResponse();
                            }});
        }
    }

    public void updateUsersCrypto(String cryptoId) {
        presenter.onRequestStart();
        Observable<List<CryptoResponse>> observable = apiService.getCrypto(cryptoId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> presenter.onResponse())
                .subscribe(responseData ->
                                realm.executeTransaction(realm ->
                                        realm.copyToRealmOrUpdate(responseData.get(0))
                                ),
                        throwable -> presenter.onErrorResponse());

    }
}
