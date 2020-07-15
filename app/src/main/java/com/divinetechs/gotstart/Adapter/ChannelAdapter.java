package com.divinetechs.gotstart.Adapter;

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
import com.divinetechs.gotstart.Model.ChannelModel.Result;
import com.divinetechs.gotstart.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.squareup.picasso.Picasso.Priority.HIGH;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.MyViewHolder> {

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

    public ChannelAdapter(Context context, List<Result> ChannelList, String status) {
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

        holder.txt_episode_title.setText("" + ChannelList.get(position).getChannelName());
        try {
            Picasso.with(mcontext).load(ChannelList.get(position).getChannelImage()).priority(HIGH)
                    .resize(700, 700).centerInside().into(holder.iv_thumb);
        } catch (Exception e) {
            Log.e("Exception", "" + e.getMessage());
        }

        holder.iv_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("click", "call" + position);
                Intent intent = new Intent(mcontext, ChannelDetailsActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("id", ChannelList.get(position).getId());
                intent.putExtra("desc", ChannelList.get(position).getChannel_desc());
                intent.putExtra("image", ChannelList.get(position).getChannelImage());
                intent.putExtra("name", ChannelList.get(position).getChannelName());
                intent.putExtra("url", ChannelList.get(position).getChannelUrl());
                mcontext.startActivity(intent);

                if (status.equalsIgnoreCase("episode")) {
//                    ((Activity) mcontext).finish();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return ChannelList.size();
    }

}
