package com.divinetechs.gotstart.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;
import android.view.Window;


import com.divinetechs.gotstart.Model.TvSerialModel.Result;
import com.divinetechs.gotstart.R;

import java.util.ArrayList;
import java.util.List;

public class PrefManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;
    public static String pushRID = "0";
    // Shared preferences file name
    private static final String PREF_NAME = "androidhive-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String LOGIN_ID = "LOGIN";
    private static final String PREMIUM_ID = "PREMIUM";
    public static String Type = "image";

    public static List<Result> DataList = new ArrayList<>();
    public static List<com.divinetechs.gotstart.Model.TvVideoModel.Result> DataList_video = new ArrayList<>();

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public void setBool(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBool(String key) {
        return pref.getBoolean(key, true);
    }

    public void setValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getValue(String key) {
        return pref.getString(key, "0");
    }

    public void setLoginId(String id) {
        editor.putString(LOGIN_ID, id);
        editor.commit();
    }

    public static void forceRTLIfSupported(Window window, Activity activity) {
        if (activity.getResources().getString(R.string.isRTL).equals("true")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                window.getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }
    }

    public String getLoginId() {
        return pref.getString(LOGIN_ID, "0");
    }

    public String getPremiumID() {
        return pref.getString(PREMIUM_ID, "0");
    }

    public void setPremiumID(String id) {
        editor.putString(PREMIUM_ID, id);
        editor.commit();
        editor.apply();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}
