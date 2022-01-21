package com.zjw.mvvm_demo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.zjw.mvvm_demo.databinding.ActivityLoginBinding;
import com.zjw.mvvm_demo.viewmodels.LoginViewModel;
import com.zjw.mvvm_demo.viewmodels.User;

public class LoginActivity extends AppCompatActivity {

    LoginViewModel loginViewModel;

    ActivityLoginBinding binding;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        user = new User( binding.etUsername.getText().toString(), binding.etPassword.getText().toString());


        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getUser().setValue(user);

        MutableLiveData<User> user1 = loginViewModel.getUser();
        user1.observe(this, user2 -> binding.setViewModel(loginViewModel));


        binding.btLogin.setOnClickListener(v -> {
            if (loginViewModel.user.getValue().getUsername().isEmpty()) {
                Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
                return;
            }

            if (loginViewModel.user.getValue().getPassword().isEmpty()) {
                Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        });
    }
}