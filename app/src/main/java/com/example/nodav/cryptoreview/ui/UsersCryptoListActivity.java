package com.example.nodav.cryptoreview.ui;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.nodav.cryptoreview.App;
import com.example.nodav.cryptoreview.R;
import com.example.nodav.cryptoreview.adapters.UsersCryptoAdapter;
import com.example.nodav.cryptoreview.model.CryptoResponse;
import com.example.nodav.cryptoreview.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import retrofit2.Retrofit;

public class UsersCryptoListActivity extends AppCompatActivity {

    @Inject
    Realm realm;
    @Inject
    Retrofit retrofit;

    @BindView(R.id.recycler_view_crypto_users)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private static List<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_list);

        ButterKnife.bind(this);
        ((App) getApplication()).getAppComponent().inject(this);
        initViews();

    }

    private void initViews() {
        if (titles == null) {
            titles = new ArrayList<>();
            titles = getCryptoTitles();
        }
        UsersCryptoAdapter adapter = new UsersCryptoAdapter(realm.where(CryptoResponse.class).findAll(), realm);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @OnClick(R.id.fab)
    public void onFABClick() {

        AddCryptoDialog dialog = new AddCryptoDialog(this, titles, realm);
        dialog.show();
    }

    private List<String> getCryptoTitles() {
        List<String> titles = new ArrayList<>();
        Observable<List<CryptoResponse>> observable = retrofit.create(ApiService.class).getCryptos(0);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseData -> {

                    for (CryptoResponse item : responseData) {
                        titles.add(item.getId());
                    }
                });
        return titles;
    }

}
