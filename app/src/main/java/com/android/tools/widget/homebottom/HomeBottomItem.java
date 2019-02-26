package com.android.tools.widget.homebottom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.tools.R;

/**
 * Created by Mouse on 2019/2/22.
 */
public class HomeBottomItem extends LinearLayout implements IHomeBottomItem {

    private IHomeBottomItemConfig iHomeBottomItemConfig;
    private View icon;
    private TextView textView;

    public HomeBottomItem(Context context, IHomeBottomItemConfig iHomeBottomItemConfig) {
        super(context);
        this.iHomeBottomItemConfig = iHomeBottomItemConfig;
        initView();
    }

    public HomeBottomItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_home_bottom_item, this);
        icon = findViewById(R.id.icon);
        textView = findViewById(R.id.text);
        textView.setText(iHomeBottomItemConfig.getText());
        setState(false);
    }

    @Override
    public void onSelectedListener() {
        IHomeBottomItem iHomeBottom = iHomeBottomItemConfig.getIHomeBottom();
        if (null != iHomeBottom) {
            iHomeBottom.onSelectedListener();
        }
        setState(true);
    }

    @Override
    public void onUnSelectedListener() {
        IHomeBottomItem iHomeBottom = iHomeBottomItemConfig.getIHomeBottom();
        if (null != iHomeBottom) {
            iHomeBottom.onUnSelectedListener();
        }
        setState(false);
    }

    @Override
    public View getView() {
        return this;
    }

    private void setState(boolean isSelected) {
        if (isSelected) {
            icon.setBackgroundResource(iHomeBottomItemConfig.getSelectedImage());
            textView.setTextColor(iHomeBottomItemConfig.getSelectedTextColor());
        } else {
            icon.setBackgroundResource(iHomeBottomItemConfig.getDefaultImage());
            textView.setTextColor(iHomeBottomItemConfig.getDefaultTextColor());
        }

    }
}
