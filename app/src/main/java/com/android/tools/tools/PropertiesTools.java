package com.android.tools.tools;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Mouse on 2018/11/15.
 */
public class PropertiesTools {

    public static Properties getProperties(Context context, String url) {
        InputStream in = null;
        try {
            AssetManager assets = context.getResources().getAssets();
            Properties properties = new Properties();
            in = assets.open(url);
            properties.load(in);
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String getValue(Context context, String url, String key) {
        Properties properties = getProperties(context, url);
        return null == properties ? "" : properties.getProperty(key, "");
    }

}
