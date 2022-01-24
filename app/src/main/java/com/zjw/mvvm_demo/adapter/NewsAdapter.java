package com.zjw.mvvm_demo.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.bean.NewsResponse;
import com.zjw.mvvm_demo.databinding.ItemNewsBinding;
import com.zjw.mvvm_demo.ui.activity.WebActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NewsAdapter extends BaseQuickAdapter<NewsResponse.Result.Data, BaseDataBindingHolder<ItemNewsBinding>> {

    public NewsAdapter(List<NewsResponse.Result.Data> dataList) {
        super(R.layout.item_news, dataList);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemNewsBinding> bindingHolder, NewsResponse.Result.Data data) {
        ItemNewsBinding dataBinding = bindingHolder.getDataBinding();
        if (dataBinding != null) {
            dataBinding.setNews(data);
            dataBinding.setOnClick(new ClickBinding());
            dataBinding.executePendingBindings();
        }
    }

    public static class ClickBinding {
        public void itemClick(NewsResponse.Result.Data dataBean, View view) {
            if("1".equals(dataBean.getIsContent())){
                Intent intent = new Intent(view.getContext(), WebActivity.class);
                intent.putExtra("uniquekey", dataBean.getUniquekey());
                view.getContext().startActivity(intent);
            } else {
                Toast.makeText(view.getContext(), "没有详情信息", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
