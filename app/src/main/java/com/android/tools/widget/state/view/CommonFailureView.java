package com.android.tools.widget.state.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.tools.R;
import com.android.tools.widget.state.IFailureView;

/**
 * Created by Mouse on 2018/10/22.
 */
public class CommonFailureView extends LinearLayout implements IFailureView {

    private TextView textFailure, textRetry;

    public CommonFailureView(Context context) {
        super(context);
        initLayout();
    }

    public CommonFailureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    private void initLayout(){
        LayoutInflater.from(getContext()).inflate(R.layout.state_failure, this);
        textFailure = findViewById(R.id.view_state_text);
        textRetry = findViewById(R.id.retry);
        setBackgroundColor(Color.WHITE);
    }

    @Override
    public void setText(String text) {
        textFailure.setText(text);
    }

    @Override
    public void setOnRetryClickListener(OnClickListener onRetryClickListener) {
        textRetry.setOnClickListener(onRetryClickListener);
    }

    @Override
    public void setVisiable(boolean isShow) {
        textRetry.setVisibility(isShow?View.VISIBLE:View.GONE);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public TextView getRetryTextView() {
        return textRetry;
    }
}
