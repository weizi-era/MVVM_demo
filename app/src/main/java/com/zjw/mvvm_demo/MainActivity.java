package com.zjw.mvvm_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.zjw.mvvm_demo.bean.BiYingResponse;
import com.zjw.mvvm_demo.bean.WallPaperResponse;
import com.zjw.mvvm_demo.databinding.ActivityMainBinding;
import com.zjw.mvvm_demo.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private MainViewModel mainViewModel;
    private WallPaperAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initView();

        initData();

    }

    private void initData() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mainViewModel.getBiYing();
        mainViewModel.getHotWall();


        mainViewModel.biYing.observe(this, response -> binding.setViewModel(mainViewModel));
        mainViewModel.hotWall.observe(this, response -> {
            binding.setViewModel(mainViewModel);
            binding.recycler.setAdapter(new WallPaperAdapter(response.getRes().getVertical()));
        });
    }

    private void initView() {
        binding.recycler.setLayoutManager(new GridLayoutManager(this, 2));

        binding.appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShow = true;
            int scrollChange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollChange == -1) {
                    scrollChange = appBarLayout.getTotalScrollRange();
                }
                if (scrollChange + verticalOffset == 0) {
                    binding.toolbarLayout.setTitle("MVVM-Demo");
                    isShow = true;
                } else if (isShow) {
                    binding.toolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });
    }
}