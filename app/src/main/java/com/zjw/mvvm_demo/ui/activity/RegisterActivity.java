package com.zjw.mvvm_demo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.databinding.ActivityRegisterBinding;
import com.zjw.mvvm_demo.db.bean.User;
import com.zjw.mvvm_demo.viewmodels.RegisterViewModel;

public class RegisterActivity extends BaseActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        viewModel.getUser().setValue(new User(0, "", "", "", "", ""));
        binding.setRegister(viewModel);

        initView();
    }

    private void initView() {
        //back(binding.toolbar);
        binding.btnRegister.setOnClickListener(v -> {
            if (viewModel.user.getValue().getAccount().isEmpty()) {
                showMsg("请输入账号");
                return;
            }
            if (viewModel.user.getValue().getPwd().isEmpty()) {
                showMsg("请输入密码");
                return;
            }
            if (viewModel.user.getValue().getConfirmPwd().isEmpty()) {
                showMsg("请确认密码");
                return;
            }
            if (!viewModel.user.getValue().getPwd().equals(viewModel.user.getValue().getConfirmPwd())) {
                showMsg("两次输入密码不一致");
                return;
            }

            viewModel.register();
            viewModel.failed.observe(this, failed -> {
                showMsg("200".equals(failed) ? "注册成功" : failed);
                if ("200".equals(failed)) {
                    finish();
                }
            });
        });
    }
}