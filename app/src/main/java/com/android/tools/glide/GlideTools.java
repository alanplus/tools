package com.android.tools.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.android.tools.rx.RxSchedulers;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Mouse on 2018/10/18.
 */
public class GlideTools {


    public static void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();

    }

    /**
     * 从 url中加载 圆形 图片
     * glide 使用ApplicationContext 会导致内存泄漏
     *
     * @param activity
     * @param imgView
     * @param url
     * @param defRes
     */
    public static void setCircleImageUrl(Context activity, ImageView imgView, String url, int defRes) {
        try {
            if (TextUtils.isEmpty(url)) {
                imgView.setImageResource(defRes);
                return;
            }
            Glide.with(activity).load(url).placeholder(defRes).dontAnimate().transform(new GlideCircleTransform(activity)).into(imgView);
        } catch (Exception e) {// 解决：Glide You cannot start a load for a destroyed activity

        }
    }


    /**
     * 通过url或 文件路径 加载图片
     *
     * @param imgView
     * @param url
     * @param defRes
     */
    public static void setImageUrl(Context activity, ImageView imgView, String url, int defRes) {
        try {
            if (TextUtils.isEmpty(url)) {
                imgView.setImageResource(defRes);
                return;
            }
            Glide.with(activity).load(url).dontAnimate().placeholder(defRes).into(imgView);
        } catch (Exception e) {// 解决：Glide You cannot start a load for a destroyed activity

        }

    }

    /**
     * 通过url或 文件路径 加载图片
     *
     * @param url
     */
    public static void setImageUrl(Context activity, String url, SimpleTarget<GlideDrawable> simpleTarget) {
        try {
            Glide.with(activity).load(url).dontAnimate().into(simpleTarget);
        } catch (Exception e) {// 解决：Glide You cannot start a load for a destroyed activity

        }
    }

    /**
     * 通过url或 文件路径 加载图片
     *
     * @param imgView
     * @param imageRes
     * @param defRes
     */
    public static void setImageUrl(Context activity, ImageView imgView, int imageRes, int defRes) {
        try {
            Glide.with(activity).load(imageRes).dontAnimate().placeholder(defRes).into(imgView);
        } catch (Exception e) {// 解决：Glide You cannot start a load for a destroyed activity

        }
    }

    /**
     * 通过url或 文件路径 加载图片
     *
     * @param imgView
     * @param imageRes
     */
    public static void setImageUrl(Context activity, ImageView imgView, int imageRes) {
        try {
            Glide.with(activity).load(imageRes).dontAnimate().into(imgView);
        } catch (Exception e) {// 解决：Glide You cannot start a load for a destroyed activity

        }
    }

    /**
     * 设置圆角图片 从资源中
     *
     * @param context
     * @param imgView
     * @param res
     */
    public static void setRoundImageUrl(Context context, ImageView imgView, int res) {
        try {
            Glide.with(context).load(res).transform(new GlideRoundTransform(context, 5)).dontAnimate().into(imgView);
        } catch (Exception ignore) {// 解决：Glide You cannot start a load for a destroyed activity
        }
    }

    /**
     * 设置圆角图片 从Url中
     *
     * @param activity
     * @param imgView
     * @param url
     * @param defRes
     */
    public static void setRoundImageUrl(Context activity, ImageView imgView, String url, int defRes) {
        try {
            if (TextUtils.isEmpty(url)) {
                imgView.setImageResource(defRes);
                return;
            }
            Glide.with(activity).load(url).transform(new CenterCrop(activity), new GlideRoundTransform(activity, 5)).dontAnimate().placeholder(defRes).into(imgView);
        } catch (Exception e) {// 解决：Glide You cannot start a load for a destroyed activity

        }
    }


    public static Bitmap getBitmap(String url, Context context) throws ExecutionException, InterruptedException {
        try {
            if (TextUtils.isEmpty(url)) {
                return null;
            }
            return Glide.with(context).load(url).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
        } catch (Exception e) {// 解决：Glide You cannot start a load for a destroyed activity

        }
        return null;
    }

    /**
     * ------------------------- 带动画 --------------
     */
    /**
     * 从 url中加载 圆形 图片
     * glide 使用ApplicationContext 会导致内存泄漏
     *
     * @param activity
     * @param imgView
     * @param url
     * @param defRes
     */
    public static void setCircleImageUrlWithAnim(Context activity, ImageView imgView, String url, int defRes) {
        try {
            if (TextUtils.isEmpty(url)) {
                imgView.setImageResource(defRes);
                return;
            }
            Glide.with(activity).load(url).placeholder(defRes).transform(new GlideCircleTransform(activity)).into(imgView);
        } catch (Exception e) {// 解决：Glide You cannot start a load for a destroyed activity

        }
    }


    /**
     * 通过url或 文件路径 加载图片
     *
     * @param imgView
     * @param url
     * @param defRes
     */
    public static void setImageUrlWithAnim(Context activity, ImageView imgView, String url, int defRes) {
        try {
            if (TextUtils.isEmpty(url)) {
                imgView.setImageResource(defRes);
                return;
            }
            Glide.with(activity).load(url).placeholder(defRes).into(imgView);
        } catch (Exception e) {// 解决：Glide You cannot start a load for a destroyed activity

        }
    }

    /**
     * 通过url或 文件路径 加载图片
     *
     * @param imgView
     * @param imageRes
     * @param defRes
     */
    public static void setImageUrlWithAnim(Context activity, ImageView imgView, int imageRes, int defRes) {
        try {
            Glide.with(activity).load(imageRes).placeholder(defRes).into(imgView);
        } catch (Exception e) {// 解决：Glide You cannot start a load for a destroyed activity

        }
    }

    /**
     * 设置圆角图片 从资源中
     *
     * @param context
     * @param imgView
     * @param res
     */
    public static void setRoundImageUrlWithAnim(Context context, ImageView imgView, int res) {
        try {
            Glide.with(context).load(res).transform(new GlideRoundTransform(context, 5)).into(imgView);
        } catch (Exception e) {// 解决：Glide You cannot start a load for a destroyed activity

        }

    }

    /**
     * 设置圆角图片 从Url中
     *
     * @param activity
     * @param imgView
     * @param url
     * @param defRes
     */
    public static void setRoundImageUrlWithAnim(Context activity, ImageView imgView, String url, int defRes) {
        try {
            if (TextUtils.isEmpty(url)) {
                imgView.setImageResource(defRes);
                return;
            }
            Glide.with(activity).load(url).transform(new CenterCrop(activity), new GlideRoundTransform(activity, 5)).placeholder(defRes).into(imgView);
        } catch (Exception e) {// 解决：Glide You cannot start a load for a destroyed activity

        }

    }

    public static void setBackgound(View view, String url) {
        Observable.create((ObservableOnSubscribe<Bitmap>) e -> {
            e.onNext(getBitmap(url, view.getContext()));
            e.onComplete();
        }).compose(RxSchedulers.ioMain()).doOnNext(bitmap -> {
            if (null == bitmap) return;
            view.setBackgroundDrawable(new BitmapDrawable(bitmap));
        }).subscribe();
    }

}
