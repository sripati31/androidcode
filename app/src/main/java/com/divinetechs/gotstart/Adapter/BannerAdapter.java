package com.divinetechs.gotstart.Adapter;

import android.content.Context;
import android.content.Intent;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

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

public class BannerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Result> BannerList;

    public BannerAdapter(Context context, List<Result> BannerList) {
        this.context = context;
        this.BannerList = BannerList;
    }

    @Override
    public int getCount() {
        if (BannerList.size() > 5)
            return 5;
        else
            return BannerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.banner_item, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.iv_thumb);
        TextView txt_count = (TextView) view.findViewById(R.id.txt_count);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        TextView txt_category = (TextView) view.findViewById(R.id.txt_category);
        TextView txt_play = (TextView) view.findViewById(R.id.txt_play);
        TextView txt_premium = (TextView) view.findViewById(R.id.txt_premium);

        txt_title.setText("" + BannerList.get(position).getTvvName());
        txt_count.setText("" + (position + 1) + "/" + BannerList.size());
        txt_category.setText("" + BannerList.get(position).getCat_name());

        if (BannerList.get(position).getIspremium().equalsIgnoreCase("1"))
            txt_premium.setVisibility(View.VISIBLE);
        else
            txt_premium.setVisibility(View.GONE);

        Picasso.with(context).load(BannerList.get(position).getTvvThumbnail()).priority(HIGH)
                .resize(700, 1000).centerInside().into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "call");
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("tvs_id", BannerList.get(position).getTvvId());
                intent.putExtra("tvs_name", BannerList.get(position).getTvvName());
                intent.putExtra("position", position);
                intent.putExtra("fc_id", BannerList.get(position).getFc_id());
                intent.putExtra("type", BannerList.get(position).getV_type());
                context.startActivity(intent);
            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}