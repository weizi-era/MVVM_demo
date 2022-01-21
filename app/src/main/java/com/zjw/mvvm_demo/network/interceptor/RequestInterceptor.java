package com.zjw.mvvm_demo.network.interceptor;

import com.zjw.mvvm_demo.network.INetworkRequiredInfo;
import com.zjw.mvvm_demo.network.utils.DateUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求拦截器
 * @author zjw
 */
public class RequestInterceptor implements Interceptor {

    private final INetworkRequiredInfo iNetworkRequiredInfo;

    public RequestInterceptor(INetworkRequiredInfo iNetworkRequiredInfo) {
        this.iNetworkRequiredInfo = iNetworkRequiredInfo;
    }

    @Override
    public @NotNull Response intercept(Chain chain) throws IOException {
        String nowDateTime = DateUtil.getDateTime();
        //构建器
        Request.Builder builder = chain.request().newBuilder();
        //添加使用环境
        builder.addHeader("os","android");
        //添加版本号
        builder.addHeader("appVersionCode", this.iNetworkRequiredInfo.getAppVersionCode());
        //添加版本名
        builder.addHeader("appVersionName", this.iNetworkRequiredInfo.getAppVersionName());
        //添加日期时间
        builder.addHeader("datetime", nowDateTime);
        //返回
        return chain.proceed(builder.build());
    }
}
