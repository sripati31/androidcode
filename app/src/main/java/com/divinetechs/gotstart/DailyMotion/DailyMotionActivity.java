package com.divinetechs.gotstart.DailyMotion;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.dailymotion.android.player.sdk.PlayerWebView;
import com.divinetechs.gotstart.R;

public class DailyMotionActivity extends AppCompatActivity {

    String Id;
    String url;
    String[] Video_ID;

    private PlayerWebView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.dailymotion);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
            Log.e("url", "" + url);
            getSupportActionBar().setTitle("" + bundle.getString("title"));

            if (!url.isEmpty()) {
                if (url.contains("watch")) {
                    Video_ID = url.split("v=");
                    Log.e("Video_ID[1]", "" + Video_ID[1]);
                } else if (url.contains("dailymotion.com")) {
                    Video_ID = url.split("/video/");
                    Log.e("Video_ID[1]", "" + Video_ID[1]);
                } else {
                    Toast.makeText(DailyMotionActivity.this, "dailymotion Video link is not Valid", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(DailyMotionActivity.this, "dailymotion Video link is not Valid", Toast.LENGTH_LONG).show();
            }
        }

        mVideoView = ((PlayerWebView) findViewById(R.id.dmWebVideoView));
        mVideoView.load(Video_ID[1]);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            mVideoView.onPause();
        }else{
            mVideoView.onPause();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            mVideoView.onResume();
        }else{
            mVideoView.onResume();
        }
    }

}