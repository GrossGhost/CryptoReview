package com.example.nodav.cryptoreview;

import android.app.Application;
import android.util.Log;

import com.example.nodav.cryptoreview.dagger.component.AppComponent;

import com.example.nodav.cryptoreview.dagger.component.DaggerAppComponent;
import com.example.nodav.cryptoreview.dagger.module.NetModule;
import com.example.nodav.cryptoreview.dagger.module.PresenterModule;
import com.example.nodav.cryptoreview.dagger.module.RealmModule;


import java.util.ArrayList;
import java.util.List;



public class App extends Application {

    private static App instance = null;

    private AppComponent appComponent;
    private List<String> titles = new ArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appComponent = DaggerAppComponent.builder()
                .netModule(new NetModule())
                .realmModule(new RealmModule(this))
                .presenterModule(new PresenterModule())
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public List<String> getTitles() {
        return titles;

    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }
}
