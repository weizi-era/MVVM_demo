package com.zjw.mvvm_demo.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.zjw.mvvm_demo.ApiService;
import com.zjw.mvvm_demo.BaseApplication;
import com.zjw.mvvm_demo.Constants;
import com.zjw.mvvm_demo.bean.NewsResponse;
import com.zjw.mvvm_demo.bean.VideoResponse;
import com.zjw.mvvm_demo.db.bean.News;
import com.zjw.mvvm_demo.db.bean.Video;
import com.zjw.mvvm_demo.network.NetworkApi;
import com.zjw.mvvm_demo.network.base.BaseObserver;
import com.zjw.mvvm_demo.network.utils.DateUtil;
import com.zjw.mvvm_demo.utils.MVUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class VideoRepository {

    private static final String TAG = VideoRepository.class.getSimpleName();

    final MutableLiveData<VideoResponse> videos = new MutableLiveData<>();

    public final MutableLiveData<String> failure = new MutableLiveData<>();

    public MutableLiveData<VideoResponse> getVideos() {
        if (MVUtils.getBoolean(Constants.IS_TODAY_REQUEST_VIDEO)) {
            if (DateUtil.getTimestamp() <= MVUtils.getLong(Constants.REQUEST_TIMESTAMP_VIDEO)) {
                getVideoFromDB();
            } else {
                requestVideoApi();
            }
        } else {
            requestVideoApi();
        }

        return videos;
    }

    @SuppressLint("CheckResult")
    private void requestVideoApi() {
        Log.d(TAG, "requestVideoApi: 从网络获取 热门视频");
        NetworkApi.createService(ApiService.class, 3).video().compose(NetworkApi.applySchedulers(new BaseObserver<VideoResponse>() {
            @Override
            protected void onSuccess(VideoResponse VideoResponse) {
                if (VideoResponse.getErrorCode() == Constants.SUCCESS) {
                    saveVideo(VideoResponse);
                    videos.postValue(VideoResponse);
                } else {
                    failure.postValue(VideoResponse.getReason());
                }

            }

            @Override
            protected void onFailure(Throwable e) {
                failure.postValue(e.getMessage());
            }
        }));
    }

    /**
     * 保存视频数据
     * @param videoResponse 视频数据
     */
    private void saveVideo(VideoResponse videoResponse) {
        MVUtils.put(Constants.IS_TODAY_REQUEST_VIDEO, true);
        MVUtils.put(Constants.REQUEST_TIMESTAMP_VIDEO, DateUtil.getMillisNextEarlyMorning());

        Completable deleteAll = BaseApplication.getDatabase().videoDao().deleteAll();
        CustomDisposable.addDisposable(deleteAll, () -> {
            Log.d(TAG, "saveVideo: 删除数据成功");
            List<Video> videoList = new ArrayList<>();
            for (VideoResponse.Result video : videoResponse.getResult()) {
                videoList.add(new Video(video.getTitle(), video.getShareUrl(), video.getAuthor(), video.getItemCover(), video.getHotWords()));
            }

            Completable insertAll = BaseApplication.getDatabase().videoDao().insertAll(videoList);
            CustomDisposable.addDisposable(insertAll, () -> Log.d(TAG, "saveVideo: 插入视频数据成功"));
        });
    }

    /**
     * 从本地数据库获取新闻数据
     */
    private void getVideoFromDB() {

        VideoResponse videoResponse = new VideoResponse();

        Flowable<List<Video>> listFlowable = BaseApplication.getDatabase().videoDao().getAll();

        List<VideoResponse.Result> dataList = new ArrayList<>();
        CustomDisposable.addDisposable(listFlowable, videoList -> {
            for (Video video : videoList) {
                VideoResponse.Result data = new VideoResponse.Result();

                data.setAuthor(video.getAuthor());
                data.setTitle(video.getTitle());
                data.setHotWords(video.getHotWords());
                data.setShareUrl(video.getShareUrl());
                data.setItemCover(video.getItemCover());

                dataList.add(data);
            }

            videoResponse.setResult(dataList);
            videos.postValue(videoResponse);
        });
    }
}
