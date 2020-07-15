package com.divinetechs.gotstart.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.divinetechs.gotstart.Activity.ViewAllActivity;
import com.divinetechs.gotstart.Adapter.BannerAdapter;
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

public class MovieHome extends Fragment {
    PrefManager prefManager;
    ProgressDialog progressDialog;
    ViewPager mViewPager;
    BannerAdapter bannerAdapter;
    List<Result> BannerList;
    Timer timer;

    RecyclerView recycler_top_pick;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> TopPickList;
    PopularMSNAdapter popularMSNAdapter;

    RecyclerView recycler_popular_show;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> PopularShowList;

    RecyclerView recycler_popular_drama;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> PopularDramaList;

    RecyclerView recycler_popular_reality;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> PopularRealityList;

    RecyclerView recycler_popular_romance;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> PopularRomanceList;

    RecyclerView recycler_popular_family;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> PopularfamilyList;

    RecyclerView recycler_popular_action;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> PopularActionList;

    RecyclerView recycler_popular_crime;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> PopularCrimeList;

    RecyclerView recycler_popular_thriller;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> PopularThrillerList;

    RecyclerView recycler_popular_comedy;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> PopularComedyList;

    RecyclerView recycler_popular_kids;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> PopularKidsList;

    RecyclerView recycler_popular_biopic;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> PopularBiopicList;

    RecyclerView recycler_popular_documentary;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> PopularDocumentaryList;

    RecyclerView recycler_popular_teen;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> PopularTeenList;

    RecyclerView recycler_popular_talkshow;
    List<com.divinetechs.gotstart.Model.TvVideoModel.Result> PopularTalkshowList;

    LinearLayout txt_viewall_treding, txt_viewall_popular_show, txt_viewall_popular_drama, txt_viewall_popular_reality, txt_viewall_popular_romance,
            txt_viewall_popular_Family, txt_viewall_popular_action, txt_viewall_popular_crime,
            txt_viewall_popular_thriller, txt_viewall_popular_comedy, txt_viewall_popular_kids,
            txt_viewall_popular_biopic, txt_viewall_popular_documentary, txt_viewall_popular_teen,
            txt_viewall_popular_talkshow;

    LinearLayout ly_top_pick, ly_popular_show, ly_popular_drama, ly_popular_reality, ly_popular_romance,
            ly_popular_Family, ly_popular_action, ly_popular_crime, ly_popular_thriller, ly_popular_comedy,
            ly_popular_kids, ly_popular_biopic, ly_popular_documentary, ly_popular_teen,
            ly_popular_talkshow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.homefragment, container, false);

        prefManager = new PrefManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        mViewPager = root.findViewById(R.id.viewPager);

        getActivity().setTitle("" + prefManager.getValue("app_name"));

        recycler_top_pick = (RecyclerView) root.findViewById(R.id.recycler_top_pick);
        recycler_popular_show = (RecyclerView) root.findViewById(R.id.recycler_popular_show);
        recycler_popular_drama = (RecyclerView) root.findViewById(R.id.recycler_popular_drama);
        recycler_popular_reality = (RecyclerView) root.findViewById(R.id.recycler_popular_reality);
        recycler_popular_romance = (RecyclerView) root.findViewById(R.id.recycler_popular_romance);
        recycler_popular_family = (RecyclerView) root.findViewById(R.id.recycler_popular_Family);
        recycler_popular_action = (RecyclerView) root.findViewById(R.id.recycler_popular_action);

        recycler_popular_crime = (RecyclerView) root.findViewById(R.id.recycler_popular_crime);
        recycler_popular_thriller = (RecyclerView) root.findViewById(R.id.recycler_popular_thriller);
        recycler_popular_comedy = (RecyclerView) root.findViewById(R.id.recycler_popular_comedy);
        recycler_popular_kids = (RecyclerView) root.findViewById(R.id.recycler_popular_kids);
        recycler_popular_biopic = (RecyclerView) root.findViewById(R.id.recycler_popular_biopic);
        recycler_popular_documentary = (RecyclerView) root.findViewById(R.id.recycler_popular_documentary);
        recycler_popular_teen = (RecyclerView) root.findViewById(R.id.recycler_popular_teen);
        recycler_popular_talkshow = (RecyclerView) root.findViewById(R.id.recycler_popular_talkshow);

        ly_top_pick = (LinearLayout) root.findViewById(R.id.ly_top_pick);
        ly_popular_show = (LinearLayout) root.findViewById(R.id.ly_popular_show);
        ly_popular_drama = (LinearLayout) root.findViewById(R.id.ly_popular_drama);
        ly_popular_reality = (LinearLayout) root.findViewById(R.id.ly_popular_reality);
        ly_popular_romance = (LinearLayout) root.findViewById(R.id.ly_popular_romance);
        ly_popular_Family = (LinearLayout) root.findViewById(R.id.ly_popular_Family);
        ly_popular_action = (LinearLayout) root.findViewById(R.id.ly_popular_action);
        ly_popular_crime = (LinearLayout) root.findViewById(R.id.ly_popular_crime);
        ly_popular_thriller = (LinearLayout) root.findViewById(R.id.ly_popular_thriller);
        ly_popular_comedy = (LinearLayout) root.findViewById(R.id.ly_popular_comedy);
        ly_popular_kids = (LinearLayout) root.findViewById(R.id.ly_popular_kids);
        ly_popular_biopic = (LinearLayout) root.findViewById(R.id.ly_popular_biopic);
        ly_popular_documentary = (LinearLayout) root.findViewById(R.id.ly_popular_documentary);
        ly_popular_teen = (LinearLayout) root.findViewById(R.id.ly_popular_teen);
        ly_popular_talkshow = (LinearLayout) root.findViewById(R.id.ly_popular_talkshow);

        txt_viewall_treding = (LinearLayout) root.findViewById(R.id.txt_viewall_treding);
        txt_viewall_treding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.DataList_video = TopPickList;
                Intent intent = new Intent(getActivity(), ViewAllActivity.class);
                intent.putExtra("title", "Top Picks For You");
                startActivity(intent);
            }
        });

        txt_viewall_popular_show = (LinearLayout) root.findViewById(R.id.txt_viewall_popular_show);
        txt_viewall_popular_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.DataList_video = PopularShowList;
                Intent intent = new Intent(getActivity(), ViewAllActivity.class);
                intent.putExtra("title", "Popular Show");
                startActivity(intent);
            }
        });

        txt_viewall_popular_drama = (LinearLayout) root.findViewById(R.id.txt_viewall_popular_drama);
        txt_viewall_popular_drama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.DataList_video = PopularDramaList;
                Intent intent = new Intent(getActivity(), ViewAllActivity.class);
                intent.putExtra("title", "Popular in Drama");
                startActivity(intent);
            }
        });

        txt_viewall_popular_reality = (LinearLayout) root.findViewById(R.id.txt_viewall_popular_reality);
        txt_viewall_popular_reality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.DataList_video = PopularRealityList;
                Intent intent = new Intent(getActivity(), ViewAllActivity.class);
                intent.putExtra("title", "Popular in Reality");
                startActivity(intent);
            }
        });

        txt_viewall_popular_romance = (LinearLayout) root.findViewById(R.id.txt_viewall_popular_romance);
        txt_viewall_popular_romance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.DataList_video = PopularRomanceList;
                Intent intent = new Intent(getActivity(), ViewAllActivity.class);
                intent.putExtra("title", "Popular in Romance");
                startActivity(intent);
            }
        });

        txt_viewall_popular_Family = (LinearLayout) root.findViewById(R.id.txt_viewall_popular_Family);
        txt_viewall_popular_Family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.DataList_video = PopularfamilyList;
                Intent intent = new Intent(getActivity(), ViewAllActivity.class);
                intent.putExtra("title", "Popular in Family");
                startActivity(intent);
            }
        });

        txt_viewall_popular_action = (LinearLayout) root.findViewById(R.id.txt_viewall_popular_action);
        txt_viewall_popular_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.DataList_video = PopularActionList;
                Intent intent = new Intent(getActivity(), ViewAllActivity.class);
                intent.putExtra("title", "Popular in Action");
                startActivity(intent);
            }
        });

        txt_viewall_popular_crime = (LinearLayout) root.findViewById(R.id.txt_viewall_popular_crime);
        txt_viewall_popular_crime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.DataList_video = PopularCrimeList;
                Intent intent = new Intent(getActivity(), ViewAllActivity.class);
                intent.putExtra("title", "Popular in Crime");
                startActivity(intent);
            }
        });

        txt_viewall_popular_thriller = (LinearLayout) root.findViewById(R.id.txt_viewall_popular_thriller);
        txt_viewall_popular_thriller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.DataList_video = PopularThrillerList;
                Intent intent = new Intent(getActivity(), ViewAllActivity.class);
                intent.putExtra("title", "Popular in Thriller");
                startActivity(intent);
            }
        });

        txt_viewall_popular_comedy = (LinearLayout) root.findViewById(R.id.txt_viewall_popular_comedy);
        txt_viewall_popular_comedy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.DataList_video = PopularComedyList;
                Intent intent = new Intent(getActivity(), ViewAllActivity.class);
                intent.putExtra("title", "Popular in Comedy");
                startActivity(intent);
            }
        });

        txt_viewall_popular_kids = (LinearLayout) root.findViewById(R.id.txt_viewall_popular_kids);
        txt_viewall_popular_kids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.DataList_video = PopularKidsList;
                Intent intent = new Intent(getActivity(), ViewAllActivity.class);
                intent.putExtra("title", "Popular in Kids");
                startActivity(intent);
            }
        });

        txt_viewall_popular_biopic = (LinearLayout) root.findViewById(R.id.txt_viewall_popular_biopic);
        txt_viewall_popular_biopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.DataList_video = PopularBiopicList;
                Intent intent = new Intent(getActivity(), ViewAllActivity.class);
                intent.putExtra("title", "Popular in Biopic");
                startActivity(intent);
            }
        });

        txt_viewall_popular_documentary = (LinearLayout) root.findViewById(R.id.txt_viewall_popular_documentary);
        txt_viewall_popular_documentary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.DataList_video = PopularDocumentaryList;
                Intent intent = new Intent(getActivity(), ViewAllActivity.class);
                intent.putExtra("title", "Popular in Documentary");
                startActivity(intent);
            }
        });

        txt_viewall_popular_teen = (LinearLayout) root.findViewById(R.id.txt_viewall_popular_teen);
        txt_viewall_popular_teen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.DataList_video = PopularTeenList;
                Intent intent = new Intent(getActivity(), ViewAllActivity.class);
                intent.putExtra("title", "Popular in Teen");
                startActivity(intent);
            }
        });

        txt_viewall_popular_talkshow = (LinearLayout) root.findViewById(R.id.txt_viewall_popular_talkshow);
        txt_viewall_popular_talkshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.DataList_video = PopularTalkshowList;
                Intent intent = new Intent(getActivity(), ViewAllActivity.class);
                intent.putExtra("title", "Popular in Talkshow");
                startActivity(intent);
            }
        });

        Get_banner();
//
        Get_Top_Pick_For_You();

//      popular show
        popularshow();

//      for Popular in Drama
        popular_in("1");
//      for Popular in Reality
        popular_in("2");

//      for Popular in Romance
        popular_in("3");
//
//      for Popular in family
        popular_in("4");

//      for Popular in Action
        popular_in("5");

//        for Popular in Crime
        popular_in("6");

//      for Popular in Thriller
        popular_in("7");

//      for Popular in Comedy
        popular_in("8");

//      for Popular in Kids
        popular_in("9");

//      for Popular in Biopic
        popular_in("10");

//      for Popular in Documentary
        popular_in("11");

//      for Popular in Teen
        popular_in("12");

//      for Popular in TalkShow
        popular_in("13");

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
        Call<TvVideoModel> call = bookNPlayAPI.Banner("movie");
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
        Call<TvVideoModel> call = bookNPlayAPI.top_pick_for_you_msn("movie");
        call.enqueue(new Callback<TvVideoModel>() {
            @Override
            public void onResponse(Call<TvVideoModel> call, Response<TvVideoModel> response) {
                if (response.code() == 200) {

                    TopPickList = new ArrayList<>();
                    TopPickList = response.body().getResult();
                    Log.e("TopPickList", "" + TopPickList.size());

                    if (TopPickList.size() > 0) {
                        PrefManager.DataList_video = TopPickList;
                        popularMSNAdapter = new PopularMSNAdapter(getActivity(), TopPickList, "episode");
                        recycler_top_pick.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                                LinearLayoutManager.HORIZONTAL, false);
                        recycler_top_pick.setLayoutManager(mLayoutManager3);
                        recycler_top_pick.setItemAnimator(new DefaultItemAnimator());
                        recycler_top_pick.setAdapter(popularMSNAdapter);
                        popularMSNAdapter.notifyDataSetChanged();
                        ly_top_pick.setVisibility(View.VISIBLE);
                    } else {
                        ly_top_pick.setVisibility(View.GONE);
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

    private void popularshow() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<TvVideoModel> call = bookNPlayAPI.popularshow_MSN("movie");
        call.enqueue(new Callback<TvVideoModel>() {
            @Override
            public void onResponse(Call<TvVideoModel> call, Response<TvVideoModel> response) {
                if (response.code() == 200) {

                    PopularShowList = new ArrayList<>();
                    PopularShowList = response.body().getResult();
                    Log.e("popularshowList", "" + PopularShowList.size());
                    if (PopularShowList.size() > 0) {
                        popularMSNAdapter = new PopularMSNAdapter(getActivity(), PopularShowList, "episode");
                        recycler_popular_show.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                                LinearLayoutManager.HORIZONTAL, false);
                        recycler_popular_show.setLayoutManager(mLayoutManager3);
                        recycler_popular_show.setItemAnimator(new DefaultItemAnimator());
                        recycler_popular_show.setAdapter(popularMSNAdapter);
                        popularMSNAdapter.notifyDataSetChanged();
                        ly_popular_show.setVisibility(View.VISIBLE);
                    } else {
                        ly_popular_show.setVisibility(View.GONE);
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

    private void popular_in(String CatID) {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<TvVideoModel> call = bookNPlayAPI.popular_in_msn(CatID, "movie");
        call.enqueue(new Callback<TvVideoModel>() {
            @Override
            public void onResponse(Call<TvVideoModel> call, Response<TvVideoModel> response) {
                if (response.code() == 200) {

                    if (CatID.equalsIgnoreCase("1")) {
                        PopularDramaList = new ArrayList<>();
                        PopularDramaList = response.body().getResult();
                        Log.e("populardramaList", "" + PopularDramaList.size());
                        if (PopularDramaList.size() > 0) {
                            popularMSNAdapter = new PopularMSNAdapter(getActivity(), PopularDramaList, "episode");
                            recycler_popular_drama.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                                    LinearLayoutManager.HORIZONTAL, false);
                            recycler_popular_drama.setLayoutManager(mLayoutManager3);
                            recycler_popular_drama.setItemAnimator(new DefaultItemAnimator());
                            recycler_popular_drama.setAdapter(popularMSNAdapter);
                            popularMSNAdapter.notifyDataSetChanged();
                            ly_popular_drama.setVisibility(View.VISIBLE);
                        } else {
                            ly_popular_drama.setVisibility(View.GONE);
                        }
                    } else if (CatID.equalsIgnoreCase("2")) {
                        Log.e("response=reality", "" + response.body().getResult());
                        PopularRealityList = new ArrayList<>();
                        PopularRealityList = response.body().getResult();
                        Log.e("PopularRealityList", "" + PopularRealityList.size());
                        if (PopularRealityList.size() > 0) {
                            popularMSNAdapter = new PopularMSNAdapter(getActivity(), PopularRealityList, "episode");
                            recycler_popular_reality.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                                    LinearLayoutManager.HORIZONTAL, false);
                            recycler_popular_reality.setLayoutManager(mLayoutManager3);
                            recycler_popular_reality.setItemAnimator(new DefaultItemAnimator());
                            recycler_popular_reality.setAdapter(popularMSNAdapter);
                            popularMSNAdapter.notifyDataSetChanged();
                            ly_popular_reality.setVisibility(View.VISIBLE);
                        } else {
                            ly_popular_reality.setVisibility(View.GONE);
                        }
                    } else if (CatID.equalsIgnoreCase("3")) {
                        PopularRomanceList = new ArrayList<>();
                        PopularRomanceList = response.body().getResult();
                        Log.e("PopularRomanceList", "" + PopularRomanceList.size());
                        if (PopularRomanceList.size() > 0) {
                            popularMSNAdapter = new PopularMSNAdapter(getActivity(), PopularRomanceList, "episode");
                            recycler_popular_romance.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                                    LinearLayoutManager.HORIZONTAL, false);
                            recycler_popular_romance.setLayoutManager(mLayoutManager3);
                            recycler_popular_romance.setItemAnimator(new DefaultItemAnimator());
                            recycler_popular_romance.setAdapter(popularMSNAdapter);
                            popularMSNAdapter.notifyDataSetChanged();
                            ly_popular_romance.setVisibility(View.VISIBLE);
                        } else {
                            ly_popular_romance.setVisibility(View.GONE);
                        }
                    } else if (CatID.equalsIgnoreCase("4")) {
                        PopularfamilyList = new ArrayList<>();
                        PopularfamilyList = response.body().getResult();
                        Log.e("PopularfamilyList", "" + PopularfamilyList.size());
                        if (PopularfamilyList.size() > 0) {
                            popularMSNAdapter = new PopularMSNAdapter(getActivity(), PopularfamilyList, "episode");
                            recycler_popular_family.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                                    LinearLayoutManager.HORIZONTAL, false);
                            recycler_popular_family.setLayoutManager(mLayoutManager3);
                            recycler_popular_family.setItemAnimator(new DefaultItemAnimator());
                            recycler_popular_family.setAdapter(popularMSNAdapter);
                            popularMSNAdapter.notifyDataSetChanged();
                            ly_popular_Family.setVisibility(View.VISIBLE);
                        } else {
                            ly_popular_Family.setVisibility(View.GONE);
                        }
                    } else if (CatID.equalsIgnoreCase("5")) {
                        PopularActionList = new ArrayList<>();
                        PopularActionList = response.body().getResult();
                        Log.e("PopularActionList", "" + PopularActionList.size());
                        if (PopularActionList.size() > 0) {
                            popularMSNAdapter = new PopularMSNAdapter(getActivity(), PopularActionList, "episode");
                            recycler_popular_action.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                                    LinearLayoutManager.HORIZONTAL, false);
                            recycler_popular_action.setLayoutManager(mLayoutManager3);
                            recycler_popular_action.setItemAnimator(new DefaultItemAnimator());
                            recycler_popular_action.setAdapter(popularMSNAdapter);
                            popularMSNAdapter.notifyDataSetChanged();
                            ly_popular_action.setVisibility(View.VISIBLE);
                        } else {
                            ly_popular_action.setVisibility(View.GONE);
                        }
                    } else if (CatID.equalsIgnoreCase("6")) {
                        PopularCrimeList = new ArrayList<>();
                        PopularCrimeList = response.body().getResult();
                        Log.e("PopularCrimeList", "" + PopularCrimeList.size());
                        if (PopularCrimeList.size() > 0) {
                            popularMSNAdapter = new PopularMSNAdapter(getActivity(), PopularCrimeList, "episode");
                            recycler_popular_crime.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                                    LinearLayoutManager.HORIZONTAL, false);
                            recycler_popular_crime.setLayoutManager(mLayoutManager3);
                            recycler_popular_crime.setItemAnimator(new DefaultItemAnimator());
                            recycler_popular_crime.setAdapter(popularMSNAdapter);
                            popularMSNAdapter.notifyDataSetChanged();
                            ly_popular_crime.setVisibility(View.VISIBLE);
                        } else {
                            ly_popular_crime.setVisibility(View.GONE);
                        }
                    } else if (CatID.equalsIgnoreCase("7")) {
                        PopularThrillerList = new ArrayList<>();
                        PopularThrillerList = response.body().getResult();
                        Log.e("PopularCrimeList", "" + PopularThrillerList.size());
                        if (PopularThrillerList.size() > 0) {
                            popularMSNAdapter = new PopularMSNAdapter(getActivity(), PopularThrillerList, "episode");
                            recycler_popular_thriller.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                                    LinearLayoutManager.HORIZONTAL, false);
                            recycler_popular_thriller.setLayoutManager(mLayoutManager3);
                            recycler_popular_thriller.setItemAnimator(new DefaultItemAnimator());
                            recycler_popular_thriller.setAdapter(popularMSNAdapter);
                            popularMSNAdapter.notifyDataSetChanged();
                            ly_popular_thriller.setVisibility(View.VISIBLE);
                        } else {
                            ly_popular_thriller.setVisibility(View.GONE);
                        }
                    } else if (CatID.equalsIgnoreCase("8")) {
                        PopularComedyList = new ArrayList<>();
                        PopularComedyList = response.body().getResult();
                        Log.e("PopularComedyList", "" + PopularComedyList.size());
                        if (PopularComedyList.size() > 0) {
                            popularMSNAdapter = new PopularMSNAdapter(getActivity(), PopularComedyList, "episode");
                            recycler_popular_comedy.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                                    LinearLayoutManager.HORIZONTAL, false);
                            recycler_popular_comedy.setLayoutManager(mLayoutManager3);
                            recycler_popular_comedy.setItemAnimator(new DefaultItemAnimator());
                            recycler_popular_comedy.setAdapter(popularMSNAdapter);
                            popularMSNAdapter.notifyDataSetChanged();
                            ly_popular_comedy.setVisibility(View.VISIBLE);
                        } else {
                            ly_popular_comedy.setVisibility(View.GONE);
                        }
                    } else if (CatID.equalsIgnoreCase("9")) {
                        PopularKidsList = new ArrayList<>();
                        PopularKidsList = response.body().getResult();
                        Log.e("PopularKidsList", "" + PopularKidsList.size());
                        if (PopularKidsList.size() > 0) {
                            popularMSNAdapter = new PopularMSNAdapter(getActivity(), PopularKidsList, "episode");
                            recycler_popular_kids.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                                    LinearLayoutManager.HORIZONTAL, false);
                            recycler_popular_kids.setLayoutManager(mLayoutManager3);
                            recycler_popular_kids.setItemAnimator(new DefaultItemAnimator());
                            recycler_popular_kids.setAdapter(popularMSNAdapter);
                            popularMSNAdapter.notifyDataSetChanged();
                            ly_popular_kids.setVisibility(View.VISIBLE);
                        } else {
                            ly_popular_kids.setVisibility(View.GONE);
                        }
                    } else if (CatID.equalsIgnoreCase("10")) {
                        PopularBiopicList = new ArrayList<>();
                        PopularBiopicList = response.body().getResult();
                        Log.e("PopularBiopicList", "" + PopularBiopicList.size());
                        if (PopularBiopicList.size() > 0) {
                            popularMSNAdapter = new PopularMSNAdapter(getActivity(), PopularBiopicList, "episodes");
                            recycler_popular_biopic.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                                    LinearLayoutManager.HORIZONTAL, false);
                            recycler_popular_biopic.setLayoutManager(mLayoutManager3);
                            recycler_popular_biopic.setItemAnimator(new DefaultItemAnimator());
                            recycler_popular_biopic.setAdapter(popularMSNAdapter);
                            popularMSNAdapter.notifyDataSetChanged();
                            ly_popular_biopic.setVisibility(View.VISIBLE);
                        } else {
                            ly_popular_biopic.setVisibility(View.GONE);
                        }
                    } else if (CatID.equalsIgnoreCase("11")) {
                        PopularDocumentaryList = new ArrayList<>();
                        PopularDocumentaryList = response.body().getResult();
                        Log.e("PopularDocumentaryList", "" + PopularDocumentaryList.size());
                        if (PopularDocumentaryList.size() > 0) {
                            popularMSNAdapter = new PopularMSNAdapter(getActivity(), PopularDocumentaryList, "episodes");
                            recycler_popular_documentary.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                                    LinearLayoutManager.HORIZONTAL, false);
                            recycler_popular_documentary.setLayoutManager(mLayoutManager3);
                            recycler_popular_documentary.setItemAnimator(new DefaultItemAnimator());
                            recycler_popular_documentary.setAdapter(popularMSNAdapter);
                            popularMSNAdapter.notifyDataSetChanged();
                            ly_popular_documentary.setVisibility(View.VISIBLE);
                        } else {
                            ly_popular_documentary.setVisibility(View.GONE);
                        }
                    } else if (CatID.equalsIgnoreCase("12")) {
                        PopularTeenList = new ArrayList<>();
                        PopularTeenList = response.body().getResult();
                        Log.e("PopularTeenList", "" + PopularTeenList.size());
                        if (PopularTeenList.size() > 0) {
                            popularMSNAdapter = new PopularMSNAdapter(getActivity(), PopularTeenList, "episodes");
                            recycler_popular_teen.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                                    LinearLayoutManager.HORIZONTAL, false);
                            recycler_popular_teen.setLayoutManager(mLayoutManager3);
                            recycler_popular_teen.setItemAnimator(new DefaultItemAnimator());
                            recycler_popular_teen.setAdapter(popularMSNAdapter);
                            popularMSNAdapter.notifyDataSetChanged();
                            ly_popular_teen.setVisibility(View.VISIBLE);
                        } else {
                            ly_popular_teen.setVisibility(View.GONE);
                        }
                    } else if (CatID.equalsIgnoreCase("13")) {
                        PopularTalkshowList = new ArrayList<>();
                        PopularTalkshowList = response.body().getResult();
                        Log.e("PopularTalkshowList", "" + PopularTalkshowList.size());
                        if (PopularTalkshowList.size() > 0) {
                            popularMSNAdapter = new PopularMSNAdapter(getActivity(), PopularTalkshowList, "episodes");
                            recycler_popular_talkshow.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(),
                                    LinearLayoutManager.HORIZONTAL, false);
                            recycler_popular_talkshow.setLayoutManager(mLayoutManager3);
                            recycler_popular_talkshow.setItemAnimator(new DefaultItemAnimator());
                            recycler_popular_talkshow.setAdapter(popularMSNAdapter);
                            popularMSNAdapter.notifyDataSetChanged();
                            ly_popular_talkshow.setVisibility(View.VISIBLE);
                        } else {
                            ly_popular_talkshow.setVisibility(View.GONE);
                        }
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
