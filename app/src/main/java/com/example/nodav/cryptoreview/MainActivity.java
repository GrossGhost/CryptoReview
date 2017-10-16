package com.example.nodav.cryptoreview;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.nodav.cryptoreview.network.RestManager;


public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_crypto);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        swipeRefreshLayout.setOnRefreshListener(() -> {

            String response = RestManager.loadCrypto();
            if (!response.equals(""))
                Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();

            swipeRefreshLayout.setRefreshing(false);

        });

        CryptoAdapter adapter = new CryptoAdapter(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

}
