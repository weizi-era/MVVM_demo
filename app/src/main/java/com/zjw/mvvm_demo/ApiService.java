package com.zjw.mvvm_demo;

import com.zjw.mvvm_demo.bean.BiYingResponse;
import com.zjw.mvvm_demo.bean.NewsDetailResponse;
import com.zjw.mvvm_demo.bean.NewsResponse;
import com.zjw.mvvm_demo.bean.VideoResponse;
import com.zjw.mvvm_demo.bean.WallPaperResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * 必应壁纸
     */
    @GET("/HPImageArchive.aspx?format=js&idx=4&n=3")
    Observable<BiYingResponse> getBiYing();

    /**
     * 热门壁纸
     */
    @GET("/v1/vertical/category/4e4d610cdf714d2966000000/vertical?limit=50&adult=false&order=hot")
    Observable<WallPaperResponse> wallPaper(@Query("first") int first);

    /**
     * 聚合新闻数据
     */
    @GET("/toutiao/index?type=top&page=1&page_size=30&is_filter=1&key=8b28fbe4da6f18a1b5fba4b18287c95f")
    Observable<NewsResponse> news();

    /**
     * 聚合热门视频数据
     */
    @GET("/fapig/douyin/billboard?type=hot_video&size=20&key=83868a8be24065823f4e45c78ff64134")
    Observable<VideoResponse> video();

    /**
     * 新闻详情
     */
    @GET("/toutiao/content?key=8b28fbe4da6f18a1b5fba4b18287c95f")
    Observable<NewsDetailResponse> newsDetail(@Query("uniquekey") String uniquekey);

}
