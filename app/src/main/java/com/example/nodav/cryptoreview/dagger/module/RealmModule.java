package com.example.nodav.cryptoreview.dagger.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;


@Module
public class RealmModule {

    public RealmModule(Context context) {
        Realm.init(context);
    }

    @Provides
    @Singleton
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }
}
