package com.example.nodav.cryptoreview.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.nodav.cryptoreview.R;
import com.example.nodav.cryptoreview.model.CryptoResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;


public class CryptoTitleAdapter extends RecyclerView.Adapter<CryptoTitleAdapter.ViewHolder> {

    private List<String> titles;
    private List<String> titlesCopy = new ArrayList<>();
    private List<String> titlesUser = new ArrayList<>();
    private Realm realm;

    public  CryptoTitleAdapter(List<String> titles, Realm realm){
        this.titles = titles;
        this.titlesCopy.addAll(titles);
        this.realm = realm;

        RealmResults<CryptoResponse> usersCrypto = realm.where(CryptoResponse.class).findAll();
        for (CryptoResponse response : usersCrypto){
            titlesUser.add(response.getId());
        }
    }

    public void filter(String text){
        titles.clear();
        if (text.isEmpty()){
            titles.addAll(titlesCopy);
        } else {
            text = text.toLowerCase();
            for (String item : titlesCopy){
                if(item.toLowerCase().startsWith(text)){
                    titles.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_title_all, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String title = titles.get(position);
        holder.title.setText(title);

        holder.checkBox.setOnCheckedChangeListener(null);

        if (titlesUser.contains(title)){
            holder.checkBox.setChecked(true);
            holder.cardView.setClickable(false);
            holder.cardView.setCardBackgroundColor(Color.GRAY);

        }else{
            holder.checkBox.setChecked(false);
            holder.cardView.setClickable(true);
            holder.cardView.setCardBackgroundColor(Color.WHITE);
        }

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_text_view)
        TextView title;
        @BindView(R.id.title_checkbox)
        CheckBox checkBox;
        @BindView(R.id.card_view)
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        @OnClick(R.id.card_view)
        void submit() {
            CryptoResponse crypto = new CryptoResponse();
            crypto.setId(title.getText()+"");

            realm.beginTransaction();
            realm.insert(crypto);
            realm.commitTransaction();

            checkBox.setChecked(true);
            cardView.setClickable(false);
            titlesUser.add(title.getText()+"");
            cardView.setCardBackgroundColor(Color.GRAY);
        }
    }
}
