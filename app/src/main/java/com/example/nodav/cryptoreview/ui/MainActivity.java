package com.example.nodav.cryptoreview.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.nodav.cryptoreview.App;
import com.example.nodav.cryptoreview.adapters.CryptoAdapter;
import com.example.nodav.cryptoreview.R;
import com.example.nodav.cryptoreview.model.CryptoResponse;
import com.example.nodav.cryptoreview.network.ApiService;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    @Inject
    Retrofit retrofit;
    @Inject
    Realm realm;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout ;
    @BindView(R.id.recycler_view_crypto)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((App) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);

        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_users_list:
                showUsersListActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUsersListActivity() {
        startActivity(new Intent(this, UsersCryptoListActivity.class));
    }

    private void initViews() {

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getPopularCrypto();
            swipeRefreshLayout.setRefreshing(false);
        });

        CryptoAdapter adapter = new CryptoAdapter(this, realm.where(CryptoResponse.class).findAll());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void getPopularCrypto() {
        Observable<List<CryptoResponse>> call = retrofit.create(ApiService.class).getCrypto(25);
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseData -> realm.executeTransaction(realm -> {
                    realm.deleteAll();
                    realm.insert(responseData);
                }), throwable -> {
                    Toast.makeText(this,"updating data error", Toast.LENGTH_SHORT).show();
                });
    }

}
