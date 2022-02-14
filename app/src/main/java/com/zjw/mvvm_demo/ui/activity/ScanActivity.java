package com.zjw.mvvm_demo.ui.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.zxing.Result;
import com.king.zxing.CaptureActivity;
import com.king.zxing.DecodeConfig;
import com.king.zxing.DecodeFormatManager;
import com.king.zxing.analyze.MultiFormatAnalyzer;
import com.king.zxing.util.CodeUtils;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.utils.CameraUtils;
import com.zjw.mvvm_demo.utils.PermissionUtils;

import static com.zjw.mvvm_demo.ui.activity.BaseActivity.SELECT_PHOTO_CODE;

public class ScanActivity extends CaptureActivity {

    ImageView ivBack;
    ImageView ivAlbum;

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan;
    }

    @Override
    public void initUI() {
        super.initUI();

        ivBack = findViewById(R.id.iv_back);
        ivAlbum = findViewById(R.id.iv_album);
        ivBack.setOnClickListener( v -> finish());
        ivAlbum.setOnClickListener(v -> requestCallAlbum());
    }

    private void requestCallAlbum() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //请求打开外部存储管理
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, PermissionUtils.REQUEST_MANAGE_EXTERNAL_STORAGE_CODE);
        } else {
            if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                //打开相册
                openAlbum();
                return;
            }
            if (!PermissionUtils.hasPermission(this, PermissionUtils.READ_EXTERNAL_STORAGE)) {
                PermissionUtils.requestPermission(this, PermissionUtils.READ_EXTERNAL_STORAGE);
                return;
            }
            //打开相册
            openAlbum();
        }
    }

    private void openAlbum() {
        startActivityForResult(CameraUtils.getSelectPhotoIntent(), SELECT_PHOTO_CODE);
    }

    @Override
    public void initCameraScan() {
        super.initCameraScan();

        //初始化解码配置
        DecodeConfig decodeConfig = new DecodeConfig();
        decodeConfig.setHints(DecodeFormatManager.QR_CODE_HINTS)//如果只有识别二维码的需求，这样设置效率会更高，不设置默认为DecodeFormatManager.DEFAULT_HINTS
                .setFullAreaScan(false)//设置是否全区域识别，默认false
                .setAreaRectRatio(0.8f)//设置识别区域比例，默认0.8，设置的比例最终会在预览区域裁剪基于此比例的一个矩形进行扫码识别
                .setAreaRectVerticalOffset(0)//设置识别区域垂直方向偏移量，默认为0，为0表示居中，可以为负数
                .setAreaRectHorizontalOffset(0);//设置识别区域水平方向偏移量，默认为0，为0表示居中，可以为负数

        //在启动预览之前，设置分析器，只识别二维码
        getCameraScan()
                .setVibrate(true)//设置是否震动，默认为false
                .setNeedAutoZoom(true)//二维码太小时可自动缩放，默认为false
                .setAnalyzer(new MultiFormatAnalyzer(decodeConfig));//设置分析器,如果内置实现的一些分析器不满足您的需求，你也可以自定义去实现
    }

    @Override
    public boolean onScanResultCallback(Result result) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(result.getText())));

        return super.onScanResultCallback(result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PermissionUtils.REQUEST_MANAGE_EXTERNAL_STORAGE_CODE:
                    //从外部存储管理页面返回
                    if (!Environment.isExternalStorageManager()) {
                        Toast.makeText(this, "未打开外部存储管理开关，无法打开相册，抱歉", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!PermissionUtils.hasPermission(this, PermissionUtils.READ_EXTERNAL_STORAGE)) {
                        PermissionUtils.requestPermission(this, PermissionUtils.READ_EXTERNAL_STORAGE);
                        return;
                    }
                    openAlbum();
                    break;
                case SELECT_PHOTO_CODE:
                    if (data != null) {
                        Uri uri = data.getData();
                        ContentResolver cr = getContentResolver();

                        try {
                            Bitmap mBitmap = MediaStore.Images.Media.getBitmap(cr, uri);// 得到bitmap图片
                            Result result = CodeUtils.parseCodeResult(mBitmap);
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(result.getText())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 权限请求结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.REQUEST_STORAGE_CODE:
                //文件读写权限
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "您拒绝了读写文件权限，无法打开相册，抱歉。", Toast.LENGTH_SHORT).show();
                    return;
                }
                openAlbum();
                break;
            default:
                break;
        }
    }
}