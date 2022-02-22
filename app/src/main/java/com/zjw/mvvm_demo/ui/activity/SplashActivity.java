package com.zjw.mvvm_demo.ui.activity;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.zjw.mvvm_demo.Constants;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.databinding.ActivitySplashBinding;
import com.zjw.mvvm_demo.utils.EasyAnimation;
import com.zjw.mvvm_demo.utils.MVUtils;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        setStatusBar(true);
        EasyAnimation.moveViewWidth(binding.tvTranslate, new EasyAnimation.TranslateCallback() {
            @Override
            public void animationEnd() {
                binding.tvMvvm.setVisibility(View.VISIBLE);
                jumpActivityFinish(MVUtils.getBoolean(Constants.IS_LOGIN) ? MainActivity.class : LoginActivity.class);
            }
        });
    }
}