package com.example.nodav.cryptoreview.adapters;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nodav.cryptoreview.R;
import com.example.nodav.cryptoreview.model.CryptoResponse;
import com.example.nodav.cryptoreview.model.UserHoldings;
import com.example.nodav.cryptoreview.presenter.MainActivityPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.realm.RealmResults;


public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.ViewHolder> {

    private RealmResults<CryptoResponse> data;
    private RealmResults<UserHoldings> holdingsData;
    private MainActivityPresenter presenter;
    private Resources resources;


    public CryptoAdapter(MainActivityPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        resources = parent.getResources();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CryptoResponse item = data.get(position);
        UserHoldings holding = holdingsData.where().contains("id", item.getId()).findFirst();

        holder.name.setText(item.getSymbol());

        holder.holding.setText("$" + holding.getHolding());
        holder.holdingCount.setText(holding.getHoldingCount() + "");

        holder.price.setText("$" + item.getPriceUsd());
        holder.priceChange.setText(item.getPercentChange24h() + "%");
        if (item.getPercentChange24h() != null) {
            if (Double.parseDouble(item.getPercentChange24h()) < 0)
                holder.priceChange.setTextColor(resources.getColor(R.color.colorRed));
            else
                holder.priceChange.setTextColor(resources.getColor(R.color.colorGreen));
        }

        if (item.getPriceUsd() != null) {
            double price = Double.parseDouble(item.getPriceUsd());
            holder.profit.setText("$" + String.format("%.2f", holding.getProfit(price)));

            holder.profitPercent.setText(String.format("%.2f", holding.getProfitPercent(price)) + "%");
            if (holding.getProfitPercent(price) < 0) {
                holder.profit.setTextColor(resources.getColor(R.color.colorRed));
                holder.profitPercent.setTextColor(resources.getColor(R.color.colorRed));
            } else {
                holder.profit.setTextColor(resources.getColor(R.color.colorGreen));
                holder.profitPercent.setTextColor(resources.getColor(R.color.colorGreen));
            }
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(RealmResults<CryptoResponse> data, RealmResults<UserHoldings> holdingsData) {
        this.data = null;
        this.data = data;
        this.holdingsData = null;
        this.holdingsData = holdingsData;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.tv_name_coin)
        TextView name;
        @BindView(R.id.tv_holding)
        TextView holding;
        @BindView(R.id.tv_holding_count)
        TextView holdingCount;
        @BindView(R.id.tv_price)
        TextView price;
        @BindView(R.id.tv_price_change)
        TextView priceChange;
        @BindView(R.id.tv_profit)
        TextView profit;
        @BindView(R.id.tv_profit_percent)
        TextView profitPercent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.card_view)
        void onClick() {
            presenter.onCryptoClick(data.get(getAdapterPosition()).getId(), getAdapterPosition());
        }

        @OnLongClick(R.id.card_view)
        boolean delete() {
            presenter.onCryptoDelete(data.get(getAdapterPosition()).getId(), getAdapterPosition());
            return true;
        }
    }
}
