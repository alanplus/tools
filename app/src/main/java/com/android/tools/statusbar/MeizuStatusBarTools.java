package com.android.tools.statusbar;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created by Mouse on 2018/11/6.
 */
public class MeizuStatusBarTools implements IStatusBarTools {

    @Override
    public boolean setStatusBarColor(Activity activity, int bgColor, boolean isWhite) {
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
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (!isWhite) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                return true;
            } catch (Exception e) {

            }
        }
        return false;
    }
}
