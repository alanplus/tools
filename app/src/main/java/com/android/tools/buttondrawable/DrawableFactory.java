package com.android.tools.buttondrawable;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;

import com.alan.common.AndroidTools;

/**
 * Created by Mouse on 2018/9/11.
 */

public class DrawableFactory {

    private static final int COLOR_DEFAULT = Color.parseColor("#4dce96");
    private static final int COLOR_DEFAULT_PRESS = Color.parseColor("#4dbd96");
    private static final int COLOR_DEFAULT_UNENABLE = Color.argb(0xff, 217, 216, 217);

    private static final int radio = 5;
    private static final int radio_llarge = 25;


    public static Drawable buildButtonDrawable(Context context, @DrawableStyle.Style int theme) {
        return buildButtonDrawable(context, theme, radio);
    }

    public static Drawable buildButtonDrawable(Context context, @DrawableStyle.Style int theme, int radio) {
        Drawable drawable = null;
        switch (theme) {
            case DrawableStyle.THEME_MAIN:
                drawable = DrawableManager.getClickDrawable(COLOR_DEFAULT, COLOR_DEFAULT_PRESS, AndroidTools.dip2px( radio));
                break;
            case DrawableStyle.THEME_MAIN_STATE:
                drawable = DrawableManager.getEnableDrawable(COLOR_DEFAULT, COLOR_DEFAULT_UNENABLE, AndroidTools.dip2px( radio));
                break;
            case DrawableStyle.THEME_MAIN_LARGE:
                drawable = DrawableManager.getClickDrawable(COLOR_DEFAULT, COLOR_DEFAULT_PRESS, AndroidTools.dip2px( radio_llarge));
                break;
            case DrawableStyle.THEME_MAIN_STATE_LARGE:
                drawable = DrawableManager.getEnableDrawable(COLOR_DEFAULT, COLOR_DEFAULT_UNENABLE, AndroidTools.dip2px( radio_llarge));
                break;
        }
        return drawable;
    }


    public static Drawable buildButtonDrawable(Context context, @DrawableStyle.Style int theme, int radio, @ColorInt int dcolor, @ColorInt int color) {
        Drawable drawable = null;
        switch (theme) {
            case DrawableStyle.THEME_MAIN:
                drawable = DrawableManager.getClickDrawable(dcolor, color, AndroidTools.dip2px(radio));
                break;
            case DrawableStyle.THEME_MAIN_STATE:
                drawable = DrawableManager.getEnableDrawable(dcolor, color, AndroidTools.dip2px(radio));
                break;
        }
        return drawable;
    }

    public static Drawable buildClickButtonDrawable(@ColorInt int dcolor, @ColorInt int color) {
        return DrawableManager.getClickDrawable(dcolor, color, 0);
    }

    public static Drawable buildShapeDrawable(int radio, @ColorInt int color) {
        return DrawableManager.getShapeDrawable(color, radio);
    }

    public static Drawable buildResDrawable(Context context, @DrawableRes int dres, @DrawableRes int pres) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable drawable = context.getResources().getDrawable(dres);
        Drawable drawable1 = context.getResources().getDrawable(pres);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, drawable1);
        return drawable;
    }

    /**
     * 从上到下
     *
     * @param colors
     * @return
     */
    public static Drawable buildGridentDrawable(int... colors) {
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);//创建drawable
        gd.setCornerRadius(0);
        gd.setStroke(0, Color.parseColor("#00000000"));
        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        return gd;
    }

    /**
     * @param unColor
     * @param color
     * @param radio
     * @return
     */
    public static Drawable buildChcekBoxDrawable(@ColorInt int unColor, @ColorInt int color, int radio) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable shapeDrawable = DrawableManager.getShapeDrawable(unColor, radio);
        Drawable shapeDrawable1 = DrawableManager.getShapeDrawable(color, radio);
        stateListDrawable.addState(new int[]{android.R.attr.checked}, shapeDrawable1);
        stateListDrawable.addState(new int[]{-android.R.attr.checked}, shapeDrawable);
        return stateListDrawable;
    }

    /**
     * @param unColor
     * @param color
     * @param radio
     * @return
     */
    public static Drawable buildChcekBoxDrawableWithBorder(@ColorInt int unColor, @ColorInt int color, int radio, int unwidth, int unstrokeColr, int width, int strokecolor) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable shapeDrawable = DrawableManager.getGradientDrawable(unColor, radio, unwidth, unstrokeColr);
        Drawable shapeDrawable1 = DrawableManager.getGradientDrawable(color, radio, width, strokecolor);
        stateListDrawable.addState(new int[]{-android.R.attr.state_checked}, shapeDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, shapeDrawable1);
        stateListDrawable.addState(new int[]{}, shapeDrawable);
        return stateListDrawable;
    }

    public static ColorStateList buildChcekcBoxColor(@ColorInt int unColor, @ColorInt int color) {
        int[] colors = new int[]{unColor, color, unColor};
        int[][] states = new int[3][];
        states[0] = new int[]{-android.R.attr.state_checked};
        states[1] = new int[]{android.R.attr.state_checked};
        states[2] = new int[]{};
        return new ColorStateList(states, colors);
    }

    public static ColorStateList buildClickTextColor(@ColorInt int unColor, @ColorInt int color) {
        int[] colors = new int[]{color, unColor};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{};
        return new ColorStateList(states, colors);
    }

    /**
     * 文字点击效果
     * @param unColor
     * @param color
     * @param unenablecolor
     * @return
     */
    public static ColorStateList buildClickTextColor(@ColorInt int unColor, @ColorInt int color, @ColorInt int unenablecolor) {
        int[] colors = new int[]{color, unenablecolor, unColor};
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{-android.R.attr.state_enabled};
        states[2] = new int[]{};
        return new ColorStateList(states, colors);
    }

    public static ColorStateList buildEnableTextColor(@ColorInt int unColor, @ColorInt int color) {
        int[] colors = new int[]{unColor, color, unColor};
        int[][] states = new int[3][];
        states[0] = new int[]{-android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled};
        states[2] = new int[]{};
        return new ColorStateList(states, colors);
    }

}
