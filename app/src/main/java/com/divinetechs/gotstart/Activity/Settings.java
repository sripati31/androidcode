package com.divinetechs.gotstart.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.divinetechs.gotstart.BuildConfig;
import com.divinetechs.gotstart.R;
import com.divinetechs.gotstart.Utility.PrefManager;
import com.onesignal.OneSignal;

import java.io.File;

public class Settings extends AppCompatActivity {
    SwitchCompat switch_push;
    PrefManager prefManager;
    LinearLayout txt_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        prefManager = new PrefManager(Settings.this);

        switch_push = (SwitchCompat) findViewById(R.id.switch_push);
        ImageView iv_clear = (ImageView) findViewById(R.id.iv_clear);
        TextView txt_share_app = (TextView) findViewById(R.id.txt_share_app);
        TextView txt_rate_app = (TextView) findViewById(R.id.txt_rate_app);
        txt_back = (LinearLayout) findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (prefManager.getBool("PUSH")) {
            switch_push.setChecked(true);
        } else {
            switch_push.setChecked(false);
        }

        switch_push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    OneSignal.setSubscription(true);
                } else {
                    OneSignal.setSubscription(false);
                }
                prefManager.setBool("PUSH", isChecked);
            }
        });

        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String root = Settings.this.getExternalCacheDir().getAbsolutePath();
                File file = new File(root);
                if (file.isDirectory()) {
                    String[] children = file.list();
                    for (String aChildren : children) {
                        new File(file, aChildren).delete();
                    }
                    Toast.makeText(Settings.this, getResources().getString(R.string.locally_cached_data), Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "" + getResources().getString(R.string.app_name));
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        txt_rate_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + Settings.this.getPackageName())));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + Settings.this.getPackageName())));
                }
            }
        });
    }
}
