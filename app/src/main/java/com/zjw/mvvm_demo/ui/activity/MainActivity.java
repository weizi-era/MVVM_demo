package com.zjw.mvvm_demo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.adapter.WallPaperAdapter;
import com.zjw.mvvm_demo.databinding.ActivityMainBinding;
import com.zjw.mvvm_demo.viewmodels.MainViewModel;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;
    private MainViewModel mainViewModel;

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
        mainViewModel.hotWall.observe(this, response -> binding.recycler.setAdapter(new WallPaperAdapter(response.getRes().getVertical())));
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

        //页面上下滑动监听
        binding.scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                binding.fabHome.hide();
            } else {
                binding.fabHome.show();
            }
        });
    }

    public void goHome(View view) {
        jumpActivity(HomeActivity.class);
    }
}