package com.android.tools.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.android.tools.rx.RxSchedulers;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Mouse on 2018/10/29.
 */
public class YxTextSwitcher extends TextSwitcher {

    private long interval;

    private int times;
    private String[] strings;

    private int position;
    private Disposable subscribe;

    public YxTextSwitcher(Context context) {
        this(context, null);
    }

    public YxTextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        interval = 5000;
        this.times = -1;
        position = 0;
        setFactory(() -> {
            final TextView tv = new TextView(getContext());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
            tv.setTextColor(Color.parseColor("#ff989898"));
            tv.setGravity(Gravity.CENTER);
            return tv;
        });
    }

    public void setInterval(long interval) {
        this.interval = interval;

    }

    public void setTextArray(String[] strings) {
        this.strings = strings;
    }

    public void start() {
        if (null == strings || strings.length == 0) return;
        subscribe = Observable.interval(0, interval, TimeUnit.MILLISECONDS).takeUntil(aLong -> false).compose(RxSchedulers.ioMain()).subscribe(aLong -> setText(strings[++position % strings.length]));
    }

    public void destroy() {
        if (null != subscribe) subscribe.dispose();
    }
}
