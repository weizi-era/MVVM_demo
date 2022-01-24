package com.zjw.mvvm_demo.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.zjw.mvvm_demo.ApiService;
import com.zjw.mvvm_demo.BaseApplication;
import com.zjw.mvvm_demo.Constants;
import com.zjw.mvvm_demo.bean.NewsResponse;
import com.zjw.mvvm_demo.db.bean.News;
import com.zjw.mvvm_demo.network.NetworkApi;
import com.zjw.mvvm_demo.network.base.BaseObserver;
import com.zjw.mvvm_demo.network.utils.DateUtil;
import com.zjw.mvvm_demo.utils.MVUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class NewsRepository {

    private static final String TAG = NewsRepository.class.getSimpleName();

    final MutableLiveData<NewsResponse> news = new MutableLiveData<>();

    public final MutableLiveData<String> failure = new MutableLiveData<>();

    public MutableLiveData<NewsResponse> getNews() {
        if (MVUtils.getBoolean(Constants.IS_TODAY_REQUEST_NEWS)) {
            if (DateUtil.getTimestamp() <= MVUtils.getLong(Constants.REQUEST_TIMESTAMP_NEWS)) {
                getNewsFromDB();
            } else {
                requestNewsApi();
            }
        } else {
            requestNewsApi();
        }

        return news;
    }

    @SuppressLint("CheckResult")
    private void requestNewsApi() {
        Log.d(TAG, "requestNewsApi: 从网络获取 新闻");
        NetworkApi.createService(ApiService.class, 2).news().compose(NetworkApi.applySchedulers(new BaseObserver<NewsResponse>() {
            @Override
            protected void onSuccess(NewsResponse newsResponse) {
                if (newsResponse.getErrorCode() == Constants.SUCCESS) {
                    saveNews(newsResponse);
                    news.postValue(newsResponse);
                } else {
                    failure.postValue(newsResponse.getReason());
                }

            }

            @Override
            protected void onFailure(Throwable e) {
                failure.postValue(e.getMessage());
            }
        }));
    }

    /**
     * 保存新闻数据
     * @param newsResponse 新闻数据
     */
    private void saveNews(NewsResponse newsResponse) {
        MVUtils.put(Constants.IS_TODAY_REQUEST_NEWS, true);
        MVUtils.put(Constants.REQUEST_TIMESTAMP_NEWS, DateUtil.getMillisNextEarlyMorning());

        Completable deleteAll = BaseApplication.getDatabase().newsDao().deleteAll();
        CustomDisposable.addDisposable(deleteAll, () -> {
            Log.d(TAG, "saveNews: 删除数据成功");
            List<News> newsList = new ArrayList<>();
            for (NewsResponse.Result.Data data : newsResponse.getResult().getData()) {
                newsList.add(new News(data.getUniquekey(), data.getTitle(), data.getDate(), data.getCategory(), data.getAuthorName(),
                        data.getUrl(), data.getThumbnailPicS(), data.getIsContent()));
            }

            Completable insertAll = BaseApplication.getDatabase().newsDao().insertAll(newsList);
            CustomDisposable.addDisposable(insertAll, () -> Log.d(TAG, "saveNews: 插入新闻数据成功"));
        });
    }

    /**
     * 从本地数据库获取新闻数据
     */
    private void getNewsFromDB() {

        NewsResponse newsResponse = new NewsResponse();
        NewsResponse.Result resultBean = new NewsResponse.Result();

        Flowable<List<News>> listFlowable = BaseApplication.getDatabase().newsDao().getAll();

        List<NewsResponse.Result.Data> dataList = new ArrayList<>();
        CustomDisposable.addDisposable(listFlowable, newsList -> {
            for (News news1 : newsList) {
                NewsResponse.Result.Data data = new NewsResponse.Result.Data();
                data.setUniquekey(news1.getUniquekey());
                data.setAuthorName(news1.getAuthorName());
                data.setCategory(news1.getCategory());
                data.setDate(news1.getDate());
                data.setIsContent(news1.getIsContent());
                data.setThumbnailPicS(news1.getThumbnailPicS());
                data.setTitle(news1.getTitle());
                data.setUrl(news1.getUrl());
                dataList.add(data);
            }

            resultBean.setData(dataList);
            newsResponse.setResult(resultBean);
            news.postValue(newsResponse);
        });
    }
}
