package com.example.nodav.cryptoreview;

import android.app.Application;

import android.content.Context;
import android.widget.Toast;


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
import retrofit2.Retrofit;


public class App extends Application {

    @Inject
    Retrofit retrofit;
    @Inject
    Realm realm;

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .netModule(new NetModule())
                .realmModule(new RealmModule(this))
                .build();

        appComponent.inject(this);

        //getCryptoTitles();
        getCrypto(25);

    }

//    private void getCryptoTitles() {
//        Observable<List<CryptoResponse>> observable = retrofit.create(ApiService.class).getCrypto(0);
//        observable.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(responseData -> {
//
//                    for (CryptoResponse item : responseData){
//                        Log.d("SSS",item.getId());
//                        Log.d("SSS",item.getName());
//                    }
//                });
//    }

    private void getCrypto(int limit) {
        Observable<List<CryptoResponse>> observable = retrofit.create(ApiService.class).getCrypto(limit);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseData -> realm.executeTransaction(realm -> {
                    realm.deleteAll();
                    realm.insert(responseData);
                }), throwable -> {
                    Toast.makeText(getApplicationContext(),"updating data error", Toast.LENGTH_SHORT).show();
                });
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }


    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

}
