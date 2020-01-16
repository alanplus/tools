package com.android.tools.rx.schedule;

/**
 * @author Alan
 * 时 间：2019-12-17
 * 简 述：<功能简述>
 */
public class RxCache<T> {

    private T t;

    private Throwable throwable;

    public RxCache() {

    }

    public RxCache(Throwable throwable) {
        this.throwable = throwable;
    }

    public RxCache(T t) {
        this.t = t;
    }

    public void set(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }

    public boolean isNull() {
        return t == null;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
