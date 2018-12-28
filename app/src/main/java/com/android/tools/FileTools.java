package com.android.tools;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mouse on 2018/10/18.
 */
public class FileTools {

    public static String getFilename(String path) {
        if (TextUtils.isEmpty(path)) return "";
        int index = path.lastIndexOf("/");
        return path.substring(index + 1);
    }

    public static String getFileExt(String path) {
        if (path == null)
            return null;
        int index = path.lastIndexOf(".");
        if (index >= 0) {
            return path.substring(index + 1, path.length()).toLowerCase();
        }
        return null;
    }

    public static String getFilenameByKeyHashCode(String url) {
        int firstHalfLength = url.length() / 2;
        String localFilename = String.valueOf(url.substring(0, firstHalfLength).hashCode());
        localFilename += String.valueOf(url.substring(firstHalfLength).hashCode());
        return localFilename;
    }

    public static List<String> readStringList(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        List<String> list = new ArrayList<>();
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
