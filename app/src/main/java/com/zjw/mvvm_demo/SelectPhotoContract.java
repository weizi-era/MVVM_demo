package com.zjw.mvvm_demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

public class SelectPhotoContract extends ActivityResultContract<String[], Uri> {

    @NotNull
    @Override
    public Intent createIntent(@NonNull @NotNull Context context, String[] input) {
        return new Intent(Intent.ACTION_PICK).setType("image/*");
    }

    @Override
    public Uri parseResult(int resultCode, @Nullable Intent intent) {
        if (intent == null || resultCode != Activity.RESULT_OK) return null;
        return intent.getData();
    }
}
