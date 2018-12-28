package com.android.tools.widget.corner;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;


/***
 * 设置圆角
 * @author wxw
 */
public class WRadioGroup extends RadioGroup {

	public WViewHelper WViewHelper;
	public WRadioGroup(Context context) {
		super(context);
		init(context, null);
	}

	public WRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs){
		WViewHelper = new WViewHelper();
		WViewHelper.init(context, attrs, this);
	}
}
