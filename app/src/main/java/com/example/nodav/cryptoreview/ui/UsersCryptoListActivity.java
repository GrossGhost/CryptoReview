package com.example.nodav.cryptoreview.ui;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.nodav.cryptoreview.App;
import com.example.nodav.cryptoreview.R;
import com.example.nodav.cryptoreview.adapters.CryptoTitleAdapter;
import com.example.nodav.cryptoreview.model.UsersCryptoList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class UsersCryptoListActivity extends AppCompatActivity {

    @Inject
    Realm realm;

    @BindView(R.id.recycler_view_crypto_users)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_list);

        ButterKnife.bind(this);
        ((App) getApplication()).getAppComponent().inject(this);
        initViews();
    }

    private void initViews() {
        CryptoTitleAdapter adapter = new CryptoTitleAdapter(realm.where(UsersCryptoList.class).findAll());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
