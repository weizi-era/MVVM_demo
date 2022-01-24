package com.zjw.mvvm_demo.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavAction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.databinding.ActivityHomeBinding;

import org.jetbrains.annotations.NotNull;

public class HomeActivity extends BaseActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        initView();
    }

    private void initView() {

        //获取navController
        NavController controller = Navigation.findNavController(this, R.id.nav_host_fragment);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.news_fragment:
                    binding.tvTitle.setText("头条新闻");
                    controller.navigate(R.id.news_fragment);
                    break;
                case R.id.video_fragment:
                    binding.tvTitle.setText("热门视频");
                    controller.navigate(R.id.video_fragment);
                    break;
            }

            return true;
        });

        binding.ivAvatar.setOnClickListener(v -> binding.drawerLayout.open());
        binding.naviView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) item -> {
            switch (item.getItemId()) {
                case R.id.item_setting:
                    break;
                case R.id.item_logout:
                    break;
            }

            return true;
        });
    }
}