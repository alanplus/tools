package com.android.tools.statusbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.android.tools.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Mouse on 2018/11/6.
 */
public class MiUIStatusBarTools implements IStatusBarTools {


    @Override
    public boolean setStatusBarColor(Activity activity, int bgColor, boolean isWhite) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return false;
        if (setStatusBarColor(activity, isWhite)) {
            return setStatusBarColor(activity, bgColor);
        }
        return false;
    }

    @Override
    public boolean setStatusBarColor(Activity activity, int bgColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new AndroidStatusBarTools().setStatusBarColor(activity, bgColor);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(bgColor);
            return true;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup var2 = (ViewGroup) activity.getWindow().getDecorView();
            View var3 = var2.findViewById(R.id.statusbarutil_translucent_view);
            if (var3 != null) {
                if (var3.getVisibility() == View.GONE) {
                    var3.setVisibility(View.VISIBLE);
                }
                var3.setBackgroundColor(bgColor);
            } else {
                View statusBarView = new View(activity);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
                statusBarView.setLayoutParams(params);
                statusBarView.setBackgroundColor(bgColor);
                statusBarView.setId(R.id.statusbarutil_fake_status_bar_view);
                statusBarView.setVisibility(View.VISIBLE);
                var2.addView(statusBarView);
            }
            setRootView(activity);
            return true;
        }

        return false;
    }

    private static void setRootView(Activity var0) {
        ViewGroup var1 = var0.findViewById(android.R.id.content);
        int var2 = 0;
        for (int var3 = var1.getChildCount(); var2 < var3; ++var2) {
            View childView = var1.getChildAt(var2);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }

    }

    private static int getStatusBarHeight(Context var0) {
        int var1 = var0.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return var0.getResources().getDimensionPixelSize(var1);
    }

    @Override
    public boolean setStatusBarColor(Activity activity, boolean isWhite) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", Integer.TYPE, Integer.TYPE);
                if (!isWhite) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!isWhite) {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        window.setStatusBarColor(Color.WHITE);
                    }
                } else {
                    int flag = activity.getWindow().getDecorView().getSystemUiVisibility()
                            & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    window.getDecorView().setSystemUiVisibility(flag);


                }

                return true;
            } catch (Exception e) {
            }
        }
        return false;
    }
}
