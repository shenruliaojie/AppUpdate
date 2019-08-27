package com.demo.appupdate.updater.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;
import java.io.File;

/**
 * author： liguangwei
 * date： 2019/08/27
 **/
public class AppUtils {

    public static long getVersionCode(Context context){
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                long longVersionCode = packageInfo.getLongVersionCode();
                return longVersionCode;
            }else{
                return packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void installApk(Context context, File targetFile){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(intent.ACTION_VIEW);
        Uri uri = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            uri = FileProvider.getUriForFile(context,context.getPackageName()+".fileprovider",targetFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }else{
            uri = Uri.fromFile(targetFile);
        }
        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
