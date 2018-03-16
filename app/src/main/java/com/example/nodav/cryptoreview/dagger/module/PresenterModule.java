package com.example.nodav.cryptoreview.dagger.module;

import com.example.nodav.cryptoreview.presenter.MainActivityPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;



@Module
public class PresenterModule {

    @Provides
    @Singleton
    MainActivityPresenter providePresenter(Realm realm){
        return new MainActivityPresenter(realm);
    }
}
