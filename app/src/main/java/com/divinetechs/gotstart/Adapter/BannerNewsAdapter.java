package com.divinetechs.gotstart.Adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.divinetechs.gotstart.Model.TvVideoModel.Result;
import com.divinetechs.gotstart.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.squareup.picasso.Picasso.Priority.HIGH;

public class BannerNewsAdapter extends RecyclerView.Adapter<BannerNewsAdapter.MyViewHolder> {

    private List<Result> PopularList;
    Context mcontext;
    String status;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_count, txt_title;
        ImageView imageView, iv_small_icon;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.iv_thumb);
            iv_small_icon = (ImageView) view.findViewById(R.id.iv_small_icon);
            txt_count = (TextView) view.findViewById(R.id.txt_count);
            txt_title = (TextView) view.findViewById(R.id.txt_title);
        }
    }

    public BannerNewsAdapter(Context context, List<Result> PopularList, String status) {
        this.PopularList = PopularList;
        this.mcontext = context;
        this.status = status;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.banner_new_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txt_title.setText("" + PopularList.get(position).getTvvName());
        holder.txt_count.setText("" + (position + 1) + "/" + PopularList.size());

        Picasso.with(mcontext).load(PopularList.get(position).getTvvThumbnail()).priority(HIGH)
                .resize(700, 1000).centerInside().into(holder.imageView);

        Picasso.with(mcontext).load(PopularList.get(position).getTvvThumbnail()).priority(HIGH)
                .resize(100, 100).centerInside().into(holder.iv_small_icon);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "call");
//                Intent intent = new Intent(mcontext, DetailsActivity.class);
//                intent.putExtra("tvs_id", PopularList.get(position).getTvsId());
//                intent.putExtra("tvs_name", PopularList.get(position).getTvsName());
//                intent.putExtra("position", position);
//                intent.putExtra("fc_id", PopularList.get(position).getFcId());
//                mcontext.startActivity(intent);
//
//                if (status.equalsIgnoreCase("episode")) {
//                    ((Activity) mcontext).finish();
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return PopularList.size();
    }

}
