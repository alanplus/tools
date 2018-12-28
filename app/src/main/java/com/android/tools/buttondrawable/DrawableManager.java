package com.android.tools.buttondrawable;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.support.annotation.ColorInt;

/**
 * Created by Mouse on 2018/8/2.
 */

public class DrawableManager {

    private static final int COLOR_DEFAULT = Color.parseColor("#31af00");
    private static final int COLOR_DEFAULT_PRESS = Color.parseColor("#31bF00");
    private static final int COLOR_DEFAULT_UNENABLE = Color.argb(0xff, 217, 216, 217);

    /**
     * get a special radiu and colorldrawable
     *
     * @param color
     * @param radio
     * @return
     */
    public static Drawable getShapeDrawable(@ColorInt int color, int radio) {
        float[] out = new float[]{radio, radio, radio, radio, radio, radio, radio, radio};
        ShapeDrawable drawable = new ShapeDrawable(new RoundRectShape(out, null, null));
        drawable.getPaint().setColor(color);
        return drawable;
    }

    public static Drawable getGradientDrawable(@ColorInt int color, int radio, int width, int strokeColor) {
        float[] out = new float[]{radio, radio, radio, radio, radio, radio, radio, radio};
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadii(out);
        gradientDrawable.setStroke(width,strokeColor);
        return gradientDrawable;
    }

    /**
     * 5.0以下 点击效果
     * @param ncolor
     * @param pcolor
     * @param radio
     * @return
     */
    private static StateListDrawable getCommonClickDrawable(@ColorInt int ncolor, @ColorInt int pcolor, int radio) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable ndrawable = getShapeDrawable(ncolor, radio);
        Drawable pdrawable = getShapeDrawable(pcolor, radio);
        Drawable undrawable = getShapeDrawable(Color.parseColor("#e0e0e0"), radio);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pdrawable);
        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, undrawable);
        stateListDrawable.addState(new int[]{}, ndrawable);
        return stateListDrawable;
    }

    /**
     * 5.0 以上
     * @param ncolor
     * @param pcolor
     * @param radio
     * @return
     */
    private static Drawable getRippleDrawable(@ColorInt int ncolor, @ColorInt int pcolor, int radio) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable shapeDrawable = getEnableDrawable(ncolor, pcolor, radio);
//            Drawable shapeDrawable = getShapeDrawable(ncolor, radio);
            return new RippleDrawable(ColorStateList.valueOf(pcolor), shapeDrawable, null);
        }
        return null;
    }

    /**
     * 点击效果
     * @param ncolor
     * @param pcolor
     * @param radio
     * @return
     */
    public static Drawable getClickDrawable(@ColorInt int ncolor, @ColorInt int pcolor, int radio){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            return getRippleDrawable(ncolor,pcolor,radio);
        }
        return getCommonClickDrawable(ncolor,pcolor,radio);
    }

    /**
     * 正常状态和不可用状态
     * @param ncolor
     * @param pcolor
     * @param radio
     * @return
     */
    public static Drawable getEnableDrawable(@ColorInt int ncolor, @ColorInt int pcolor, int radio){
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable shapeDrawable = getShapeDrawable(ncolor, radio);
        Drawable shapeDrawable1 = getShapeDrawable(pcolor, radio);
        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, shapeDrawable1);
        stateListDrawable.addState(new int[]{}, shapeDrawable);
        return stateListDrawable;
    }

    public static Drawable getBorderDrawable(){
        Drawable shapeDrawable = getShapeDrawable(Color.RED, 15);
        InsetDrawable insetDrawable = new InsetDrawable(shapeDrawable,10);
        insetDrawable.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC);
        return insetDrawable;
    }
}
