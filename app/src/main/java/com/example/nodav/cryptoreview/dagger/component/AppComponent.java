package com.example.nodav.cryptoreview.dagger.component;

import com.example.nodav.cryptoreview.dagger.module.NetModule;
import com.example.nodav.cryptoreview.dagger.module.PresenterModule;
import com.example.nodav.cryptoreview.dagger.module.RealmModule;
import com.example.nodav.cryptoreview.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = { NetModule.class, RealmModule.class, PresenterModule.class})
public interface AppComponent {
    void inject(MainActivity activity);
}
