package com.zjw.mvvm_demo.ui.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.adapter.NewsAdapter;
import com.zjw.mvvm_demo.bean.NewsResponse;
import com.zjw.mvvm_demo.databinding.NewsFragmentBinding;
import com.zjw.mvvm_demo.viewmodels.NewsViewModel;

import org.jetbrains.annotations.NotNull;

public class NewsFragment extends BaseFragment {

    private NewsViewModel mViewModel;
    private NewsFragmentBinding binding;

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.news_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        mViewModel.getNews();
        binding.rv.setLayoutManager(new LinearLayoutManager(context));

        mViewModel.news.observe(context, newsResponse -> binding.rv.setAdapter(new NewsAdapter(newsResponse.getResult().getData())));

        mViewModel.failed.observe(context, this::showMsg);
    }
}