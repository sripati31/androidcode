package com.divinetechs.gotstart.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.divinetechs.gotstart.Adapter.DownloadListAdapter;
import com.divinetechs.gotstart.Adapter.WatchListAdapter;
import com.divinetechs.gotstart.Interface.Watchinter;
import com.divinetechs.gotstart.Model.DownloadModel.DownloadModel;
import com.divinetechs.gotstart.Model.DownloadModel.Result;
import com.divinetechs.gotstart.Model.SuccessModel.SuccessModel;
import com.divinetechs.gotstart.R;
import com.divinetechs.gotstart.Utility.PrefManager;
import com.divinetechs.gotstart.Webservice.AppAPI;
import com.divinetechs.gotstart.Webservice.BaseURL;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadList extends AppCompatActivity implements Watchinter {

    RecyclerView recycler_search;
    List<Result> DownloadList;
    DownloadListAdapter downloadListAdapter;

    PrefManager prefManager;
    ProgressDialog progressDialog;
    LinearLayout bottom_sheet;

    TextView txt_delete, txt_tite,toolbar_title;
    Button btn_close;
    String w_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.downloadlist);

        prefManager = new PrefManager(DownloadList.this);
        progressDialog = new ProgressDialog(DownloadList.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        recycler_search = (RecyclerView) findViewById(R.id.recycler_search);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("My Download");

        TextView txt_back = (TextView) findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bottom_sheet = (LinearLayout) findViewById(R.id.bottom_sheet);

        txt_tite = (TextView) findViewById(R.id.txt_tite);
        txt_delete = (TextView) findViewById(R.id.txt_delete);
        btn_close = (Button) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_sheet.setVisibility(View.GONE);
            }
        });

        txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
                Call<SuccessModel> call = bookNPlayAPI.remove_watchlist(w_id);
                call.enqueue(new Callback<SuccessModel>() {
                    @Override
                    public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                        Log.e("response", "" + response.body());
                        progressDialog.dismiss();
                        bottom_sheet.setVisibility(View.GONE);
                        Toast.makeText(DownloadList.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT)
                                .show();
                        getdownloadlist();
                    }

                    @Override
                    public void onFailure(Call<SuccessModel> call, Throwable t) {
                        progressDialog.dismiss();
                    }
                });
            }
        });

        getdownloadlist();

    }

    private void getdownloadlist() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<DownloadModel> call = bookNPlayAPI.getdownload("" + prefManager.getLoginId());
        call.enqueue(new Callback<DownloadModel>() {
            @Override
            public void onResponse(Call<DownloadModel> call, Response<DownloadModel> response) {

                DownloadList = new ArrayList<>();
                DownloadList = response.body().getResult();
                Log.e("DownloadList", "" + DownloadList.size());
                downloadListAdapter = new DownloadListAdapter(DownloadList.this, DownloadList,
                        "episode", DownloadList.this);
                recycler_search.setHasFixedSize(true);
                recycler_search.setLayoutManager(new GridLayoutManager(DownloadList.this, 2));
                recycler_search.setItemAnimator(new DefaultItemAnimator());
                recycler_search.setAdapter(downloadListAdapter);
                downloadListAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<DownloadModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void remove_item(String id) {
        w_id = id;
        Log.e("d_id", "" + w_id);

        bottom_sheet.setVisibility(View.VISIBLE);

        for (int i = 0; i < DownloadList.size(); i++) {
            if (DownloadList.get(i).getDId().equalsIgnoreCase(w_id)) {
                txt_tite.setText(DownloadList.get(i).getTvvName());
            }
        }
    }
}
