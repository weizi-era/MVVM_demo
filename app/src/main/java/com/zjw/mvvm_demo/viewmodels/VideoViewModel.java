package com.zjw.mvvm_demo.viewmodels;

import androidx.lifecycle.LiveData;

import com.zjw.mvvm_demo.bean.VideoResponse;
import com.zjw.mvvm_demo.repository.VideoRepository;

public class VideoViewModel extends BaseViewModel {

    public LiveData<VideoResponse> video;

    public void getVideo() {
        VideoRepository videoRepository = new VideoRepository();
        video = videoRepository.getVideos();
        failed = videoRepository.failure;
    }
}