package com.example.nodav.cryptoreview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.nodav.cryptoreview.model.CryptoResponse;
import com.example.nodav.cryptoreview.network.RestManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {

    private RestManager restManager;
    private Realm realm;
    private RealmResults<CryptoResponse> cryptoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restManager = new RestManager();
        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();
        cryptoList = realm.where(CryptoResponse.class).findAll();

        if (cryptoList.size() == 0)
            load();
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (CryptoResponse crypto : cryptoList ) {
            Log.v("REalm1", crypto.getName() + " " + crypto.getPriceUsd() );

        }


    }

    private void load() {

        Observable<List<CryptoResponse>> observable = restManager.getApiService().getCrypto(10);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseData ->{

                    for (CryptoResponse crypto : responseData ) {
                        Log.v("RESPONSE1", crypto.getName() + " " + crypto.getPriceUsd() );
                    }

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.deleteAll();
                            realm.insert(responseData);

                        }
                    });


                });

    }

}
