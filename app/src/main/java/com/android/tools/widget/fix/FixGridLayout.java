package com.android.tools.widget.fix;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.alan.common.AndroidTools;
import com.android.tools.R;

import java.util.ArrayList;
import java.util.List;

public class FixGridLayout extends ViewGroup {

    private float gapX = 0;
    private float gapY = 0;

    public List<int[]> childInfoList;

    public FixGridLayout(Context context) {
        this(context, null);
    }

    public FixGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        childInfoList = new ArrayList<>();
        if (null == attrs) return;
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.FixGridViewGroup);
        gapX = attributes.getDimension(R.styleable.FixGridViewGroup_padding_x, 0);
        gapY = attributes.getDimension(R.styleable.FixGridViewGroup_padding_y, 0);
        attributes.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            int[] data = childInfoList.get(i);
            view.layout(data[2], data[3], data[2] + data[0], data[3] + data[1]);
            if (view instanceof SentenceTestTextView) {
                ((SentenceTestTextView) view).setGravity(Gravity.CENTER);
            }
        }
    }


    /**
     * 计算控件及子控件所占区域
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        childInfoList.clear();
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int[] screenSize = AndroidTools.getScreenSize(getContext());
        int width = screenSize[0];
        if (mode == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();


        int contentWidth = width - paddingLeft - paddingRight;
        int count = getChildCount();
        int x = paddingLeft;
        int y = paddingTop;
        int maxheight = 0;
        for (int i = 0; i < count; i++) {
            int[] ints = measureChild(getChildAt(i));
            if (x + ints[0] > contentWidth) {
                x = paddingLeft;
                y += maxheight + gapY;
            }
            maxheight = Math.max(maxheight, ints[1]);
            childInfoList.add(new int[]{ints[0], ints[1], x, y});
            x += ints[0] + gapX;
        }
        int h = y + maxheight + paddingBottom;
        setMeasuredDimension(width, h);
    }


    protected int[] measureChild(View child) {
        if (child instanceof IFixChildView) {
            int[] measure = ((IFixChildView) child).measure();
            child.measure(measure[0], measure[1]);
            child.forceLayout();
            return measure;
        }


        /**
         * It's best to implement this feature by yourself
         */
        LayoutParams p = child.getLayoutParams();
        int lpHeight = p.height;
        int lpWidth = p.width;
        if (lpHeight > 0) {
            lpHeight = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            lpHeight = MeasureSpec.makeMeasureSpec(getSuggestedMinimumHeight(), MeasureSpec.UNSPECIFIED);
        }

        if (lpWidth > 0) {
            lpWidth = MeasureSpec.makeMeasureSpec(lpWidth, MeasureSpec.EXACTLY);
        } else {
            lpWidth = MeasureSpec.makeMeasureSpec(getSuggestedMinimumWidth(), MeasureSpec.UNSPECIFIED);
        }
        child.measure(lpWidth, lpHeight);

        // Since this view was measured directly aginst the parent measure
        // spec, we must measure it again before reuse.
        child.forceLayout();
        return new int[]{lpWidth, lpHeight};
    }
}
