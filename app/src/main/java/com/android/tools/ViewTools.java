package com.android.tools;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Mouse on 2018/10/15.
 */
public class ViewTools {

    public static void setClickEffection(View view) {
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setAlpha(0.6f);
                    break;
                case MotionEvent.ACTION_OUTSIDE:
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_SCROLL:
                    v.setAlpha(1.0f);
                    break;
                default:
                    break;
            }

            return false;
        });
    }

    public static Bitmap getViewScreenBitmap(View v) {
        if (v == null) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        Bitmap bitmap = v.getDrawingCache();
        return bitmap;
    }

    /**
     * 防止重复点击
     *
     * @param target
     * @param listener
     */
    public static void preventRepeatedClick(final View target, final View.OnClickListener listener) {
        if (null == target || listener == null) {
            return;
        }
        RxView.clicks(target).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object value) {
                listener.onClick(target);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 防止重复点击
     *
     * @param target
     * @param onItemClickListener
     */
    public static void preventRepeatedClick(final ListView target, final AdapterView.OnItemClickListener onItemClickListener) {
        if (null == target || onItemClickListener == null) {
            return;
        }
        RxAdapterView.itemClicks(target).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                onItemClickListener.onItemClick(target, null, value, 0);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
