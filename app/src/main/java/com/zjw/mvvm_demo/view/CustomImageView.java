package com.zjw.mvvm_demo.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.zjw.mvvm_demo.BaseApplication;


public class CustomImageView extends ShapeableImageView {

    public CustomImageView(@NonNull Context context) {
        super(context);
    }

    public CustomImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 必应壁纸  因为拿到的url不完整，因此需要做一次地址拼接
     * @param imageView 图片视图
     * @param url 网络url
     */
    @BindingAdapter(value = "biyingUrl", requireAll = false)
    public static void setBiyingUrl(ImageView imageView, String url) {
        String assembleUrl = "http://cn.bing.com" + url;
        Glide.with(BaseApplication.getContext()).load(assembleUrl).into(imageView);
    }

    /**
     * 普通网络地址图片
     * @param imageView 图片视图
     * @param url 网络url
     */
    @BindingAdapter(value = {"networkUrl"}, requireAll = false)
    public static void setNetworkUrl(ImageView imageView, String url) {
        Glide.with(BaseApplication.getContext()).load(url).into(imageView);
    }
}
