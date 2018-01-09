package com.example.nodav.cryptoreview.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.nodav.cryptoreview.adapters.CryptoAdapter;
import com.example.nodav.cryptoreview.R;
import com.example.nodav.cryptoreview.network.RestManager;


public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_crypto);
        swipeRefreshLayout = findViewById(R.id.swipe_container);

        swipeRefreshLayout.setOnRefreshListener(() -> {

            RestManager.loadCrypto( () ->
                    Toast.makeText(getApplicationContext(),"updating data error", Toast.LENGTH_SHORT).show(), 25);

            swipeRefreshLayout.setRefreshing(false);

        });

        CryptoAdapter adapter = new CryptoAdapter(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

}
