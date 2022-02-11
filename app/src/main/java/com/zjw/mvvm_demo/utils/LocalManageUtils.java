package com.zjw.mvvm_demo.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.Log;


import com.zjw.mvvm_demo.R;

import java.util.Locale;

/**
 * 本地管理工具类
 *
 * @author llw
 */
public class LocalManageUtils {

    private static final String TAG = "LocalManage";

    /**
     * 获取系统的 locale对象
     *
     * @return locale对象
     */
    public static Locale getSystemLocal() {

        return MVUtils.getSystemCurrentLocal();
    }

    /**
     * 获取当前APP使用的语言
     *
     * @param context 上下文参数
     * @return 语言类型描述
     */
    public static String getSelectLanguage(Context context) {
        //根据缓存中的语言返回当前设置的语言类型文字
        switch (MVUtils.getLanguage()) {
            case 0:
                return context.getString(R.string.system_lang);
            case 2:
                return context.getString(R.string.english);
            case 3:
                return context.getString(R.string.traditional_chinese);
            case 1:
            default:
                return context.getString(R.string.chinese);
        }
    }

    /**
     * 获取选择的语言设置
     *
     * @return locale
     */
    public static Locale getSelectLanguageLocal() {
        switch (MVUtils.getLanguage()) {
            case 0:
                return getSystemLocal();//系统语言
            case 2:
                return Locale.ENGLISH;//英文
            case 3:
                return Locale.TRADITIONAL_CHINESE;//中文繁体
            case 1:
            default:
                return Locale.SIMPLIFIED_CHINESE;//中文
        }
    }

    /**
     * 设置选中的语言
     *
     * @param context 上下文参数
     * @param select  选中的语言项
     */
    public static void setSelectLanguage(Context context, int select) {
        //放入缓存中
        MVUtils.setLanguage(select);
        //设置APP语言
        setAppLanguage(context);
    }

    /**
     * 设置App语言
     *
     * @param context 上下文
     */
    public static void setAppLanguage(Context context) {
        //通过应用全局上下文获取资源对象
        Resources resources = context.getApplicationContext().getResources();
        //获取资源配置对象
        Configuration config = resources.getConfiguration();
        //获取系统本地对象
        Locale locale = getSelectLanguageLocal();
        //配置本地资源
        config.setLocale(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//Android7.0及以上
            //获取本地语言列表
            LocaleList localeList = new LocaleList(locale);
            //设置默认语言列表
            LocaleList.setDefault(localeList);
            //设置语言环境列表
            config.setLocales(localeList);
            //创建配置系统的上下文参数
            context.getApplicationContext().createConfigurationContext(config);
            //设置默认语言
            Locale.setDefault(locale);
        }
        //更新资源配置
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    /**
     * 设置本地语言
     *
     * @param context 上下文参数
     * @return 设置后的对象
     */
    public static Context setLocale(Context context) {
        return updateResource(context, getSelectLanguageLocal());
    }

    /**
     * 更新资源
     *
     * @param context 上下文参数
     * @param locale  本地语言
     * @return 上下文
     */
    private static Context updateResource(Context context, Locale locale) {
        //设置默认语言
        Locale.setDefault(locale);
        //通过上下文获取资源对象
        Resources resources = context.getResources();
        //资源配置对象
        Configuration config = new Configuration(resources.getConfiguration());
        //Android4.2及以上
        //设置语言
        config.setLocale(locale);
        //获取设置之后的上下文参数
        context = context.createConfigurationContext(config);
        return context;
    }

    /**
     * 设置系统当前语言
     *
     */
    public static void setSystemCurrentLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//Android7.0及以上
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        Log.d(TAG, locale.getLanguage());
        //设置应用当前本地语言
        MVUtils.setSystemCurrentLocal(locale);
    }

    /**
     * 更改应用配置
     * @param context 上下文参数
     */
    public static void onConfigurationChanged(Context context) {
        setSystemCurrentLanguage();
        setLocale(context);
        setAppLanguage(context);
    }
}

