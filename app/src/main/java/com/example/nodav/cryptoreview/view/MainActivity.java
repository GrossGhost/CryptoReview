package com.example.nodav.cryptoreview.view;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.nodav.cryptoreview.App;
import com.example.nodav.cryptoreview.adapters.CryptoAdapter;
import com.example.nodav.cryptoreview.R;
import com.example.nodav.cryptoreview.model.CryptoResponse;
import com.example.nodav.cryptoreview.presenter.Presenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    @Inject
    Retrofit retrofit;
    @Inject
    Realm realm;
    @Inject
    Presenter presenter;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view_crypto)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private CryptoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.getInstance().getAppComponent().inject(this);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {

        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.onRefreshData();
            swipeRefreshLayout.setRefreshing(false);
        });

        adapter = new CryptoAdapter(this, realm);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        presenter = new Presenter(realm);
        presenter.attachView(this);
        presenter.viewIsReady();
    }

    @OnClick(R.id.fab)
    public void onFABClick() {

        AddCryptoDialog dialog = new AddCryptoDialog(this, App.getInstance().getTitles(), realm);
        dialog.show();
    }

    public void showCrypto(RealmResults<CryptoResponse> data) {
        adapter.setData(data);
    }
    public void updateCrypto(){
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
