package com.zjw.mvvm_demo.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;


import java.io.File;

public class UpdateUtils {

    public static void downloadFile(Context context, String name, String url) {
        Uri uri = Uri.parse(url);
        DownloadManager downloadManager = (DownloadManager) context
                .getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        //在通知栏中显示
        request.setVisibleInDownloadsUi(true);
        request.setTitle("应用更新");
        request.setDescription("");
        //MIME_MapTable是所有文件的后缀名所对应的MIME类型的一个String数组  {".apk",    "application/vnd.android.package-archive"},
        request.setMimeType("application/vnd.android.package-archive");
        //指定下载地址
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);
        //根据下载地址找到原来的安装包并删除
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Uri uriFile = Uri.withAppendedPath(Uri.fromFile(file), name);
        InstallReceiver.uriToFile(uriFile, context).delete();
        //下载管理Id
        downloadManager.enqueue(request);
    }

}
