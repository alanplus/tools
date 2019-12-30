package com.android.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Mouse on 2018/10/18.
 */
public abstract class AndroidToolsConfig {

    public static AndroidToolsConfig androidToolsConfig;

    public Context context;


    public abstract HashMap<String, String> getBaseParams();

    public abstract HashMap<String, String> getUserBaseParams();

    public abstract void onErrorCode(int code);

    public abstract void onActivityResume(Activity activity);

    public abstract void onActivityPause(Activity activity);

    public abstract void onActivityCreate(Activity activity);

    public abstract void onActivityDestroy(Activity activity);

    public abstract void onActivityStop(Activity activity);

    public abstract Handler getHandler();

    public boolean isDebug() {
        return true;
    }


    public static void init(AndroidToolsConfig androidToolsConfig) {
        AndroidToolsConfig.androidToolsConfig = androidToolsConfig;
    }

    public String getDownloadTempPath() {
        String path = "/data/data/" + context.getPackageName() + "/temp/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

}
