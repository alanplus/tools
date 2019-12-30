package com.android.tools.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.android.tools.Logger;
import com.android.tools.util.FileUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {

    public enum TYPE {GET, POST, IMG, UPDATE_FILE}

    private static final OkHttpClient mOkHttpClient = new OkHttpClient();

    private static final int NETWORK_TIMEOUT = 600;
    private static final String CHARSET_NAME = "UTF-8";
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    public static final MediaType MEDIA_TYPE_APPLICATION = MediaType.parse("application/x-www-form-urlencoded");
    public static final MediaType MEDIA_TYPE_APPLICATION_FILE = MediaType.parse("application/octet-stream");

    static {
        mOkHttpClient.setConnectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * 不会开启异步线程。
     *
     * @param request
     * @return
     * @throws IOException
     */
    private static Response execute(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 开启异步线程访问网络
     *
     * @param request
     * @param responseCallback
     */
    private static void enqueue(Request request, Callback responseCallback) {
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }

    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     *
     * @param request
     */
    public static void enqueue(Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });
    }

    /**
     * 同步 get 方法
     *
     * @param url
     * @param map
     * @return
     * @throws IOException
     */
    public static String getStrByGet(String url, Map<String, String> map) {
        try {
            Response response = getResponseByGet(url, map);
            if (response.isSuccessful()) {
                String responseUrl = response.body().string();
                Logger.d("data:" + responseUrl);
                return responseUrl;
            }
        } catch (Exception e) {
        }
        return null;
    }

    private static Response getResponseByGet(String url, Map<String, String> map) throws IOException {
        String param = getContentFromMap(map);
        url += "?" + param;
        Logger.d(url);
        Request request = new Request.Builder().tag(url).url(url).build();
        return execute(request);
    }

    public static Response getResponseByPost(String url, HashMap<String, String> body) throws IOException {
        Request request = new Request.Builder().url(url).post(getPostBuilder(body)).build();
        Logger.d(url + "?" + getContentFromMap(body));
        return mOkHttpClient.newCall(request).execute();
    }

    private static RequestBody getPostBuilder(HashMap<String, String> params) {
        FormEncodingBuilder fe = new FormEncodingBuilder();
        if (null == params)
            return fe.build();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            String value = params.get(key);
            if (value != null) {
                fe.add(key, value);
            }
        }
        return fe.build();
    }

    /**
     * 表单提交
     */
    public static Response getResponseByListPost(String url, String body) throws IOException {
        Request request = new Request.Builder().url(url).post(RequestBody.create(MEDIA_TYPE_APPLICATION, body)).build();
        return mOkHttpClient.newCall(request).execute();
    }

    public static Response getResponseByPost(String url, String body) throws IOException {
        Request request = new Request.Builder().url(url).post(RequestBody.create(MEDIA_TYPE_APPLICATION, body)).build();
        return mOkHttpClient.newCall(request).execute();
    }

    public static String getStrByPost(String url, String body) {
        try {
            Response response = getResponseByPost(url, body);
            String string = response.body().string();
            Logger.d("data:" + string);
            return string;
        } catch (Exception e) {
        }
        return null;
    }


    /**
     * 同步post 方法
     *
     * @param url
     * @param map
     * @return
     */
    public static String getStrByPost(String url, HashMap<String, String> map) {
        return getStrByPost(url, map, true);
    }

    /**
     * 同步 post 方法
     *
     * @param url
     * @param map
     * @param encoder
     * @return
     */
    public static String getStrByPost(String url, HashMap<String, String> map, boolean encoder) {
        try {
            Logger.d("url:" + url);
            Response response = getResponseByPost(url, getContentFromMap(map, encoder));
            String string = response.body().string();
            Logger.d("result:" + string);
            return string;
        } catch (Exception e) {
        }
        return null;
    }


    public static String getContentFromMap(Map<String, String> params) {
        try {
            return getContentFromMap(params, true);
        } catch (UnsupportedEncodingException ignored) {
        }
        return "";
    }

    public static String getContentFromMap(Map<String, String> params, boolean encoder) throws UnsupportedEncodingException {
        if (null == params)
            return "";
        StringBuilder sb = new StringBuilder();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            String value = params.get(key);
            if (value != null) {
                sb.append(key).append('=').append(encoder ? URLEncoder.encode(value, CHARSET_NAME) : value).append('&');
            }
        }
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);
        Logger.d("data:" + sb.toString());
        return sb.toString();
    }

    public static String uploadFileByPost(String url, File file, HashMap<String, String> map) {
        mOkHttpClient.setConnectTimeout(5 * NETWORK_TIMEOUT, TimeUnit.SECONDS);
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
        builder.addFormDataPart("user_code", map.get("user_code"));
        builder.addFormDataPart("session", map.get("session"));
        builder.addFormDataPart("app_id", map.get("app_id"));
        String fileName = map.get("file_name");
        if (TextUtils.isEmpty(fileName)) fileName = "";
        builder.addFormDataPart("file_name", fileName);
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url)// 地址
                .post(requestBody)// 添加请求体
                .build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (null != response) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String uploadAudioFileByPost(String url, File file, HashMap<String, String> map) {
        mOkHttpClient.setConnectTimeout(5 * NETWORK_TIMEOUT, TimeUnit.SECONDS);
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("audio/mpeg"), file));
        builder.addFormDataPart("user_code", map.get("user_code"));
        builder.addFormDataPart("session", map.get("session"));
        builder.addFormDataPart("app_id", "8");
        builder.addFormDataPart("file_name", "");
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url)// 地址
                .post(requestBody)// 添加请求体
                .build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (null != response) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String uploadFileByPost(String url, File file) {

        String content = "";
        mOkHttpClient.setConnectTimeout(5 * NETWORK_TIMEOUT, TimeUnit.SECONDS);
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        builder.addFormDataPart("name", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
        builder.addPart(RequestBody.create(MEDIA_TYPE_APPLICATION, content));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url)// 地址
                .post(requestBody)// 添加请求体
                .build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (null != response) {
                String string = response.body().string();
                return string;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 同步 get 方法
     *
     * @param url
     * @param map
     * @return
     * @throws IOException
     */
    public static Bitmap getBitmapByGet(String url, Map<String, String> map) {
        try {
            Response response = getResponseByGet(url, map);
            if (response.isSuccessful()) {
                InputStream in = response.body().byteStream();
                return BitmapFactory.decodeStream(in);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static boolean downloadFile(String url, String name, String tempname, String path) {
//        Log.i("studyLog", "url:" + url);
        clearFile(path, tempname);
        File file = createFile(path, tempname);
        try {
            Request request = new Request.Builder().url(url).build();
            FileOutputStream out = new FileOutputStream(file);
            Response response = mOkHttpClient.newCall(request).execute();
            if (response == null || !response.isSuccessful()) {
                return false;
            }
            /*int code = response.code();//code不等于200时会抛出FailedException异常，故注释，返回boolean在数据更新时使用
            if(code!=200){
                throw new NullPointerException("错误码："+code);
            }*/
            InputStream in = response.body().byteStream();
            byte[] buf = new byte[2048 * 40];
            int len = 0;
            long l = 0;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
                l += len;
                Log.i("weiciLog", l + "");
            }
            out.flush();
            out.close();
            clearFile(path, name);
            return file.renameTo(new File(path, name));
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean downloadFile(Context context, String url, String name) {
        File file = createDefaultFile(context);
        try {
            Request request = new Request.Builder().url(url).build();
            FileOutputStream out = new FileOutputStream(file);
            Response response = mOkHttpClient.newCall(request).execute();
            if (response == null || !response.isSuccessful()) {
                return false;
            }
            InputStream in = response.body().byteStream();
            byte[] buf = new byte[2048 * 40];
            int len = 0;
            long l = 0;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
                l += len;
                Log.i("weiciLog", l + "");
            }
            out.flush();
            out.close();
            File file1 = new File(name);
            return file1.exists() || file.renameTo(new File(name));
        } catch (Exception ignore) {
        }
        return false;
    }


    public static boolean downloadFile(String url, String name, String tempname, String path, OnProgressCallback callback) {
        clearFile(path, tempname);
        File file = createFile(path, tempname);
        try {
            Request request = new Request.Builder().url(url).build();
            FileOutputStream out = new FileOutputStream(file);
            Response response = mOkHttpClient.newCall(request).execute();
            if (response == null || !response.isSuccessful()) {
                return false;
            }
            /*int code = response.code();//code不等于200时会抛出FailedException异常，故注释，返回boolean在数据更新时使用
            if(code!=200){
                throw new NullPointerException("错误码："+code);
            }*/
            InputStream in = response.body().byteStream();
            byte[] buf = new byte[2048];
            int len = 0;
            long current = 0;
            long total = response.body().contentLength();
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
                current += len;
                if (null != callback) {
                    callback.onProgressCallback(current, total);
                }
            }
            out.flush();
            out.close();
            clearFile(path, name);
            return file.renameTo(new File(path, name));
        } catch (Exception e) {
//            Log.d("test_error","message:"+e.getMessage());
        }
        return false;
    }

    private static void clearFile(String path, String filename) {
        File file = new File(path, filename);
        if (file.exists()) {
            file.delete();
        }
    }

    private static File createFile(String path, String filename) {

        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        File file = new File(path, filename);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException ignored) {
        }
        return file;
    }

    private static File createDefaultFile(Context context) {
        String path = "/data/data/" + context.getPackageName() + "/temp/";

        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        File file = new File(path, System.currentTimeMillis() + "download");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException ignored) {
        }
        return file;
    }

    @SuppressWarnings("resource")
    public static boolean downloadFile(String url, String src, String dest, OnProgressCallback callback) {
        String path = src;
        String filename = FileUtils.getFilename(url);
        String tempName = filename + ".download";
        clearFile(path, filename);
        clearFile(path, tempName);
        File file = createFile(path, tempName);
        Request request = new Request.Builder().url(url).build();
        try {
            FileOutputStream out = new FileOutputStream(file);
            Response response = mOkHttpClient.newCall(request).execute();
            InputStream in = response.body().byteStream();
            byte[] buf = new byte[2048];
            int len = 0;
            long current = 0;
            long total = response.body().contentLength();
            while ((len = in.read(buf)) != -1) {
                current += len;
                out.write(buf, 0, len);
                if (null != callback) {
                    callback.onProgressCallback(current, total);
                }
            }
            out.flush();
            callback.onProgressCallback(total, total);
            File file1 = new File(dest);
            if (file1.exists()) {
                file1.delete();
            }
            file.renameTo(file1);
            return true;
        } catch (IOException e) {

        }
        return false;
    }

    public static OkHttpClient newOkHttpClient() {
        OkHttpClient client = new OkHttpClient();
        client.setWriteTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setConnectTimeout(1, TimeUnit.HOURS);
        return client;
    }

    public static void cancel(String url) {
        mOkHttpClient.cancel(url);
    }

}
