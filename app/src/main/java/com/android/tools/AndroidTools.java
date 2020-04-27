package com.android.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Mouse on 2018/10/15.
 */
public class AndroidTools {

    /**
     * dp转px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * dp转px
     *
     * @param dpValue
     * @return
     */
    public static int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率 px(像素) 转 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率 px(像素) 转 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕分辨
     *
     * @param context
     * @return
     */
    public static int[] getScreenSize(Context context) {
        DisplayMetrics metrics = getDisplayMetrics(context);
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }

    /**
     * 返回原尺寸的DisplayMetrics�?4.0默认会减掉�?�知栏部分，故要作处�?
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
        Display display = wm.getDefaultDisplay();

        //4.0之前的SDK直接返回当前metric
        if (Build.VERSION.SDK_INT < 14) {
            return metric;
        }

        int rawWidth = metric.widthPixels;
        int rawHeight = metric.heightPixels;
        try {
            Method mGetRawH = Display.class.getMethod("getRawHeight");
            Method mGetRawW = Display.class.getMethod("getRawWidth");
            rawWidth = (Integer) mGetRawW.invoke(display);
            rawHeight = (Integer) mGetRawH.invoke(display);
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        metric.widthPixels = rawWidth;
        metric.heightPixels = rawHeight;

        return metric;
    }

    public static String getMetaData(Context context, String key) {
        ApplicationInfo appInfo;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return null;
    }

    public static int getCurrentSdk() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 判断当前网络是否是wifi
     */
    @SuppressLint("MissingPermission")
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static int getVersionCode(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getVersionName(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static long getSystemLeftSpace() {
        File root = Environment.getDataDirectory();
        StatFs sf = new StatFs(root.getPath());
        long blockSize = sf.getBlockSize();
        return sf.getAvailableBlocks() * blockSize;
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    /**
     * 获取当前应用名称
     *
     * @param context
     * @return
     */
    public static String getApplicationName(Context context) {
        return getSomeApplicationName(context.getPackageName(),context);
    }

    /**
     * 通过包名获取应用名称
     * @param pkg
     * @param context
     * @return
     */
    public static String getSomeApplicationName(String pkg, Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    pkg, 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
