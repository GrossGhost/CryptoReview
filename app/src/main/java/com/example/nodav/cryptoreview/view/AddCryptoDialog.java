package com.example.nodav.cryptoreview.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.example.nodav.cryptoreview.R;
import com.example.nodav.cryptoreview.adapters.CryptoTitleAdapter;


import butterknife.BindView;
import butterknife.ButterKnife;


class AddCryptoDialog extends Dialog {

    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.recycler_view_dialog)
    RecyclerView recyclerView;


    AddCryptoDialog(@NonNull Context context, CryptoTitleAdapter adapter) {
        super(context, R.style.Theme_AppCompat_Dialog);

        View view = View.inflate(getContext(), R.layout.dialog_crypto_list, null);
        setContentView(view);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
    }
}
