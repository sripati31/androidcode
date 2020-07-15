package com.divinetechs.gotstart.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.divinetechs.gotstart.Adapter.ViewAllAdapter;
import com.divinetechs.gotstart.Adapter.ViewAllPremiumAdapter;
import com.divinetechs.gotstart.Model.TvVideoModel.Result;
import com.divinetechs.gotstart.R;
import com.divinetechs.gotstart.Utility.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class ViewAllPremiumActivity extends AppCompatActivity {

    RecyclerView recycler_channel;
    Toolbar toolbar;
    ProgressDialog progressDialog;
    PrefManager prefManager;
    public static List<Result> PremiumList;
    ViewAllPremiumAdapter viewAllAdapter;
    TextView txt_back, toolbar_title;
    String Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewall_activity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Title = bundle.getString("title");
        }
        prefManager = new PrefManager(ViewAllPremiumActivity.this);
        progressDialog = new ProgressDialog(ViewAllPremiumActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        toolbar_title.setText(Title);

        recycler_channel = (RecyclerView) findViewById(R.id.recycler_channel);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }

        Log.e("DataList_video", "" + PrefManager.DataList_video);
        PremiumList = new ArrayList<>();
        PremiumList = PrefManager.DataList_video;

        viewAllAdapter = new ViewAllPremiumAdapter(ViewAllPremiumActivity.this, PremiumList, "");
        recycler_channel.setHasFixedSize(true);
        recycler_channel.setLayoutManager(new GridLayoutManager(ViewAllPremiumActivity.this, 2));
        recycler_channel.setItemAnimator(new DefaultItemAnimator());
        recycler_channel.setAdapter(viewAllAdapter);
        viewAllAdapter.notifyDataSetChanged();

    }

}
