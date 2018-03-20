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
import com.example.nodav.cryptoreview.presenter.MainActivityPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CryptoTitleAdapter extends RecyclerView.Adapter<CryptoTitleAdapter.ViewHolder> {

    private List<String> titlesAll;
    private List<String> titlesAllCopy = new ArrayList<>();
    private List<String> titlesUser;
    private MainActivityPresenter presenter;

    public  CryptoTitleAdapter(List<String> titlesAll, MainActivityPresenter presenter){
        this.titlesAll = titlesAll;
        this.titlesAllCopy.addAll(this.titlesAll);
        this.titlesUser = presenter.getUsersCryptoList();
        this.presenter = presenter;

    }

    public void filter(String text){
        titlesAll.clear();
        if (text.isEmpty()){
            titlesAll.addAll(titlesAllCopy);
        } else {
            text = text.toLowerCase();
            for (String item : titlesAllCopy){
                if(item.toLowerCase().startsWith(text)){
                    titlesAll.add(item);
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
        final String title = titlesAll.get(position);
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
        return titlesAll.size();
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

            presenter.onCryptoAdd(title.getText()+"", titlesUser.size());

            checkBox.setChecked(true);
            cardView.setClickable(false);
            titlesUser.add(title.getText()+"");
            cardView.setCardBackgroundColor(Color.GRAY);
        }
    }
}
