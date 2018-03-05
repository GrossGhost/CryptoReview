package com.example.nodav.cryptoreview.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nodav.cryptoreview.R;
import com.example.nodav.cryptoreview.model.CryptoResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.ViewHolder> implements RealmChangeListener {

    private RealmResults<CryptoResponse> data;
    private Context context;

    public CryptoAdapter(Context c, RealmResults<CryptoResponse> data) {
        context = c;
        this.data = data;
        data.addChangeListener(this);
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
        if (data.get(position).getPercentChange1h() != null){
            if (Double.parseDouble(item.getPercentChange1h()) < 0)
                holder.changeOneH.setTextColor(Color.RED);
            else
                holder.changeOneH.setTextColor(Color.GREEN);
        }


        holder.change24H.setText(item.getPercentChange24h() + "%");
        if (item.getPercentChange24h() != null) {
            if (Double.parseDouble(item.getPercentChange24h()) < 0)
                holder.change24H.setTextColor(Color.RED);
            else
                holder.change24H.setTextColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onChange(Object o) {
        notifyDataSetChanged();
        //Toast.makeText(context, "Data Updated", Toast.LENGTH_SHORT).show();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

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
    }
}
