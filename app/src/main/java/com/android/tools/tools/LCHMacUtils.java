package com.android.tools.tools;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Mouse on 2019/1/30.
 */
public class LCHMacUtils {


    private static final String TAG = "awcn.HMacUtil";

    private static byte[] hmacSha1(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        SecretKeySpec hmacSHA256 = new SecretKeySpec(paramArrayOfByte1, "HmacSHA256");
        try {
            Mac localMac = Mac.getInstance("HmacSHA256");
            localMac.init(hmacSHA256);
            paramArrayOfByte1 = localMac.doFinal(paramArrayOfByte2);
            return paramArrayOfByte1;
        } catch (InvalidKeyException ignore) {
            return null;
        } catch (NoSuchAlgorithmException ignore) {
        }
        return null;
    }

    public static String hmacSha1Hex(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        try {
            String s = StringUtils.bytesToHexString(hmacSha1(paramArrayOfByte1, paramArrayOfByte2));
            return s;
        } catch (Throwable ignore) {
        }
        return null;
    }


    private static final byte[] a = { 10, 1, 11, 5, 4, 15, 7, 9, 23, 3, 1, 6, 8, 12, 13, 91 };
    private static final byte[] b = { -117, -89, 54, 10, -43, 97, -75, -102, 88, -112, 88, -112, 44, -58, 68, 98 };

//    public static String a(String paramString)
//    {
//        long l = System.currentTimeMillis();
//        try
//        {
//            Security.addProvider(new a());
//            SecretKeySpec localSecretKeySpec = new SecretKeySpec(b, "AES");
//            Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
//            localCipher.init(2, localSecretKeySpec, new IvParameterSpec(a));
//            paramString = new c().a(paramString);
//            try
//            {
//                paramString = localCipher.doFinal(paramString);
//                paramString = new String(paramString);
//                return paramString;
//            }
//            catch (Exception ignore)
//            {
//                return null;
//            }
//            return null;
//        }
//        catch (Exception paramString)
//        {
//            paramString.printStackTrace();
//        }
//    }
}
