package com.android.tools.rx.schedule;

/**
 * @author Alan
 * 时 间：2020-01-02
 * 简 述：<功能简述>
 */
public abstract class SimpleSchedulerRunnable<T> implements RxScheduleRunnable<T> {

    @Override
    public void onCompleteOnMainThread() {

    }

    @Override
    public T onError(Throwable throwable) {
        return null;
    }
}
