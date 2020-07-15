package com.divinetechs.gotstart.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.divinetechs.gotstart.Activity.ViewAllActivity;
import com.divinetechs.gotstart.Activity.ViewAllPremiumActivity;
import com.divinetechs.gotstart.Adapter.NewsLiveAdapter;
import com.divinetechs.gotstart.Adapter.NewsLiveheadlineAdapter;
import com.divinetechs.gotstart.Adapter.PremiumAdapter;
import com.divinetechs.gotstart.Adapter.SubscriptionAdapter;
import com.divinetechs.gotstart.Model.TvVideoModel.TvVideoModel;
import com.divinetechs.gotstart.R;
import com.divinetechs.gotstart.Utility.PrefManager;
import com.divinetechs.gotstart.Webservice.AppAPI;
import com.divinetechs.gotstart.Webservice.BaseURL;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Premium extends Fragment {
    PrefManager prefManager;
    ProgressDialog progressDialog;

    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> TvPremiumList;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> MoviePremiumList;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> SportPremiumList;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> NewsPremiumList;

    PremiumAdapter premiumAdapter;

    RecyclerView recycler_tv, recycler_movie, recycler_sport, recycler_news;
    LinearLayout txt_viewall_tv, txt_viewall_movie, txt_viewall_sport, txt_viewall_news;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.premiumfragment, container, false);

        prefManager = new PrefManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        getActivity().setTitle("" + prefManager.getValue("app_name"));

        recycler_tv = (RecyclerView) root.findViewById(R.id.recycler_tv);
        recycler_movie = (RecyclerView) root.findViewById(R.id.recycler_movie);
        recycler_sport = (RecyclerView) root.findViewById(R.id.recycler_sport);
        recycler_news = (RecyclerView) root.findViewById(R.id.recycler_news);

        txt_viewall_tv = (LinearLayout) root.findViewById(R.id.txt_viewall_tv);
        txt_viewall_movie = (LinearLayout) root.findViewById(R.id.txt_viewall_movie);
        txt_viewall_sport = (LinearLayout) root.findViewById(R.id.txt_viewall_sport);
        txt_viewall_news = (LinearLayout) root.findViewById(R.id.txt_viewall_news);

        txt_viewall_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.DataList_video = TvPremiumList;
                Intent intent = new Intent(getActivity(), ViewAllPremiumActivity.class);
                intent.putExtra("title", "Premium Tv Show");
                startActivity(intent);
            }
        });

        txt_viewall_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.DataList_video = MoviePremiumList;
                Intent intent = new Intent(getActivity(), ViewAllPremiumActivity.class);
                intent.putExtra("title", "Premium Movie");
                startActivity(intent);
            }
        });

        txt_viewall_sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.DataList_video = SportPremiumList;
                Intent intent = new Intent(getActivity(), ViewAllPremiumActivity.class);
                intent.putExtra("title", "Premium Sport");
                startActivity(intent);
            }
        });

        txt_viewall_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.DataList_video = NewsPremiumList;
                Intent intent = new Intent(getActivity(), ViewAllPremiumActivity.class);
                intent.putExtra("title", "Premium News");
                startActivity(intent);
            }
        });

        Get_TV_Premium();

        Get_Movie_Premium();

        Get_Sport_Premium();

        Get_News_Premium();

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void Get_TV_Premium() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<TvVideoModel> call = bookNPlayAPI.premium_video("tv");
        call.enqueue(new Callback<TvVideoModel>() {
            @Override
            public void onResponse(Call<TvVideoModel> call, Response<TvVideoModel> response) {
                if (response.code() == 200) {

                    TvPremiumList = new ArrayList<>();
                    TvPremiumList = response.body().getResult();
                    Log.e("TvPremiumList", "" + TvPremiumList.size());

                    premiumAdapter = new PremiumAdapter(getActivity(), TvPremiumList, "episode");
                    recycler_tv.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                            LinearLayoutManager.HORIZONTAL, false);
                    recycler_tv.setLayoutManager(mLayoutManager3);
                    recycler_tv.setItemAnimator(new DefaultItemAnimator());
                    recycler_tv.setAdapter(premiumAdapter);
                    premiumAdapter.notifyDataSetChanged();

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<TvVideoModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void Get_Movie_Premium() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<TvVideoModel> call = bookNPlayAPI.premium_video("movie");
        call.enqueue(new Callback<TvVideoModel>() {
            @Override
            public void onResponse(Call<TvVideoModel> call, Response<TvVideoModel> response) {
                if (response.code() == 200) {

                    MoviePremiumList = new ArrayList<>();
                    MoviePremiumList = response.body().getResult();
                    Log.e("MoviePremiumList", "" + MoviePremiumList.size());

                    premiumAdapter = new PremiumAdapter(getActivity(), MoviePremiumList, "episode");
                    recycler_movie.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                            LinearLayoutManager.HORIZONTAL, false);
                    recycler_movie.setLayoutManager(mLayoutManager3);
                    recycler_movie.setItemAnimator(new DefaultItemAnimator());
                    recycler_movie.setAdapter(premiumAdapter);
                    premiumAdapter.notifyDataSetChanged();

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<TvVideoModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void Get_Sport_Premium() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<TvVideoModel> call = bookNPlayAPI.premium_video("sport");
        call.enqueue(new Callback<TvVideoModel>() {
            @Override
            public void onResponse(Call<TvVideoModel> call, Response<TvVideoModel> response) {
                if (response.code() == 200) {

                    SportPremiumList = new ArrayList<>();
                    SportPremiumList = response.body().getResult();
                    Log.e("SportPremiumList", "" + SportPremiumList.size());

                    premiumAdapter = new PremiumAdapter(getActivity(), SportPremiumList, "episode");
                    recycler_sport.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                            LinearLayoutManager.HORIZONTAL, false);
                    recycler_sport.setLayoutManager(mLayoutManager3);
                    recycler_sport.setItemAnimator(new DefaultItemAnimator());
                    recycler_sport.setAdapter(premiumAdapter);
                    premiumAdapter.notifyDataSetChanged();

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<TvVideoModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void Get_News_Premium() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<TvVideoModel> call = bookNPlayAPI.premium_video("news");
        call.enqueue(new Callback<TvVideoModel>() {
            @Override
            public void onResponse(Call<TvVideoModel> call, Response<TvVideoModel> response) {
                if (response.code() == 200) {

                    NewsPremiumList = new ArrayList<>();
                    NewsPremiumList = response.body().getResult();
                    Log.e("NewsPremiumList", "" + NewsPremiumList.size());

                    premiumAdapter = new PremiumAdapter(getActivity(), NewsPremiumList, "episode");
                    recycler_news.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                            LinearLayoutManager.HORIZONTAL, false);
                    recycler_news.setLayoutManager(mLayoutManager3);
                    recycler_news.setItemAnimator(new DefaultItemAnimator());
                    recycler_news.setAdapter(premiumAdapter);
                    premiumAdapter.notifyDataSetChanged();

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<TvVideoModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}
