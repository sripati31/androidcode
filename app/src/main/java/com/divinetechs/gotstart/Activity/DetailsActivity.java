package com.divinetechs.gotstart.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.divinetechs.gotstart.Adapter.EpisodeDetailsAdapter;
import com.divinetechs.gotstart.Adapter.PopularAdapter;
import com.divinetechs.gotstart.DailyMotion.DailyMotionActivity;
import com.divinetechs.gotstart.Interface.Detailsdata;
import com.divinetechs.gotstart.Model.SuccessModel.SuccessModel;
import com.divinetechs.gotstart.Model.TvVideoModel.Result;
import com.divinetechs.gotstart.Model.TvVideoModel.TvVideoModel;
import com.divinetechs.gotstart.R;
import com.divinetechs.gotstart.Utility.PrefManager;
import com.divinetechs.gotstart.Utility.Utils;
import com.divinetechs.gotstart.Vimeo.VimeoActivity;
import com.divinetechs.gotstart.Webservice.AppAPI;
import com.divinetechs.gotstart.Webservice.BaseURL;
import com.divinetechs.gotstart.YouTube.YoutubePlayer;
import com.divinetechs.gotstart.expandedcontrols.ExpandedControlsActivity;
import com.divinetechs.gotstart.settings.CastPreference;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaLoadRequestData;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;
import com.potyvideo.library.AndExoPlayerView;
import com.potyvideo.library.globalEnums.EnumResizeMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity implements Detailsdata {

    Toolbar toolbar;
    TextView txt_toolbar;
    ScrollView scrollbar;

    String tvs_id, tvv_id, tvs_name, fc_id, type;
    int position;

    PrefManager prefManager;
    ProgressDialog progressDialog;

    List<Result> VideoDetailsList;
    List<Result> DetailsList;
    RecyclerView recycler_episodes;
    EpisodeDetailsAdapter episodeDetailsAdapter;

    List<com.divinetechs.gotstart.Model.TvSerialModel.Result> TopPickList;
    PopularAdapter popularAdapter2;

    ImageView iv_thumb;

    TextView txt_title, txt_play, txt_category, txt_title2, txt_category2, txt_description, txt_top_pick_for_you;

    LinearLayout ly_watchlist, ly_download, ly_share, ly_episode, ly_cast, ly_popular_clip, ly_you_may_also_like;

    TextView txt_premium;
    ImageView iv_show_thumb, iv_play;
    RelativeLayout ry_channel, ry_server;
    int pos = 0;
    AndExoPlayerView andExoPlayerView;

    Call<TvVideoModel> call;

    private static final String TAG = "DetailsActivity";
    private Timer mControllersTimer;
    private PlaybackState mPlaybackState;
    private final Handler mHandler = new Handler();
    private final float mAspectRatio = 72f / 128;
    private boolean mControllersVisible;
    private int mDuration;
    private PlaybackLocation mLocation;
    private CastContext mCastContext;
    private CastSession mCastSession;
    private SessionManagerListener<CastSession> mSessionManagerListener;
    private MenuItem mediaRouteMenuItem;
    String URL = "https://storage.googleapis.com/spatial-racer-235722.appspot.com/tt6146586_LAT_HD.mp4";
    Boolean StartFalse;

    private IntroductoryOverlay mIntroductoryOverlay;
    private CastStateListener mCastStateListener;


    public enum PlaybackLocation {
        LOCAL,
        REMOTE
    }

    public enum PlaybackState {
        PLAYING, PAUSED, BUFFERING, IDLE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailsactivity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        setupCastListener();

        prefManager = new PrefManager(DetailsActivity.this);
        progressDialog = new ProgressDialog(DetailsActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        txt_toolbar = (TextView) findViewById(R.id.txt_toolbar);
        scrollbar = (ScrollView) findViewById(R.id.scrollbar);

        ry_server = (RelativeLayout) findViewById(R.id.ry_server);
        ry_channel = (RelativeLayout) findViewById(R.id.ry_channel);
        iv_thumb = (ImageView) findViewById(R.id.iv_thumb);
        iv_show_thumb = (ImageView) findViewById(R.id.iv_show_thumb);
        iv_play = (ImageView) findViewById(R.id.iv_play);

        txt_premium = (TextView) findViewById(R.id.txt_premium);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_play = (TextView) findViewById(R.id.txt_play);
        txt_category = (TextView) findViewById(R.id.txt_category);
        txt_title2 = (TextView) findViewById(R.id.txt_title2);
        txt_category2 = (TextView) findViewById(R.id.txt_category2);
        txt_description = (TextView) findViewById(R.id.txt_description);
        txt_top_pick_for_you = (TextView) findViewById(R.id.txt_top_pick_for_you);

        recycler_episodes = (RecyclerView) findViewById(R.id.recycler_episodes);

        ly_cast = (LinearLayout) findViewById(R.id.ly_cast);
        ly_download = (LinearLayout) findViewById(R.id.ly_download);
        ly_watchlist = (LinearLayout) findViewById(R.id.ly_watchlist);
        ly_share = (LinearLayout) findViewById(R.id.ly_share);
        ly_episode = (LinearLayout) findViewById(R.id.ly_episode);

        andExoPlayerView = (AndExoPlayerView) findViewById(R.id.andExoPlayerView);

        toolbar.setNavigationIcon(R.drawable.ic_back); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tvs_id = bundle.getString("tvs_id");
            tvs_name = bundle.getString("tvs_name");
            position = bundle.getInt("position");
            fc_id = bundle.getString("fc_id");
            tvv_id = bundle.getString("tvs_id");
            type = bundle.getString("type");
            Log.e("ftvs_id", "" + tvs_id + "position=>" + position);
            Log.e("tvv_id", "" + tvv_id);
            Log.e("type", "" + type);

            txt_toolbar.setText("" + tvs_name);

            if (type.equalsIgnoreCase("sport") ||
                    type.equalsIgnoreCase("movie") ||
                    type.equalsIgnoreCase("news"))
                Video_DetailsMSN();
            else
                Video_Details();

        }

        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VideoDetailsList.get(pos).getIspremium().equalsIgnoreCase("0")) {

                    if (VideoDetailsList.get(pos).getTvvVideoType().equalsIgnoreCase("Server Video")) {
                        ry_channel.setVisibility(View.GONE);
                        ry_server.setVisibility(View.VISIBLE);
                        try {
                            Log.e("getTvvVideo", BaseURL.BASE_URL_video + "" + VideoDetailsList.get(pos).getTvvVideo().replace(" ", "_"));
                            Log.e("getTvvVideoURL", "" + VideoDetailsList.get(pos).getTvvVideourl());
                            if (VideoDetailsList.get(pos).getTvvVideo().length() != 0)
                                andExoPlayerView.setSource(BaseURL.BASE_URL_video + "" + VideoDetailsList.get(pos).getTvvVideo().replace(" ", "_"));
                            else
                                andExoPlayerView.setSource(VideoDetailsList.get(pos).getTvvVideourl());
                            andExoPlayerView.setPlayWhenReady(true);
                        } catch (Exception e) {
                            Log.e("Exception", "" + e.getMessage());
                        }
                    } else if (VideoDetailsList.get(pos).getTvvVideoType().equalsIgnoreCase("Youtube Video")) {
                        ry_channel.setVisibility(View.VISIBLE);
                        ry_server.setVisibility(View.GONE);
                        Intent intent = new Intent(DetailsActivity.this, YoutubePlayer.class);
                        intent.putExtra("Id", "" + VideoDetailsList.get(pos).getTvvId());
                        intent.putExtra("url", "" + VideoDetailsList.get(pos).getTvvVideourl());
                        intent.putExtra("title", "" + VideoDetailsList.get(pos).getTvvName());
                        startActivity(intent);
                    } else if (VideoDetailsList.get(pos).getTvvVideoType().equalsIgnoreCase("Vimeo Video")) {
                        ry_channel.setVisibility(View.VISIBLE);
                        ry_server.setVisibility(View.GONE);
                        Intent intent = new Intent(DetailsActivity.this, VimeoActivity.class);
                        intent.putExtra("Id", "" + VideoDetailsList.get(pos).getTvvId());
                        intent.putExtra("url", "" + VideoDetailsList.get(pos).getTvvVideourl());
                        intent.putExtra("title", "" + VideoDetailsList.get(pos).getTvvName());
                        startActivity(intent);
                    } else if (VideoDetailsList.get(pos).getTvvVideoType().equalsIgnoreCase("Daily Motion")) {
                        ry_channel.setVisibility(View.VISIBLE);
                        ry_server.setVisibility(View.GONE);
                        Intent intent = new Intent(DetailsActivity.this, DailyMotionActivity.class);
                        intent.putExtra("Id", "" + VideoDetailsList.get(pos).getTvvId());
                        intent.putExtra("url", "" + VideoDetailsList.get(pos).getTvvVideourl());
                        intent.putExtra("title", "" + VideoDetailsList.get(pos).getTvvName());
                        startActivity(intent);
                    }
                } else if (VideoDetailsList.get(pos).getIspremium().equalsIgnoreCase("1") &&
                        prefManager.getPremiumID().equalsIgnoreCase("1")) {

                    if (VideoDetailsList.get(pos).getTvvVideoType().equalsIgnoreCase("Server Video")) {
                        ry_channel.setVisibility(View.GONE);
                        ry_server.setVisibility(View.VISIBLE);
                        try {
                            Log.e("getTvvVideo", BaseURL.BASE_URL_video + "" + VideoDetailsList.get(pos).getTvvVideo().replace(" ", "_"));
                            Log.e("getTvvVideoURL", "" + VideoDetailsList.get(pos).getTvvVideourl());
                            if (VideoDetailsList.get(pos).getTvvVideo().length() != 0)
                                andExoPlayerView.setSource(BaseURL.BASE_URL_video + "" + VideoDetailsList.get(pos).getTvvVideo().replace(" ", "_"));
                            else
                                andExoPlayerView.setSource(VideoDetailsList.get(pos).getTvvVideourl());
                            andExoPlayerView.setPlayWhenReady(true);
                        } catch (Exception e) {
                            Log.e("Exception", "" + e.getMessage());
                        }
                    } else if (VideoDetailsList.get(pos).getTvvVideoType().equalsIgnoreCase("Youtube Video")) {
                        ry_channel.setVisibility(View.VISIBLE);
                        ry_server.setVisibility(View.GONE);
                        Intent intent = new Intent(DetailsActivity.this, YoutubePlayer.class);
                        intent.putExtra("Id", "" + VideoDetailsList.get(pos).getTvvId());
                        intent.putExtra("url", "" + VideoDetailsList.get(pos).getTvvVideourl());
                        intent.putExtra("title", "" + VideoDetailsList.get(pos).getTvvName());
                        startActivity(intent);
                    } else if (VideoDetailsList.get(pos).getTvvVideoType().equalsIgnoreCase("Vimeo Video")) {
                        ry_channel.setVisibility(View.VISIBLE);
                        ry_server.setVisibility(View.GONE);
                        Intent intent = new Intent(DetailsActivity.this, VimeoActivity.class);
                        intent.putExtra("Id", "" + VideoDetailsList.get(pos).getTvvId());
                        intent.putExtra("url", "" + VideoDetailsList.get(pos).getTvvVideourl());
                        intent.putExtra("title", "" + VideoDetailsList.get(pos).getTvvName());
                        startActivity(intent);
                    } else if (VideoDetailsList.get(pos).getTvvVideoType().equalsIgnoreCase("Daily Motion")) {
                        ry_channel.setVisibility(View.VISIBLE);
                        ry_server.setVisibility(View.GONE);
                        Intent intent = new Intent(DetailsActivity.this, DailyMotionActivity.class);
                        intent.putExtra("Id", "" + VideoDetailsList.get(pos).getTvvId());
                        intent.putExtra("url", "" + VideoDetailsList.get(pos).getTvvVideourl());
                        intent.putExtra("title", "" + VideoDetailsList.get(pos).getTvvName());
                        startActivity(intent);
                    }
                } else {
                    startActivity(new Intent(DetailsActivity.this, Subscription.class));
                }
            }
        });

        txt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        ly_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!prefManager.getLoginId().equalsIgnoreCase("0")) {
                    if (VideoDetailsList.get(pos).getTvvVideoType().equalsIgnoreCase("Server Video")) {
                        new DownloadVideo1("save").execute(BaseURL.BASE_URL_video + "" +
                                VideoDetailsList.get(0).getTvvVideo().replace(" ", "_"));
                    } else {
                        Toast.makeText(DetailsActivity.this, "download feature not support on this video",
                                Toast.LENGTH_SHORT).show();
                    }
                } else
                    startActivity(new Intent(DetailsActivity.this, LoginActivity.class));
            }
        });

        ly_watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!prefManager.getLoginId().equalsIgnoreCase("0"))
                    add_watch();
                else
                    startActivity(new Intent(DetailsActivity.this, LoginActivity.class));
            }
        });

        ly_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (prefManager.getLoginId().equalsIgnoreCase("0")) {
                    startActivity(new Intent(DetailsActivity.this, LoginActivity.class));
                } else {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + "\n\n" + VideoDetailsList.get(0).getTvvName()
                            + "\n\n" + VideoDetailsList.get(0).getTvvVideo());
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }

            }
        });

        ly_cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity.this, CastPreference.class);
                startActivity(i);
            }
        });

        mCastContext = CastContext.getSharedInstance(this);
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();
        // see what we need to play and where

        mCastStateListener = new CastStateListener() {
            @Override
            public void onCastStateChanged(int newState) {
                Log.e("==>newState", "" + newState);
                if (newState != CastState.NO_DEVICES_AVAILABLE) {
                    showIntroductoryOverlay();
                } else {
                    Log.e("==>", "" + CastState.NO_DEVICES_AVAILABLE);
                }
            }
        };

    }

    private void Video_Details() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<TvVideoModel> call = bookNPlayAPI.tv_video_by_serial_id(tvs_id);
        call.enqueue(new Callback<TvVideoModel>() {
            @Override
            public void onResponse(Call<TvVideoModel> call, Response<TvVideoModel> response) {
                if (response.code() == 200) {

                    VideoDetailsList = new ArrayList<>();
                    VideoDetailsList = response.body().getResult();
                    if (VideoDetailsList.size() > 0) {
                        Log.e("Video Details", "" + VideoDetailsList.size());
                        for (int i = 0; i < VideoDetailsList.size(); i++) {
                            if (VideoDetailsList.get(i).getTvvId().equalsIgnoreCase(tvv_id)) {
                                pos = i;
                                Log.e("pos", "" + pos);
                            }
                        }

                        txt_title.setText("" + VideoDetailsList.get(pos).getTvvName());
                        txt_category.setText("" + VideoDetailsList.get(pos).getCat_name());
                        txt_title2.setText("" + VideoDetailsList.get(pos).getTvvName());
                        txt_description.setText("" + VideoDetailsList.get(pos).getTvvdescription());

                        if (VideoDetailsList.get(pos).getV_type().equalsIgnoreCase("tv")) {
                            txt_category2.setText(VideoDetailsList.get(pos).getTvsName() + " - " + 0 + " Episodes");
                            txt_top_pick_for_you.setText("Recent Episodes");
                        } else if (VideoDetailsList.get(pos).getV_type().equalsIgnoreCase("movie")) {
                            txt_top_pick_for_you.setText("Related Movies");
                            txt_category2.setText(VideoDetailsList.get(pos).getTvsName());
                        } else if (VideoDetailsList.get(pos).getV_type().equalsIgnoreCase("news")) {
                            txt_top_pick_for_you.setText("Related News");
                            txt_category2.setText(VideoDetailsList.get(pos).getTvsName());
                        } else if (VideoDetailsList.get(pos).getV_type().equalsIgnoreCase("sport")) {
                            txt_top_pick_for_you.setText("Related Sports");
                            txt_category2.setText(VideoDetailsList.get(pos).getTvsName());
                        }

                        if (VideoDetailsList.get(pos).getTvvVideoType().equalsIgnoreCase("Server Video")) {
                            try {
                                Log.e("getTvvVideo", BaseURL.BASE_URL_video + "" + VideoDetailsList.get(pos).getTvvVideo().replace(" ", "_"));
                                Log.e("getTvvVideoURL", "" + VideoDetailsList.get(pos).getTvvVideourl());

                                if (VideoDetailsList.get(pos).getTvvVideo().length() != 0)
                                    andExoPlayerView.setSource(BaseURL.BASE_URL_video + "" + VideoDetailsList.get(pos).getTvvVideo().replace(" ", "_"));
                                else
                                    andExoPlayerView.setSource(VideoDetailsList.get(pos).getTvvVideourl());
                                andExoPlayerView.setPlayWhenReady(false);
                                andExoPlayerView.setResizeMode(EnumResizeMode.FILL);

                            } catch (Exception e) {
                                Log.e("Exception", "" + e.getMessage());
                            }
                            Glide.with(DetailsActivity.this).load(VideoDetailsList.get(pos).getTvvThumbnail()).into(iv_show_thumb);
                            ry_channel.setVisibility(View.VISIBLE);
                            ry_server.setVisibility(View.GONE);
                        } else {
                            Glide.with(DetailsActivity.this).load(VideoDetailsList.get(pos).getTvvThumbnail()).into(iv_show_thumb);
                            ry_channel.setVisibility(View.VISIBLE);
                            ry_server.setVisibility(View.GONE);
                        }

                        Log.e("Ispremium", "" + VideoDetailsList.get(pos).getIspremium());
                        if (VideoDetailsList.get(pos).getIspremium().equalsIgnoreCase("1")) {
                            txt_premium.setVisibility(View.VISIBLE);
                        } else {
                            txt_premium.setVisibility(View.GONE);
                        }

                    } else {
                        ly_episode.setVisibility(View.GONE);
                    }
                    // Episode
                    Episode_by_id();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<TvVideoModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void Video_DetailsMSN() {
        progressDialog.show();

        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();

        call = bookNPlayAPI.tv_video_by_serial_id_msn(tvs_id, type);

        call.enqueue(new Callback<TvVideoModel>() {
            @Override
            public void onResponse(Call<TvVideoModel> call, Response<TvVideoModel> response) {
                if (response.code() == 200) {

                    VideoDetailsList = new ArrayList<>();
                    VideoDetailsList = response.body().getResult();
                    if (VideoDetailsList.size() > 0) {
                        Log.e("Video Details", "" + VideoDetailsList.size());
                        for (int i = 0; i < VideoDetailsList.size(); i++) {
                            if (VideoDetailsList.get(i).getTvvId().equalsIgnoreCase(tvv_id)) {
                                pos = i;
                                Log.e("pos", "" + pos);
                            }
                        }

                        txt_title.setText("" + VideoDetailsList.get(pos).getTvvName());
                        txt_category.setText("" + VideoDetailsList.get(pos).getCat_name());
                        txt_title2.setText("" + VideoDetailsList.get(pos).getTvvName());
                        txt_description.setText("" + VideoDetailsList.get(pos).getTvvdescription());

                        if (VideoDetailsList.get(pos).getV_type().equalsIgnoreCase("tv")) {
                            txt_category2.setText(VideoDetailsList.get(pos).getTvsName() + " - " + 0 + " Episodes");
                            txt_top_pick_for_you.setText("Recent Episodes");
                        } else if (VideoDetailsList.get(pos).getV_type().equalsIgnoreCase("movie")) {
                            txt_top_pick_for_you.setText("Related Movies");
                            txt_category2.setText(VideoDetailsList.get(pos).getTvsName());
                        } else if (VideoDetailsList.get(pos).getV_type().equalsIgnoreCase("news")) {
                            txt_top_pick_for_you.setText("Related News");
                            txt_category2.setText(VideoDetailsList.get(pos).getTvsName());
                        } else if (VideoDetailsList.get(pos).getV_type().equalsIgnoreCase("sport")) {
                            txt_top_pick_for_you.setText("Related Sports");
                            txt_category2.setText(VideoDetailsList.get(pos).getTvsName());
                        }

                        if (VideoDetailsList.get(pos).getTvvVideoType().equalsIgnoreCase("Server Video")) {
                            try {
                                Log.e("getTvvVideo", BaseURL.BASE_URL_video + "" + VideoDetailsList.get(pos).getTvvVideo().replace(" ", "_"));
                                Log.e("getTvvVideoURL", "" + VideoDetailsList.get(pos).getTvvVideourl());

                                if (VideoDetailsList.get(pos).getTvvVideo().length() != 0)
                                    andExoPlayerView.setSource(BaseURL.BASE_URL_video + "" + VideoDetailsList.get(pos).getTvvVideo().replace(" ", "_"));
                                else
                                    andExoPlayerView.setSource(VideoDetailsList.get(pos).getTvvVideourl());
                                andExoPlayerView.setPlayWhenReady(false);
                                andExoPlayerView.setResizeMode(EnumResizeMode.FILL);

                            } catch (Exception e) {
                                Log.e("Exception", "" + e.getMessage());
                            }
                            Glide.with(DetailsActivity.this).load(VideoDetailsList.get(pos).getTvvThumbnail()).into(iv_show_thumb);
                            ry_channel.setVisibility(View.VISIBLE);
                            ry_server.setVisibility(View.GONE);
                        } else {
                            Glide.with(DetailsActivity.this).load(VideoDetailsList.get(pos).getTvvThumbnail()).into(iv_show_thumb);
                            ry_channel.setVisibility(View.VISIBLE);
                            ry_server.setVisibility(View.GONE);
                        }

                        Log.e("Ispremium", "" + VideoDetailsList.get(pos).getIspremium());
                        if (VideoDetailsList.get(pos).getIspremium().equalsIgnoreCase("1")) {
                            txt_premium.setVisibility(View.VISIBLE);
                        } else {
                            txt_premium.setVisibility(View.GONE);
                        }

                    } else {
                        ly_episode.setVisibility(View.GONE);
                    }

                    if (type.equalsIgnoreCase("sport") ||
                            type.equalsIgnoreCase("movie")||
                            type.equalsIgnoreCase("news"))
                        MSN_by_id();
                    else
                        Episode_by_id();

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<TvVideoModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void Episode_by_id() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<TvVideoModel> call = bookNPlayAPI.episode_by_id(tvs_id);
        call.enqueue(new Callback<TvVideoModel>() {
            @Override
            public void onResponse(Call<TvVideoModel> call, Response<TvVideoModel> response) {
                if (response.code() == 200) {

                    DetailsList = new ArrayList<>();
                    DetailsList = response.body().getResult();

                    if (DetailsList.size() > 0) {
                        Log.e("DetailsList", "" + DetailsList.size());
                        episodeDetailsAdapter = new EpisodeDetailsAdapter(DetailsActivity.this, DetailsList, "episode",
                                DetailsActivity.this);
                        recycler_episodes.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(DetailsActivity.this,
                                LinearLayoutManager.VERTICAL, false);
                        recycler_episodes.setLayoutManager(mLayoutManager3);
                        recycler_episodes.setItemAnimator(new DefaultItemAnimator());
                        recycler_episodes.setAdapter(episodeDetailsAdapter);
                        episodeDetailsAdapter.notifyDataSetChanged();

                        if (VideoDetailsList.get(0).getV_type().equalsIgnoreCase("tv")) {
                            txt_category2.setText(VideoDetailsList.get(0).getTvsName() + " - " + 0 + " Episodes");
                            txt_top_pick_for_you.setText("Recent Episodes");
                        } else if (VideoDetailsList.get(0).getV_type().equalsIgnoreCase("movie")) {
                            txt_top_pick_for_you.setText("Related Movies");
                            txt_category2.setText(VideoDetailsList.get(0).getTvsName());
                        } else if (VideoDetailsList.get(0).getV_type().equalsIgnoreCase("news")) {
                            txt_top_pick_for_you.setText("Related News");
                            txt_category2.setText(VideoDetailsList.get(0).getTvsName());
                        } else if (VideoDetailsList.get(0).getV_type().equalsIgnoreCase("sport")) {
                            txt_top_pick_for_you.setText("Related Sports");
                            txt_category2.setText(VideoDetailsList.get(0).getTvsName());
                        }
                    } else {
                        ly_episode.setVisibility(View.GONE);
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


    private void MSN_by_id() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();

        if (type.equalsIgnoreCase("sport"))
            call = bookNPlayAPI.sport_by_id(fc_id);
        else if (type.equalsIgnoreCase("movie"))
            call = bookNPlayAPI.movie_by_id(fc_id);
        else if (type.equalsIgnoreCase("news"))
            call = bookNPlayAPI.news_by_id(fc_id);

        call.enqueue(new Callback<TvVideoModel>() {
            @Override
            public void onResponse(Call<TvVideoModel> call, Response<TvVideoModel> response) {
                if (response.code() == 200) {

                    DetailsList = new ArrayList<>();
                    DetailsList = response.body().getResult();

                    if (DetailsList.size() > 0) {
                        Log.e("DetailsList", "" + DetailsList.size());
                        episodeDetailsAdapter = new EpisodeDetailsAdapter(DetailsActivity.this, DetailsList, "episode",
                                DetailsActivity.this);
                        recycler_episodes.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(DetailsActivity.this,
                                LinearLayoutManager.VERTICAL, false);
                        recycler_episodes.setLayoutManager(mLayoutManager3);
                        recycler_episodes.setItemAnimator(new DefaultItemAnimator());
                        recycler_episodes.setAdapter(episodeDetailsAdapter);
                        episodeDetailsAdapter.notifyDataSetChanged();

                        if (VideoDetailsList.get(0).getV_type().equalsIgnoreCase("tv")) {
                            txt_category2.setText(VideoDetailsList.get(0).getTvsName() + " - " + 0 + " Episodes");
                            txt_top_pick_for_you.setText("Recent Episodes");
                        } else if (VideoDetailsList.get(0).getV_type().equalsIgnoreCase("movie")) {
                            txt_top_pick_for_you.setText("Related Movies");
                            txt_category2.setText(VideoDetailsList.get(0).getTvsName());
                        } else if (VideoDetailsList.get(0).getV_type().equalsIgnoreCase("news")) {
                            txt_top_pick_for_you.setText("Related News");
                            txt_category2.setText(VideoDetailsList.get(0).getTvsName());
                        } else if (VideoDetailsList.get(0).getV_type().equalsIgnoreCase("sport")) {
                            txt_top_pick_for_you.setText("Related Sports");
                            txt_category2.setText(VideoDetailsList.get(0).getTvsName());
                        }
                    } else {
                        ly_episode.setVisibility(View.GONE);
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

    public class DownloadVideo1 extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        URL myFileUrl;
        String option;
        File file;

        DownloadVideo1(String option) {
            this.option = option;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(DetailsActivity.this, AlertDialog.THEME_HOLO_LIGHT);
            pDialog.setMessage(getResources().getString(R.string.downloading));
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                myFileUrl = new URL(args[0]);
                Log.e("myFileUrl", "" + myFileUrl);
                String path = myFileUrl.getPath();
                String fileName = path.substring(path.lastIndexOf('/') + 1);
                File dir = new File(getExternalCacheDir().getAbsolutePath() + "/" + getString(R.string.app_name) + "/");
                dir.mkdirs();
                file = new File(dir, fileName);

                if (!file.exists()) {
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    FileOutputStream fos = new FileOutputStream(file);
                    byte data[] = new byte[4096];
                    int count;
                    while ((count = is.read(data)) != -1) {
                        if (isCancelled()) {
                            is.close();
                            return null;
                        }
                        fos.write(data, 0, count);
                    }
                    fos.flush();
                    fos.close();

                    if (option.equals("save")) {
                        MediaScannerConnection.scanFile(DetailsActivity.this, new String[]{file.getAbsolutePath()},
                                null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {

                                    }
                                });
                    }
                    return "1";
                } else {
                    return "2";
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Exception", "" + e.getMessage());
                return "0";
            }
        }

        @Override
        protected void onPostExecute(String args) {
            if (args.equals("1") || args.equals("2")) {
                switch (option) {
                    case "save":
                        if (args.equals("2")) {
                            Toast.makeText(DetailsActivity.this, "Already Download", Toast.LENGTH_SHORT).show();
                        } else {
                            add_download();
                            Toast.makeText(DetailsActivity.this, "Thank for Download", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            } else {
                Toast.makeText(DetailsActivity.this, "Please try again.", Toast.LENGTH_SHORT).show();
            }
            pDialog.dismiss();
        }
    }

    private void add_download() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<SuccessModel> call = bookNPlayAPI.add_download(prefManager.getLoginId(),
                VideoDetailsList.get(0).getTvvId(), "");
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                progressDialog.dismiss();
                Log.e("Download", "" + response.body().getMessage());
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("onFailure", "" + t.getMessage());
            }
        });
    }

    private void add_watch() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<SuccessModel> call = bookNPlayAPI.add_watchlist(prefManager.getLoginId(),
                VideoDetailsList.get(0).getTvvId(), "", "");
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                progressDialog.dismiss();
                Toast.makeText(DetailsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void showIntroductoryOverlay() {
        if (mIntroductoryOverlay != null) {
            mIntroductoryOverlay.remove();
        }
        if ((mediaRouteMenuItem != null) && mediaRouteMenuItem.isVisible()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mIntroductoryOverlay = new IntroductoryOverlay.Builder(
                            DetailsActivity.this, mediaRouteMenuItem)
                            .setTitleText("Introducing Cast")
                            .setSingleTime()
                            .setOnOverlayDismissedListener(
                                    new IntroductoryOverlay.OnOverlayDismissedListener() {
                                        @Override
                                        public void onOverlayDismissed() {
                                            mIntroductoryOverlay = null;
                                        }
                                    })
                            .build();
                    mIntroductoryOverlay.show();
                }
            });
        }
    }

    private void setupCastListener() {
        mSessionManagerListener = new SessionManagerListener<CastSession>() {

            @Override
            public void onSessionEnded(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionResumed(CastSession session, boolean wasSuspended) {
                onApplicationConnected(session);
            }

            @Override
            public void onSessionResumeFailed(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarted(CastSession session, String sessionId) {
                onApplicationConnected(session);
            }

            @Override
            public void onSessionStartFailed(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarting(CastSession session) {
            }

            @Override
            public void onSessionEnding(CastSession session) {
            }

            @Override
            public void onSessionResuming(CastSession session, String sessionId) {
            }

            @Override
            public void onSessionSuspended(CastSession session, int reason) {
            }

            private void onApplicationConnected(CastSession castSession) {
                mCastSession = castSession;
                if (null != URL) {
                    if (mPlaybackState == PlaybackState.PLAYING) {
                        andExoPlayerView.setSource(URL);
                        andExoPlayerView.setPlayWhenReady(false);
                        loadRemoteMedia(0, true);
                        return;
                    } else {
                        mPlaybackState = PlaybackState.IDLE;
                        updatePlaybackLocation(PlaybackLocation.REMOTE);
                    }
                }
                updatePlayButton(mPlaybackState);
                invalidateOptionsMenu();
            }

            private void onApplicationDisconnected() {
                updatePlaybackLocation(PlaybackLocation.LOCAL);
                mPlaybackState = PlaybackState.IDLE;
                mLocation = PlaybackLocation.LOCAL;
                updatePlayButton(mPlaybackState);
                invalidateOptionsMenu();
            }
        };
    }

    private void updatePlaybackLocation(PlaybackLocation location) {
        mLocation = location;
        if (location == PlaybackLocation.LOCAL) {
            if (mPlaybackState == PlaybackState.PLAYING
                    || mPlaybackState == PlaybackState.BUFFERING) {
                startControllersTimer();
            } else {
                stopControllersTimer();
            }
        } else {
            stopControllersTimer();
            updateControllersVisibility(false);
        }
    }

    private void play(int position) {
        startControllersTimer();
        switch (mLocation) {
            case LOCAL:
                andExoPlayerView.setPlayWhenReady(true);
                break;
            case REMOTE:
                mPlaybackState = PlaybackState.BUFFERING;
                updatePlayButton(mPlaybackState);
                mCastSession.getRemoteMediaClient().seek(position);
                break;
            default:
                break;
        }
    }

    private void togglePlayback() {
        stopControllersTimer();
        switch (mPlaybackState) {
            case PAUSED:
                switch (mLocation) {
                    case LOCAL:
                        andExoPlayerView.setPlayWhenReady(true);
                        Log.d(TAG, "Playing locally...");
                        mPlaybackState = PlaybackState.PLAYING;
                        startControllersTimer();
                        updatePlaybackLocation(PlaybackLocation.LOCAL);
                        break;
                    case REMOTE:
                        finish();
                        break;
                    default:
                        break;
                }
                break;

            case PLAYING:
                mPlaybackState = PlaybackState.PAUSED;
                andExoPlayerView.setPlayWhenReady(false);
                break;

            case IDLE:
                switch (mLocation) {
                    case LOCAL:
                        andExoPlayerView.setSource(URL);
                        andExoPlayerView.setPlayWhenReady(true);
                        mPlaybackState = PlaybackState.PLAYING;
                        updatePlaybackLocation(PlaybackLocation.LOCAL);
                        break;
                    case REMOTE:
                        if (mCastSession != null && mCastSession.isConnected()) {
                            loadRemoteMedia(0, true);
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        updatePlayButton(mPlaybackState);
    }

    private void loadRemoteMedia(int position, boolean autoPlay) {
        if (mCastSession == null) {
            return;
        }
        final RemoteMediaClient remoteMediaClient = mCastSession.getRemoteMediaClient();
        if (remoteMediaClient == null) {
            return;
        }
        remoteMediaClient.registerCallback(new RemoteMediaClient.Callback() {
            @Override
            public void onStatusUpdated() {
                Intent intent = new Intent(DetailsActivity.this, ExpandedControlsActivity.class);
                startActivity(intent);
                remoteMediaClient.unregisterCallback(this);
            }
        });
        remoteMediaClient.load(new MediaLoadRequestData.Builder()
                .setMediaInfo(buildMediaInfo())
                .setAutoplay(autoPlay)
                .setCurrentTime(position).build());
    }

    private void stopControllersTimer() {
        if (mControllersTimer != null) {
            mControllersTimer.cancel();
        }
    }

    private void startControllersTimer() {
        if (mControllersTimer != null) {
            mControllersTimer.cancel();
        }
        if (mLocation == PlaybackLocation.REMOTE) {
            return;
        }
        mControllersTimer = new Timer();
        mControllersTimer.schedule(new HideControllersTask(), 5000);
    }

    // should be called from the main thread
    private void updateControllersVisibility(boolean show) {
        if (show) {
            getSupportActionBar().show();
        } else {
            if (!Utils.isOrientationPortrait(this)) {
                getSupportActionBar().hide();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() was called");
        mCastContext.removeCastStateListener(mCastStateListener);

        if (mLocation == PlaybackLocation.LOCAL) {
            if (mControllersTimer != null) {
                mControllersTimer.cancel();
            }
            andExoPlayerView.setPlayWhenReady(true);
            mPlaybackState = PlaybackState.PAUSED;
            updatePlayButton(PlaybackState.PAUSED);
        }
        mCastContext.getSessionManager().removeSessionManagerListener(
                mSessionManagerListener, CastSession.class);

    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop() was called");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy() is called");
        stopControllersTimer();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        Log.e(TAG, "onStart was called");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume() was called");
        mCastContext.addCastStateListener(mCastStateListener);

        mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);
        if (mCastSession != null && mCastSession.isConnected()) {
            updatePlaybackLocation(PlaybackLocation.REMOTE);
        } else {
            updatePlaybackLocation(PlaybackLocation.LOCAL);
        }
        super.onResume();
    }

    private class HideControllersTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateControllersVisibility(false);
                    mControllersVisible = false;
                }
            });
        }
    }

    private void updatePlayButton(PlaybackState state) {
        Log.e(TAG, "Controls: PlayBackState: " + state);
        boolean isConnected = (mCastSession != null)
                && (mCastSession.isConnected() || mCastSession.isConnecting());
        switch (state) {
            case PLAYING:
                break;
            case IDLE:
                break;
            case PAUSED:
                break;
            case BUFFERING:
                break;
            default:
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getSupportActionBar().show();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
            }
            updateMetadata(false);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) andExoPlayerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            andExoPlayerView.setLayoutParams(params);
            andExoPlayerView.setResizeMode(EnumResizeMode.FILL);

        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
            updateMetadata(true);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) andExoPlayerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.WRAP_CONTENT;
            andExoPlayerView.setLayoutParams(params);
            andExoPlayerView.setResizeMode(EnumResizeMode.FILL);
            andExoPlayerView.setShowFullScreen(true);
        }
    }

    private void updateMetadata(boolean visible) {
        Point displaySize;
        if (!visible) {
            displaySize = Utils.getDisplaySize(this);
            RelativeLayout.LayoutParams lp = new
                    RelativeLayout.LayoutParams(displaySize.x,
                    displaySize.y + getSupportActionBar().getHeight());
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        } else {
            displaySize = Utils.getDisplaySize(this);
            RelativeLayout.LayoutParams lp = new
                    RelativeLayout.LayoutParams(displaySize.x,
                    (int) (displaySize.x * mAspectRatio));
            lp.addRule(RelativeLayout.BELOW, R.id.toolbar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.browse, menu);
        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu,
                R.id.media_route_menu_item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
        }
        return true;
    }

    private MediaInfo buildMediaInfo() {
        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, tvs_name);
        movieMetadata.putString(MediaMetadata.KEY_TITLE, tvs_name);
        movieMetadata.addImage(new WebImage(Uri.parse("" + VideoDetailsList.get(pos).getTvvThumbnail())));

        return new MediaInfo.Builder(URL)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType(getType(URL))
                .setMetadata(movieMetadata)
                .build();
    }

    private String getType(String videoUrl) {
        if (videoUrl.endsWith(".mp4")) {
            return "videos/mp4";
        } else if (videoUrl.endsWith(".m3u8")) {
            return "application/x-mpegurl";
        } else {
            return "application/x-mpegurl";
        }
    }

    @Override
    public void item_click(int pos, String id) {

        txt_title.setText("" + DetailsList.get(pos).getTvvName());
        txt_category.setText("" + DetailsList.get(pos).getCat_name());
        txt_title2.setText("" + DetailsList.get(pos).getTvvName());
        txt_description.setText("" + DetailsList.get(pos).getTvvdescription());

        if (DetailsList.get(pos).getTvvVideoType().equalsIgnoreCase("Server Video")) {
            try {
                Log.e("getTvvVideo", BaseURL.BASE_URL_video + "" + DetailsList.get(pos).getTvvVideo().replace(" ", "_"));
                Log.e("getTvvVideoURL", "" + DetailsList.get(pos).getTvvVideourl());

                if (DetailsList.get(pos).getTvvVideo().length() != 0)
                    andExoPlayerView.setSource(BaseURL.BASE_URL_video + "" + DetailsList.get(pos).getTvvVideo().replace(" ", "_"));
                else
                    andExoPlayerView.setSource(DetailsList.get(pos).getTvvVideourl());
                andExoPlayerView.setPlayWhenReady(false);
            } catch (Exception e) {
                Log.e("Exception", "" + e.getMessage());
            }
            Glide.with(DetailsActivity.this).load(DetailsList.get(pos).getTvvThumbnail()).into(iv_show_thumb);
            ry_channel.setVisibility(View.VISIBLE);
            ry_server.setVisibility(View.GONE);
        } else {
            Glide.with(DetailsActivity.this).load(DetailsList.get(pos).getTvvThumbnail()).into(iv_show_thumb);
            ry_channel.setVisibility(View.VISIBLE);
            ry_server.setVisibility(View.GONE);
        }

        Log.e("Ispremium", "" + DetailsList.get(pos).getIspremium());
        if (DetailsList.get(pos).getIspremium().equalsIgnoreCase("1")) {
            txt_premium.setVisibility(View.VISIBLE);
        } else {
            txt_premium.setVisibility(View.GONE);
        }
    }
}
