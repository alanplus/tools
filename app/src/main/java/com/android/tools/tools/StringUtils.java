package com.android.tools.tools;

import android.text.TextUtils;

import java.security.MessageDigest;

/**
 * Created by Mouse on 2019/1/30.
 */
public class StringUtils {

    private static final char[] DIGITS_LOWER = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};

    public static String buildKey(String paramString1, String paramString2) {
        return concatString(paramString1, "://", paramString2);
    }

    public static String bytesToHexString(byte[] paramArrayOfByte) {
        if (paramArrayOfByte == null) {
            return "";
        }
        return bytesToHexString(paramArrayOfByte, DIGITS_LOWER);
    }

    private static String bytesToHexString(byte[] paramArrayOfByte, char[] paramArrayOfChar) {
        int j = 0;
        int k = paramArrayOfByte.length;
        char[] arrayOfChar = new char[k << 1];
        int i = 0;
        while (i < k) {
            int m = j + 1;
            arrayOfChar[j] = paramArrayOfChar[((paramArrayOfByte[i] & 0xF0) >>> 4)];
            j = m + 1;
            arrayOfChar[m] = paramArrayOfChar[(paramArrayOfByte[i] & 0xF)];
            i += 1;
        }
        return new String(arrayOfChar);
    }

    public static String concatString(String paramString1, String paramString2) {
        return paramString1.length() + paramString2.length() + paramString1 + paramString2;
    }

    public static String concatString(String paramString1, String paramString2, String paramString3) {
        return paramString1.length() + paramString2.length() + paramString3.length() + paramString1 + paramString2 + paramString3;
    }

    public static String longToIP(long paramLong) {
        StringBuilder localStringBuilder = new StringBuilder(16);
        long l2 = 1000000000L;
        long l1 = paramLong;
        paramLong = l2;
        do {
            localStringBuilder.append(l1 / paramLong);
            localStringBuilder.append('.');
            l1 %= paramLong;
            l2 = paramLong / 1000L;
            paramLong = l2;
        } while (l2 > 0L);
        localStringBuilder.setLength(localStringBuilder.length() - 1);
        return localStringBuilder.toString();
    }

    public static String md5ToHex(String paramString) {
        if (paramString == null) {
            return null;
        }
        try {
            paramString = bytesToHexString(MessageDigest.getInstance("MD5").digest(paramString.getBytes("utf-8")));
            return paramString;
        } catch (Exception ignore) {
        }
        return null;
    }


    public static String simplifyString(String paramString, int paramInt) {
        if (paramString.length() <= paramInt) {
            return paramString;
        }
        return concatString(paramString.substring(0, paramInt), "......");
    }

    public static String stringNull2Empty(String paramString) {
        String str = paramString;
        if (paramString == null) {
            str = "";
        }
        return str;
    }
}
