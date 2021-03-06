package com.zjw.mvvm_demo;

import com.zjw.mvvm_demo.bean.BiYingResponse;
import com.zjw.mvvm_demo.bean.NewsDetailResponse;
import com.zjw.mvvm_demo.bean.NewsResponse;
import com.zjw.mvvm_demo.bean.RecognitionResponse;
import com.zjw.mvvm_demo.bean.TokenResponse;
import com.zjw.mvvm_demo.bean.VideoResponse;
import com.zjw.mvvm_demo.bean.WallPaperResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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

    /**
     * 获取鉴权认证Token
     */
    @FormUrlEncoded
    @POST("/oauth/2.0/token")
    Observable<TokenResponse> getToken(@Field("grant_type") String grant_type,
                                       @Field("client_id") String client_id,
                                       @Field("client_secret") String client_secret);


    /**
     * 获取图像识别结果
     */
    @FormUrlEncoded
    @POST("/rest/2.0/image-classify/v2/advanced_general")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Observable<RecognitionResponse> getRecognitionResult(@Field("access_token") String access_token,
                                                         @Field("image") String image,
                                                         @Field("url") String url);

}
