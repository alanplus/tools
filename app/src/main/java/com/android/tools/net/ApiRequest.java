package com.android.tools.net;

import com.android.tools.AndroidTools;
import com.android.tools.AndroidToolsConfig;
import com.android.tools.rx.RxSchedulers;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

public class ApiRequest {

    public static final int ERROR_CODE_NO_NET = -2;

    public static void execute(final ApiBody apiBody, final OnCallBackListener listener) {
        final ApiResult result = new ApiResult(-1);
        if (!AndroidTools.isNetworkAvailable(AndroidToolsConfig.androidToolsConfig.context)) {
            result.code = ERROR_CODE_NO_NET;//无网络错误码
            if (listener != null) listener.callBack(result);
            return;
        }
        Observable.create((ObservableOnSubscribe<ApiResult>) e -> {
            doApiBody(apiBody, result);
            e.onNext(result);
        }).compose(RxSchedulers.threadMain()).doOnNext(integer -> {
            if (listener != null) listener.callBack(result);
        }).subscribe();
    }

    /**
     * @param apiBody
     * @param path               目标路径+目标名称
     * @param onProgressCallback
     */
    public static void downloadFile(ApiBody apiBody, String path, OnProgressListener onProgressCallback) {
        Observable.create((ObservableOnSubscribe<Long[]>) e -> {
            String downloadTempPath = AndroidToolsConfig.androidToolsConfig.getDownloadTempPath();
            boolean b = OkHttpUtil.downloadFile(apiBody.URL, downloadTempPath, path, (progress, total) -> {
                if (null != onProgressCallback) e.onNext(new Long[]{progress, total});
            });
            if (b) e.onComplete();
            else e.onError(new Exception());
        }).compose(RxSchedulers.threadMain()).doOnNext(integer -> onProgressCallback.onProgress(integer[0], integer[1])).doOnComplete(() -> {
            if (null != onProgressCallback) onProgressCallback.onFinish(true);
        }).onErrorReturn(throwable -> {
            if (null != onProgressCallback) onProgressCallback.onFinish(false);
            return new Long[0];
        }).subscribe();
    }


    private static void doApiBody(ApiBody apiBody, ApiResult result) {
        String url = apiBody.URL;
        String s = null;
        if (apiBody.type == OkHttpUtil.TYPE.GET) {
            s = OkHttpUtil.getStrByGet(url, apiBody.params);
        } else if (apiBody.type == OkHttpUtil.TYPE.POST) {
            s = OkHttpUtil.getStrByPost(url, apiBody.params, apiBody.Encoder);
        } else if (apiBody.type == OkHttpUtil.TYPE.IMG) {
            result.object = OkHttpUtil.getBitmapByGet(url, apiBody.params);
        } else if (apiBody.type == OkHttpUtil.TYPE.UPDATE_FILE) {
            s = OkHttpUtil.uploadFileByPost(url, apiBody.file, apiBody.params);
        }
        apiBody.parse(s, result);
    }

    public static ApiResult executeOnGet(ApiBody apiBody) {
        ApiResult result = new ApiResult(-1);
        String url = apiBody.URL;
        String s = OkHttpUtil.getStrByGet(url, apiBody.params);
        apiBody.parse(s, result);
        return result;
    }

    public static ApiResult executeOnPost(ApiBody apiBody) {
        return executeOnPost(apiBody, true);
    }

    public static ApiResult executeOnPost(ApiBody apiBody, boolean isencode) {
        ApiResult result = new ApiResult(-1);
        String url = apiBody.URL;
        String s = OkHttpUtil.getStrByPost(url, apiBody.params, isencode);
        apiBody.parse(s, result);
        return result;
    }


    public interface OnCallBackListener {
        void callBack(ApiResult result);
    }


    public static void cancel(String url) {
        OkHttpUtil.cancel(url);
    }
}
