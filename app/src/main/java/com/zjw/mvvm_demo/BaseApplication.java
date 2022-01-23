package com.zjw.mvvm_demo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.tencent.mmkv.MMKV;
import com.zjw.mvvm_demo.db.AppDatabase;
import com.zjw.mvvm_demo.network.NetworkApi;
import com.zjw.mvvm_demo.utils.MVUtils;

public class BaseApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    public static Context context;

    // 数据库
    public static AppDatabase database;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        NetworkApi.init(new NetworkRequiredInfo(this));
        MMKV.initialize(this);
        MVUtils.getInstance();
        database = AppDatabase.getInstance(this);
    }

    public static Context getContext() {
        return context;
    }

    public static AppDatabase getDatabase() {
        return database;
    }
}
