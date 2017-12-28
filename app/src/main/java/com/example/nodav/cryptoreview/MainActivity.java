package com.example.nodav.cryptoreview;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.nodav.cryptoreview.network.RestManager;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;


public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_crypto);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        swipeRefreshLayout.setOnRefreshListener(() -> {

            RestManager.loadCrypto( () ->
                    Toast.makeText(getApplicationContext(),"updating data error", Toast.LENGTH_SHORT).show());

            swipeRefreshLayout.setRefreshing(false);

        });

        CryptoAdapter adapter = new CryptoAdapter(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new AppUpdater(this)
                .setDisplay(Display.SNACKBAR)
                .setDisplay(Display.DIALOG)
                .setDisplay(Display.NOTIFICATION)
                .setUpdateFrom(UpdateFrom.GITHUB)
                .setGitHubUserAndRepo("GrossGhost", "CryptoReview")
                .start();

//        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(this)
//                .setUpdateFrom(UpdateFrom.GITHUB)
//                .setGitHubUserAndRepo("GrossGhost", "CryptoReview")
//                .withListener(new AppUpdaterUtils.UpdateListener() {
//                    @Override
//                    public void onSuccess(Update update, Boolean isUpdateAvailable) {
//                        Log.d("Latest Version", update.getLatestVersion());
//                        Log.d("Latest Version Code", update.getLatestVersionCode() + "");
//                        Log.d("Release notes", update.getReleaseNotes());
//                        Log.d("URL", update.getUrlToDownload()+ "");
//                        Log.d("Is update available?", Boolean.toString(isUpdateAvailable));
//                    }
//
//                    @Override
//                    public void onFailed(AppUpdaterError error) {
//                        Log.d("AppUpdater Error", "Something went wrong");
//                    }
//                });
//        appUpdaterUtils.start();
    }

}
