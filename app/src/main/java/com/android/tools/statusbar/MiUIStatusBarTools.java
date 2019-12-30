package com.android.tools.statusbar;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;

import com.android.tools.Logger;

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
        return new AndroidStatusBarTools().setStatusBarColor(activity, bgColor);
    }

    @Override
    public boolean setStatusBarColor(Activity activity, boolean isWhite) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!isWhite) {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }
                } else {
                    int flag = activity.getWindow().getDecorView().getSystemUiVisibility()
                            & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    window.getDecorView().setSystemUiVisibility(flag);
                }

                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (!isWhite) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }


                return true;
            } catch (Exception e) {
                Logger.error(e);
            }
        }
        return false;
    }
}
