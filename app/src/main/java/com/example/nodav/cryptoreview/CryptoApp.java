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

        RestManager.loadCrypto( () ->
                Toast.makeText(getApplicationContext(),"updating data error", Toast.LENGTH_SHORT).show(), 25);
    }


}
