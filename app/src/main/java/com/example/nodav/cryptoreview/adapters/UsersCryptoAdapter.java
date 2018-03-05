package com.example.nodav.cryptoreview.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nodav.cryptoreview.R;
import com.example.nodav.cryptoreview.model.CryptoResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class UsersCryptoAdapter extends RecyclerView.Adapter<UsersCryptoAdapter.ViewHolder> implements RealmChangeListener {

    private RealmResults<CryptoResponse> data;
    private Realm realm;

    public UsersCryptoAdapter(RealmResults<CryptoResponse> data, Realm realm){
        this.data = data;
        this.realm = realm;
        data.addChangeListener(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_title_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(data.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onChange(Object o) {
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title_text_view)
        TextView title;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.title_text_view)
        public void delete(TextView view) {

            realm.beginTransaction();
            realm.where(CryptoResponse.class).equalTo("id", view.getText()+"").findFirst().deleteFromRealm();
            realm.commitTransaction();

        }

    }
}
