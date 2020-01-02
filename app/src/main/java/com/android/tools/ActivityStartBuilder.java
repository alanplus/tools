package com.android.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.android.tools.widget.ToastManager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mouse on 2018/10/29.
 */
public class ActivityStartBuilder {

    private Intent intent;
    private Context context;

    public ActivityStartBuilder(Context context, Class clazz) {
        this.context = context;
        intent = new Intent();
        intent.setClass(context, clazz);
    }

    public ActivityStartBuilder add(String key, String value) {
        intent.putExtra(key, value);
        return this;
    }

    public ActivityStartBuilder add(String key, Serializable value) {
        intent.putExtra(key, value);
        return this;
    }

    public ActivityStartBuilder add(String key, ArrayList value) {
        intent.putExtra(key, value);
        return this;
    }

    public ActivityStartBuilder add(String key, int value) {
        intent.putExtra(key, value);
        return this;
    }

    public void start() {
        context.startActivity(intent);
    }

    public void startWithoutAnim() {
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    public static void startMarket(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        } catch (Exception e) {
            ToastManager.getInstance().showToast(context, "没有找到应用市场");
        }

    }
}
