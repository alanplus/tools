package com.android.tools.util.bitmpap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;


/**
 * @author Alan
 * 时 间：2019-12-27
 * 简 述：<功能简述>
 */
public class BitmapHandlerBuilder {

    private Bitmap bitmap;
    private boolean isDel;

    // 处理 高斯模糊
    private int radius;
    private float scale;
    private @ColorInt
    int coverColor;
    private boolean hasCover;

    private int originWidth;
    private int originHeight;

    private String targetPath;

    //clip
    int startX, startY, stopX, stopY;

    private BitmapHandlerBuilder(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.isDel = false;
        this.radius = 25;
        this.scale = 0f;
        this.coverColor = Color.parseColor("#80000000");
        this.hasCover = false;
    }

    public static BitmapHandlerBuilder get(Bitmap bitmap) {
        BitmapHandlerBuilder bitmapHandlerBuilder = new BitmapHandlerBuilder(bitmap);
        bitmapHandlerBuilder.setScale(0.5f);
        return bitmapHandlerBuilder;
    }

    public static BitmapHandlerBuilder file(Context context, String path) {
        Bitmap bitmap = BitmapUtils.getBitmapByPath(path, context);
        return new BitmapHandlerBuilder(bitmap);
    }

    public static BitmapHandlerBuilder res(Context context, @DrawableRes int res) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res, BitmapUtils.getBitmapOption(context, res));
        return new BitmapHandlerBuilder(bitmap);
    }


    public BitmapHandlerBuilder setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        return this;
    }

    public BitmapHandlerBuilder setDel(boolean del) {
        isDel = del;
        return this;
    }

    public BitmapHandlerBuilder setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    public BitmapHandlerBuilder setScale(float scale) {
        this.scale = scale;
        return this;
    }

    public BitmapHandlerBuilder setCoverColor(@ColorInt int coverColor) {
        this.hasCover = true;
        this.coverColor = coverColor;
        return this;
    }

    public BitmapHandlerBuilder setHasCover(boolean hasCover) {
        this.hasCover = hasCover;
        return this;
    }

    public BitmapHandlerBuilder setOriginWidth(int originWidth) {
        this.originWidth = originWidth;
        return this;
    }

    public BitmapHandlerBuilder setOriginHeight(int originHeight) {
        this.originHeight = originHeight;
        return this;
    }

    public BitmapHandlerBuilder setTargetPath(String targetPath) {
        this.targetPath = targetPath;
        return this;
    }

    public BitmapHandlerBuilder setRect(int startX, int startY, int stopX, int stopY) {
        this.startX = startX;
        this.startY = startY;
        this.stopX = stopX;
        this.stopY = stopY;
        return this;
    }

    /**
     * 图片高斯模糊
     * 主要参数
     * 1. radius 模糊半径
     * 2. scale 缩放半径
     * 3. hasCover 是否有蒙层
     * 4. coverColor 蒙层颜色
     *
     * @return
     */
    public Bitmap blur() {
        if (null == this.bitmap) {
            return null;
        }
        Bitmap bitmap = copy(this.bitmap);
        bitmap = handlerClip(bitmap, true);
        bitmap = handlerBitmapSize(bitmap, true);
        Bitmap bBlur = BitmapUtils.doBlur(bitmap, radius, scale);
        BitmapUtils.recycle(bitmap);
        if (hasCover) {
            BitmapUtils.drawCover(bBlur, coverColor);
        }

        if (isDel) {
            BitmapUtils.recycle(this.bitmap, bitmap);
        }

        return bBlur;
    }

    public Bitmap clip() {
        if (null == this.bitmap) {
            return null;
        }
        Bitmap bitmap = handlerClip(this.bitmap, false);
        if (isDel) {
            BitmapUtils.recycle(this.bitmap);
        }
        return bitmap;
    }

    /**
     * 获取bitmap
     * 主要参数
     * 1. scale 缩放比例
     * 2. originWidth 实际宽度
     * 3. originHeight 实际高度
     *
     * @return
     */
    public Bitmap get() {
        if (this.bitmap == null) {
            return null;
        }
        if (scale != 0) {
            Bitmap bitmap = Bitmap.createScaledBitmap(this.bitmap, (int) (this.bitmap.getWidth() * scale), (int) (this.bitmap.getHeight() * scale), false);
            if (isDel) {
                BitmapUtils.recycle(this.bitmap);
            }
            return bitmap;
        }
        Bitmap bitmap = handlerBitmapSize(this.bitmap, false);
        if (isDel) {
            BitmapUtils.recycle(this.bitmap);
        }
        return bitmap;
    }

    private Bitmap copy(Bitmap b) {
        return b.copy(Bitmap.Config.ARGB_8888, true);
    }

    private Bitmap handlerBitmapSize(Bitmap bitmap, boolean isRecycle) {
        int width = originWidth;
        int height = originHeight;
        if (originWidth == 0 && originHeight == 0) {
            return bitmap;
        } else if (originWidth == 0) {
            width = originHeight * bitmap.getWidth() / bitmap.getHeight();
        } else if (originHeight == 0) {
            height = originWidth * bitmap.getHeight() / bitmap.getWidth();
        }

        if (width == bitmap.getWidth() && height == bitmap.getHeight()) {
            return bitmap;
        }
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        if (isRecycle) {
            BitmapUtils.recycle(bitmap);
        }
        return scaledBitmap;
    }

    private Bitmap handlerClip(Bitmap b, boolean isRecycle) {
        if (startX != 0 || startY != 0 || stopX != 0 || stopY != 0) {
            stopX = stopX == 0 ? b.getWidth() : stopX;
            stopY = stopY == 0 ? b.getHeight() : stopY;
            Bitmap bTemp = Bitmap.createBitmap(b, startX, startY, stopX - startX, stopY - startY);
            if (isRecycle) {
                BitmapUtils.recycle(b);
            }
            return bTemp;
        }
        return b;
    }

    public boolean save() {
        Bitmap bitmap = get();
        return BitmapUtils.save(bitmap, this.targetPath);
    }
}
