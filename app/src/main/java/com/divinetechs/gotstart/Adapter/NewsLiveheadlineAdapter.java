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

import com.divinetechs.gotstart.Activity.DetailsActivity;
import com.divinetechs.gotstart.Model.TvVideoModel.Result;
import com.divinetechs.gotstart.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.squareup.picasso.Picasso.Priority.HIGH;

public class NewsLiveheadlineAdapter extends RecyclerView.Adapter<NewsLiveheadlineAdapter.MyViewHolder> {

    private List<Result> PopularList;
    Context mcontext;
    String status;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_new_title, txt_date, txt_description;
        ImageView iv_thumb, iv_news_thumb;

        public MyViewHolder(View view) {
            super(view);
            iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
            txt_new_title = (TextView) view.findViewById(R.id.txt_new_title);
            txt_date = (TextView) view.findViewById(R.id.txt_date);
            txt_description = (TextView) view.findViewById(R.id.txt_description);
            iv_news_thumb = (ImageView) view.findViewById(R.id.iv_news_thumb);
        }
    }

    public NewsLiveheadlineAdapter(Context context, List<Result> PopularList, String status) {
        this.PopularList = PopularList;
        this.mcontext = context;
        this.status = status;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (status.equalsIgnoreCase("episode")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_live_item, parent, false);
            return new MyViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_item_land, parent, false);
            return new MyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        if (status.equalsIgnoreCase("episode")) {
            holder.txt_new_title.setText("" + PopularList.get(position).getTvvName());
            try {
                Picasso.with(mcontext).load(PopularList.get(position).getTvvThumbnail()).priority(HIGH)
                        .resize(700, 700).centerInside().into(holder.iv_thumb);

                Picasso.with(mcontext).load(PopularList.get(position).getTvsImage()).priority(HIGH)
                        .resize(700, 700).centerInside().into(holder.iv_news_thumb);

            } catch (Exception e) {
                Log.e("Exception", "" + e.getMessage());
            }
        } else {
            holder.txt_new_title.setText("" + PopularList.get(position).getTvvName());
            holder.txt_description.setText("" + PopularList.get(position).getTvvdescription());
            try {
                Picasso.with(mcontext).load(PopularList.get(position).getTvvThumbnail()).priority(HIGH)
                        .resize(700, 700).centerInside().into(holder.iv_thumb);

                Picasso.with(mcontext).load(PopularList.get(position).getTvsImage()).priority(HIGH)
                        .resize(700, 700).centerInside().into(holder.iv_news_thumb);
            } catch (Exception e) {
                Log.e("Exception", "" + e.getMessage());
            }
        }

        holder.iv_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("click", "call" + position);
                Intent intent = new Intent(mcontext, DetailsActivity.class);
                intent.putExtra("tvs_id", PopularList.get(position).getTvvId());
                intent.putExtra("tvs_name", PopularList.get(position).getTvvName());
                intent.putExtra("position", position);
                intent.putExtra("fc_id", PopularList.get(position).getFc_id());
                intent.putExtra("type", PopularList.get(position).getV_type());
                mcontext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return PopularList.size();
    }

}
