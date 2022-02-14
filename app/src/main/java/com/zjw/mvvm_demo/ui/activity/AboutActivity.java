package com.zjw.mvvm_demo.ui.activity;

import androidx.databinding.DataBindingUtil;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pgyer.pgyersdk.PgyerSDKManager;
import com.pgyer.pgyersdk.callback.CheckoutCallBack;
import com.pgyer.pgyersdk.model.CheckSoftModel;
import com.zjw.mvvm_demo.BuildConfig;
import com.zjw.mvvm_demo.R;


import com.zjw.mvvm_demo.databinding.ActivityAboutBinding;
import com.zjw.mvvm_demo.databinding.DialogUpdateBinding;
import com.zjw.mvvm_demo.utils.LocalManageUtils;
import com.zjw.mvvm_demo.utils.SizeUtils;
import com.zjw.mvvm_demo.utils.UpdateUtils;
import com.zjw.mvvm_demo.view.dialog.AlertDialog;

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

    /**
     * 隐私政策
     */
    private final String PRIVATE_POLICY = "https://www.yuque.com/weizi-era/kb/yuy5kg";

    /**
     * 用户协议
     */
    private final String USER_PROTOCOL = "https://www.yuque.com/weizi-era/kb/tvweb5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        backAndFinish(binding.toolbar);

        binding.versionCode.setText(BuildConfig.VERSION_NAME);
        binding.tvLanguage.setText(LocalManageUtils.getSelectLanguage(this));
        binding.rlVersion.setOnClickListener(v -> {
            showLoading();
            PgyerSDKManager.checkVersionUpdate(this, new CheckoutCallBack() {
                @Override
                public void onNewVersionExist(CheckSoftModel checkSoftModel) {
                    dismissLoading();
                    showUpdateDialog(checkSoftModel);
                }

                @Override
                public void onNonentityVersionExist(String s) {
                    dismissLoading();
                    showMsg("当前版本已最新");
                }

                @Override
                public void onFail(String s) {

                }
            });

        });
        binding.rlBlog.setOnClickListener(v -> jumpUrl(CSDN));
        binding.rlSourceCode.setOnClickListener(v -> jumpUrl(GITHUB_URL));
        binding.rlPrivatePolicy.setOnClickListener(v -> jumpUrl(PRIVATE_POLICY));
        binding.rlUserProtocol.setOnClickListener(v -> jumpUrl(USER_PROTOCOL));
        binding.rlMail.setOnClickListener(v -> copyEmail());

        binding.rlLanguage.setOnClickListener(v -> jumpActivity(LanguageChooseActivity.class));
    }

    private void copyEmail() {
        ClipboardManager myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", "408689023@qq.com");
        myClipboard.setPrimaryClip(myClip);
        showMsg("邮箱已复制");
    }
}