package com.android.tools.widget.softinput;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.tools.R;
import com.android.tools.widget.softinput.listener.OnKeyBoradListener;
import com.android.tools.widget.softinput.style.KeyBoardStyle;

/**
 * Created by Mouse on 2018/5/18.
 */

public class BaseKeyBoardCard extends LinearLayout {

    protected OnKeyBoradListener onWeiciKeyBoradListener;

    protected KeyBoardStyle keyBoardStyle;

    public BaseKeyBoardCard(Context context) {
        super(context);
        init();
    }

    public BaseKeyBoardCard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        initView();
    }

    protected void initView() {

    }

    public void setOnWeiciKeyBoradListener(OnKeyBoradListener onWeiciKeyBoradListener) {
        this.onWeiciKeyBoradListener = onWeiciKeyBoradListener;
    }

    public TextView getConfirmButton() {
        return null;
    }

    public void setConfirmEnable(boolean enable) {
        TextView confirmButton = getConfirmButton();
        confirmButton.setEnabled(enable);
        confirmButton.setClickable(enable);
        confirmButton.setBackgroundResource(enable ? R.drawable.bg_key_1_style2 : R.drawable.bg_key_gray);
    }

    public void setConfirmText(String text) {
        TextView confirmButton = getConfirmButton();
        confirmButton.setText(text);
    }

    public void setConfirmTextColor(){

    }

    public void setKeyBoardStyle(KeyBoardStyle keyBoardStyle) {
        this.keyBoardStyle = keyBoardStyle;
        onWeiciKeyBoradStyleChanged();
    }

    public void onWeiciKeyBoradStyleChanged(){}
}
