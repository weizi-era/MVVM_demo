package com.zjw.mvvm_demo.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.bean.RecognitionResponse;
import com.zjw.mvvm_demo.databinding.ItemRecognitionBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecognitionAdapter extends BaseQuickAdapter<RecognitionResponse.Result, BaseDataBindingHolder<ItemRecognitionBinding>> {

    public RecognitionAdapter(List<RecognitionResponse.Result> results) {
        super(R.layout.item_recognition, results);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemRecognitionBinding> bindingHolder, RecognitionResponse.Result result) {
        if (result == null) {
            return;
        }

        ItemRecognitionBinding dataBinding = bindingHolder.getDataBinding();
        if (dataBinding != null) {
            dataBinding.setRecognition(result);
            dataBinding.executePendingBindings();
        }
    }
}
