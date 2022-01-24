package com.zjw.mvvm_demo.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.databinding.ItemImageBinding;
import com.zjw.mvvm_demo.databinding.ItemWallPaperBinding;
import com.zjw.mvvm_demo.db.bean.WallPaper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ImageAdapter extends BaseQuickAdapter<WallPaper, BaseDataBindingHolder<ItemImageBinding>> {

    public ImageAdapter(List<WallPaper> data) {
        super(R.layout.item_image, data);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemImageBinding> bindingHolder, WallPaper wallPaper) {
        if (wallPaper == null) {
            return;
        }

        ItemImageBinding binding = bindingHolder.getDataBinding();
        if (binding != null) {
            binding.setWallpaper(wallPaper);
            binding.executePendingBindings();
        }
    }
}
