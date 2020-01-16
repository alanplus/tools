package com.android.tools.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alan.common.Logger;
import com.android.tools.rx.RxManager;

/**
 * Created by Mouse on 2018/11/1.
 */
public abstract class BaseFragment extends Fragment {

    protected View mRoot;
    protected RxManager rxManager;

    protected Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(onCreateContent(), container, false);
        try {
            initView();
        } catch (Exception e) {
            Logger.d("test_fragment e:" + e.getMessage());
        }
        registMyReceiver();
        return mRoot;
    }

    public void registMyReceiver() {

    }

    protected void initView() {
    }

    protected abstract int onCreateContent();

    /**
     * 设置fragment name
     */
    protected abstract String getPageName();

    public void setRxManager(RxManager rxManager) {
        this.rxManager = rxManager;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

}
