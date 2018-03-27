package com.example.nodav.cryptoreview.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nodav.cryptoreview.R;
import com.example.nodav.cryptoreview.presenter.MainActivityPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChangeHoldingDialog extends Dialog {

    @BindView(R.id.edit_text_new_holding)
    EditText holdingEditText;
    @BindView(R.id.edit_text_new_count)
    EditText countEditText;
    @BindView(R.id.tv_crypto)
    TextView tvCrypto;
    private String id;
    private int position;
    private MainActivityPresenter presenter;
    private double holding, count;

    public ChangeHoldingDialog(@NonNull Context context, String id, int position, MainActivityPresenter presenter, double holding, double count) {
        super(context);
        View view = View.inflate(context, R.layout.dialog_change_holdings, null);
        setContentView(view);
        ButterKnife.bind(this,view);
        this.id = id;
        this.position = position;
        this.presenter = presenter;
        this.holding = holding;
        this.count = count;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvCrypto.setText(id);
        holdingEditText.setText(holding + "");
        countEditText.setText(count + "");
    }

    @OnClick(R.id.button_cancel_holding)
    public void cancel(){
        dismiss();
    }

    @OnClick(R.id.button_submit_holding)
    public void submit(){
        Double holding = Double.parseDouble(holdingEditText.getText().toString());
        Double count = Double.parseDouble(countEditText.getText().toString());

        presenter.onHoldingChanged(id, position, holding, count);
        dismiss();
    }
}
