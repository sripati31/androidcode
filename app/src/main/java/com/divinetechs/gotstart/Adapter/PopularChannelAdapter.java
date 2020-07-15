package com.divinetechs.gotstart.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.divinetechs.gotstart.Activity.ChannelDetailsActivity;
import com.divinetechs.gotstart.Interface.Detailsdata;
import com.divinetechs.gotstart.Model.ChannelModel.Result;
import com.divinetechs.gotstart.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.squareup.picasso.Picasso.Priority.HIGH;

public class PopularChannelAdapter extends RecyclerView.Adapter<PopularChannelAdapter.MyViewHolder> {

    private List<Result> PopularList;
    Context mcontext;
    String status;
    Detailsdata detailsdata;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_episode_title;
        ImageView iv_thumb, iv_copy;

        public MyViewHolder(View view) {
            super(view);
            iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
            txt_episode_title = (TextView) view.findViewById(R.id.txt_episode_title);
        }
    }

    public PopularChannelAdapter(Context context, List<Result> PopularList,
                                 String status, Detailsdata detailsdata) {
        this.PopularList = PopularList;
        this.mcontext = context;
        this.status = status;
        this.detailsdata = detailsdata;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (status.equalsIgnoreCase("episode")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.popular_ch_item_land, parent, false);
            return new MyViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.popular_item, parent, false);
            return new MyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        if (status.equalsIgnoreCase("episode")) {
            holder.txt_episode_title.setText("" + PopularList.get(position).getChannelName());
            try {
                Picasso.with(mcontext).load(PopularList.get(position).getChannelImage()).priority(HIGH)
                        .resize(700, 700).centerInside().into(holder.iv_thumb);
            } catch (Exception e) {
                Log.e("Exception", "" + e.getMessage());
            }
        } else {
            holder.txt_episode_title.setText("" + PopularList.get(position).getChannelName());
            try {
                Picasso.with(mcontext).load(PopularList.get(position).getChannelImage()).priority(HIGH)
                        .resize(700, 700).centerInside().into(holder.iv_thumb);
            } catch (Exception e) {
                Log.e("Exception", "" + e.getMessage());
            }
        }

        holder.iv_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("click", "call" + position);
                detailsdata.item_click(position, "");
            }
        });

    }

    @Override
    public int getItemCount() {
        return PopularList.size();
    }

}
