package com.zjw.mvvm_demo.repository;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

import com.zjw.mvvm_demo.ApiService;
import com.zjw.mvvm_demo.Constants;
import com.zjw.mvvm_demo.bean.NewsDetailResponse;
import com.zjw.mvvm_demo.network.NetworkApi;
import com.zjw.mvvm_demo.network.base.BaseObserver;

public class WebRepository {

    final MutableLiveData<NewsDetailResponse> newsDetail = new MutableLiveData<>();

    public final MutableLiveData<String> failed = new MutableLiveData<>();

    @SuppressLint("CheckResult")
    public MutableLiveData<NewsDetailResponse> getNewsDetail(String uniquekey) {
        NetworkApi.createService(ApiService.class, 2).newsDetail(uniquekey).compose(NetworkApi.applySchedulers(new BaseObserver<NewsDetailResponse>() {
            @Override
            protected void onSuccess(NewsDetailResponse newsDetailResponse) {
                if (newsDetailResponse.getErrorCode() == Constants.SUCCESS) {
                    newsDetail.setValue(newsDetailResponse);
                } else {
                    failed.setValue(newsDetailResponse.getReason());
                }
            }

            @Override
            protected void onFailure(Throwable e) {
                failed.setValue(e.getMessage());
            }
        }));

        return newsDetail;
    }
}
