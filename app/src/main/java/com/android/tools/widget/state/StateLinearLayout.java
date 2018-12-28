package com.android.tools.widget.state;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.tools.widget.state.view.CommonFailureView;
import com.android.tools.widget.state.view.CommonLoadingView;


/**
 * Created by Mouse on 2018/9/20.
 */

public class StateLinearLayout extends LinearLayout implements IStateViewListener {


    protected ILoadingView iLoadingView;
    protected IFailureView iFailureView;

    public StateLinearLayout(Context context) {
        this(context, null);
    }

    public StateLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void showLoadingState() {
        reset();
        addView(generateLoadingView(), generateLoadingLayoutParams());
        iLoadingView.start();
    }

    @Override
    public void showFailureState(String text, boolean isRetry) {
        reset();
        addView(generateFailureView(), 0, generateLoadingLayoutParams());
        iFailureView.setText(text);
        iFailureView.setVisiable(isRetry);
    }

    @Override
    public void showSuccessState() {
        reset();
    }

    @Override
    public TextView getRetryView() {
        return iFailureView.getRetryTextView();
    }

    protected LayoutParams generateLoadingLayoutParams() {
        LayoutParams lp = new LayoutParams(-1, -1);
        lp.gravity = Gravity.CENTER;
        return lp;
    }

    protected View generateLoadingView() {
        if (null == iLoadingView) {
            iLoadingView = new CommonLoadingView(getContext());
        }
        return (View) iLoadingView;
    }

    protected View generateFailureView() {
        if (null == iFailureView) {
            iFailureView = new CommonFailureView(getContext());
        }
        return iFailureView.getView();
    }


    public void reset() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if ((null != iLoadingView && iLoadingView.getView() == v) || (null != iFailureView && iFailureView.getView() == v)) {
                removeView(v);
            }
        }
    }

}
