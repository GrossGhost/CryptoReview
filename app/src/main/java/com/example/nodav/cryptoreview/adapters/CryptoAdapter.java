package com.example.nodav.cryptoreview.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nodav.cryptoreview.R;
import com.example.nodav.cryptoreview.model.CryptoResponse;
import com.example.nodav.cryptoreview.presenter.MainActivityPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;
import io.realm.RealmResults;


public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.ViewHolder> {

    private RealmResults<CryptoResponse> data;
    private Context context;
    private MainActivityPresenter presenter;

    public CryptoAdapter(Context c, MainActivityPresenter presenter) {
        context = c;
        this.presenter = presenter;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CryptoResponse item = data.get(position);
        holder.name.setText(item.getId());
        holder.price.setText(context.getString(R.string.dollar_price, item.getPriceUsd()));

        holder.changeOneH.setText(item.getPercentChange1h() + "%");
        if (data.get(position).getPercentChange1h() != null) {
            if (Double.parseDouble(item.getPercentChange1h()) < 0)
                holder.changeOneH.setTextColor(context.getResources().getColor(R.color.colorRed));
            else
                holder.changeOneH.setTextColor(context.getResources().getColor(R.color.colorGreen));
        }


        holder.change24H.setText(item.getPercentChange24h() + "%");
        if (item.getPercentChange24h() != null) {
            if (Double.parseDouble(item.getPercentChange24h()) < 0)
                holder.change24H.setTextColor(context.getResources().getColor(R.color.colorRed));
            else
                holder.change24H.setTextColor(context.getResources().getColor(R.color.colorGreen));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(RealmResults<CryptoResponse> data){
        this.data = null;
        this.data = data;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.tv_name_coin)
        TextView name;
        @BindView(R.id.tv_price)
        TextView price;
        @BindView(R.id.tv_percent_change_1h)
        TextView changeOneH;
        @BindView(R.id.tv_percent_change_24h)
        TextView change24H;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnLongClick(R.id.card_view)
        boolean delete() {
            presenter.onCryptoDelete(name.getText()+"", getAdapterPosition());
            return true;
        }
    }
}
