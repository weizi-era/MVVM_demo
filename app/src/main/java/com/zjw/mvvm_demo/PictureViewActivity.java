package com.zjw.mvvm_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class PictureViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_view);

        String img = getIntent().getStringExtra("img");
        if (img != null) {
            ImageView imageView = findViewById(R.id.image);
            Glide.with(this).load(img).into(imageView);
        }

    }
}