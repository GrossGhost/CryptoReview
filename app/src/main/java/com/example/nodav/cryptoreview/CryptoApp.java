package com.example.nodav.cryptoreview;

import android.app.Application;
import android.widget.Toast;

import com.example.nodav.cryptoreview.network.RestManager;

import io.realm.Realm;


public class CryptoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());

        //String response = RestManager.loadCrypto();
        //if (!response.equals(""))
        //    Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();

        RestManager.loadCrypto( () ->
                Toast.makeText(getApplicationContext(),"updating data error", Toast.LENGTH_SHORT).show());
    }


}
