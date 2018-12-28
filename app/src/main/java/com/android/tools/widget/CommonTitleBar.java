package com.android.tools.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.tools.R;
import com.android.tools.buttondrawable.DrawableFactory;

/**
 * Created by Mouse on 2018/6/18.
 */

public class CommonTitleBar extends RelativeLayout {

    private TextView title, rightText;
    private ImageButton imageButtonBack;

    private boolean isLight;
    private String titleStr;

    public CommonTitleBar(Context context) {
        this(context, null);
    }

    public CommonTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attributeSet) {
        isLight = false;
        if (null != attributeSet) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.CommontTitleBarView);
            titleStr = typedArray.getString(R.styleable.CommontTitleBarView_bar_title);
            isLight = typedArray.getBoolean(R.styleable.CommontTitleBarView_is_light, false);
            typedArray.recycle();
        }
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_common_titlebar, this);
        title = findViewById(R.id.main_tilte);
        title.setTextColor(isLight ? Color.parseColor("#535353") : Color.WHITE);
        title.setText(titleStr);
        imageButtonBack = findViewById(R.id.icon_back);
        imageButtonBack.setImageResource(isLight ? R.drawable.icon_titlebar_back : R.drawable.icon_titlebar_back_light);
        rightText = findViewById(R.id.right_text);
        rightText.setTextColor(isLight ? Color.parseColor("#535353") : Color.WHITE);
        imageButtonBack.setOnClickListener(v -> {
            if (getContext() instanceof Activity) {
                ((Activity) getContext()).finish();
            }
        });
    }

    public void setRightIcon1(int res, OnClickListener onClickListener) {
        findViewById(R.id.icon1).setBackgroundResource(res);
        findViewById(R.id.view1).setOnClickListener(onClickListener);
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    public void setTitle(String text, OnClickListener onClickListener) {
        setTitle(text);
        title.setOnClickListener(onClickListener);
    }

    public void addRightTextButton(String text, final OnClickListener onClickListener) {
        rightText.setVisibility(VISIBLE);
        rightText.setText(text);
        findViewById(R.id.view1).setVisibility(View.GONE);
        int color = isLight ? Color.parseColor("#535353") : Color.WHITE;
        int color1 = Color.parseColor("#31bF00");
        ColorStateList colorStateList = DrawableFactory.buildClickTextColor(color, color1);
        rightText.setTextColor(colorStateList);
        if (null != onClickListener) {
            rightText.setOnClickListener(onClickListener);
        }
    }

    public void addEnableRightTextButton(String text, final OnClickListener onClickListener) {
        rightText.setVisibility(VISIBLE);
        rightText.setText(text);
        findViewById(R.id.view1).setVisibility(View.GONE);
        int color = Color.parseColor("#e0e0e0");
        int color1 = Color.parseColor("#31bF00");
        DrawableFactory.buildEnableTextColor(color, color1);
        if (null != onClickListener) {
            rightText.setOnClickListener(onClickListener);
        }
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getRightText() {
        return rightText;
    }

    public ImageButton getImageButtonBack() {
        return imageButtonBack;
    }
}
