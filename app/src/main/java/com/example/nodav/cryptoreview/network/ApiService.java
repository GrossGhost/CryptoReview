package com.example.nodav.cryptoreview.network;

import com.example.nodav.cryptoreview.model.CryptoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("v1/ticker/")
    Call<CryptoResponse> getCrypto(@Query("after") int limit);
}
