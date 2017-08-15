package com.juborajsarker.systeminfo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;

import com.juborajsarker.systeminfo.AppInfo;
import com.juborajsarker.systeminfo.R;
import com.juborajsarker.systeminfo.SystemInfoManager;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UtilsApp {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_READ = 1;


    public static File getDefaultAppFolder() {
        return new File(Environment.getExternalStorageDirectory() + "/SystemInfo");
    }


    public static File getAppFolder() {
        AppPreferences appPreferences = SystemInfoManager.getAppPreferences();
        return new File(appPreferences.getCustomPath());
    }

    public static Boolean copyFile(AppInfo appInfo) {
        Boolean res = false;

        File initialFile = new File(appInfo.getSource());
        File finalFile = getOutputFilename(appInfo);

        try {
            FileUtils.copyFile(initialFile, finalFile);
            res = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }


    public static String getAPKFilename(AppInfo appInfo) {
        AppPreferences appPreferences = SystemInfoManager.getAppPreferences();
        String res;

        switch (appPreferences.getCustomFilename()) {
            case "1":
                res = appInfo.getAPK() + "_" + appInfo.getVersion();
                break;
            case "2":
                res = appInfo.getName() + "_" + appInfo.getVersion();
                break;
            case "4":
                res = appInfo.getName();
                break;
            default:
                res = appInfo.getAPK();
                break;
        }

        return res;
    }


    public static File getOutputFilename(AppInfo appInfo) {
        return new File(getAppFolder().getPath() + "/" + getAPKFilename(appInfo) + ".apk");
    }

    public static Boolean deleteAppFiles() {
        Boolean res = false;
        File f = getAppFolder();
        if (f.exists() && f.isDirectory()) {
            File[] files = f.listFiles();
            for (File file : files) {
                file.delete();
            }
            if (f.listFiles().length == 0) {
                res = true;
            }
        }
        return res;
    }


    public static void goToGooglePlay(Context context, String id) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + id)));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + id)));
        }
    }


    public static void goToGooglePlus(Context context, String id) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + id)));
    }


    public static String getAppVersionName(Context context) {
        String res = "0.0.0.0";
        try {
            res = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }


    public static int getAppVersionCode(Context context) {
        int res = 0;
        try {
            res = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public static Intent getShareIntent(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.setType("application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }













    public static Boolean saveIconToCache(Context context, AppInfo appInfo) {
        Boolean res = false;

        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(appInfo.getAPK(), 0);
            File fileUri = new File(context.getCacheDir(), appInfo.getAPK());
            FileOutputStream out = new FileOutputStream(fileUri);
            Drawable icon = context.getPackageManager().getApplicationIcon(applicationInfo);
            BitmapDrawable iconBitmap = (BitmapDrawable) icon;
            iconBitmap.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
            res = true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return res;
    }


    public static Boolean removeIconFromCache(Context context, AppInfo appInfo) {
        File file = new File(context.getCacheDir(), appInfo.getAPK());
        return file.delete();
    }


    public static Drawable getIconFromCache(Context context, AppInfo appInfo) {
        Drawable res;

        try {
            File fileUri = new File(context.getCacheDir(), appInfo.getAPK());
            Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath());
            res = new BitmapDrawable(context.getResources(), bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            res = context.getResources().getDrawable(R.drawable.ic_android);
        }

        return res;
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Boolean checkPermissions(Activity activity) {
        Boolean res = false;
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_READ);
        } else {
            res = true;
        }

        return res;
    }

}
