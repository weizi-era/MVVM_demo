package com.zjw.mvvm_demo.repository;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.zjw.mvvm_demo.ApiService;
import com.zjw.mvvm_demo.Constants;
import com.zjw.mvvm_demo.bean.RecognitionResponse;
import com.zjw.mvvm_demo.bean.TokenResponse;
import com.zjw.mvvm_demo.network.NetworkApi;
import com.zjw.mvvm_demo.network.base.BaseObserver;
import com.zjw.mvvm_demo.utils.MVUtils;

public class RecognitionRepository {


    final MutableLiveData<RecognitionResponse> recognition = new MutableLiveData<>();
    public final MutableLiveData<String> failure = new MutableLiveData<>();

    String accessToken;

    @SuppressLint("CheckResult")
    private void requestApiGetToken() {

        NetworkApi.createService(ApiService.class, 4)
                .getToken(Constants.GRANT_TYPE, Constants.CLIENT_ID, Constants.CLIENT_SECRET)
                .compose(NetworkApi.getInstance().applySchedulers(new BaseObserver<TokenResponse>() {
                    @Override
                    protected void onSuccess(TokenResponse tokenResponse) {

                        accessToken = tokenResponse.getAccessToken();
                        long expiresIn = tokenResponse.getExpiresIn();
                        long currentTimeMillis = System.currentTimeMillis() / 1000;

                        MVUtils.put(Constants.TOKEN, accessToken);
                        MVUtils.put(Constants.GET_TOKEN_TIME, currentTimeMillis);
                        MVUtils.put(Constants.TOKEN_VALID_PERIOD, expiresIn);
                    }

                    @Override
                    protected void onFailure(Throwable e) {

                    }
                }));

    }

    /**
     * 判断Token是否过期
     * @return true 过期 false 未过期
     */
    private boolean isTokenExpired() {
        long getTokenTime = MVUtils.getLong(Constants.GET_TOKEN_TIME);
        long effectiveTime = MVUtils.getLong(Constants.TOKEN_VALID_PERIOD);
        //获取当前系统时间
        long currentTime = System.currentTimeMillis() / 1000;

        return (currentTime - getTokenTime) >= effectiveTime;
    }

    /**
     * 获取鉴权Token
     */
    public String getAccessToken() {
        String token = MVUtils.getString(Constants.TOKEN);
        if (TextUtils.isEmpty(token)) {
            //访问API获取接口
            requestApiGetToken();
        } else {
            //则判断Token是否过期
            if (isTokenExpired()) {
                //过期
                requestApiGetToken();
            } else {
                accessToken = token;
            }
        }

        return accessToken;
    }

    @SuppressLint("CheckResult")
    public MutableLiveData<RecognitionResponse> getNetworkRecognition(String token, String url) {
        NetworkApi.createService(ApiService.class, 4).getRecognitionResult(token, null, url)
                .compose(NetworkApi.getInstance().applySchedulers(new BaseObserver<RecognitionResponse>() {
                    @Override
                    protected void onSuccess(RecognitionResponse recognitionResponse) {
                        recognition.postValue(recognitionResponse);
                    }

                    @Override
                    protected void onFailure(Throwable e) {
                        failure.postValue(e.getMessage());
                    }
                }));

        return recognition;
    }

    @SuppressLint("CheckResult")
    public MutableLiveData<RecognitionResponse> getAlbumRecognition(String token, String imageBase64) {
        NetworkApi.createService(ApiService.class, 4).getRecognitionResult(token, imageBase64, null)
                .compose(NetworkApi.getInstance().applySchedulers(new BaseObserver<RecognitionResponse>() {
                    @Override
                    protected void onSuccess(RecognitionResponse recognitionResponse) {
                        recognition.postValue(recognitionResponse);
                    }

                    @Override
                    protected void onFailure(Throwable e) {
                        failure.postValue(e.getMessage());
                    }
                }));

        return recognition;
    }
}
