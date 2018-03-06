package com.example.nodav.cryptoreview.dagger.component;

import com.example.nodav.cryptoreview.App;
import com.example.nodav.cryptoreview.dagger.module.NetModule;
import com.example.nodav.cryptoreview.dagger.module.RealmModule;
import com.example.nodav.cryptoreview.ui.MainActivity;
import com.example.nodav.cryptoreview.ui.UsersCryptoListActivity;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = { NetModule.class, RealmModule.class})
public interface AppComponent {
    void inject(App app);
    void inject(MainActivity activity);
    void inject(UsersCryptoListActivity activity);
}