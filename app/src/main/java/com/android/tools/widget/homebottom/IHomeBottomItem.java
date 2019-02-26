package com.android.tools.widget.homebottom;

import android.view.View;

/**
 * Created by Mouse on 2019/2/22.
 */
public interface IHomeBottomItem {

    void onSelectedListener();

    void onUnSelectedListener();

    View getView();

}
