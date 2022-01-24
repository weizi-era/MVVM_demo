package com.zjw.mvvm_demo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.tencent.mmkv.MMKV;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.zjw.mvvm_demo.db.AppDatabase;
import com.zjw.mvvm_demo.network.NetworkApi;
import com.zjw.mvvm_demo.utils.MVUtils;

import java.util.HashMap;
import java.util.Map;

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

        initX5WebView();

    }

    public static Context getContext() {
        return context;
    }

    public static AppDatabase getDatabase() {
        return database;
    }

    private void initX5WebView() {
        Map<String, Object> map = new HashMap<>();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);

        QbSdk.initTbsSettings(map);
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }
        };

        QbSdk.initX5Environment(getApplicationContext(), cb);
    }
}
