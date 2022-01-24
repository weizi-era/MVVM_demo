package com.zjw.mvvm_demo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.bean.NewsDetailResponse;
import com.zjw.mvvm_demo.databinding.ActivityWebBinding;
import com.zjw.mvvm_demo.viewmodels.WebViewModel;

import java.util.HashMap;
import java.util.Map;

public class WebActivity extends BaseActivity {

    ActivityWebBinding binding;
    private WebViewModel webViewModel;
    private String uniqueKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web);

        initData();
    }


    private void initData() {
        webViewModel = new ViewModelProvider(this).get(WebViewModel.class);

        binding.webView.setWebViewClient(client);
        setStatusBar(true);

        uniqueKey = getIntent().getStringExtra("uniquekey");
        if (uniqueKey != null) {
            webViewModel.getNewsDetail(uniqueKey);
            webViewModel.data.observe(context, newsDetailResponse -> binding.webView.loadUrl(newsDetailResponse.getResult().getDetail().getUrl()));

            webViewModel.failed.observe(context, this::showMsg);
        }
    }


    private final WebViewClient client = new WebViewClient() {
        /**
         * 防止加载网页时调起系统浏览器
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView webview,
                                              com.tencent.smtt.export.external.interfaces.HttpAuthHandler httpAuthHandlerhost, String host,
                                              String realm) {
            boolean flag = httpAuthHandlerhost.useHttpAuthUsernamePassword();
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
        }

        @Override
        public void onReceivedError(WebView webView, int i, String s, String s1) {
            System.out.println("***********onReceivedError ************");
            super.onReceivedError(webView, i, s, s1);
        }

        @Override
        public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
            System.out.println("***********onReceivedHttpError ************");
            super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
        }
    };


}