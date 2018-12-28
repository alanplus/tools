package com.android.tools.buttondrawable;

import android.support.annotation.IntDef;

/**
 * Created by Mouse on 2018/9/11.
 */

public class DrawableStyle {

    // 主题色 圆角5dp
    public static final int THEME_MAIN = 0;
    public static final int THEME_MAIN_STATE = 1;
    public static final int THEME_MAIN_LARGE = 2;
    public static final int THEME_MAIN_STATE_LARGE = 3;

    @IntDef({THEME_MAIN,THEME_MAIN_STATE,THEME_MAIN_LARGE,THEME_MAIN_STATE_LARGE})
    public @interface Style{}

}
