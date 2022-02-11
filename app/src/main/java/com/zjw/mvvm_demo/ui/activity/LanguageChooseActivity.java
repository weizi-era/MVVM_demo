package com.zjw.mvvm_demo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.databinding.ActivityLanguageChooseBinding;
import com.zjw.mvvm_demo.utils.LocalManageUtils;
import com.zjw.mvvm_demo.utils.MVUtils;

public class LanguageChooseActivity extends BaseActivity implements View.OnClickListener {

    ActivityLanguageChooseBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language_choose);

        backAndFinish(binding.toolbar);

        binding.layChinese.setOnClickListener(this);
        binding.laySystem.setOnClickListener(this);
        binding.layEnglish.setOnClickListener(this);
        binding.layTraditionalChinese.setOnClickListener(this);

        chooseLanguage(MVUtils.getLanguage());
    }

    private void chooseLanguage(int select) {
        switch (select) {
            case 0: // 系统
                binding.ivSystem.setVisibility(View.VISIBLE);
                binding.ivChinese.setVisibility(View.GONE);
                binding.ivEnglish.setVisibility(View.GONE);
                binding.ivTraditionalChinese.setVisibility(View.GONE);
                break;
            case 1: // 中文简体
                binding.ivSystem.setVisibility(View.GONE);
                binding.ivChinese.setVisibility(View.VISIBLE);
                binding.ivEnglish.setVisibility(View.GONE);
                binding.ivTraditionalChinese.setVisibility(View.GONE);
                break;
            case 2: // 英文
                binding.ivSystem.setVisibility(View.GONE);
                binding.ivChinese.setVisibility(View.GONE);
                binding.ivEnglish.setVisibility(View.VISIBLE);
                binding.ivTraditionalChinese.setVisibility(View.GONE);
                break;
            case 3: // 中文繁体
                binding.ivSystem.setVisibility(View.GONE);
                binding.ivChinese.setVisibility(View.GONE);
                binding.ivEnglish.setVisibility(View.GONE);
                binding.ivTraditionalChinese.setVisibility(View.VISIBLE);
                break;
            default:
                break;

        }
    }
    /**
     * 选择语言后，重新进入setting页面
     *
     * @param context 上下文
     * @param select  选择的语言
     */
    public void reStart(Context context, int select) {
        LocalManageUtils.setSelectLanguage(this, select);
        chooseLanguage(select);//控制选择的视图
        finish();
        Intent intent = new Intent(context, AboutActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_system:
                reStart(this, 0);
                break;
            case R.id.lay_chinese:
                reStart(this, 1);
                break;
            case R.id.lay_english:
                reStart(this, 2);
                break;
            case R.id.lay_traditional_chinese:
                reStart(this, 3);
                break;
        }
    }
}