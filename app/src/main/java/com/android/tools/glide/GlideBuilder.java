package com.android.tools.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AnimRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.view.View;
import android.widget.ImageView;

import com.android.tools.Logger;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Alan
 * 时 间：2019-12-30
 * 简 述：<功能简述>
 */
public class GlideBuilder {

    private String url;
    private int res;
    private int placeHolder;
    // 0 正常 1 圆形 2 圆角

    private @ImgType
    int imageType;

    private int corner;


    // 0 url 1 res
    private int type;

    private @AnimRes
    int anim;

    private boolean isAnim;

    public static final int IMG_NORMAL = 0;
    public static final int IMG_CIRCLE = 1;
    public static final int IMG_CORNER = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({IMG_NORMAL, IMG_CIRCLE, IMG_CORNER})
    public @interface ImgType {
    }

    private GlideBuilder(String url) {
        this.url = url;
        this.type = 0;
        this.isAnim = false;
    }

    private GlideBuilder(int res) {
        this.res = res;
        this.type = 1;
        this.isAnim = false;
    }

    public static GlideBuilder get(String url) {
        return new GlideBuilder(url);
    }

    public static GlideBuilder get( @DrawableRes int res){
        return new GlideBuilder(res);
    }

    public void into(View imageView) {

        try {
            RequestManager requestManager = Glide.with(imageView.getContext());
            DrawableTypeRequest load = load(requestManager);
            if (null == load) {
                return;
            }
            handlerShape(imageView.getContext(), load);

            if (placeHolder != 0) {
                load.placeholder(placeHolder);
            }
            if(imageView instanceof ImageView){
                load.into((ImageView) imageView);
            }else {
                load.into(new SimpleTarget<GlideDrawable>() {

                    @Override
                    public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        Drawable current = glideDrawable.getCurrent();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            imageView.setBackground(current);
                        }else{
                            imageView.setBackgroundDrawable(current);
                        }
                    }
                });
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public void into(Context context,SimpleTarget<GlideDrawable> simpleTarget){

        try {
            RequestManager requestManager = Glide.with(context);
            DrawableTypeRequest load = load(requestManager);
            if (null == load) {
                return;
            }
            handlerShape(context, load);

            if (placeHolder != 0) {
                load.placeholder(placeHolder);
            }

            load.into(simpleTarget);
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public GlideBuilder setPlaceHolder(int placeHolder) {
        this.placeHolder = placeHolder;
        return this;
    }

    public GlideBuilder setImageType(int imageType) {
        this.imageType = imageType;
        return this;
    }

    public GlideBuilder setCorner(int corner) {
        this.corner = corner;
        return this;
    }

    public GlideBuilder setType(int type) {
        this.type = type;
        return this;
    }

    public GlideBuilder setAnim(int anim) {
        this.anim = anim;
        this.isAnim = true;
        return this;
    }

    public GlideBuilder setAnimEnable(boolean anim) {
        isAnim = anim;
        return this;
    }

    private DrawableTypeRequest load(RequestManager requestManager) {
        switch (type) {
            case 0:
                return requestManager.load(this.url);
            case 1:
                return requestManager.load(res);
        }
        return null;
    }

    private void handlerShape(Context context, DrawableTypeRequest requestManager) {

        switch (imageType) {
            case IMG_CIRCLE:
                requestManager.transform(new GlideCircleTransform(context));
                break;
            case IMG_CORNER:
                if (corner != 0) {
                    requestManager.transform(new GlideRoundTransform(context, corner));
                }
                break;
        }
    }

    private void handlerAnim(DrawableTypeRequest requestManager) {
        if (isAnim) {
            if(anim!=0){
                requestManager.animate(anim);
            }
        } else {
            requestManager.dontAnimate();
        }
    }
}
