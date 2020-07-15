package com.divinetechs.gotstart.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.divinetechs.gotstart.Adapter.NewsLiveAdapter;
import com.divinetechs.gotstart.Adapter.NewsLiveheadlineAdapter;
import com.divinetechs.gotstart.Model.TvSerialModel.TvSerialModel;
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

public class NewsHome extends Fragment {
    PrefManager prefManager;
    ProgressDialog progressDialog;

    RecyclerView recycler_live_news, recycler_all_news;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> TopPickList;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> NewsList;
    NewsLiveAdapter newsLiveAdapter;
    NewsLiveheadlineAdapter newsLiveheadlineAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.newsfragment, container, false);

        prefManager = new PrefManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        getActivity().setTitle(""+prefManager.getValue("app_name"));

        recycler_live_news = (RecyclerView) root.findViewById(R.id.recycler_live_news);
        recycler_all_news = (RecyclerView) root.findViewById(R.id.recycler_all_news);

        Get_Top_Pick_For_You();

        Get_All_news();

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void Get_Top_Pick_For_You() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<TvVideoModel> call = bookNPlayAPI.top_pick_for_you_news("news");
        call.enqueue(new Callback<TvVideoModel>() {
            @Override
            public void onResponse(Call<TvVideoModel> call, Response<TvVideoModel> response) {
                if (response.code() == 200) {

                    TopPickList = new ArrayList<>();
                    TopPickList = response.body().getResult();
                    Log.e("Live News", "" + TopPickList.size());
                    newsLiveAdapter = new NewsLiveAdapter(getActivity(), TopPickList, "episode");
                    recycler_live_news.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                            LinearLayoutManager.HORIZONTAL, false);
                    recycler_live_news.setLayoutManager(mLayoutManager3);
                    recycler_live_news.setItemAnimator(new DefaultItemAnimator());
                    recycler_live_news.setAdapter(newsLiveAdapter);
                    newsLiveAdapter.notifyDataSetChanged();

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<TvVideoModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void Get_All_news() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<TvVideoModel> call = bookNPlayAPI.get_all_news("news");
        call.enqueue(new Callback<TvVideoModel>() {
            @Override
            public void onResponse(Call<TvVideoModel> call, Response<TvVideoModel> response) {
                if (response.code() == 200) {

                    NewsList = new ArrayList<>();
                    NewsList = response.body().getResult();
                    Log.e("All News", "" + NewsList.size());
                    newsLiveheadlineAdapter = new NewsLiveheadlineAdapter(getActivity(), NewsList, "");
                    recycler_all_news.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                            LinearLayoutManager.VERTICAL, false);
                    recycler_all_news.setLayoutManager(mLayoutManager3);
                    recycler_all_news.setItemAnimator(new DefaultItemAnimator());
                    recycler_all_news.setAdapter(newsLiveheadlineAdapter);
                    newsLiveheadlineAdapter.notifyDataSetChanged();

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
