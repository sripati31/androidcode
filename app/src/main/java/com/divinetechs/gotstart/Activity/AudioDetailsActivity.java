package com.divinetechs.gotstart.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.divinetechs.gotstart.Adapter.PopularAudioAdapter;
import com.divinetechs.gotstart.Adapter.PopularChannelAdapter;
import com.divinetechs.gotstart.Model.AudioModel.AudioModel;
import com.divinetechs.gotstart.Model.AudioModel.Result;
import com.divinetechs.gotstart.Model.SuccessModel.SuccessModel;
import com.divinetechs.gotstart.R;
import com.divinetechs.gotstart.Utility.PrefManager;
import com.divinetechs.gotstart.Webservice.AppAPI;
import com.divinetechs.gotstart.Webservice.BaseURL;
import com.potyvideo.library.AndExoPlayerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.squareup.picasso.Picasso.Priority.HIGH;

public class AudioDetailsActivity extends AppCompatActivity {

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
    List<Result> AudioList;
    PopularAudioAdapter popularAudioAdapter;

    AndExoPlayerView andExoPlayerView;
    ImageView iv_play;
    int play_pause = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audiodetailsactivity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        prefManager = new PrefManager(AudioDetailsActivity.this);
        progressDialog = new ProgressDialog(AudioDetailsActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        txt_toolbar = (TextView) findViewById(R.id.txt_toolbar);
        scrollbar = (ScrollView) findViewById(R.id.scrollbar);

        iv_thumb = (ImageView) findViewById(R.id.iv_thumb);
        iv_play = (ImageView) findViewById(R.id.iv_play);

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
            andExoPlayerView.setPlayWhenReady(true);

            iv_play.setBackground(getResources().getDrawable(R.drawable.ic_pause));

            iv_thumb.setVisibility(View.VISIBLE);
            andExoPlayerView.setVisibility(View.GONE);

            try {
                Picasso.with(AudioDetailsActivity.this).load(image).priority(HIGH)
                        .resize(400, 400).centerInside().into(iv_thumb);
            } catch (Exception e) {
                Log.e("Exception", "" + e.getMessage());
            }

            // Episode
            PopularAudiolist();

        }

        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (play_pause == 0) {
                    iv_play.setBackground(getResources().getDrawable(R.drawable.ic_play));
                    andExoPlayerView.setPlayWhenReady(false);
                    play_pause=1;
                } else {
                    iv_play.setBackground(getResources().getDrawable(R.drawable.ic_pause));
                    andExoPlayerView.setPlayWhenReady(true);
                    play_pause=0;
                }
            }
        });

        ly_watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!prefManager.getLoginId().equalsIgnoreCase("0"))
                    add_watch();
                else
                    startActivity(new Intent(AudioDetailsActivity.this, LoginActivity.class));
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

    private void PopularAudiolist() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<AudioModel> call = bookNPlayAPI.Popularaudiolist();
        call.enqueue(new Callback<AudioModel>() {
            @Override
            public void onResponse(Call<AudioModel> call, Response<AudioModel> response) {
                if (response.code() == 200) {

                    AudioList = new ArrayList<>();
                    AudioList = response.body().getResult();
                    Log.e("AudioList", "" + AudioList.size());
                    popularAudioAdapter = new PopularAudioAdapter(AudioDetailsActivity.this, AudioList, "episode");
                    recycler_episodes.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(AudioDetailsActivity.this,
                            LinearLayoutManager.VERTICAL, false);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(AudioDetailsActivity.this, 2,
                            LinearLayoutManager.VERTICAL, false);
                    recycler_episodes.setLayoutManager(gridLayoutManager);
                    recycler_episodes.setItemAnimator(new DefaultItemAnimator());
                    recycler_episodes.setAdapter(popularAudioAdapter);
                    popularAudioAdapter.notifyDataSetChanged();

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<AudioModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void add_watch() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<SuccessModel> call = bookNPlayAPI.add_watchlist(prefManager.getLoginId(),
                "", c_id, "");
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                progressDialog.dismiss();
                Toast.makeText(AudioDetailsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
}
