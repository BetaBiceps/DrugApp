package com.example.chris.drugapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by Chris on 14/04/2016.
 */
public class MyApplication extends Application {
    private static Context c;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.c = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.c;
    }
}
