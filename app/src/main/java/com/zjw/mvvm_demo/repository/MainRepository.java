package com.zjw.mvvm_demo.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.zjw.mvvm_demo.ApiService;
import com.zjw.mvvm_demo.BaseApplication;
import com.zjw.mvvm_demo.Constants;
import com.zjw.mvvm_demo.bean.BiYingResponse;
import com.zjw.mvvm_demo.bean.WallPaperResponse;
import com.zjw.mvvm_demo.db.bean.BiYing;
import com.zjw.mvvm_demo.db.bean.WallPaper;
import com.zjw.mvvm_demo.db.dao.WallPaperDao;
import com.zjw.mvvm_demo.network.NetworkApi;
import com.zjw.mvvm_demo.network.base.BaseObserver;
import com.zjw.mvvm_demo.network.utils.DateUtil;
import com.zjw.mvvm_demo.network.utils.KLog;
import com.zjw.mvvm_demo.utils.MVUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class MainRepository {

    private static final String TAG = MainRepository.class.getSimpleName();
    final MutableLiveData<BiYingResponse> biYingImage = new MutableLiveData<>();
    final MutableLiveData<WallPaperResponse> wallPaperImage = new MutableLiveData<>();

    private int first = 1;

    /**
     * 获取必应数据
     * @return
     */
    public MutableLiveData<BiYingResponse> getBiYing() {
        // 今日接口是否已经请求
        if (MVUtils.getBoolean(Constants.IS_TODAY_REQUEST)) {
            if (DateUtil.getTimestamp() <= MVUtils.getLong(Constants.REQUEST_TIMESTAMP)) {
                //当前时间未超过次日0点，从本地获取
                getBiYingFromDB();
            } else {
                //大于则数据需要更新，从网络获取
                requestBiYingApi();
            }
        } else {
            //没有请求过接口 或 当前时间，从网络获取
            requestBiYingApi();
        }

        return biYingImage;
    }

    /**
     * 从网络上请求必应数据
     */
    @SuppressLint("CheckResult")
    private void requestBiYingApi() {
        Log.d(TAG, "requestBiYingApi: 从网络获取");
        NetworkApi.createService(ApiService.class, 0).getBiYing().compose(NetworkApi.applySchedulers(new BaseObserver<BiYingResponse>() {
            @Override
            public void onSuccess(BiYingResponse biYingImgResponse) {
                //存储到本地数据库中，并记录今日已请求了数据
                saveBiYingData(biYingImgResponse);
                biYingImage.setValue(biYingImgResponse);
            }

            @Override
            public void onFailure(Throwable e) {
                KLog.e("BiYing Error: " + e.toString());
            }
        }));
    }

    /**
     * 从网络上请求壁纸数据
     */
    @SuppressLint("CheckResult")
    private void requestWallPagerApi() {
        Log.d(TAG, "requestWallPagerApi: 从网络获取");
        int first1 = MVUtils.getInt("first");

        if (first1 == 0) {
            MVUtils.put("first", first);
        } else {
            first = MVUtils.getInt("first");
        }
        NetworkApi.createService(ApiService.class, 1).wallPaper(first).compose(NetworkApi.applySchedulers(new BaseObserver<WallPaperResponse>() {
            @Override
            public void onSuccess(WallPaperResponse wallPaperResponse) {
                //存储到本地数据库中，并记录今日已请求了数据
                saveWallPaperData(wallPaperResponse);
                MVUtils.put("first", first++);
                wallPaperImage.setValue(wallPaperResponse);
            }

            @Override
            public void onFailure(Throwable e) {
                KLog.e("BiYing Error: " + e.toString());
            }
        }));

    }


    /**
     * 获取壁纸数据
     * @return
     */
    @SuppressLint("CheckResult")
    public MutableLiveData<WallPaperResponse> getHotWall() {
        // 今日接口是否已经请求
        if (MVUtils.getBoolean(Constants.IS_TODAY_REQUEST)) {
            if (DateUtil.getTimestamp() <= MVUtils.getLong(Constants.REQUEST_TIMESTAMP)) {
                //当前时间未超过次日0点，从本地获取
                getWallPaperFromDB();
            } else {
                //大于则数据需要更新，从网络获取
                requestWallPagerApi();
            }
        } else {
            //没有请求过接口 或 当前时间，从网络获取
            requestWallPagerApi();
        }

        return wallPaperImage;
    }

    /**
     * 保存壁纸数据
     */
    private void saveWallPaperData(WallPaperResponse response) {
        // 记录今日已请求
        MVUtils.put(Constants.IS_TODAY_REQUEST, true);
        MVUtils.put(Constants.REQUEST_TIMESTAMP, DateUtil.getMillisNextEarlyMorning());

        Completable deleteAll = BaseApplication.getDatabase().wallPaperDao().deleteAll();

        CustomDisposable.addDisposable(deleteAll, () -> {
            KLog.d("deleteWallPaperData : 删除数据成功");
            List<WallPaper> wallPaperList = new ArrayList<>();
            for (WallPaperResponse.Res.Vertical image : response.getRes().getVertical()) {
                wallPaperList.add(new WallPaper(image.getImg()));
            }

            // 保存到数据库
            Completable insertAll = BaseApplication.getDatabase().wallPaperDao().insertAll(wallPaperList);
            CustomDisposable.addDisposable(insertAll, () -> KLog.d("saveWallPaperData : 热门天气数据保存成功"));
        });
    }

    /**
     * 保存必应数据
     * @param response
     */
    private void saveBiYingData(BiYingResponse response) {
        // 记录今日已请求
        MVUtils.put(Constants.IS_TODAY_REQUEST, true);
        MVUtils.put(Constants.REQUEST_TIMESTAMP, DateUtil.getMillisNextEarlyMorning());

        BiYingResponse.Images images = response.getImages().get(0);
        // 保存到数据库
        Completable insert = BaseApplication.getDatabase().imageDao().insertAll(new BiYing(1, images.getUrl(), images.getUrlbase(),
                images.getCopyright(), images.getCopyrightlink(), images.getTitle()));

        CustomDisposable.addDisposable(insert, () -> KLog.d("saveBiYingData : 插入数据成功"));
    }

    /**
     * 从数据库获取必应数据
     */
    public void getBiYingFromDB() {
        Log.d(TAG, "getBiYingFromDB: 从本地数据库获取");
        BiYingResponse biYingResponse = new BiYingResponse();
        Flowable<BiYing> biYingFlowable = BaseApplication.getDatabase().imageDao().queryById(1);
        CustomDisposable.addDisposable(biYingFlowable, biYing -> {
            BiYingResponse.Images images = new BiYingResponse.Images();
            images.setUrl(biYing.getUrl());
            images.setUrlbase(biYing.getUrlbase());
            images.setCopyright(biYing.getCopyright());
            images.setCopyrightlink(biYing.getCopyrightlink());
            images.setTitle(biYing.getTitle());
            List<BiYingResponse.Images> imagesList = new ArrayList<>();
            imagesList.add(images);
            biYingResponse.setImages(imagesList);
            biYingImage.postValue(biYingResponse);
        });

    }

    /**
     * 从数据库获取壁纸数据
     */
    public void getWallPaperFromDB() {
        Log.d(TAG, "getWallPaperFromDB: 从本地数据库获取");
        WallPaperResponse wallPaperResponse = new WallPaperResponse();
        WallPaperResponse.Res res = new WallPaperResponse.Res();

        Flowable<List<WallPaper>> wallPaperListFlowable = BaseApplication.getDatabase().wallPaperDao().getAll();

        List<WallPaperResponse.Res.Vertical> images = new ArrayList<>();
        CustomDisposable.addDisposable(wallPaperListFlowable, wallPapers -> {
            for (WallPaper wallPaper : wallPapers) {
                WallPaperResponse.Res.Vertical images1 = new WallPaperResponse.Res.Vertical();
                images1.setImg(wallPaper.getImg());
                images.add(images1);
            }

            res.setVertical(images);
            wallPaperResponse.setRes(res);
            wallPaperImage.postValue(wallPaperResponse);
        });
    }
}
