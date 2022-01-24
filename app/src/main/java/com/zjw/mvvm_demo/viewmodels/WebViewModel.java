package com.zjw.mvvm_demo.viewmodels;

import androidx.lifecycle.LiveData;

import com.zjw.mvvm_demo.bean.NewsDetailResponse;
import com.zjw.mvvm_demo.repository.WebRepository;

public class WebViewModel extends BaseViewModel {

    public LiveData<NewsDetailResponse> data;

    public void getNewsDetail(String uniquekey) {
        WebRepository webRepository = new WebRepository();
        data = webRepository.getNewsDetail(uniquekey);
        failed = webRepository.failed;
    }
}
