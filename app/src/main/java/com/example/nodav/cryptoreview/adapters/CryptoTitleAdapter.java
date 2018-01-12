package com.example.nodav.cryptoreview.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nodav.cryptoreview.R;
import com.example.nodav.cryptoreview.model.UsersCryptoList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


public class CryptoTitleAdapter extends RecyclerView.Adapter<CryptoTitleAdapter.ViewHolder> implements RealmChangeListener {

    private RealmResults<UsersCryptoList> data;

    public  CryptoTitleAdapter(RealmResults<UsersCryptoList> data){
        this.data = data;
        data.addChangeListener(this);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_title, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(data.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onChange(Object o) {
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_text_view)
        TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
