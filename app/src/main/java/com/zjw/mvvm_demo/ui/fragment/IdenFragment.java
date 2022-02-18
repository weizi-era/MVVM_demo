package com.zjw.mvvm_demo.ui.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.SelectPhotoContract;
import com.zjw.mvvm_demo.adapter.RecognitionAdapter;
import com.zjw.mvvm_demo.bean.RecognitionResponse;
import com.zjw.mvvm_demo.databinding.IdenFragmentBinding;
import com.zjw.mvvm_demo.utils.BitmapUtils;
import com.zjw.mvvm_demo.utils.CameraUtils;
import com.zjw.mvvm_demo.utils.PermissionUtils;
import com.zjw.mvvm_demo.viewmodels.IdenViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Base64;


public class IdenFragment extends BaseFragment {

    private IdenFragmentBinding binding;

    private IdenViewModel viewModel;

    //用于保存拍照图片的uri
    private Uri mCameraUri;

    /**
     * 底部弹窗
     */
    private BottomSheetDialog bottomSheetDialog;
    /**
     * 弹窗视图
     */
    private ViewDataBinding bottomViewBinding;

    /**
     * 常规使用 通过意图进行跳转
     */
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;

    /**
     * 相册活动结果启动器
     */
    private ActivityResultLauncher<String[]> openAlbumActivityResultLauncher;

    /**
     * 拍照活动结果启动器
     */
    private ActivityResultLauncher<Uri> takePictureActivityResultLauncher;

    /**
     * 页面权限请求 结果启动器
     */
    private ActivityResultLauncher<String[]> permissionActivityResultLauncher;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.iden_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        register();
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(IdenViewModel.class);

        bottomSheetDialog = new BottomSheetDialog(context);

        bottomViewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_bottom, null, false);

        viewModel.getToken();

        binding.networkImage.setOnClickListener(v -> networkImage());
        binding.albumImage.setOnClickListener(v -> albumImage());
        binding.cameraImage.setOnClickListener(v -> cameraImage());
    }

    private void register() {
        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                //从外部存储管理页面返回
                if (!isStorageManager()) {
                    showMsg("未打开外部存储管理开关，无法打开相册，抱歉");
                    return;
                }
                if (!hasPermission(PermissionUtils.READ_EXTERNAL_STORAGE)) {
                    permissionActivityResultLauncher.launch(new String[]{PermissionUtils.READ_EXTERNAL_STORAGE});
                    return;
                }
                //打开相册
                openAlbum();
            }
        });

        //调用MediaStore.ACTION_IMAGE_CAPTURE拍照，并将图片保存到给定的Uri地址，返回true表示保存成功。
        takePictureActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if (result) {
                callbackRequest(mCameraUri);
            }
        });

        // 自定义相册Contract，选择相册，返回它的Uri。
        openAlbumActivityResultLauncher = registerForActivityResult(new SelectPhotoContract(), result -> {
            if (result != null) {
                callbackRequest(result);
            }
        });

        //多个权限返回结果
        permissionActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            if (result.containsKey(PermissionUtils.CAMERA)) {
                //相机权限
                if (!result.get(PermissionUtils.CAMERA)) {
                    showMsg("您拒绝了相机权限，无法打开相机，抱歉。");
                    return;
                }
                takePicture();
            } else if (result.containsKey(PermissionUtils.READ_EXTERNAL_STORAGE)) {
                //文件读写权限
                if (!result.get(PermissionUtils.READ_EXTERNAL_STORAGE)) {
                    showMsg("您拒绝了读写文件权限，无法打开相册，抱歉。");
                    return;
                }
                openAlbum();
            } else if (result.containsKey(PermissionUtils.LOCATION)) {
                //定位权限
                if (!result.get(PermissionUtils.LOCATION)) {
                    showMsg("您拒绝了位置许可，将无法使用地图，抱歉。");
                }
            }
        });
    }

    /**
     * 新的拍照
     */
    private void takePicture() {
        mCameraUri = context.getContentResolver().insert(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI : MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        takePictureActivityResultLauncher.launch(mCameraUri);
    }

    private void cameraImage() {
        if (!isAndroid6()) {
            //打开相机
            takePicture();
            return;
        }
        if (!hasPermission(PermissionUtils.CAMERA)) {
            permissionActivityResultLauncher.launch(new String[]{PermissionUtils.CAMERA});
            return;
        }
        //打开相机
        takePicture();
    }

    private void albumImage() {
        if (isAndroid11()) {
            //请求打开外部存储管理
            requestManageExternalStorage(intentActivityResultLauncher);
        } else {
            if (!isAndroid6()) {
                //打开相册
                openAlbum();
                return;
            }
            if (!hasPermission(PermissionUtils.READ_EXTERNAL_STORAGE)) {
                permissionActivityResultLauncher.launch(new String[]{PermissionUtils.READ_EXTERNAL_STORAGE});
                return;
            }
            //打开相册
            openAlbum();
        }
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        openAlbumActivityResultLauncher.launch(new String[]{"image/*"});
    }

    private void networkImage() {
        String url = "http://www.dnzhuti.com/uploads/allimg/170124/95-1F124112919.jpg";
        Glide.with(this).load(url).into(binding.ivPicture);
        showLoading();

        viewModel.getNetworkRecognition(url);

        observe();
    }

    private void callbackRequest(Uri uri) {
        Glide.with(this).load(uri.toString()).into(binding.ivPicture);
        String imagePath = CameraUtils.getImagePath(uri, null, context);
        String base64 = BitmapUtils.imageToBase64(imagePath);
        showLoading();
        viewModel.getAlbumRecognition(base64);
        if (viewModel.recognition != null) {
            observe();
        }
    }


    private void observe() {
        viewModel.recognition.observe(context, recognitionResponse -> {
            dismissLoading();
            bottomSheetDialog.setContentView(bottomViewBinding.getRoot());

            bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);

            RecyclerView rv_result = bottomViewBinding.getRoot().findViewById(R.id.rv_result);

            rv_result.setLayoutManager(new LinearLayoutManager(context));
            rv_result.setAdapter(new RecognitionAdapter(recognitionResponse.getResult()));

            bottomSheetDialog.show();

        });
    }
}
