package com.zjw.mvvm_demo.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zjw.mvvm_demo.BaseApplication;
import com.zjw.mvvm_demo.utils.PermissionUtils;
import com.zjw.mvvm_demo.view.dialog.LoadingDialog;

public class BaseActivity extends AppCompatActivity {

    protected AppCompatActivity context;
    private LoadingDialog loadingDialog;

    /**
     * 打开相册请求码
     */
    protected static final int SELECT_PHOTO_CODE = 2000;

    /**
     * 打开相机请求码
     */
    protected static final int TAKE_PHOTO_CODE = 2001;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        BaseApplication.getActivityManager().addActivity(this);
        PermissionUtils.getInstance();
    }

    protected void showMsg(CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showLongMsg(CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 跳转页面
     * @param clazz 目标页面
     */
    protected void jumpActivity(final Class<?> clazz) {
        startActivity(new Intent(context, clazz));
    }

    /**
     * 跳转页面并关闭当前页面
     * @param clazz 目标页面
     */
    protected void jumpActivityFinish(final Class<?> clazz) {
        startActivity(new Intent(context, clazz));
        finish();
    }

    /**
     * 状态栏文字图标颜色
     * @param dark 深色 false 为浅色
     */
    protected void setStatusBar(boolean dark) {
        View decor = getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    /**
     * 显示加载框
     */
    protected void showLoading() {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
    }

    /**
     * 显示加载弹窗
     *
     * @param isClose true 则点击其他区域弹窗关闭， false 不关闭。
     */
    protected void showLoading(boolean isClose) {
        loadingDialog = new LoadingDialog(this, isClose);
    }

    /**
     * 隐藏加载弹窗
     */
    protected void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 当前是否在Android11.0及以上
     */
    protected boolean isAndroid11() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
    }

    /**
     * 当前是否在Android10.0及以上
     */
    protected boolean isAndroid10() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    /**
     * 当前是否在Android7.0及以上
     */
    protected boolean isAndroid7() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    /**
     * 当前是否在Android6.0及以上
     */
    protected boolean isAndroid6() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    protected boolean isStorageManager() {
        return Environment.isExternalStorageManager();
    }

    protected boolean hasPermission(String permissionName) {
        return PermissionUtils.hasPermission(this, permissionName);
    }

    protected void requestPermission(String permissionName) {
        PermissionUtils.requestPermission(this, permissionName);
    }

    /**
     * 请求外部存储管理 Android11版本时获取文件读写权限时调用
     */
    protected void requestManageExternalStorage() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, PermissionUtils.REQUEST_MANAGE_EXTERNAL_STORAGE_CODE);
    }


}
