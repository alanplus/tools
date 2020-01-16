package com.android.tools.widget.bottomline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.alan.common.Logger;
import com.alan.common.utils.PaintUtils;

/**
 * Created by Mouse on 2018/10/25.
 */
public class WithBottomLineEditText extends android.support.v7.widget.AppCompatEditText {

    public WithBottomLineEditText(Context context) {
        this(context, null);
    }

    public WithBottomLineEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.TRANSPARENT);
        setLineSpacing(0, 1.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Layout layout = getLayout();
        TextPaint paint = getPaint();
        int lineCount = getLineCount();
        int height = getMeasuredHeight();
        int lineHeight = height / lineCount;

        for (int i = 0; i < lineCount; i++) {
//            int lineAscent = layout.getLineAscent(i);
//            int lineBaseline = layout.getLineBaseline(i);
//            int lineDescent = layout.getLineDescent(i);
//            int lineTop = layout.getLineTop(i);
//            int lineBottom = layout.getLineBottom(i);

            int y = (i + 1) * lineHeight;
            Logger.d("acture height:" + lineHeight);
            int w = (i == lineCount - 1 && i != 0) ? getLastLineWidth(i) : getWidth();
            canvas.drawLine(0, y - 1, w, y, paint);
        }
    }

    private int getLastLineWidth(int lines) {
        if (lines == 0) return getWidth();
        Layout layout = getLayout();
        float lineWidth = layout.getLineWidth(lines);
        return lineWidth == 0 ? PaintUtils.getStringWidth("a", getPaint()) : (int) lineWidth;
    }
}
