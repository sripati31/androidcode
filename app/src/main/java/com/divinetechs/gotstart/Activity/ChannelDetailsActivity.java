package com.divinetechs.gotstart.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.divinetechs.gotstart.Adapter.PopularChannelAdapter;
import com.divinetechs.gotstart.Interface.Detailsdata;
import com.divinetechs.gotstart.Model.ChannelModel.ChannelModel;
import com.divinetechs.gotstart.Model.ChannelModel.Result;
import com.divinetechs.gotstart.Model.SuccessModel.SuccessModel;
import com.divinetechs.gotstart.R;
import com.divinetechs.gotstart.Utility.PrefManager;
import com.divinetechs.gotstart.Webservice.AppAPI;
import com.divinetechs.gotstart.Webservice.BaseURL;
import com.potyvideo.library.AndExoPlayerView;
import com.potyvideo.library.globalEnums.EnumResizeMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelDetailsActivity extends AppCompatActivity implements Detailsdata {

    Toolbar toolbar;
    TextView txt_toolbar;
    ScrollView scrollbar;

    int position;

    String c_id, name, desc, url, image;

    PrefManager prefManager;
    ProgressDialog progressDialog;

    RecyclerView recycler_episodes;

    ImageView iv_thumb;
    TextView txt_title, txt_play, txt_category, txt_title2, txt_description;
    LinearLayout ly_watchlist, ly_share, ly_episode;
    List<Result> ChannelList;
    PopularChannelAdapter popularChannelAdapter;

    AndExoPlayerView andExoPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channeldetailsactivity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        prefManager = new PrefManager(ChannelDetailsActivity.this);
        progressDialog = new ProgressDialog(ChannelDetailsActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        txt_toolbar = (TextView) findViewById(R.id.txt_toolbar);
        scrollbar = (ScrollView) findViewById(R.id.scrollbar);

        iv_thumb = (ImageView) findViewById(R.id.iv_thumb);

        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_play = (TextView) findViewById(R.id.txt_play);
        txt_category = (TextView) findViewById(R.id.txt_category);
        txt_title2 = (TextView) findViewById(R.id.txt_title2);
        txt_description = (TextView) findViewById(R.id.txt_description);

        recycler_episodes = (RecyclerView) findViewById(R.id.recycler_episodes);

        ly_watchlist = (LinearLayout) findViewById(R.id.ly_watchlist);
        ly_share = (LinearLayout) findViewById(R.id.ly_share);
        ly_episode = (LinearLayout) findViewById(R.id.ly_episode);

        andExoPlayerView = findViewById(R.id.andExoPlayerView);

        toolbar.setNavigationIcon(R.drawable.ic_back); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            position = bundle.getInt("position");
            Log.e("position", "position=>" + position);

            c_id = bundle.getString("id");
            name = bundle.getString("name");
            desc = bundle.getString("desc");
            url = bundle.getString("url");
            image = bundle.getString("image");

            txt_toolbar.setText("" + bundle.getString("name"));
            txt_title.setText("" + bundle.getString("name"));
            txt_title2.setText("" + bundle.getString("name"));
            txt_description.setText("" + bundle.getString("desc"));
            Log.e("URL", "" + bundle.getString("desc"));

            andExoPlayerView.setSource(bundle.getString("url"));
            andExoPlayerView.setResizeMode(EnumResizeMode.FILL);

            // Episode
            PopularChannellist();

        }

        txt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        ly_watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!prefManager.getLoginId().equalsIgnoreCase("0"))
                    add_watch();
                else
                    startActivity(new Intent(ChannelDetailsActivity.this, LoginActivity.class));
            }
        });

        ly_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + "\n\n" + name
                        + "\n\n" + url);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checking the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e("COnfigration",""+Configuration.ORIENTATION_LANDSCAPE);
            //First Hide other objects (listview or recyclerview), better hide them using Gone.
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) andExoPlayerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=params.MATCH_PARENT;
            andExoPlayerView.setLayoutParams(params);
            andExoPlayerView.setResizeMode(EnumResizeMode.FILL);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Log.e("COnfigration=>portrait",""+Configuration.ORIENTATION_PORTRAIT);
            //unhide your objects here.
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) andExoPlayerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=params.WRAP_CONTENT;
            andExoPlayerView.setLayoutParams(params);
            andExoPlayerView.setResizeMode(EnumResizeMode.FILL);
            andExoPlayerView.setShowFullScreen(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        andExoPlayerView.setPlayWhenReady(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        andExoPlayerView.setPlayWhenReady(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void PopularChannellist() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<ChannelModel> call = bookNPlayAPI.PopularChannellist();
        call.enqueue(new Callback<ChannelModel>() {
            @Override
            public void onResponse(Call<ChannelModel> call, Response<ChannelModel> response) {
                if (response.code() == 200) {

                    ChannelList = new ArrayList<>();
                    ChannelList = response.body().getResult();
                    Log.e("popularshowList", "" + ChannelList.size());
                    popularChannelAdapter = new PopularChannelAdapter(ChannelDetailsActivity.this, ChannelList,
                            "episode", ChannelDetailsActivity.this);
                    recycler_episodes.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(ChannelDetailsActivity.this,
                            LinearLayoutManager.VERTICAL, false);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(ChannelDetailsActivity.this, 2,
                            LinearLayoutManager.VERTICAL, false);
                    recycler_episodes.setLayoutManager(gridLayoutManager);
                    recycler_episodes.setItemAnimator(new DefaultItemAnimator());
                    recycler_episodes.setAdapter(popularChannelAdapter);
                    popularChannelAdapter.notifyDataSetChanged();

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ChannelModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    private void add_watch() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<SuccessModel> call = bookNPlayAPI.add_watchlist(prefManager.getLoginId(),
                "", "", c_id);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                progressDialog.dismiss();
                Toast.makeText(ChannelDetailsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void item_click(int pos, String id) {

        txt_toolbar.setText("" + ChannelList.get(pos).getChannelName());
        txt_title.setText("" + ChannelList.get(pos).getChannelName());
        txt_title2.setText("" + ChannelList.get(pos).getChannelName());
        txt_description.setText("" + ChannelList.get(pos).getChannel_desc());
        Log.e("URL", "" + ChannelList.get(pos).getChannelUrl());

        andExoPlayerView.setSource(ChannelList.get(pos).getChannelUrl());
    }
}
