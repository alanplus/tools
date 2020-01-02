package com.android.tools.rx.schedule;

/**
 * @author Alan
 * 时 间：2019-12-17
 * 简 述：<功能简述>
 */
public class RxCache<T> {

    private T t;

    public RxCache() {

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
}
