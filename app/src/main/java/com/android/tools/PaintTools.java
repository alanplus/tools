package com.android.tools;

import android.graphics.Paint;

/**
 * Created by Mouse on 2018/10/15.
 */
public class PaintTools {

    /**
     * 获取文字的高度
     *
     * @param paint
     * @return
     */
    public static int getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.top) + 2;
    }

    /**
     * @return 返回指定笔离文字顶部的基准距离
     */
    public static float getFontLeading(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.leading - fm.ascent;
    }

    /**
     * 获取文字的宽度
     *
     * @param str
     * @param paint
     * @return
     */
    public static int getStringWidth(String str, Paint paint) {
        return (int) paint.measureText(str);
    }
}
