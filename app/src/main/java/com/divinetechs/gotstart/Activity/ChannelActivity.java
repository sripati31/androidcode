package com.divinetechs.gotstart.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.divinetechs.gotstart.Adapter.ChannelAdapter;
import com.divinetechs.gotstart.Model.ChannelModel.ChannelModel;
import com.divinetechs.gotstart.Model.ChannelModel.Result;
import com.divinetechs.gotstart.R;
import com.divinetechs.gotstart.Utility.PrefManager;
import com.divinetechs.gotstart.Webservice.AppAPI;
import com.divinetechs.gotstart.Webservice.BaseURL;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelActivity extends AppCompatActivity {

    RecyclerView recycler_channel;
    Toolbar toolbar;
    ProgressDialog progressDialog;
    PrefManager prefManager;
    public List<Result> ChannelList;
    ChannelAdapter channelAdapter;
    TextView txt_back, toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_activity);

        prefManager = new PrefManager(ChannelActivity.this);
        progressDialog = new ProgressDialog(ChannelActivity.this);
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
        toolbar_title.setText("Channels");

        recycler_channel = (RecyclerView) findViewById(R.id.recycler_channel);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }

        Channellist();
    }

    private void Channellist() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<ChannelModel> call = bookNPlayAPI.Channellist();
        call.enqueue(new Callback<ChannelModel>() {
            @Override
            public void onResponse(Call<ChannelModel> call, Response<ChannelModel> response) {
                if (response.code() == 200) {

                    ChannelList = new ArrayList<>();
                    ChannelList = response.body().getResult();
                    Log.e("ChannelList", "" + ChannelList.size());
                    channelAdapter = new ChannelAdapter(ChannelActivity.this, ChannelList, "");
                    recycler_channel.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(ChannelActivity.this,
                            LinearLayoutManager.HORIZONTAL, false);
//                    recycler_channel.setLayoutManager(mLayoutManager3);
                    recycler_channel.setLayoutManager(new GridLayoutManager(ChannelActivity.this, 2));
                    recycler_channel.setItemAnimator(new DefaultItemAnimator());
                    recycler_channel.setAdapter(channelAdapter);
                    channelAdapter.notifyDataSetChanged();

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ChannelModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}
