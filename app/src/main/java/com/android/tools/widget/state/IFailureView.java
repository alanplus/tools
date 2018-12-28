package com.android.tools.widget.state;

import android.view.View;
import android.widget.TextView;

/**
 * Created by Mouse on 2018/10/22.
 */
public interface IFailureView {

    void setText(String text);
    void setOnRetryClickListener(View.OnClickListener onRetryClickListener);
    void setVisiable(boolean isShow);
    View getView();
    TextView getRetryTextView();
}
