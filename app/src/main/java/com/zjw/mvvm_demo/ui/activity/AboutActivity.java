package com.zjw.mvvm_demo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.databinding.ActivityAboutBinding;

public class AboutActivity extends BaseActivity {

    private ActivityAboutBinding binding;

    /**
     * 博客个人主页
     */
    private final String CSDN = "https://blog.csdn.net/qq_26498311";
    /**
     * 源码地址
     */
    private final String GITHUB_URL = "https://github.com/weizi-era/MVVM_demo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        back(binding.toolbar);
        binding.rlBlog.setOnClickListener(v -> jumpUrl(CSDN));
        binding.rlSourceCode.setOnClickListener(v -> jumpUrl(GITHUB_URL));
        binding.rlMail.setOnClickListener(v -> copyEmail());
    }

    /**
     * 跳转URL
     *
     * @param url 地址
     */
    private void jumpUrl(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    private void copyEmail() {
        ClipboardManager myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", "408689023@qq.com");
        myClipboard.setPrimaryClip(myClip);
        showMsg("邮箱已复制");
    }
}