package com.divinetechs.gotstart.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.divinetechs.gotstart.Model.GeneralSettings.GeneralSettings;
import com.divinetechs.gotstart.R;
import com.divinetechs.gotstart.Utility.ConnectivityReceiver;
import com.divinetechs.gotstart.Utility.MyApp;
import com.divinetechs.gotstart.Utility.PrefManager;
import com.divinetechs.gotstart.Webservice.AppAPI;
import com.divinetechs.gotstart.Webservice.BaseURL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    Intent mainIntent;

    ProgressDialog progressDialog;
    PrefManager prefManager;

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash);

        prefManager = new PrefManager(SplashActivity.this);
        progressDialog = new ProgressDialog(SplashActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        checkConnection();

//        boolean isConnected = ConnectivityReceiver.isConnected();
//        if (isConnected) {
//            general_settings();
//        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
            } else {
                Permission();
            }
        } else {
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void Permission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Intent intent_status = new Intent(getApplicationContext(), PermissionActivity.class);
                    startActivityForResult(intent_status, PERMISSION_REQUEST_CODE);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                } else {
                    Intent intent_status = new Intent(getApplicationContext(), PermissionActivity.class);
                    startActivityForResult(intent_status, PERMISSION_REQUEST_CODE);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            } else {
            }
        } else {
            boolean isConnected = ConnectivityReceiver.isConnected();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                    boolean isConnected = ConnectivityReceiver.isConnected();
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void general_settings() {
        progressDialog.show();
        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<GeneralSettings> call = bookNPlayAPI.general_settings();
        call.enqueue(new Callback<GeneralSettings>() {
            @Override
            public void onResponse(Call<GeneralSettings> call, Response<GeneralSettings> response) {
                if (response.code() == 200) {

                    prefManager = new PrefManager(SplashActivity.this);

                    for (int i = 0; i < response.body().getResult().size(); i++) {
                        Log.e("==>", "" + response.body().getResult().get(i).getValue());
                        prefManager.setValue(response.body().getResult().get(i).getKey(), response.body().getResult().get(i).getValue());
                    }

                    if (!prefManager.isFirstTimeLaunch()) {
                        if (prefManager.getLoginId().equalsIgnoreCase("0"))
                            mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                        else
                            mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                    } else {
                        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GeneralSettings> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;

            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        Log.e("called", "onResume");
        MyApp.getInstance().setConnectivityListener(this);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                boolean isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    general_settings();
                }
            } else {
            }
        } else {
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
