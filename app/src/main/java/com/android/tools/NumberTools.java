package com.android.tools;

/**
 * Created by Mouse on 2018/11/5.
 */
public class NumberTools {

    public static float divide(int a, int b) {
        if (b == 0) {
            return 0;
        }
        return Float.valueOf(a) / Float.valueOf(b);
    }

}
