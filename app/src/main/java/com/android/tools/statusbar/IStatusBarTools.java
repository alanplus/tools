package com.android.tools.statusbar;

import android.app.Activity;

/**
 * Created by Mouse on 2018/11/6.
 */
public interface IStatusBarTools {

    boolean setStatusBarColor(Activity activity, int bgColor, boolean isWhite);

    boolean setStatusBarColor(Activity activity, int bgColor);

    boolean setStatusBarColor(Activity activity, boolean isWhite);

}
