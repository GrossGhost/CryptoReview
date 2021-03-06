package com.example.nodav.cryptoreview.view;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nodav.cryptoreview.App;
import com.example.nodav.cryptoreview.adapters.CryptoAdapter;
import com.example.nodav.cryptoreview.R;
import com.example.nodav.cryptoreview.adapters.CryptoTitleAdapter;
import com.example.nodav.cryptoreview.model.CryptoResponse;
import com.example.nodav.cryptoreview.model.UserHoldings;
import com.example.nodav.cryptoreview.presenter.MainActivityPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {

    @Inject
    MainActivityPresenter presenter;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view_crypto)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_value_numbers)
    TextView tvValue;
    @BindView(R.id.tv_change_value)
    TextView tvChange;

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

        adapter = new CryptoAdapter(presenter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        presenter.attachView(this);
        presenter.viewIsReady();

    }

    @OnClick(R.id.fab)
    public void onFABClick() {
        if (App.getInstance().getTitles().size() == 0)
            presenter.getCryptoTitles();
        else
            showCryptoDialog(App.getInstance().getTitles());
    }

    public void showCryptoDialog(List<String> titles){
        AddCryptoDialog dialog = new AddCryptoDialog(this, new CryptoTitleAdapter(titles, presenter));
        dialog.show();
    }

    public void showCrypto(RealmResults<CryptoResponse> data, RealmResults<UserHoldings> holdingsData) {
        adapter.setData(data, holdingsData);
    }

    public void updateTotalStats(double value, double change){
        tvValue.setText(getResources().getString(R.string.dollar_price , value));
        tvChange.setText(String.format("%.2f", change)+"%");
        if (change < 0)
            tvChange.setTextColor(getResources().getColor(R.color.colorRed));
        else
            tvChange.setTextColor(getResources().getColor(R.color.colorGreen));
    }

    public CryptoAdapter getCryptoAdapter(){
        return adapter;
    }

    public void showProgressbar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressbar(){
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
