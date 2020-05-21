package com.android.tools.statusbar;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.android.tools.R;

/**
 * Created by Mouse on 2018/11/6.
 */
public class AndroidStatusBarTools implements IStatusBarTools {

    @Override
    public boolean setStatusBarColor(Activity activity, int bgColor, boolean isWhite) {
        if (setStatusBarColor(activity, isWhite)) {
            return setStatusBarColor(activity, bgColor);
        }
        return false;
    }

    @Override
    public boolean setStatusBarColor(Activity activity, int bgColor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(bgColor);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
            View view = viewGroup.findViewById(R.id.statusbarutil_translucent_view);
            if (view != null) {
                if (view.getVisibility() == View.GONE) {
                    view.setVisibility(View.VISIBLE);
                }
                view.setBackgroundColor(bgColor);
            } else {
                viewGroup.addView(createStatusBarView(activity, bgColor));
            }
            setRootView(activity);
        }
        return false;
    }

    @Override
    public boolean setStatusBarColor(Activity activity, boolean isWhite) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;
        if (!isWhite)
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        return true;
    }

    public static View createStatusBarView(Activity activity, @ColorInt int color) {
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        statusBarView.setId(R.id.statusbarutil_fake_status_bar_view);
        return statusBarView;
    }

    public static int getStatusBarHeight(Context context) {
        int statusbar = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(statusbar);
    }

    public static void setRootView(Activity activity) {
        ViewGroup content = activity.findViewById(android.R.id.content);
        int n = 0;
        for (int count = content.getChildCount(); n < count; ++n) {
            View childView = content.getChildAt(n);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }
}
