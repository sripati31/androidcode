package com.divinetechs.gotstart.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.divinetechs.gotstart.Activity.AudioDetailsActivity;
import com.divinetechs.gotstart.Activity.ChannelDetailsActivity;
import com.divinetechs.gotstart.Model.AudioModel.Result;
import com.divinetechs.gotstart.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.squareup.picasso.Picasso.Priority.HIGH;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.MyViewHolder> {

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

    public AudioAdapter(Context context, List<Result> ChannelList, String status) {
        this.ChannelList = ChannelList;
        this.mcontext = context;
        this.status = status;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.audio_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txt_episode_title.setText("" + ChannelList.get(position).getAudioName());
        try {
            Picasso.with(mcontext).load(ChannelList.get(position).getAudioImage()).priority(HIGH)
                    .resize(700, 700).centerInside().into(holder.iv_thumb);
        } catch (Exception e) {
            Log.e("Exception", "" + e.getMessage());
        }

        holder.iv_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("click", "call" + position);
                Intent intent = new Intent(mcontext, AudioDetailsActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("id", ChannelList.get(position).getId());
                intent.putExtra("desc", ChannelList.get(position).getAudioDesc());
                intent.putExtra("image", ChannelList.get(position).getAudioImage());
                intent.putExtra("name", ChannelList.get(position).getAudioName());
                intent.putExtra("url", ChannelList.get(position).getAudioUrl());
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
