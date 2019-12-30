package com.android.tools.widget.bottomline;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.android.tools.AndroidTools;
import com.android.tools.PaintTools;
import com.android.tools.R;

/**
 * Created by Mouse on 2018/10/25.
 */
public class WithBottomLineTextView extends View {

    private int lineHeight;
    private String text;

    private TextPaint paint;
    private int color;
    private int lineColor;

    private String hint;
    private int hintColor;
    private int gravity;

    public WithBottomLineTextView(Context context) {
        super(context);
    }

    public WithBottomLineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.WithBottomLineTextView);
        int i = AndroidTools.dip2px(10);
        lineHeight = (int) typedArray.getDimension(R.styleable.WithBottomLineTextView_line_space, i);
        color = typedArray.getColor(R.styleable.WithBottomLineTextView_bottom_color, Color.parseColor("#1A1A1A"));
        lineColor = typedArray.getColor(R.styleable.WithBottomLineTextView_bottom_line_color, color);
        hintColor = typedArray.getColor(R.styleable.WithBottomLineTextView_hint_color, Color.parseColor("#DDDDDD"));
        hint = typedArray.getString(R.styleable.WithBottomLineTextView_hint);
        gravity = typedArray.getInt(R.styleable.WithBottomLineTextView_bottom_gravity, 0);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int customHeight = getCustomHeight();
        setMeasuredDimension(widthMeasureSpec, customHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        StaticLayout staticLayout = getStaticLayout();
        String string = getText();
        int lineHeight = getLineHeight(staticLayout.getLineCount());
        int start = 0;
        for (int i = 0; i < staticLayout.getLineCount(); i++) {
            int end = staticLayout.getLineEnd(i);
            String substring = string.substring(start, end);
            start = end;
            int drawerStartPosition = getDrawerStartPosition(substring, i == 0, false);
            drawText(canvas, substring, i, lineHeight, drawerStartPosition);
            int lineStart = getDrawerStartPosition(substring, i == 0 || i != staticLayout.getLineCount() - 1, true);
            drawLine(canvas, i, lineHeight, getLineWidth(i == staticLayout.getLineCount() - 1, substring, i), lineStart);
        }
        drawHint(canvas);
    }

    private int getDrawerStartPosition(String text, boolean isFirstLine, boolean isLine) {
        if (gravity == 0 || (isFirstLine && isLine)) {
            return 0;
        }
        int stringWidth = PaintTools.getStringWidth(text, getPaint(true));
        int measuredWidth = getMeasuredWidth();
        int start = (measuredWidth - stringWidth) / 2;
        return start >= 0 ? start : 0;
    }

    private void drawHint(Canvas canvas) {
        if (TextUtils.isEmpty(getText()) && !TextUtils.isEmpty(hint)) {
            float fontLeading = PaintTools.getFontLeading(getPaint(true));
            TextPaint paint = getPaint(true);
            paint.setColor(hintColor);
            canvas.drawText(hint, 0, fontLeading, paint);
        }
    }

    private int getCustomHeight() {
        StaticLayout staticLayout = getStaticLayout();
        int fontHeight = PaintTools.getFontHeight(getPaint(true));
        int lineCount = staticLayout.getLineCount();
        return lineCount * (fontHeight + lineHeight);
    }

    private StaticLayout getStaticLayout() {
        String text = getText();
        return new StaticLayout(text, getPaint(true), getMeasuredWidth(), Layout.Alignment.ALIGN_NORMAL, 1.5f, 0, false);
    }

    private void drawText(Canvas canvas, String text, int line, int lineHeight, int start) {
        float fontLeading = PaintTools.getFontLeading(getPaint(true));
        int h = (int) (line * lineHeight + fontLeading);
        canvas.drawText(text, start, h, getPaint(true));
    }

    private void drawLine(Canvas canvas, int line, int lineHeight, int width, int start) {
        canvas.drawLine(start, (line + 1) * lineHeight - 1 - this.lineHeight / 2, width + start, (line + 1) * lineHeight - this.lineHeight / 2, getPaint(false));
    }

    private int getLineWidth(boolean isLast, String text, int line) {
        if (line == 0 || !isLast) return getMeasuredWidth();

        int stringWidth = PaintTools.getStringWidth(text, getPaint(false));
        if (stringWidth == 0) return PaintTools.getStringWidth("a", getPaint(false));
        return stringWidth;
    }

    private int getLineHeight(int lines) {
        int measuredHeight = getMeasuredHeight();
        return measuredHeight / lines;
    }

    public void setText(String text) {
        this.text = text;
        requestLayout();
        /**
         * I don't konwn that why the text is not displayed on version 4.3
         * So this is going to solve program,but I don't konw.
         */
        if (AndroidTools.getCurrentSdk() < 21) {
            postInvalidate();
        }
    }

    public String getText() {
        return text;
    }

    public TextPaint getPaint(boolean isText) {
        if (null == paint) {
            paint = new TextPaint();
            paint.setAntiAlias(true);
            paint.setTextSize(AndroidTools.dip2px(17));
        }
        paint.setColor(isText ? color : lineColor);
        return paint;
    }


    public void setColor(@ColorInt int color) {
        this.color = color;
        this.lineColor = color;
        postInvalidate();
    }

    public void setTextColor(@ColorInt int color) {
        this.color = color;
        postInvalidate();
    }

    public void setLineColor(@ColorInt int lineColor) {
        this.lineColor = lineColor;
        postInvalidate();
    }
}
