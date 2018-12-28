package com.android.tools.widget.inputlayout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.android.tools.Logger;

/**
 * Created by Mouse on 2018/10/25.
 */
public class InputMethodStateRelativeLayout extends RelativeLayout {

    private int initBottom = -1;
    private int initLeft = -1;

    private OnInputMethodChangedListener onInputMethodChangedListener;

    public InputMethodStateRelativeLayout(Context context) {
        super(context);
    }

    public InputMethodStateRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (initBottom == -1 || initLeft == -1) {
            initBottom = b;
            initLeft = l;
            return;
        }
        if (changed) {
            int height = b - initBottom; //高度变化值（弹出输入法，布局变小，则为负值）
//            Logger.d("height:" + height);
            if (null != onInputMethodChangedListener) {
                if (height < 0) {
                    onInputMethodChangedListener.onShowInputMethodListener();
                } else {
                    onInputMethodChangedListener.onHiddenInputMethodListener();
                }
            }
        }
    }

    public void setOnInputMethodChangedListener(OnInputMethodChangedListener
                                                        onInputMethodChangedListener) {
        this.onInputMethodChangedListener = onInputMethodChangedListener;
    }

    public interface OnInputMethodChangedListener {
        void onShowInputMethodListener();
        void onHiddenInputMethodListener();
    }
}
