package com.example.nodav.cryptoreview.network;

import android.support.annotation.NonNull;

import com.example.nodav.cryptoreview.model.CryptoResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import retrofit2.Retrofit;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestManager {

    private static final String BASE_URL = "https://api.coinmarketcap.com/";

    private static ApiService apiService;
    private static Realm realm = Realm.getDefaultInstance();


    @NonNull
    private static ApiService getApiService(){
        //I know that double checked locking is not a good pattern, but it's enough here
        ApiService service = apiService;
        if (service == null){
            synchronized (RestManager.class){
                service = apiService;
                if (service == null){
                    service = apiService = createService();
                }
            }
        }
        return service;
    }

    @NonNull
    private static ApiService createService() {

            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(ApiService.class);
    }

    @NonNull
    public static String loadCrypto(){
        String[] response = {""};
        Observable<List<CryptoResponse>> observable = RestManager.getApiService().getCrypto(25);
        observable.subscribeOn(Schedulers.newThread())

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseData -> realm.executeTransaction(realm -> {
                    realm.deleteAll();
                    realm.insert(responseData);

                }), throwable ->
                        response[0] = throwable.getMessage());

        return response[0];
    }
}
