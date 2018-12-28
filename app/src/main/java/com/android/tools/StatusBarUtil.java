package com.android.tools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Created by Mouse on 2017/10/27.
 */

public class StatusBarUtil {

    private static final int SYSTEM_UI_FLAG_LIGHT_STATUS_BAR = 1 << 13;

    public static int setStatusbarTextColor(Activity var0) {
        byte var1 = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isMiUI(var0, true)) {
                var1 = 1;
            } else if (isMeizu(var0.getWindow(), true)) {
                var1 = 2;
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    var0.getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    var1 = 3;
                }
            }
        }

        return var1;
    }


    public static void setStatusBarColor(Activity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= 23 && isMiUI(activity, true)) {
                return;
            }

            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(calculateStatusColor(color, 0));
        } else if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup var2 = (ViewGroup) activity.getWindow().getDecorView();
            View var3 = var2.findViewById(R.id.statusbarutil_translucent_view);
            if (var3 != null) {
                if (var3.getVisibility() == View.GONE) {
                    var3.setVisibility(View.VISIBLE);
                }
                var3.setBackgroundColor(calculateStatusColor(color, 0));
            } else {
                var2.addView(createStatusBarView(activity, color));
            }
            setRootView(activity);
        }
    }


    private static int calculateStatusColor(@ColorInt int color, int alpha) {
        if (alpha == 0) {
            return color;
        }
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    private static void setRootView(Activity var0) {
        ViewGroup var1 = (ViewGroup) var0.findViewById(android.R.id.content);
        int var2 = 0;
        for (int var3 = var1.getChildCount(); var2 < var3; ++var2) {
            View childView = var1.getChildAt(var2);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }

    }

    private static View createStatusBarView(Activity activity, @ColorInt int var1) {
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(calculateStatusColor(var1, 0));
        statusBarView.setId(R.id.statusbarutil_fake_status_bar_view);
        return statusBarView;
    }

    private static int getStatusBarHeight(Context var0) {
        int var1 = var0.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return var0.getResources().getDimensionPixelSize(var1);
    }

    private static boolean isMiUI(Activity activity, boolean var1) {
        boolean var2 = false;
        Window var3 = activity.getWindow();
        if (var3 != null) {
            Class var4 = var3.getClass();

            try {
                boolean var5 = false;
                Class var6 = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field var7 = var6.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int var10 = var7.getInt(var6);
                Method var8 = var4.getMethod("setExtraFlags", new Class[]{Integer.TYPE, Integer.TYPE});
                if (var1) {
                    var8.invoke(var3, new Object[]{Integer.valueOf(var10), Integer.valueOf(var10)});
                } else {
                    var8.invoke(var3, new Object[]{Integer.valueOf(0), Integer.valueOf(var10)});
                }

                var2 = true;
                Window window = activity.getWindow();
                if (Build.VERSION.SDK_INT >= 23) {
                    if (var1) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        window.setStatusBarColor(Color.WHITE);
                    } else {
                        int flag = activity.getWindow().getDecorView().getSystemUiVisibility()
                                & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                        window.getDecorView().setSystemUiVisibility(flag);
                    }
                }
            } catch (Exception var9) {

            }
        }

        return var2;
    }

    private static boolean isMeizu(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                android.view.WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = android.view.WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = android.view.WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }

                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception var8) {
                ;
            }
        }

        return result;
    }

    @TargetApi(23)
    private static int changeStatusBarModeRetainFlag(Window window, int out) {
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_FULLSCREEN);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        return out;
    }

    public static int retainSystemUiFlag(Window window, int out, int type) {
        int now = window.getDecorView().getSystemUiVisibility();
        if ((now & type) == type) {
            out |= type;
        }
        return out;
    }


    private final static String FLYME = "flyme";
    private final static String ZTEC2016 = "zte c2016";
    private final static String ZUKZ1 = "zuk z1";
    private final static String MEIZUBOARD[] = {"m9", "M9", "mx", "MX"};

    private static String sMiuiVersionName;
    private static String sFlymeVersionName;

    private final static String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_FLYME_VERSION_NAME = "ro.build.display.id";

    static {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
            Properties properties = new Properties();
            properties.load(fileInputStream);
            Class<?> clzSystemProperties = Class.forName("android.os.SystemProperties");
            Method getMethod = clzSystemProperties.getDeclaredMethod("get", String.class);
            // miui
            sMiuiVersionName = getLowerCaseName(properties, getMethod, KEY_MIUI_VERSION_NAME);
            //flyme
            sFlymeVersionName = getLowerCaseName(properties, getMethod, KEY_FLYME_VERSION_NAME);

        } catch (Exception e) {

        } finally {
            close(fileInputStream);
        }
    }

    public static boolean translucent(Activity activity) {
        return translucent(activity, 0x00000000);
    }

    /**
     * 沉浸式状态栏
     * 支持 4.4 以上版本的 MIUI 和 Flyme，以及 5.0 以上版本的其他 Android
     *
     * @param activity
     */
    @TargetApi(19)
    public static boolean translucent(Activity activity, @ColorInt int colorOn5x) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            // 版本小于4.4，绝对不考虑沉浸式
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && supportTransclentStatusBar6()) {
                // android 6以后可以改状态栏字体颜色，因此可以自行设置为透明
                // ZUK Z1是个另类，自家应用可以实现字体颜色变色，但没开放接口
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
//                setStatusBarLightMode(activity);
                return true;
            } else {
                // android 5不能修改状态栏字体颜色，因此直接用FLAG_TRANSLUCENT_STATUS，nexus表现为半透明
                // 魅族和小米的表现如何？
                // update: 部分手机运用FLAG_TRANSLUCENT_STATUS时背景不是半透明而是没有背景了。。。。。
//                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                // 采取setStatusBarColor的方式，部分机型不支持，那就纯黑了，保证状态栏图标可见
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(colorOn5x);
                return true;
            }
        }

        // 小米和魅族4.4 以上版本支持沉浸式
        if (isMeizu() || isMIUI()) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            return true;
        }

        return false;
    }

    /**
     * Android6.0是否可以启用 window.setStatusBarColor(Color.TRANSPARENT);
     *
     * @return
     */
    public static boolean supportTransclentStatusBar6() {
        return !(isZUKZ1() || isZTKC2016());
    }

    /**
     * ZUK Z1,ZTK C2016: android 6.0,但不支持状态栏icon颜色改变
     *
     * @return
     */
    public static boolean isZUKZ1() {
        final String board = Build.MODEL;
        if (board == null) {
            return false;
        }
        return board.toLowerCase().contains(ZUKZ1);
    }

    public static boolean isZTKC2016() {
        final String board = Build.MODEL;
        if (board == null) {
            return false;
        }
        return board.toLowerCase().contains(ZTEC2016);
    }

    /**
     * 是否是魅族
     *
     * @return
     */
    public static boolean isMeizu() {
        return isPhone(MEIZUBOARD) || isFlyme();
    }

    /**
     * 是否是flyme系统
     *
     * @return
     */
    public static boolean isFlyme() {
        return !TextUtils.isEmpty(sFlymeVersionName) && sFlymeVersionName.contains(FLYME);
    }

    /**
     * 是否是小米
     *
     * @return
     */
    public static boolean isXiaomi() {
        return Build.BRAND.toLowerCase().contains("xiaomi");
    }

    private static boolean isPhone(String[] boards) {
        final String board = Build.BOARD;
        if (board == null) {
            return false;
        }
        final int size = boards.length;
        for (int i = 0; i < size; i++) {
            if (board.equals(boards[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是MIUI系统
     *
     * @return
     */
    public static boolean isMIUI() {
        return !TextUtils.isEmpty(sMiuiVersionName);
    }

    @Nullable
    private static String getLowerCaseName(Properties p, Method get, String key) {
        String name = p.getProperty(key);
        if (name == null) {
            try {
                name = (String) get.invoke(null, key);
            } catch (Exception ignored) {
            }
        }
        if (name != null) name = name.toLowerCase();
        return name;
    }

    public static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
