package com.example.nodav.cryptoreview;

import android.app.Application;


import com.example.nodav.cryptoreview.dagger.component.AppComponent;

import com.example.nodav.cryptoreview.dagger.component.DaggerAppComponent;
import com.example.nodav.cryptoreview.dagger.module.NetModule;
import com.example.nodav.cryptoreview.dagger.module.PresenterModule;
import com.example.nodav.cryptoreview.dagger.module.RealmModule;
import com.example.nodav.cryptoreview.model.CryptoResponse;
import com.example.nodav.cryptoreview.network.ApiService;


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

    private AppComponent appComponent;
    private List<String> titles = new ArrayList<>();

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

        Observable<List<CryptoResponse>> observable = retrofit.create(ApiService.class).getCryptos();
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseData -> {

                    for (CryptoResponse item : responseData) {
                        titles.add(item.getId());
                    }
                }, throwable -> {});

    }
    public void updateUsersCrypto(RealmResults<CryptoResponse> cryptos) {

        for (CryptoResponse crypto : cryptos) {
            Observable<List<CryptoResponse>> observable = retrofit.create(ApiService.class).getCrypto(crypto.getId());
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(responseData ->
                                    realm.executeTransaction(realm ->
                                            realm.copyToRealmOrUpdate(responseData.get(0))
                                    ),
                            throwable -> {});
        }
    }
}
