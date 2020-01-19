package com.android.tools.media;

/**
 * @author Alan
 * 时 间：2020-01-19
 * 简 述：<功能简述>
 */
public interface OnMediaProgressListener {

    void onMediaProgressListener(int current, int total);
    void onComplete();
}
