package com.zjw.mvvm_demo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.zjw.mvvm_demo.bean.NewsResponse;
import com.zjw.mvvm_demo.repository.NewsRepository;

public class NewsViewModel extends BaseViewModel {

    public LiveData<NewsResponse> news;

    public void getNews() {
        NewsRepository newsRepository = new NewsRepository();
        news = newsRepository.getNews();
        failed = newsRepository.failure;
    }
}