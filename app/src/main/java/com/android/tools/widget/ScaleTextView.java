package com.android.tools.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.ScaleAnimation;

import com.android.tools.R;

/**
 * Created by Mouse on 2018/12/11.
 */
public class ScaleTextView extends android.support.v7.widget.AppCompatTextView {

    private boolean scaleable;

    private float fromX, toX, fromY, toY;
    private int duration;

    public ScaleTextView(Context context) {
        this(context, null);
    }

    public ScaleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        scaleable = true;
        fromX = 1f;
        toX = 0.5f;
        fromY = 1.0f;
        toY = 0.5f;
        duration = 200;
        if (null == attributeSet) return;
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.ScaleTextView);
        scaleable = typedArray.getBoolean(R.styleable.ScaleTextView_scale_able, true);
        fromX = typedArray.getFloat(R.styleable.ScaleTextView_from_x, 1f);
        toX = typedArray.getFloat(R.styleable.ScaleTextView_to_x, 0.5f);
        fromY = typedArray.getFloat(R.styleable.ScaleTextView_from_y, 1f);
        toY = typedArray.getFloat(R.styleable.ScaleTextView_to_y, 0.5f);
        typedArray.recycle();
    }

    public void setScaleable(boolean scaleable) {
        this.scaleable = scaleable;
    }

    public void setAnimationScale(float fromX, float toX, float fromY, float toY) {
        this.fromX = fromX;
        this.toX = toX;
        this.fromY = fromY;
        this.toY = toY;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!scaleable) {
            return true;
        }
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                ScaleAnimation scaleAnimation = new ScaleAnimation(fromX, toX, fromY, toY, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(duration);
                startAnimation(scaleAnimation);
                break;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_SCROLL:
                ScaleAnimation scaleAnimation2 = new ScaleAnimation(toX, fromX, toY, fromY, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation2.setDuration(duration);
                startAnimation(scaleAnimation2);
                break;
        }
        return super.onTouchEvent(event);
    }
}
