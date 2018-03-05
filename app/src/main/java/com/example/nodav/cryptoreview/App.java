package com.example.nodav.cryptoreview;

import android.app.Application;


import com.example.nodav.cryptoreview.dagger.component.AppComponent;
import com.example.nodav.cryptoreview.dagger.component.DaggerAppComponent;
import com.example.nodav.cryptoreview.dagger.module.NetModule;
import com.example.nodav.cryptoreview.dagger.module.RealmModule;
import com.example.nodav.cryptoreview.model.CryptoResponse;
import com.example.nodav.cryptoreview.network.ApiService;


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

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appComponent = DaggerAppComponent.builder()
                .netModule(new NetModule())
                .realmModule(new RealmModule(this))
                .build();

        appComponent.inject(this);

        RealmResults<CryptoResponse> usersCryptos = realm.where(CryptoResponse.class).findAll();
        if ( usersCryptos.size() > 0){
            getUsersCrypto(usersCryptos);
        }

    }

    public void getUsersCrypto(RealmResults<CryptoResponse> cryptos){

        for (CryptoResponse crypto : cryptos) {
            Observable<List<CryptoResponse>> observable = retrofit.create(ApiService.class).getCrypto(crypto.getId());
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(responseData ->
                                    realm.executeTransaction(realm ->

                                            realm.copyToRealmOrUpdate(responseData.get(0))

                                    ),
                            throwable -> {
                    });
        }

    }

    public static App getInstance() { return  instance; }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
