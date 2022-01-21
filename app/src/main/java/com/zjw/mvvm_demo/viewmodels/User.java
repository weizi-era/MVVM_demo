package com.zjw.mvvm_demo.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.zjw.mvvm_demo.BR;


public class User extends BaseObservable {
   private String username;
   private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
}
