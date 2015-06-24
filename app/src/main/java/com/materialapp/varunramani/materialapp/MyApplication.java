package com.materialapp.varunramani.materialapp;

import android.app.Application;
import android.content.Context;

import database.MoviesDatabase;

/**
 * Created by Varun Ramani on 3/15/2015.
 */
public class MyApplication extends Application {

    private static MyApplication sInstance;
    public static final String API_KEY_ROTTEN_TOMATOES = "xt4d32dz8ad8bv9nx29rx4dq";//"nyy5ajb9nkrn24ex67e7vvcp";
    public static MoviesDatabase mDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mDatabase = new MoviesDatabase(this);
    }

    public synchronized static MoviesDatabase getWritableDatabase() {
        if (mDatabase == null) {
            mDatabase = new MoviesDatabase(getAppContext());
        }
        return mDatabase;
    }

    public static MyApplication getsInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }


}
