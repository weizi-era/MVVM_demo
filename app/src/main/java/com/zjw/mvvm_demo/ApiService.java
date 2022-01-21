package com.zjw.mvvm_demo;

import com.zjw.mvvm_demo.bean.BiYingResponse;
import com.zjw.mvvm_demo.bean.WallPaperResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/HPImageArchive.aspx?format=js&idx=4&n=3")
    Observable<BiYingResponse> getBiYing();

    /**
     * 热门壁纸
     */
    @GET("/v1/vertical/category/4e4d610cdf714d2966000000/vertical?limit=50&adult=false&first=1&order=hot")
    Observable<WallPaperResponse> wallPaper();
}
