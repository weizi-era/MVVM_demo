package com.zjw.mvvm_demo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.zjw.mvvm_demo.bean.BiYingResponse;
import com.zjw.mvvm_demo.bean.WallPaperResponse;
import com.zjw.mvvm_demo.repository.MainRepository;


public class MainViewModel extends BaseViewModel {
    public LiveData<BiYingResponse> biYing;
    public LiveData<WallPaperResponse> hotWall;

    public void getBiYing() {
        biYing = new MainRepository().getBiYing();
    }

    public void getHotWall() {
        hotWall = new MainRepository().getHotWall();
    }


}
