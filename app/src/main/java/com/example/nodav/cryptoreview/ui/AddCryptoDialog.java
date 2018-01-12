package com.example.nodav.cryptoreview.ui;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.example.nodav.cryptoreview.App;
import com.example.nodav.cryptoreview.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class AddCryptoDialog extends Dialog {

    @Inject
    Realm realm;

    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.button_dialog_ok)
    Button buttonOk;
    @BindView(R.id.recycler_view_dialog)
    RecyclerView recyclerView;

    public AddCryptoDialog(@NonNull Context context) {
        super(context);

        View view = View.inflate(getContext(), R.layout.dialog_crypto_list, null);
        setContentView(view);
        ButterKnife.bind(this, view);

        //((App) getOwnerActivity().getApplication()).getAppComponent().inject(this);
    }
}
