package com.example.nodav.cryptoreview.network;

import android.annotation.SuppressLint;

import com.example.nodav.cryptoreview.model.CryptoResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class Service {

    private final ApiService apiService;

    public Service(ApiService apiService) {
        this.apiService = apiService;
    }

    @SuppressLint("CheckResult")
    public void getCryptoTitles(final GetCryptoTitlesCallback callback) {

        apiService.getCryptos()
                .subscribeOn(Schedulers.io())
                .flatMap(Observable::fromIterable)
                .map(CryptoResponse::getId)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess, throwable -> callback.onError(new NetworkError(throwable)));

    }

    public interface GetCryptoTitlesCallback {
        void onSuccess(List<String> titles);
        void onError(NetworkError networkError);
    }

    @SuppressLint("CheckResult")
    public void updateUsersCrypto(String cryptoId, int position, final UpdateCryptoCallback callback) {

        apiService.getCrypto(cryptoId)
                .subscribeOn(Schedulers.io())
                .map(cryptoResponses -> cryptoResponses.get(0))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseData -> callback.onSuccess(responseData, position),
                        throwable -> callback.onError(new NetworkError(throwable)));

    }

    public interface UpdateCryptoCallback {
        void onSuccess(CryptoResponse response, int position);
        void onError(NetworkError networkError);
    }

//    public void updateUsersCryptos(List<CryptoResponse> cryptos, UpdateCryptosCallback callback){
//
//        Observable.fromIterable(cryptos)
//                .subscribeOn(Schedulers.io())
//                .map(CryptoResponse::getId)
//                .flatMap(apiService::getCrypto)
//                .map(cryptoResponses -> cryptoResponses.get(0))
//                .toList()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(callback::onSuccess, throwable -> callback.onError(new NetworkError(throwable)));
//    }

    @SuppressLint("CheckResult")
    public void updateUsersCryptos(List<CryptoResponse> cryptos, UpdateCryptosCallback callback) {

        List<String> tiles = new ArrayList<>();
        Observable.fromIterable(cryptos)
                .map(CryptoResponse::getId)
                .doOnNext(tiles::add)
                .subscribe();

        apiService.getCryptos()
                .subscribeOn(Schedulers.io())
                .flatMap(Observable::fromIterable)
                .filter(response -> tiles.contains(response.getId()))
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess, throwable -> callback.onError(new NetworkError(throwable)));
    }

    public interface UpdateCryptosCallback {
        void onSuccess(List<CryptoResponse> response);
        void onError(NetworkError networkError);
    }
}
