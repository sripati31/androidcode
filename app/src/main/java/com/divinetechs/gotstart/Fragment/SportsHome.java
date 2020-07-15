package com.divinetechs.gotstart.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.divinetechs.gotstart.Adapter.BannerAdapter;
import com.divinetechs.gotstart.Adapter.PopularAdapter;
import com.divinetechs.gotstart.Adapter.PopularMSNAdapter;
import com.divinetechs.gotstart.Model.TvSerialModel.TvSerialModel;
import com.divinetechs.gotstart.Model.TvVideoModel.Result;
import com.divinetechs.gotstart.Model.TvVideoModel.TvVideoModel;
import com.divinetechs.gotstart.R;
import com.divinetechs.gotstart.Utility.PrefManager;
import com.divinetechs.gotstart.Webservice.AppAPI;
import com.divinetechs.gotstart.Webservice.BaseURL;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SportsHome extends Fragment {
    PrefManager prefManager;
    ProgressDialog progressDialog;
    ViewPager mViewPager;
    BannerAdapter bannerAdapter;
    List<Result> BannerList;
    Timer timer;

    RecyclerView recycler_top_pick;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> TopPickList;
    PopularMSNAdapter popularAdapter;

    RecyclerView recycler_popular_show;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> PopularShowList;

    RecyclerView recycler_popular_drama;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> PopularDramaList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sportsfragment, container, false);

        prefManager = new PrefManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        mViewPager = root.findViewById(R.id.viewPager);

        getActivity().setTitle(""+prefManager.getValue("app_name"));

        recycler_top_pick = (RecyclerView) root.findViewById(R.id.recycler_top_pick);
        recycler_popular_show = (RecyclerView) root.findViewById(R.id.recycler_popular_show);
        recycler_popular_drama = (RecyclerView) root.findViewById(R.id.recycler_popular_drama);

        Get_banner();

        Get_Top_Pick_For_You();

//      popular show
        popularshow();

//      for Popular in Drama
        popular_in("1");

        return root;
    }

    @Override
    public void onDestroy() {
        if (timer != null)
            timer.cancel();
        super.onDestroy();
    }

    private void Get_banner() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<TvVideoModel> call = bookNPlayAPI.Banner("sport");
        call.enqueue(new Callback<TvVideoModel>() {
            @Override
            public void onResponse(Call<TvVideoModel> call, Response<TvVideoModel> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    BannerList = new ArrayList<>();
                    BannerList = response.body().getResult();
                    Log.e("==>BannerList", "" + BannerList.size());
                    SetBanner();
                }
            }

            @Override
            public void onFailure(Call<TvVideoModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    public void SetBanner() {
        bannerAdapter = new BannerAdapter(getActivity(), BannerList);
        mViewPager.setAdapter(bannerAdapter);
        if (BannerList.size() > 0) {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    mViewPager.post(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem((mViewPager.getCurrentItem() + 1) % BannerList.size());
                        }
                    });
                }
            };
            timer = new Timer();
            timer.schedule(timerTask, 10000, 10000);
        }
    }

    private void Get_Top_Pick_For_You() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<TvVideoModel> call = bookNPlayAPI.top_pick_for_you_msn("sport");
        call.enqueue(new Callback<TvVideoModel>() {
            @Override
            public void onResponse(Call<TvVideoModel> call, Response<TvVideoModel> response) {
                if (response.code() == 200) {

                    TopPickList = new ArrayList<>();
                    TopPickList = response.body().getResult();
                    Log.e("TredingList", "" + TopPickList.size());
                    popularAdapter = new PopularMSNAdapter(getActivity(), TopPickList, "episode");
                    recycler_top_pick.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                            LinearLayoutManager.HORIZONTAL, false);
                    recycler_top_pick.setLayoutManager(mLayoutManager3);
                    recycler_top_pick.setItemAnimator(new DefaultItemAnimator());
                    recycler_top_pick.setAdapter(popularAdapter);
                    popularAdapter.notifyDataSetChanged();

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<TvVideoModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void popularshow() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<TvVideoModel> call = bookNPlayAPI.popularshow_MSN("sport");
        call.enqueue(new Callback<TvVideoModel>() {
            @Override
            public void onResponse(Call<TvVideoModel> call, Response<TvVideoModel> response) {
                if (response.code() == 200) {

                    PopularShowList = new ArrayList<>();
                    PopularShowList = response.body().getResult();
                    Log.e("popularshowList", "" + PopularShowList.size());
                    popularAdapter = new PopularMSNAdapter(getActivity(), PopularShowList, "episode");
                    recycler_popular_show.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                            LinearLayoutManager.HORIZONTAL, false);
                    recycler_popular_show.setLayoutManager(mLayoutManager3);
                    recycler_popular_show.setItemAnimator(new DefaultItemAnimator());
                    recycler_popular_show.setAdapter(popularAdapter);
                    popularAdapter.notifyDataSetChanged();

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<TvVideoModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void popular_in(String CatID) {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<TvVideoModel> call = bookNPlayAPI.popular_in_msn(CatID, "sport");
        call.enqueue(new Callback<TvVideoModel>() {
            @Override
            public void onResponse(Call<TvVideoModel> call, Response<TvVideoModel> response) {
                if (response.code() == 200) {

                    if (CatID.equalsIgnoreCase("1")) {
                        PopularDramaList = new ArrayList<>();
                        PopularDramaList = response.body().getResult();
                        Log.e("populardramaList", "" + PopularDramaList.size());
                        popularAdapter = new PopularMSNAdapter(getActivity(), PopularDramaList, "episode");
                        recycler_popular_drama.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                                LinearLayoutManager.HORIZONTAL, false);
                        recycler_popular_drama.setLayoutManager(mLayoutManager3);
                        recycler_popular_drama.setItemAnimator(new DefaultItemAnimator());
                        recycler_popular_drama.setAdapter(popularAdapter);
                        popularAdapter.notifyDataSetChanged();
                    }
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
