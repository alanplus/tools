package com.android.tools;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;

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
}
