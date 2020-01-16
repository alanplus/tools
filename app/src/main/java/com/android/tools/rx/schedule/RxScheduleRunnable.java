package com.android.tools.rx.schedule;

/**
 * Created by Mouse on 2019/4/16.
 */
public interface RxScheduleRunnable<T> {

    T scheduleOnThread();

    void scheduleOnMainThread(T t);

    void onCompleteOnMainThread();

    T onError(Throwable throwable);
}
