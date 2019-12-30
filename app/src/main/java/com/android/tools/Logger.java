package com.android.tools;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.android.tools.util.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Mouse on 2018/10/15.
 */
public class Logger {


    public static void writeFile(String text) {
        if (!AndroidToolsConfig.androidToolsConfig.isDebug()) return;
        String path = Environment.getExternalStorageDirectory().getPath() + "/a.txt";
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(text);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void append(String text) {
        if (!AndroidToolsConfig.androidToolsConfig.isDebug() || TextUtils.isEmpty(text)) return;
        String path = Environment.getExternalStorageDirectory().getPath() + "/a.txt";
        File file = new File(path);


        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.append(text, 0, text.length());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String text, String path, String name) {
        if (!AndroidToolsConfig.androidToolsConfig.isDebug()) return;

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File file = new File(FileUtils.join(path, name));
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(text);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String TAG = "weiciLog";

    public static void i(String msg) {
        println(Log.INFO, TAG, msg);
    }

    public static void e(String msg) {
        println(Log.ERROR, TAG, msg);
    }

    public static void e(String tag, String msg) {
        println(Log.ERROR, tag, msg);
    }

    public static void e(String tag, String msg, Throwable e) {
        println(tag, msg, e);
    }

    public static void w(String msg) {
        println(Log.WARN, TAG, msg);
    }

    public static void w(String tag, String msg) {
        println(Log.WARN, tag, msg);
    }

    public static void i(String tag, String msg) {
        println(Log.INFO, tag, msg);
    }

    public static void v(String tag, String s) {
        println(Log.VERBOSE, tag, s);
    }

    public static void d(String tag, String s) {
        println(Log.DEBUG, tag, s);
    }

    public static void d(String s) {
        println(Log.DEBUG, TAG, s);
    }

    private static void println(int priority, String tag, String msg) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg) || !AndroidToolsConfig.androidToolsConfig.isDebug())
            return;

        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        String info = "";
        if (trace != null && trace.length >= 4) {
            try {
                StackTraceElement element = trace[4];
                info = "(" + element.getFileName() + ":" + element.getLineNumber() + ")";
            } catch (NoClassDefFoundError e1) {
            }
        }
//        Log.println(priority, tag, info + msg);
        printlnLog(priority, info, tag, msg);
    }

    private static void println(String tag, String msg, Throwable e) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg) || !AndroidToolsConfig.androidToolsConfig.isDebug())
            return;

        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        String info = "";
        if (trace != null && trace.length >= 4) {
            try {
                StackTraceElement element = trace[4];
                info = "(" + element.getFileName() + ":" + element.getLineNumber() + ")";
            } catch (NoClassDefFoundError e1) {
            }
        }
        Log.e(tag, info + msg, e);
    }

    private static void printlnLog(int priority, String info, String tag, String msg) {
        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize) {// 长度小于等于限制直接打印
            Log.println(priority, tag, info + msg);
        } else {
            int segmentIndex = 1;
            while (msg.length() > segmentSize) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize);
                msg = msg.substring(segmentSize, msg.length());
                Log.println(priority, tag, info + "segmentIndex:" + segmentIndex++ + ":" + logContent);
            }
            if (!TextUtils.isEmpty(msg))
                Log.println(priority, tag, info + "segmentIndex:" + segmentIndex + ":" + msg);// 打印剩余日志
        }
    }

    public static void error(Throwable e) {
        Logger.e(Log.getStackTraceString(e));
    }
}
