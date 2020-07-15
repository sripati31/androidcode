package com.divinetechs.gotstart.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.divinetechs.gotstart.Interface.Watchinter;
import com.divinetechs.gotstart.Model.SubPlanModel.Result;
import com.divinetechs.gotstart.R;

import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.MyViewHolder> {

    private List<Result> PopularList;
    Context mcontext;
    String status;
    Watchinter watchinter;
    int row_index = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_sub_name, txt_sub_price;
        LinearLayout card_view;
        LinearLayout ly_annual;

        public MyViewHolder(View view) {
            super(view);
            txt_sub_name = (TextView) view.findViewById(R.id.txt_sub_name);
            txt_sub_price = (TextView) view.findViewById(R.id.txt_sub_price);
            card_view = (LinearLayout) view.findViewById(R.id.card_view);
            ly_annual = (LinearLayout) view.findViewById(R.id.ly_annual);
        }
    }

    public SubscriptionAdapter(Context context, List<Result> PopularList, String status, Watchinter watchinter) {
        this.PopularList = PopularList;
        this.mcontext = context;
        this.status = status;
        this.watchinter = watchinter;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (status.equalsIgnoreCase("episode")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subscription_item_land, parent, false);
            return new MyViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subscription_item_land, parent, false);
            return new MyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        if (status.equalsIgnoreCase("episode")) {
            holder.txt_sub_name.setText("" + PopularList.get(position).getSubName());
            holder.txt_sub_price.setText(PopularList.get(position).getCurrency_type() + "" + PopularList.get(position).getSubPrice());
        } else {
            holder.txt_sub_name.setText("" + PopularList.get(position).getSubName());
            holder.txt_sub_price.setText(PopularList.get(position).getCurrency_type() + "" + PopularList.get(position).getSubPrice());
        }

        if (row_index == position) {
            holder.ly_annual.setBackground(mcontext.getResources().getDrawable(R.drawable.round_bor_yellow));
        } else {
            holder.ly_annual.setBackground(mcontext.getResources().getDrawable(R.drawable.round_bor_gray));
        }

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();

                watchinter.remove_item("" + position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return PopularList.size();
    }

}
