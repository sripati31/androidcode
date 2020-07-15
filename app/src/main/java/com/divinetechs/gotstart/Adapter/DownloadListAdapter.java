package com.divinetechs.gotstart.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.divinetechs.gotstart.Activity.ChannelDetailsActivity;
import com.divinetechs.gotstart.Activity.DetailsActivity;
import com.divinetechs.gotstart.Interface.Watchinter;
import com.divinetechs.gotstart.Model.DownloadModel.Result;
import com.divinetechs.gotstart.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.squareup.picasso.Picasso.Priority.HIGH;

public class DownloadListAdapter extends RecyclerView.Adapter<DownloadListAdapter.MyViewHolder> {

    private List<Result> DownloadList;
    Context mcontext;
    String status;
    Watchinter watchinter;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_episode_title, txt_dot;
        ImageView iv_thumb, iv_copy;
        CardView card_view;

        public MyViewHolder(View view) {
            super(view);
            iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
            txt_episode_title = (TextView) view.findViewById(R.id.txt_episode_title);
            txt_dot = (TextView) view.findViewById(R.id.txt_dot);
            card_view = (CardView) view.findViewById(R.id.card_view);
        }
    }

    public DownloadListAdapter(Context context, List<Result> DownloadList, String status,
                               Watchinter watchinter) {
        this.DownloadList = DownloadList;
        this.mcontext = context;
        this.status = status;
        this.watchinter = watchinter;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (status.equalsIgnoreCase("episode")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.download_item_land, parent, false);
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
            holder.txt_episode_title.setText("" + DownloadList.get(position).getTvvName());
            try {
                Picasso.with(mcontext).load(DownloadList.get(position).getTvvThumbnail()
                        .replace("localhost", "192.168.0.128")).priority(HIGH)
                        .resize(700, 700).centerInside().into(holder.iv_thumb);
            } catch (Exception e) {
                Log.e("Exception", "" + e.getMessage());
            }
        } else {
            holder.txt_episode_title.setText("" + DownloadList.get(position).getTvsName());
            try {
                Picasso.with(mcontext).load(DownloadList.get(position).getTvsImage()
                        .replace("localhost", "192.168.0.128")).priority(HIGH)
                        .resize(700, 700).centerInside().into(holder.iv_thumb);
            } catch (Exception e) {
                Log.e("Exception", "" + e.getMessage());
            }
        }

        holder.iv_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("click", "call" + position);

                if (DownloadList.get(position).getTvvVideoType().equalsIgnoreCase("Server Video")) {
                    Intent intent = new Intent(mcontext, DetailsActivity.class);
                    intent.putExtra("tvs_id", DownloadList.get(position).getTvsId());
                    intent.putExtra("tvs_name", DownloadList.get(position).getTvvName());
                    intent.putExtra("position", position);
                    intent.putExtra("fc_id", DownloadList.get(position).getFcId());
                    mcontext.startActivity(intent);
                } else if (DownloadList.get(position).getType().equalsIgnoreCase("channel")) {
//                    Intent intent = new Intent(mcontext, ChannelDetailsActivity.class);
//                    intent.putExtra("position", position);
//                    intent.putExtra("id", DownloadList.get(position).getCId());
//                    intent.putExtra("name", DownloadList.get(position).getTvvName());
//                    intent.putExtra("desc", DownloadList.get(position).getTvvDescription());
//                    intent.putExtra("url", DownloadList.get(position).getTvvVideoUrl());
//                    intent.putExtra("image", DownloadList.get(position).getTvvThumbnail());
//                    mcontext.startActivity(intent);
                } else if (DownloadList.get(position).getType().equalsIgnoreCase("audio")) {
//                    Intent intent = new Intent(mcontext, AudioDetailsActivity.class);
//                    intent.putExtra("position", position);
//                    intent.putExtra("id", PopularList.get(position).getAId());
//                    intent.putExtra("audio_name", PopularList.get(position).getTvvName());
//                    intent.putExtra("desc", PopularList.get(position).getTvvDescription());
//                    intent.putExtra("url", PopularList.get(position).getTvvVideoUrl());
//                    intent.putExtra("image", PopularList.get(position).getTvvThumbnail());
//                    mcontext.startActivity(intent);
                }

                if (status.equalsIgnoreCase("episode")) {
//                    ((Activity) mcontext).finish();
                }
            }
        });

        holder.txt_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("w_id", "" + DownloadList.get(position).getDId());
                watchinter.remove_item(DownloadList.get(position).getDId());

            }
        });

    }

    @Override
    public int getItemCount() {
        return DownloadList.size();
    }

}
