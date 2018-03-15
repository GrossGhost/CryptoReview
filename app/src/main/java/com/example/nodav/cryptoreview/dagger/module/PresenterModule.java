package com.example.nodav.cryptoreview.dagger.module;

import com.example.nodav.cryptoreview.presenter.Presenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;



@Module
public class PresenterModule {

    @Provides
    @Singleton
    Presenter providePresenter(Realm realm){
        return new Presenter(realm);
    }
}
