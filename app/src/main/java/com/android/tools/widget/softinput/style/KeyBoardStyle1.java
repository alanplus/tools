package com.android.tools.widget.softinput.style;

import android.graphics.Color;

import com.android.tools.R;


/**
 * Created by Mouse on 2018/6/7.
 * 主要用于 作业、闯关、计划等拼写题型
 */

public class KeyBoardStyle1 implements KeyBoardStyle {
    @Override
    public int getBackgroundRes() {
        return Color.parseColor("#ececec");
    }

    @Override
    public int getItemBackground() {
        return R.drawable.bg_key_border;
    }

    @Override
    public int getItemTextColor() {
        return Color.parseColor("#535353");
    }

    @Override
    public int getItemShiftIcon() {
        return R.drawable.icon_key_shift2;
    }

    @Override
    public int getItemBackIcon() {
        return R.drawable.icon_key_back2;
    }

    @Override
    public int getItemConfirmTextColor() {
        return Color.WHITE;
    }
}
