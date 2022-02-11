package com.zjw.mvvm_demo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import androidx.annotation.NonNull;

import com.pgyer.pgyersdk.PgyerSDKManager;
import com.pgyer.pgyersdk.pgyerenum.Features;
import com.tencent.mmkv.MMKV;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.zjw.mvvm_demo.db.AppDatabase;
import com.zjw.mvvm_demo.network.NetworkApi;
import com.zjw.mvvm_demo.ui.activity.ActivityManager;
import com.zjw.mvvm_demo.utils.LocalManageUtils;
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
        MMKV.initialize(base);
        MVUtils.getInstance();
        // 设置系统当前语言
        LocalManageUtils.setSystemCurrentLanguage();
        super.attachBaseContext(LocalManageUtils.setLocale(base));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        //设置App的语言
        LocalManageUtils.setAppLanguage(this);

        initPgyerSDK(this);
        NetworkApi.init(new NetworkRequiredInfo(this));

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

    public static ActivityManager getActivityManager() {
        return ActivityManager.getInstance();
    }

    /**
     *  初始化蒲公英SDK
     * @param application
     */
    private static void initPgyerSDK(Application application) {
        new PgyerSDKManager.Init()
                .setContext(application) //设置上下问对象
                .enable(Features.CHECK_UPDATE) //开启自动更新检测
                .start();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //通过全局的上下文参数更改相关资源配置
        LocalManageUtils.onConfigurationChanged(getApplicationContext());
    }
}
