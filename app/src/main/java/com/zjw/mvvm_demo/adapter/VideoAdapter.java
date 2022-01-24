package com.zjw.mvvm_demo.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.bean.VideoResponse;
import com.zjw.mvvm_demo.databinding.ItemVideoBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VideoAdapter extends BaseQuickAdapter<VideoResponse.Result, BaseDataBindingHolder<ItemVideoBinding>> {

    public VideoAdapter(List<VideoResponse.Result> dataList) {
        super(R.layout.item_video, dataList);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemVideoBinding> bindingHolder, VideoResponse.Result result) {
        ItemVideoBinding dataBinding = bindingHolder.getDataBinding();
        if (dataBinding != null) {
            dataBinding.setVideo(result);
            dataBinding.setOnClick(new ClickBinding());
            dataBinding.executePendingBindings();
        }
    }

    public static class ClickBinding {
        public void itemClick(@NotNull VideoResponse.Result resultBean, View view) {
            if (resultBean.getShareUrl() != null) {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(resultBean.getShareUrl())));
            } else {
                Toast.makeText(view.getContext(), "视频地址为空", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
