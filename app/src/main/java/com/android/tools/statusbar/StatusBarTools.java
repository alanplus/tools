package com.android.tools.statusbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
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


    public static boolean isMiUI() {
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
    public static void setTranslucent(AppCompatActivity activity) {
        //5.0以上手动设置statusBar为透明。不能使用getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //因为这在4.4,5.0确实是全透明，但是在6.0是半透明!!!
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
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

}
