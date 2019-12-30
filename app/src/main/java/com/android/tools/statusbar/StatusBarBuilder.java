package com.android.tools.statusbar;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.ColorInt;

/**
 * @author Alan
 * 时 间：2019-12-27
 * 简 述：<功能简述>
 */
public class StatusBarBuilder {

    private boolean isWhiteText;
    private @ColorInt
    int bgColor;
    private Activity activity;

    public StatusBarBuilder(Activity activity) {
        this.activity = activity;
    }

    public static StatusBarBuilder get(Activity activity) {
        StatusBarBuilder builder = new StatusBarBuilder(activity);
        return builder;
    }

    public StatusBarBuilder setWhiteText(boolean whiteText) {
        isWhiteText = whiteText;
        return this;
    }

    public StatusBarBuilder setBgColor(int bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    public void build() {
        boolean b = StatusBarTools.getStatusBarTools().setStatusBarColor(activity, bgColor, isWhiteText);
        if (!b) {
            StatusBarTools.getStatusBarTools().setStatusBarColor(activity, Color.BLACK, true);
        }
        activity = null;
    }
}
