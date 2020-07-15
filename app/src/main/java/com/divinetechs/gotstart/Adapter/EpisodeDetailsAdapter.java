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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.divinetechs.gotstart.Activity.DetailsActivity;
import com.divinetechs.gotstart.Interface.Detailsdata;
import com.divinetechs.gotstart.Model.TvVideoModel.Result;
import com.divinetechs.gotstart.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.squareup.picasso.Picasso.Priority.HIGH;

public class EpisodeDetailsAdapter extends RecyclerView.Adapter<EpisodeDetailsAdapter.MyViewHolder> {

    private List<Result> PopularList;
    Context mcontext;
    String status;
    Detailsdata detailsdata;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_episode_title, txt_episode_date;
        ImageView iv_thumb, iv_copy;
        LinearLayout ly_main;

        public MyViewHolder(View view) {
            super(view);
            iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
            txt_episode_title = (TextView) view.findViewById(R.id.txt_episode_title);
            txt_episode_date = (TextView) view.findViewById(R.id.txt_episode_date);
            ly_main = (LinearLayout) view.findViewById(R.id.ly_main);
        }
    }

    public EpisodeDetailsAdapter(Context context, List<Result> PopularList, String status,
                                 Detailsdata detailsdata) {
        this.PopularList = PopularList;
        this.mcontext = context;
        this.status = status;
        this.detailsdata=detailsdata;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (status.equalsIgnoreCase("episode")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.episode_details_item_land, parent, false);
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
            holder.txt_episode_title.setText("" + PopularList.get(position).getTvvName());
            holder.txt_episode_date.setText("" + PopularList.get(position).getTvvDate());
            try {
                Picasso.with(mcontext).load(PopularList.get(position).getTvvThumbnail()).priority(HIGH)
                        .resize(700, 700).centerInside().into(holder.iv_thumb);
            } catch (Exception e) {
                Log.e("Exception", "" + e.getMessage());
            }
        } else {
            holder.txt_episode_title.setText("" + PopularList.get(position).getTvvName());
            try {
                Picasso.with(mcontext).load(PopularList.get(position).getTvvThumbnail()).priority(HIGH)
                        .resize(700, 700).centerInside().into(holder.iv_thumb);
            } catch (Exception e) {
                Log.e("Exception", "" + e.getMessage());
            }
        }

        holder.ly_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("click", "call" + position);
//                Intent intent = new Intent(mcontext, DetailsActivity.class);
//                intent.putExtra("tvs_id", PopularList.get(position).getTvsId());
//                intent.putExtra("tvs_name", PopularList.get(position).getTvsName());
//                intent.putExtra("position", position);
//                intent.putExtra("fc_id", PopularList.get(position).getFc_id());
//                intent.putExtra("tvv_id", PopularList.get(position).getTvvId());
//                mcontext.startActivity(intent);

                detailsdata.item_click(position,"");

                if (status.equalsIgnoreCase("episode")) {
//                    ((Activity) mcontext).finish();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return PopularList.size();
    }

}
