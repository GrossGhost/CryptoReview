package com.example.nodav.cryptoreview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nodav.cryptoreview.model.CryptoResponse;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.MyAdapter> implements RealmChangeListener {

    private final RealmResults<CryptoResponse> data;
    private Context context;

    CryptoAdapter(Context c){
        context = c;
        Realm realm = Realm.getDefaultInstance();
        data = realm.where(CryptoResponse.class).findAll();
        data.addChangeListener(this);
        notifyDataSetChanged();
    }

    @Override
    public CryptoAdapter.MyAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(CryptoAdapter.MyAdapter holder, int position) {
        holder.name.setText(data.get(position).getName());
        holder.price.setText(context.getString(R.string.dollar_price, data.get(position).getPriceUsd()));

        holder.changeOneH.setText(data.get(position).getPercentChange1h() + "%");
        if ( Double.parseDouble(data.get(position).getPercentChange1h()) < 0)
            holder.changeOneH.setTextColor(Color.RED);
        else
            holder.changeOneH.setTextColor(Color.GREEN);

        holder.change24H.setText(data.get(position).getPercentChange24h() + "%");
        if ( Double.parseDouble(data.get(position).getPercentChange24h()) < 0)
            holder.change24H.setTextColor(Color.RED);
        else
            holder.change24H.setTextColor(Color.GREEN);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onChange(Object o) {
        notifyDataSetChanged();
        Toast.makeText(context, "Data Updated", Toast.LENGTH_SHORT).show();
    }

    class MyAdapter extends RecyclerView.ViewHolder {

        TextView name;
        TextView price;
        TextView changeOneH;
        TextView change24H;

        MyAdapter(final View view) {
            super(view);
            name = view.findViewById(R.id.tv_name_coin);
            price = view.findViewById(R.id.tv_price);
            changeOneH = view.findViewById(R.id.tv_percent_change_1h);
            change24H = view.findViewById(R.id.tv_percent_change_24h);
        }
    }
}
