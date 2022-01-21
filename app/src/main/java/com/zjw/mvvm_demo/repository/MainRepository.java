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

public class MainRepository {

    private static final String TAG = MainRepository.class.getSimpleName();
    final MutableLiveData<BiYingResponse> biYingImage = new MutableLiveData<>();
    final MutableLiveData<WallPaperResponse> wallPaperImage = new MutableLiveData<>();

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
     * 从网络上请求数据
     */
    @SuppressLint("CheckResult")
    private void requestBiYingApi() {
        Log.d(TAG, "requestNetworkApi: 从网络获取");
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

    @SuppressLint("CheckResult")
    private void requestWallPagerApi() {
        Log.d(TAG, "requestNetworkApi: 从网络获取");
        NetworkApi.createService(ApiService.class, 1).wallPaper().compose(NetworkApi.applySchedulers(new BaseObserver<WallPaperResponse>() {
            @Override
            public void onSuccess(WallPaperResponse wallPaperResponse) {
                //存储到本地数据库中，并记录今日已请求了数据
                saveWallPaperData(wallPaperResponse);
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
     * 保存数据
     */
    private void saveWallPaperData(WallPaperResponse response) {
        // 记录今日已请求
        MVUtils.put(Constants.IS_TODAY_REQUEST, true);
        MVUtils.put(Constants.REQUEST_TIMESTAMP, DateUtil.getMillisNextEarlyMorning());

        List<WallPaperResponse.Res.Vertical> images = response.getRes().getVertical();
        // 保存到数据库

        WallPaperDao wallPaperDao = BaseApplication.getDatabase().wallPaperDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<WallPaper> wallPaperList = new ArrayList<>();
                for (WallPaperResponse.Res.Vertical image : images) {
                    wallPaperList.add(new WallPaper(image.getImg()));
                }
                wallPaperDao.insertAll(wallPaperList);
            }
        }).start();
    }

    private void saveBiYingData(BiYingResponse response) {
        // 记录今日已请求
        MVUtils.put(Constants.IS_TODAY_REQUEST, true);
        MVUtils.put(Constants.REQUEST_TIMESTAMP, DateUtil.getMillisNextEarlyMorning());

        BiYingResponse.Images images = response.getImages().get(0);
        // 保存到数据库
        new Thread(() -> BaseApplication.getDatabase().imageDao().insertAll(new BiYing(1, images.getUrl(), images.getUrlbase(),
                images.getCopyright(), images.getCopyrightlink(), images.getTitle()))).start();
    }


    public void getBiYingFromDB() {
        Log.d(TAG, "getBiYingFromDB: 从本地数据库获取");
        BiYingResponse biYingResponse = new BiYingResponse();
        new Thread(new Runnable() {
            @Override
            public void run() {
                BiYing biYing = BaseApplication.getDatabase().imageDao().queryById(1);
                BiYingResponse.Images images1 = new BiYingResponse.Images();
                List<BiYingResponse.Images> images = new ArrayList<>();
                images1.setUrl(biYing.getUrl());
                images1.setUrlbase(biYing.getUrlbase());
                images1.setCopyright(biYing.getCopyright());
                images1.setCopyrightlink(biYing.getCopyrightlink());
                images1.setTitle(biYing.getTitle());
                images.add(images1);
                biYingResponse.setImages(images);
                biYingImage.postValue(biYingResponse);
            }
        }).start();
    }

    public void getWallPaperFromDB() {
        Log.d(TAG, "getBiYingFromDB: 从本地数据库获取");
        WallPaperResponse wallPaperResponse = new WallPaperResponse();
        WallPaperResponse.Res res = new WallPaperResponse.Res();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<WallPaperResponse.Res.Vertical> images = new ArrayList<>();
                WallPaperDao wallPaperDao = BaseApplication.getDatabase().wallPaperDao();
                List<WallPaper> wallPaperList = wallPaperDao.getAll();
                for (int i = 0; i < wallPaperList.size(); i++) {
                    WallPaperResponse.Res.Vertical images1 = new WallPaperResponse.Res.Vertical();
                    images1.setImg(wallPaperList.get(i).getImg());
                    images.add(images1);
                }
                res.setVertical(images);
                wallPaperResponse.setRes(res);
                wallPaperImage.postValue(wallPaperResponse);

            }
        }).start();
    }
}
