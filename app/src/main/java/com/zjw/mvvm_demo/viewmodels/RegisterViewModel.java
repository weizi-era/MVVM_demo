package com.zjw.mvvm_demo.viewmodels;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.zjw.mvvm_demo.db.bean.User;
import com.zjw.mvvm_demo.repository.UserRepository;

public class RegisterViewModel extends BaseViewModel {
    public MutableLiveData<User> user;

    public MutableLiveData<User> getUser() {
        if (user == null) {
            user = new MutableLiveData<>();
        }

        return user;
    }

    /**
     * 注册
     */
    public void register() {
        UserRepository userRepository = new UserRepository();
        failed = userRepository.failure;
        user.getValue().setUid(1);
        Log.d("TAG", "register: "+new Gson().toJson(user.getValue()));
        userRepository.saveUser(user.getValue());
    }
}
