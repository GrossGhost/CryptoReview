package com.example.nodav.cryptoreview.network;

import com.example.nodav.cryptoreview.model.CryptoResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiService {
    @GET("v1/ticker")
    Observable<List<CryptoResponse>> getCrypto(@Query("limit") Integer limit);
}
