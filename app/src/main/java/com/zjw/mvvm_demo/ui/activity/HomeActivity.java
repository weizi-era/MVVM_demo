package com.zjw.mvvm_demo.ui.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.zjw.mvvm_demo.Constants;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.SelectPhotoContract;
import com.zjw.mvvm_demo.databinding.ActivityHomeBinding;
import com.zjw.mvvm_demo.databinding.DialogEditBinding;
import com.zjw.mvvm_demo.databinding.DialogModifyUserInfoBinding;
import com.zjw.mvvm_demo.databinding.NavHeaderBinding;
import com.zjw.mvvm_demo.db.bean.User;
import com.zjw.mvvm_demo.utils.CameraUtils;
import com.zjw.mvvm_demo.utils.MVUtils;
import com.zjw.mvvm_demo.utils.PermissionUtils;
import com.zjw.mvvm_demo.utils.SizeUtils;
import com.zjw.mvvm_demo.view.dialog.AlertDialog;
import com.zjw.mvvm_demo.viewmodels.HomeViewModel;

import java.io.File;


public class HomeActivity extends BaseActivity {

    ActivityHomeBinding binding;

    //可输入弹窗
    private AlertDialog editDialog = null;
    //修改用户信息弹窗
    private AlertDialog modifyUserInfoDialog = null;
    //是否显示修改头像的两种方式
    private boolean isShow = false;
    //用于保存拍照图片的uri
    private Uri mCameraUri;
    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private String mCameraImagePath;

    private HomeViewModel homeViewModel;

    private User localUser;

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
    protected void onCreate(Bundle savedInstanceState) {
        register();
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        // 显示加载弹窗
        showLoading();
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        initView();
    }

    private void initView() {

        //获取navController
        NavController controller = Navigation.findNavController(this, R.id.nav_host_fragment);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.info_fragment:
                    binding.tvTitle.setText("热门资讯");
                    controller.navigate(R.id.info_fragment);
                    break;
                case R.id.map_fragment:
                    binding.tvTitle.setText("地图天气");
                    controller.navigate(R.id.map_fragment);
                    break;
                case R.id.iden_fragment:
                    binding.tvTitle.setText("图片识别");
                    controller.navigate(R.id.iden_fragment);
                    break;
            }

            return true;
        });

        binding.ivAvatar.setOnClickListener(v -> binding.drawerLayout.open());
        binding.naviView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_about:
                    jumpActivity(AboutActivity.class);
                    break;
                case R.id.item_notebook:
                    jumpActivity(NotebookActivity.class);
                    break;
                case R.id.item_scan:
                    jumpActivity(ScanActivity.class);
                    break;
                case R.id.item_logout:
                    logout();
                    break;
            }

            return true;
        });

        View headerView = binding.naviView.getHeaderView(0);
        headerView.setOnClickListener(v -> showModifyUserInfoDialog());
        NavHeaderBinding headerBinding = DataBindingUtil.bind(headerView);
        homeViewModel.getUser();
        homeViewModel.user.observe(this, user -> {
            localUser = user;
            binding.setHomeViewModel(homeViewModel);
            if (headerBinding != null) {
                headerBinding.setHomeViewModel(homeViewModel);
            }

            // 隐藏加载弹窗
            dismissLoading();
        });

        requestLocation();
    }

    private void logout() {
        showMsg("退出登录");
        MVUtils.put(Constants.IS_LOGIN, false);
        jumpActivityFinish(LoginActivity.class);
    }

    /**
     * 请求定位权限
     */
    private void requestLocation() {
        if (isAndroid6()) {
            if (!hasPermission(PermissionUtils.LOCATION)) {
                permissionActivityResultLauncher.launch(new String[]{PermissionUtils.LOCATION});
            }
        } else {
            showMsg("您无需动态请求权限");
        }
    }

    /**
     * 显示修改用户弹窗
     */
    private void showModifyUserInfoDialog() {
        DialogModifyUserInfoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_modify_user_info, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .addDefaultAnimation()
                .setCancelable(true)
                .setContentView(binding.getRoot())
                .setWidthAndHeight(SizeUtils.dp2px(this, 300), LinearLayout.LayoutParams.WRAP_CONTENT)
                .setOnClickListener(R.id.tv_modify_avatar, v -> {
                    //修改头像，点击显示修改头像的方式，再次点击隐藏修改方式
                    binding.layModifyAvatar.setVisibility(isShow ? View.GONE : View.VISIBLE);
                    isShow = !isShow;
                }).setOnClickListener(R.id.tv_album_selection, v -> albumSelection())//相册选择
                .setOnClickListener(R.id.tv_camera_photo, v -> cameraPhoto())//相机拍照
                .setOnClickListener(R.id.tv_modify_nickname, v -> showEditDialog(0))//修改昵称
                .setOnClickListener(R.id.tv_modify_Introduction, v -> showEditDialog(1))//修改简介
                .setOnClickListener(R.id.tv_close, v -> modifyUserInfoDialog.dismiss())//关闭弹窗
                .setOnDismissListener(dialog -> isShow = false);
        modifyUserInfoDialog = builder.create();
        modifyUserInfoDialog.show();
    }

    /**
     * 相册选择
     */
    private void albumSelection() {
        modifyUserInfoDialog.dismiss();
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
                modifyAvatar(mCameraUri.toString());
            }
        });

        // 自定义相册Contract，选择相册，返回它的Uri。
        openAlbumActivityResultLauncher = registerForActivityResult(new SelectPhotoContract(), result -> {
            if (result != null) {
                modifyAvatar(result.toString());
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
     * 修改头像
     */
    private void modifyAvatar(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            //保存到数据表中
            modifyContent(2, imagePath);
            Log.d("TAG", "modifyAvatar: " + imagePath);
        } else {
            showMsg("图片获取失败");
        }
    }

    /**
     * 修改内容
     *
     * @param type    类型 0：昵称 1：简介 2: 头像
     * @param content 修改内容
     */
    private void modifyContent(int type, String content) {
        if (type == 0) {
            localUser.setNickname(content);
        } else if (type == 1) {
            localUser.setIntroduction(content);
        } else if (type == 2) {
            localUser.setAvatar(content);
        }
        homeViewModel.updateUser(localUser);
        homeViewModel.failed.observe(this, failed -> {
            dismissLoading();
            if ("200".equals(failed)) {
                showMsg("修改成功");
            }
        });
    }

    /**
     * 相册拍照
     */
    private void cameraPhoto() {
        modifyUserInfoDialog.dismiss();
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

    /**
     * 新的拍照
     */
    private void takePicture() {
        mCameraUri = getContentResolver().insert(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI : MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        takePictureActivityResultLauncher.launch(mCameraUri);
    }

    /**
     * 显示可输入文字弹窗
     * @param type 0 修改昵称  1  修改简介
     */
    private void showEditDialog(int type) {
        modifyUserInfoDialog.dismiss();
        DialogEditBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_edit, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .addDefaultAnimation()
                .setCancelable(true)
                .setText(R.id.tv_title, type == 0 ? "修改昵称" : "修改简介")
                .setContentView(binding.getRoot())
                .setWidthAndHeight(SizeUtils.dp2px(this, 300), LinearLayout.LayoutParams.WRAP_CONTENT)
                .setOnClickListener(R.id.tv_cancel, v -> editDialog.dismiss())
                .setOnClickListener(R.id.tv_sure, v -> {
                    String content = binding.etContent.getText().toString().trim();
                    if (content.isEmpty()) {
                        showMsg(type == 0 ? "请输入昵称" : "请输入简介");
                        return;
                    }
                    if (type == 0 && content.length() > 10) {
                        showMsg("昵称过长，请输入8个以内汉字或字母");
                        return;
                    }
                    editDialog.dismiss();
                    showLoading();
                    //保存输入的值
                    modifyContent(type, content);
                });
        editDialog = builder.create();
        binding.etContent.setHint(type == 0 ? "请输入昵称" : "请输入简介");
        editDialog.show();
    }
}