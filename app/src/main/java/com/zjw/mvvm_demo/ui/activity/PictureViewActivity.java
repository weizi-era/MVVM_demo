package com.zjw.mvvm_demo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.adapter.ImageAdapter;
import com.zjw.mvvm_demo.databinding.ActivityMainBinding;
import com.zjw.mvvm_demo.databinding.ActivityPictureViewBinding;
import com.zjw.mvvm_demo.db.bean.WallPaper;
import com.zjw.mvvm_demo.viewmodels.PictureViewModel;

import java.util.List;

public class PictureViewActivity extends BaseActivity {

    private PictureViewModel viewModel;
    private ActivityPictureViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_picture_view);

        viewModel = new ViewModelProvider(this).get(PictureViewModel.class);

        String img = getIntent().getStringExtra("img");

        viewModel.getWallPaper();
        viewModel.wallPaper.observe(this, wallPapers -> {
            binding.viewpager.setAdapter(new ImageAdapter(wallPapers));
            for (int i = 0; i < wallPapers.size(); i++) {
                if (img == null) {
                    return;
                }
                if (wallPapers.get(i).getImg().equals(img)) {
                    binding.viewpager.setCurrentItem(i, false);
                    break;
                }
            }
        });
    }
}