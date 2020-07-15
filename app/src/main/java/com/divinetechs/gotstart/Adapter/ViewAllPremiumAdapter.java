package com.divinetechs.gotstart.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.divinetechs.gotstart.Activity.DetailsActivity;
import com.divinetechs.gotstart.Model.TvVideoModel.Result;
import com.divinetechs.gotstart.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.squareup.picasso.Picasso.Priority.HIGH;

public class ViewAllPremiumAdapter extends RecyclerView.Adapter<ViewAllPremiumAdapter.MyViewHolder> {

    private List<Result> ChannelList;
    Context mcontext;
    String status;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_episode_title;
        ImageView iv_thumb, iv_copy;

        public MyViewHolder(View view) {
            super(view);
            iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
            txt_episode_title = (TextView) view.findViewById(R.id.txt_episode_title);
        }
    }

    public ViewAllPremiumAdapter(Context context, List<Result> ChannelList, String status) {
        this.ChannelList = ChannelList;
        this.mcontext = context;
        this.status = status;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.channel_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txt_episode_title.setText("" + ChannelList.get(position).getTvvName());
        try {
            Picasso.with(mcontext).load(ChannelList.get(position).getTvsImage()).priority(HIGH)
                    .resize(700, 700).centerInside().into(holder.iv_thumb);
        } catch (Exception e) {
            Log.e("Exception", "" + e.getMessage());
        }

        holder.iv_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("click", "call" + position);
                Intent intent = new Intent(mcontext, DetailsActivity.class);
                intent.putExtra("tvs_id", ChannelList.get(position).getTvvId());
                intent.putExtra("tvs_name", ChannelList.get(position).getTvvName());
                intent.putExtra("position", position);
                intent.putExtra("fc_id", ChannelList.get(position).getFc_id());
                intent.putExtra("type", ChannelList.get(position).getV_type());
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ChannelList.size();
    }

}
