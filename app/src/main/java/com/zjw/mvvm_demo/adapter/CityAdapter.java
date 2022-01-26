package com.zjw.mvvm_demo.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.databinding.ItemCityBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CityAdapter extends BaseQuickAdapter<String, BaseDataBindingHolder<ItemCityBinding>> {

    public CityAdapter(@Nullable List<String> data) {
        super(R.layout.item_city, data);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemCityBinding> bindingHolder, String city) {
        ItemCityBinding dataBinding = bindingHolder.getDataBinding();
        if (dataBinding != null) {
            dataBinding.setCityName(city);
            dataBinding.executePendingBindings();
        }
    }
}
