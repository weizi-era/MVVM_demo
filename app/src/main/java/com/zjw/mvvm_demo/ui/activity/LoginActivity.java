package com.zjw.mvvm_demo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.zjw.mvvm_demo.BaseApplication;
import com.zjw.mvvm_demo.Constants;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.databinding.ActivityLoginBinding;
import com.zjw.mvvm_demo.utils.MVUtils;
import com.zjw.mvvm_demo.viewmodels.LoginViewModel;
import com.zjw.mvvm_demo.viewmodels.User;

public class LoginActivity extends BaseActivity {

    LoginViewModel loginViewModel;

    ActivityLoginBinding binding;
    private User user;

    private long timeMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        user = new User("", "");
        loginViewModel.getUser().setValue(user);

        MutableLiveData<User> user1 = loginViewModel.getUser();
        user1.observe(this, user2 -> binding.setViewModel(loginViewModel));


        binding.btnLogin.setOnClickListener(v -> {
            if (loginViewModel.user.getValue().getAccount().isEmpty()) {
                Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
                return;
            }

            if (loginViewModel.user.getValue().getPwd().isEmpty()) {
                Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                return;
            }

            checkUser();
        });
    }

    private void checkUser() {
        loginViewModel.getLocalUser();
        loginViewModel.localUser.observe(this, new Observer<com.zjw.mvvm_demo.db.bean.User>() {
            @Override
            public void onChanged(com.zjw.mvvm_demo.db.bean.User localUser) {
                if (!loginViewModel.user.getValue().getAccount().equals(localUser.getAccount()) ||
                        !loginViewModel.user.getValue().getPwd().equals(localUser.getPwd())) {
                    showMsg("账号或密码错误");
                    return;
                }

                // 记录已经登录过
                MVUtils.put(Constants.IS_LOGIN, true);
                showMsg("登录成功");
                jumpActivity(MainActivity.class);
            }
        });
        loginViewModel.failed.observe(this, this::showMsg);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - timeMillis) > 2000) {
                showMsg("再按一次退出应用");
                timeMillis = System.currentTimeMillis();
            } else {
                BaseApplication.getActivityManager().finishAllActivity();
            }

            return false;
        }
        return super.onKeyDown(keyCode, event);

    }

    public void toRegister(View view) {
        jumpActivity(RegisterActivity.class);
    }
}