package com.divinetechs.gotstart.Activity;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.divinetechs.gotstart.BuildConfig;
import com.divinetechs.gotstart.Fragment.Home;
import com.divinetechs.gotstart.Fragment.MovieHome;
import com.divinetechs.gotstart.Fragment.NewsHome;
import com.divinetechs.gotstart.Fragment.SportsHome;
import com.divinetechs.gotstart.Fragment.TVHome;
import com.divinetechs.gotstart.Model.ProfileModel.ProfileModel;
import com.divinetechs.gotstart.PushNotification.Config;
import com.divinetechs.gotstart.PushNotification.NotificationUtils;
import com.divinetechs.gotstart.R;
import com.divinetechs.gotstart.Utility.PrefManager;
import com.divinetechs.gotstart.Webservice.AppAPI;
import com.divinetechs.gotstart.Webservice.BaseURL;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private PrefManager prefManager;
    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private InterstitialAd interstitial;
    BottomNavigationView bottomNavigationView;
    public static MenuItem item;
    ProgressDialog progressDialog;
    TextView txt_username;

    LinearLayout ly_admob_banner;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        prefManager = new PrefManager(this);

        View header = navigationView.getHeaderView(0);
        txt_username = (TextView) header.findViewById(R.id.textView);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        ly_admob_banner = (LinearLayout) findViewById(R.id.ly_admob_banner);


        Menu menu = navigationView.getMenu();
        MenuItem nav_logout = menu.findItem(R.id.nav_logout);
        if (!prefManager.getLoginId().equalsIgnoreCase("0"))
            nav_logout.setTitle("LogOut");
        else
            nav_logout.setTitle("Login");

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setItemIconTintList(null);

        if (bottomNavigationView != null) {
            // Set action to perform when any menu-item is selected.
            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            // Write code to perform some actions.
                            Log.e("item", "" + item);
                            selectFragment(item);
                            return false;
                        }
                    });
        }


        pushFragment(new TVHome());
        PushInit();


        Log.e("banner_ad", "" + prefManager.getValue("banner_ad"));
        if (prefManager.getValue("banner_ad").equalsIgnoreCase("yes")) {
            Admob_Banner();
            ly_admob_banner.setVisibility(View.VISIBLE);
        } else {
            ly_admob_banner.setVisibility(View.GONE);
        }

        Log.e("interstital_ad", "" + prefManager.getValue("interstital_ad"));
        if (prefManager.getValue("interstital_ad").equalsIgnoreCase("yes")) {
            rewardAds();
        }

        if (!prefManager.getLoginId().equalsIgnoreCase("0")) {
            Get_Profile();
        }

    }

    private void Get_Profile() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<ProfileModel> call = bookNPlayAPI.profile("" + prefManager.getLoginId());
        call.enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                if (response.code() == 200) {
                    Log.e("==>", "" + response.body().getResult().get(0).getFullname());
                    txt_username.setText(response.body().getResult().get(0).getFullname());
//                    txt_name.setText(response.body().getResult().get(0).getFullname());
//                    txt_email.setText(response.body().getResult().get(0).getEmail());
//                    txt_Contact.setText(response.body().getResult().get(0).getMobileNumber());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

//        item = menu.findItem(R.id.action_search);
//        item.setTitle("00 pts");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void selectFragment(MenuItem item) {

        item.setChecked(true);

        switch (item.getItemId()) {
//            case R.id.bottom_home:
//                pushFragment(new Home());
//                break;
            case R.id.bottom_tv:
                pushFragment(new TVHome());
                break;
            case R.id.bottom_movie:
                pushFragment(new MovieHome());
                break;
            case R.id.bottom_sport:
                pushFragment(new SportsHome());
                break;
            case R.id.bottom_news:
                pushFragment(new NewsHome());
                break;
            case R.id.bottom_premium:
                pushFragment(new com.divinetechs.gotstart.Fragment.Premium());
                break;
        }
    }

    protected void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.rootLayout, fragment);
                ft.commit();
            }
        }
    }

    public void rateMe() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + this.getPackageName())));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }

    public void ShareMe() {
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

    public void logout() {
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogDanger))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage("Are you sure you want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prefManager.setLoginId("0");
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    public void PushInit() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };
        displayFirebaseRegId();
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e(TAG, "Firebase reg id: " + regId);
        if (!TextUtils.isEmpty(regId))
            Log.e(TAG, "Firebase reg id: " + regId);
        else
            Log.e(TAG, "Firebase Reg Id is not received yet!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void Admob_Banner(){
        AdView adView = new AdView(this);
        adView.setAdSize(com.google.android.gms.ads.AdSize.BANNER.SMART_BANNER);
        adView.setAdUnitId(""+prefManager.getValue("banner_adid"));
        // Create an ad request.
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
        adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
        ly_admob_banner.addView(adView);;
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }


    private void rewardAds() {
        InterstitialAd interstitial = new InterstitialAd(MainActivity.this);
        interstitial.setAdUnitId(""+prefManager.getValue("interstital_adid"));
        interstitial.loadAd(new AdRequest.Builder().build());
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                if (interstitial.isLoaded()) {
                    interstitial.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("onAdFailedToLoad2=>", "" + errorCode);
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdClosed() {
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navigation_home) {
            pushFragment(new TVHome());
        } else if (id == R.id.navigation_channel) {
            startActivity(new Intent(MainActivity.this, ChannelActivity.class));
        } else if (id == R.id.navigation_audio) {
            startActivity(new Intent(MainActivity.this, AudioActivity.class));
        } else if (id == R.id.navigation_profile) {
            if (prefManager.getLoginId().equalsIgnoreCase("0")) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this, Profile.class));
            }
        } else if (id == R.id.navigation_watchlist) {
            startActivity(new Intent(MainActivity.this, WatchList.class));
        } else if (id == R.id.navigation_download) {
            startActivity(new Intent(MainActivity.this, DownloadList.class));
        } else if (id == R.id.navigation_premium) {
            startActivity(new Intent(MainActivity.this, Subscription.class));
        } else if (id == R.id.nav_share) {
            startActivity(new Intent(MainActivity.this, Settings.class));
        } else if (id == R.id.nav_rate) {
            rateMe();
        } else if (id == R.id.nav_logout) {
            if (prefManager.getLoginId().equalsIgnoreCase("0")) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            } else {
                logout();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
