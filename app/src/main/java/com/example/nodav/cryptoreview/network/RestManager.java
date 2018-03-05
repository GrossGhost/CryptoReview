package com.example.nodav.cryptoreview.network;

import android.support.annotation.NonNull;

import io.realm.Realm;
import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.nodav.cryptoreview.Constants.BASE_URL;


public class RestManager {

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
                    //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(ApiService.class);
    }

//    @NonNull
//    public static void loadCrypto(INetworkRequestHandler handler, int limit) {
//        Observable<List<CryptoResponse>> observable = RestManager.getApiService().getCryptos(limit);
//        observable.subscribeOn(Schedulers.newThread())
//
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(responseData -> realm.executeTransaction(realm -> {
//                    realm.deleteAll();
//                    realm.insert(responseData);
//
//                }), throwable -> {
//                    handler.onRequestError();
//                });
//    }
}
