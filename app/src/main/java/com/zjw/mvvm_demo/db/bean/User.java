package com.zjw.mvvm_demo.db.bean;

import androidx.databinding.BaseObservable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.zjw.mvvm_demo.BR;
import androidx.databinding.Bindable;

@Entity(tableName = "user")
public class User extends BaseObservable {

    @PrimaryKey
    private int uid;
    private String account;
    private String pwd;

    @Ignore
    private String confirmPwd;
    private String nickname;
    private String introduction;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Bindable
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
        notifyPropertyChanged(BR.account);
    }

    @Bindable
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
        notifyPropertyChanged(BR.pwd);
    }

    @Bindable
    public String getConfirmPwd() {
        return confirmPwd;
    }

    public void setConfirmPwd(String confirmPwd) {
        this.confirmPwd = confirmPwd;
        notifyPropertyChanged(BR.confirmPwd);
    }

    @Bindable
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        notifyPropertyChanged(BR.nickname);
    }

    @Bindable
    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
        notifyPropertyChanged(BR.introduction);
    }

    public User() {
    }

    @Ignore
    public User(int uid, String account, String pwd, String confirmPwd, String nickname, String introduction) {
        this.uid = uid;
        this.account = account;
        this.pwd = pwd;
        this.confirmPwd = confirmPwd;
        this.nickname = nickname;
        this.introduction = introduction;
    }
}
