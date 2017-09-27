package com.example.nodav.cryptoreview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.nodav.cryptoreview.model.CryptoResponse;
import com.example.nodav.cryptoreview.network.RestManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private RestManager restManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restManager = new RestManager();

        load();
    }

    private void load() {

       /* Call<List<CryptoResponse>> call = restManager.getApiService().getCrypto();
        call.enqueue(new Callback<List<CryptoResponse>>() {
            @Override
            public void onResponse(Call<List<CryptoResponse>> call, Response<List<CryptoResponse>> response) {

                for (CryptoResponse crypto : response.body() ) {
                    Log.v("RESPONSE1", crypto.getName());
                }


            }

            @Override
            public void onFailure(Call<List<CryptoResponse>> call, Throwable t) {

            }
        });*/

        Observable<List<CryptoResponse>> observable = restManager.getApiService().getCrypto();
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseData ->{

                    for (CryptoResponse crypto : responseData ) {
                        Log.v("RESPONSE1", crypto.getName());
                    }

                });
    }

}
