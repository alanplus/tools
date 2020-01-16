package com.android.tools.widget.fix;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

import com.alan.common.AndroidTools;
import com.alan.common.utils.PaintUtils;

/**
 * Created by Mouse on 2018/11/27.
 * test uesd
 */
public class SentenceTestTextView extends android.support.v7.widget.AppCompatTextView implements IFixChildView {

    public int PADDING_LEFT;
    public int PADDING_TOP;
    public int PADDING_RIGHT;
    public int PADDING_BOTTOM;

    public SentenceTestTextView(Context context) {
        this(context, null);
    }

    public SentenceTestTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setGravity(Gravity.CENTER);
        setTextColor(Color.WHITE);
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        int i = AndroidTools.dip2px(8);
        setSelected(false);
        PADDING_LEFT = i;
        PADDING_BOTTOM = i;
        PADDING_TOP = i;
        PADDING_RIGHT = i;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] measure = measure();
        setMeasuredDimension(measure[0], measure[1]);
    }

    @Override
    public int[] measure() {
        String text = getText().toString();
        if (TextUtils.isEmpty(text)) return new int[]{0, 0};
        int fontHeight = PaintUtils.getFontHeight(getPaint());
        int stringWidth = PaintUtils.getStringWidth(text, getPaint());
        int[] data = {stringWidth + PADDING_LEFT + PADDING_RIGHT, fontHeight + PADDING_TOP + PADDING_BOTTOM};
        return data;
    }

}
