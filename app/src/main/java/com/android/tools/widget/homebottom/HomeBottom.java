package com.android.tools.widget.homebottom;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.android.tools.widget.viewpaper.YxViewPager;

/**
 * Created by Mouse on 2019/2/22.
 */
public class HomeBottom extends LinearLayout {

    private IHomeBottomConfig iHomeBottomConfig;
    private YxViewPager yxViewPager;
    private IHomeBottomItem[] items;
    private LinearLayout container;

    public HomeBottom(Context context) {
        this(context, null);
    }

    public HomeBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setOrientation(LinearLayout.VERTICAL);
        View line = new View(getContext());
        line.setBackgroundColor(Color.argb(255, 207, 207, 207));
        addView(line, new LinearLayout.LayoutParams(-1, 1));
        container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.HORIZONTAL);
        addView(container, new LayoutParams(-1, -1));
        setBackgroundColor(Color.argb(255, 245, 245, 245));
    }

    public void setHomeBottomConfig(IHomeBottomConfig iHomeBottomConfig, YxViewPager yxViewPager) {
        this.iHomeBottomConfig = iHomeBottomConfig;
        this.yxViewPager = yxViewPager;
        items = this.iHomeBottomConfig.getItems();
        if (null == items || items.length == 0) {
            return;
        }
        for (int i = 0; i < items.length; i++) {
            View view = items[i].getView();
            final int m = i;
            container.addView(view, new LayoutParams(0, -1, 1));
            view.setOnClickListener(v -> {
                selectItem(m);
                yxViewPager.setCurrentItem(m, false);
            });
        }
        selectItem(0);
        this.yxViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                selectItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    private void selectItem(int p) {
        for (int i = 0; i < items.length; i++) {
            if (i == p) {
                items[i].onSelectedListener();
            } else {
                items[i].onUnSelectedListener();
            }
        }
    }
}
