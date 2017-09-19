package com.example.nodav.cryptoreview.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nodav on 19.09.2017.
 */

public class RestManager {

    private static final String BASE_URL = "https://api.coinmarketcap.com/";

    private ApiService apiService;

    public ApiService getApiService() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
}
