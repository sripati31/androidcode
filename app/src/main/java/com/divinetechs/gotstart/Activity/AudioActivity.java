package com.divinetechs.gotstart.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.divinetechs.gotstart.Adapter.AudioAdapter;
import com.divinetechs.gotstart.Adapter.ChannelAdapter;
import com.divinetechs.gotstart.Model.AudioModel.AudioModel;
import com.divinetechs.gotstart.Model.AudioModel.Result;
import com.divinetechs.gotstart.R;
import com.divinetechs.gotstart.Utility.PrefManager;
import com.divinetechs.gotstart.Webservice.AppAPI;
import com.divinetechs.gotstart.Webservice.BaseURL;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AudioActivity extends AppCompatActivity {

    RecyclerView recycler_channel;
    Toolbar toolbar;
    ProgressDialog progressDialog;
    PrefManager prefManager;
    public List<Result> AudioList;
    AudioAdapter audioAdapter;
    TextView txt_back, toolbar_title;
    ImageView iv_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_activity);

        prefManager = new PrefManager(AudioActivity.this);
        progressDialog = new ProgressDialog(AudioActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("Channels");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);

        txt_back = (TextView) findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("AudioList");

        recycler_channel = (RecyclerView) findViewById(R.id.recycler_channel);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }

        Audiolist();
    }

    private void Audiolist() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<AudioModel> call = bookNPlayAPI.Audiolist();
        call.enqueue(new Callback<AudioModel>() {
            @Override
            public void onResponse(Call<AudioModel> call, Response<AudioModel> response) {
                if (response.code() == 200) {

                    AudioList = new ArrayList<>();
                    AudioList = response.body().getResult();
                    Log.e("AudioList", "" + AudioList.size());
                    audioAdapter = new AudioAdapter(AudioActivity.this, AudioList, "");
                    recycler_channel.setHasFixedSize(true);
                    recycler_channel.setLayoutManager(new GridLayoutManager(AudioActivity.this, 2));
                    recycler_channel.setItemAnimator(new DefaultItemAnimator());
                    recycler_channel.setAdapter(audioAdapter);
                    audioAdapter.notifyDataSetChanged();

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<AudioModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}
