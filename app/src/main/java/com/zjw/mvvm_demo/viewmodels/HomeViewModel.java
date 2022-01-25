package com.zjw.mvvm_demo.viewmodels;

import androidx.lifecycle.LiveData;

import com.zjw.mvvm_demo.db.bean.User;
import com.zjw.mvvm_demo.repository.UserRepository;

public class HomeViewModel extends BaseViewModel {

    public LiveData<User> user;

    public String defaultName = "初学者-Study";
    public String defaultIntroduction = "Android | Java";

    public void getUser() {
        user = UserRepository.getInstance().getUser();
    }

    public void updateUser(User user) {
        UserRepository.getInstance().updateUser(user);
        failed = UserRepository.getInstance().failure;
        getUser();
    }
}
