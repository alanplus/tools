package com.android.tools.statusbar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.android.tools.AndroidTools;
import com.android.tools.R;

import java.lang.reflect.Method;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

/**
 * Created by Mouse on 2018/11/6.
 */
public class StatusBarTools implements IStatusBarTools {


    private static StatusBarTools statusBarTools;
    private IStatusBarTools iStatusBarTools;

    private StatusBarTools() {
        if (isMiUI()) {
            iStatusBarTools = new MiUIStatusBarTools();
        } else if (isFlyme()) {
            iStatusBarTools = new MeizuStatusBarTools();
        } else {
            iStatusBarTools = new AndroidStatusBarTools();
        }
    }

    public static StatusBarTools getStatusBarTools() {
        if (null == statusBarTools) {
            statusBarTools = new StatusBarTools();
        }
        return statusBarTools;
    }


    @Override
    public boolean setStatusBarColor(Activity activity, int bgColor, boolean isWhite) {
        return iStatusBarTools.setStatusBarColor(activity, bgColor, isWhite);
    }

    @Override
    public boolean setStatusBarColor(Activity activity, int bgColor) {
        return iStatusBarTools.setStatusBarColor(activity, bgColor);
    }

    @Override
    public boolean setStatusBarColor(Activity activity, boolean isWhite) {
        return iStatusBarTools.setStatusBarColor(activity, isWhite);
    }


    @SuppressLint("PrivateApi")
    public static boolean isMiUI() {
        try {
            Class.forName("android.view.MiuiWindowManager$LayoutParams");
        } catch (Exception e) {
            return false;
        }
        return Build.MANUFACTURER.equals("Xiaomi");
    }

    public static boolean isFlyme() {
        try {
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }


    /**
     * 设置透明悬浮状态栏,让内容延伸进去
     *
     * @param activity
     */
    public static boolean setTranslucent(AppCompatActivity activity) {
        return translucent(activity);
    }


    /**
     * 设置全屏
     *
     * @param activity
     */
    public static void setFullScreen(AppCompatActivity activity) {
        //4.1及以上通用flags组合
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().getDecorView().setSystemUiVisibility(flags | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    }

    public static void hiddenStatusBar(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
        View view = viewGroup.findViewById(R.id.statusbarutil_translucent_view);
        if (view != null) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static int getStatubarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return AndroidTools.dip2px(context, 28);
    }


    public static boolean translucent(Activity activity) {
        return translucent(activity, 0x00000000);
    }

    /**
     * 沉浸式状态栏
     * 支持 4.4 以上版本的 MIUI 和 Flyme，以及 5.0 以上版本的其他 Android
     *
     * @param activity
     */
    @TargetApi(19)
    public static boolean translucent(Activity activity, @ColorInt int colorOn5x) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            // 版本小于4.4，绝对不考虑沉浸式
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && supportTransclentStatusBar6()) {
                // android 6以后可以改状态栏字体颜色，因此可以自行设置为透明
                // ZUK Z1是个另类，自家应用可以实现字体颜色变色，但没开放接口
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
//                setStatusBarLightMode(activity);
                return true;
            } else {
                // android 5不能修改状态栏字体颜色，因此直接用FLAG_TRANSLUCENT_STATUS，nexus表现为半透明
                // 魅族和小米的表现如何？
                // update: 部分手机运用FLAG_TRANSLUCENT_STATUS时背景不是半透明而是没有背景了。。。。。
//                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                // 采取setStatusBarColor的方式，部分机型不支持，那就纯黑了，保证状态栏图标可见
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(colorOn5x);
                return true;
            }
        }

        // 小米和魅族4.4 以上版本支持沉浸式
        if (isFlyme() || isMiUI()) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            return true;
        }

        return false;
    }

    /**
     * Android6.0是否可以启用 window.setStatusBarColor(Color.TRANSPARENT);
     *
     * @return
     */
    public static boolean supportTransclentStatusBar6() {
        return true;
    }


}
