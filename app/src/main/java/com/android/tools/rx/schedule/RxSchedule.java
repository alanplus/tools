package com.android.tools.rx.schedule;

import com.android.tools.rx.RxSchedulers;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Mouse on 2019/4/16.
 */
public class RxSchedule {

    public static <T> Disposable schedule(final RxScheduleRunnable<T> scheduleRunnable) {

        return Observable.create(new ObservableOnSubscribe<RxCache<T>>() {
            @Override
            public void subscribe(ObservableEmitter<RxCache<T>> emitter) {
                try {
                    T t1 = scheduleRunnable.scheduleOnThread();
                    emitter.onNext(new RxCache<>(t1));
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }).compose(RxSchedulers.threadMain()).doOnNext(new Consumer<RxCache<T>>() {
            @Override
            public void accept(RxCache<T> tRxCache) {
                try {
                    scheduleRunnable.scheduleOnMainThread(tRxCache.get());
                } catch (Exception e) {
                    scheduleRunnable.onError(e);
                }


            }
        }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                try {
                    scheduleRunnable.onCompleteOnMainThread();
                } catch (Exception e) {
                    scheduleRunnable.onError(e);
                }
            }
        }).onErrorReturn(new Function<Throwable, RxCache<T>>() {
            @Override
            public RxCache<T> apply(Throwable throwable) throws Exception {
                T t = scheduleRunnable.onError(throwable);
                RxCache<T> tRxCache = new RxCache<>(t);
                tRxCache.setThrowable(throwable);
                return tRxCache;
            }
        }).subscribe();
    }
}
