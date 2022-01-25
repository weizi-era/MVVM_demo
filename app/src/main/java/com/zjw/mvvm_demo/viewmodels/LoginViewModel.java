package com.zjw.mvvm_demo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zjw.mvvm_demo.bean.User;
import com.zjw.mvvm_demo.repository.UserRepository;

public class LoginViewModel extends BaseViewModel {

    public MutableLiveData<User> user;
    public LiveData<com.zjw.mvvm_demo.db.bean.User> localUser;

    public MutableLiveData<User> getUser() {
        if (user == null) {
            user = new MutableLiveData<>();
        }
        return user;
    }

    public void getLocalUser() {
        UserRepository userRepository = new UserRepository();
        localUser = userRepository.getUser();
        failed = userRepository.failure;
    }
}
