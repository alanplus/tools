package com.android.tools;

import android.text.TextUtils;

/**
 * Created by Mouse on 2018/10/15.
 */
public class StringTools {

    public static boolean isEnglishChar(String s) {
        if (TextUtils.isEmpty(s)) {
            return false;
        }

        if (s.length() != 1) {
            return false;
        }
        char c = s.charAt(0);
        return isEnglishChar(c);
    }

    /**
     *
     * 判断是否是英文26个字母
     * ASCII码
     * A-Z 65-90 91 [ 92 \ 93 ] 94^ 95_96` a-z 97-122
     * 33-47 标点符号 0-9 48-64
     *
     * @param s
     * @return
     */
    public static boolean isEnglishChar(char s) {
        if ((s >= 65 && s <= 90) || (s >= 97 && s <= 122)) {
            return true;
        }
        return false;
    }

    public static boolean isLetterOrChinese(String str) {
        String regex = "^[a-zA-Z\u4e00-\u9fa5]{2,10}";
        return str.matches(regex);
    }

    public static boolean isLetterDigitOrChinese(String str) {
        String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]{4,40}";
        return str.matches(regex);
    }

    public static int stringToInt(String num) {
        try {
            return Integer.valueOf(num);
        } catch (Exception e) {
            return 0;
        }
    }

    public static float stringToFloat(String num) {
        try {
            return Float.valueOf(num);
        } catch (Exception e) {
            return 0;
        }
    }


    public static boolean equals(String a, String b) {
        if (TextUtils.isEmpty(a) && TextUtils.isEmpty(b)) {
            return true;
        } else if (TextUtils.isEmpty(a) && !TextUtils.isEmpty(b)) {
            return false;
        } else {
            return a.equals(b);
        }
    }
}
