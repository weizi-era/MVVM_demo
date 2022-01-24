package com.zjw.mvvm_demo.ui.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.adapter.VideoAdapter;
import com.zjw.mvvm_demo.bean.VideoResponse;
import com.zjw.mvvm_demo.databinding.VideoFragmentBinding;
import com.zjw.mvvm_demo.viewmodels.VideoViewModel;

import org.jetbrains.annotations.NotNull;

public class VideoFragment extends BaseFragment {

    private VideoViewModel mViewModel;
    private VideoFragmentBinding binding;

    public static VideoFragment newInstance() {
        return new VideoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.video_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VideoViewModel.class);

        mViewModel.getVideo();
        binding.rv.setLayoutManager(new LinearLayoutManager(context));
        mViewModel.video.observe(context, videoResponse -> binding.rv.setAdapter(new VideoAdapter(videoResponse.getResult())));

        mViewModel.failed.observe(context, this::showMsg);
    }
}