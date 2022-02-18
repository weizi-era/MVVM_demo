package com.zjw.mvvm_demo;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ImageView;

public class EditWatcher implements TextWatcher {

    private ImageView imageView;

    public EditWatcher(ImageView imageView) {
        this.imageView = imageView;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (s.length() > 0) {
            imageView.setImageResource(R.mipmap.ic_black_ok);
            imageView.setClickable(true);
        } else {
            imageView.setImageResource(R.mipmap.ic_gray_ok);
            imageView.setClickable(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
