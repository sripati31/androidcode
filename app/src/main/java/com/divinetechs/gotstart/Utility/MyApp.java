package com.divinetechs.gotstart.Utility;

import android.app.Application;

import com.divinetechs.gotstart.R;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import com.onesignal.OneSignal;

public class MyApp extends Application {

    private static MyApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        MobileAds.initialize(this, getResources().getString(R.string.admob_main_id));
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        FirebaseApp.initializeApp(this);

    }


    public static synchronized MyApp getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}