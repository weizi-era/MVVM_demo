package com.zjw.mvvm_demo.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.zjw.mvvm_demo.view.dialog.LoadingDialog;

import org.jetbrains.annotations.NotNull;

public class BaseFragment extends Fragment {

    protected AppCompatActivity context;

    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity) {
            this.context = (AppCompatActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    protected void showMsg(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示加载弹窗
     */
    protected void showLoading() {
        loadingDialog = new LoadingDialog(context);
        loadingDialog.show();
    }

    /**
     * 显示加载弹窗
     *
     * @param isClose true 则点击其他区域弹窗关闭， false 不关闭。
     */
    protected void showLoading(boolean isClose) {
        loadingDialog = new LoadingDialog(context, isClose);
        loadingDialog.show();
    }

    /**
     * 隐藏加载弹窗
     */
    protected void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

}
