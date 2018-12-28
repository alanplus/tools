package com.android.tools.net;

/**
 * Created by Mouse on 2018/10/18.
 */
public interface OnProgressListener {

    void onProgress(long progress,long max);
    void onFinish(boolean isSuccess);
}
