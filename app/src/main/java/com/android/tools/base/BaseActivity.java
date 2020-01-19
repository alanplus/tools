package com.android.tools.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.tools.AndroidToolsConfig;
import com.android.tools.R;
import com.android.tools.ResourceUtil;
import com.android.tools.dialog.LoadingDialog;
import com.android.tools.rx.RxManager;
import com.android.tools.statusbar.StatusBarBuilder;
import com.android.tools.statusbar.StatusBarTools;
import com.android.tools.widget.ToastManager;
import com.android.tools.widget.state.IStateViewListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class BaseActivity extends AppCompatActivity {

    public static List<Activity> activityStack = new ArrayList<>();

    protected LoadingDialog mLoadingDialog;
    protected Context mContext;
    protected RxManager rxManager;
    protected int hashcode;

    protected boolean isTranslucent = false;


    protected int state;

    protected static final int STATE_ONCREATE = 1;
    protected static final int STATE_ONRESUME = 2;
    protected static final int STATE_ONSTART = 3;
    protected static final int STATE_ONPAUSE = 4;
    protected static final int STATE_ONSTOP = 5;
    protected static final int STATE_ONDESTROY = 6;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = STATE_ONCREATE;
        mContext = this;
        activityStack.add(this);
        registEvenBus();
        rxManager = new RxManager();
        hashcode = rxManager.hashCode();
        registPageState();
        if (null != AndroidToolsConfig.androidToolsConfig)
            AndroidToolsConfig.androidToolsConfig.onActivityCreate(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initStatusBar();
    }


    protected void initStatusBar() {
        if (isOnlyTranslateMode()) {
            isTranslucent = StatusBarTools.translucent(this);
            return;
        }
        int bgColor = ResourceUtil.getColorFromTheme(this, R.attr.status_bar_color, Color.WHITE);
        boolean isWhiteText = ResourceUtil.getBoolFromTheme(this, R.attr.status_bar_text_is_white, false);
        if (isNeedStatusBarLightMode() && StatusBarTools.getStatusBarTools().setStatusBarColor(this, isWhiteText)) {
            if (isNeedTranslateMode()) {
                onChangeTitlebarColor();
            } else {

                StatusBarBuilder.get(this).setBgColor(bgColor).setWhiteText(isWhiteText).build();
            }
        }
    }

    protected void registEvenBus() {
        if (configEvenBus()) {
            EventBus.getDefault().register(this);
        }
    }

    protected void onChangeTitlebarColor() {

    }

    protected boolean isNeedStatusBarLightMode() {
        return true;
    }

    protected boolean isNeedTranslateMode() {
        return false;
    }


    protected void unregistEvenBus() {
        if (configEvenBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    protected boolean configEvenBus() {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackKeyDown();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onBackKeyDown() {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            return;
        }
        onBackPressed();
    }

    protected LoadingDialog getLoadingDialog() {
        if (null == mLoadingDialog) {
            mLoadingDialog = new LoadingDialog(this);
        }
        return mLoadingDialog;
    }

    public void dismissLoadingDialog() {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public void dismissLoadingDialog(boolean success, String text) {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss(text, success, () -> onLoadingDialogDismissListener(success, ""));
        }
    }

    public void dismissLoadingDialog(boolean success, String text, LoadingDialog.OnDialogDismissListener listener) {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss(text, success, listener);
        }
    }

    public void showLoadingDialog() {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            return;
        }
        getLoadingDialog().show();
    }

    public void showLoadingDialog(String text) {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            getLoadingDialog().setText(text);
        } else {
            getLoadingDialog().setText(text).show();
        }
    }

    public void showToast(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        ToastManager.getInstance().showToast(this, msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        state = STATE_ONRESUME;
        AndroidToolsConfig.androidToolsConfig.onActivityResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        state = STATE_ONSTART;
    }

    @Override
    protected void onPause() {
        super.onPause();
        state = STATE_ONPAUSE;
        AndroidToolsConfig.androidToolsConfig.onActivityPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        state = STATE_ONSTOP;
    }

    @Override
    protected void onDestroy() {
        state = STATE_ONDESTROY;
        if (null != AndroidToolsConfig.androidToolsConfig)
            AndroidToolsConfig.androidToolsConfig.onActivityDestroy(this);
        super.onDestroy();
        activityStack.remove(this);

        unregistEvenBus();
        rxManager.clear();
    }

    public static void finishAll() {
        for (Activity activity : activityStack) {
            activity.finish();
        }
    }

    public Activity getActivity() {
        return this;
    }

    protected void registPageState() {
        rxManager.onEvent("event" + rxManager.hashCode(), (Consumer<RxObject>) rxObject -> responseEventCode(rxObject));
    }

    protected void showLoadingState() {
        if (null != getStateView()) getStateView().showLoadingState();
    }

    protected void onSuccess() {
        if (null != getStateView()) getStateView().showSuccessState();
    }

    protected void onSuccess(RxObject rxObject){
        onSuccess();
    }

    public void showFailureState(String s, boolean retry) {
        if (null != getStateView()) {
            IStateViewListener stateView = getStateView();
            stateView.showFailureState(s, retry);
            TextView retryView = stateView.getRetryView();
            if (null != retryView) {
                retryView.setOnClickListener(v -> onRetryListener());
            }
        }
    }

    protected IStateViewListener getStateView() {
        return findStateView(findViewById(android.R.id.content));
    }

    protected boolean isOnlyTranslateMode() {
        return false;
    }

    public void onLoadingDialogDismissListener(boolean isSuccess, String s) {
    }

    private void responseEventCode(RxObject rxObject) {
        switch (rxObject.code) {
            case RxObject.EVENT_CODE_LOADING_STATE:
                showLoadingState();
                break;
            case RxObject.EVENT_CODE_SUCCESS_STATE:
                onSuccess(rxObject);
                break;
            case RxObject.EVENT_CODE_FAILURE_STATE:
                showFailureState(rxObject.message, false);
                break;
            case RxObject.EVENT_CODE_FAILURE_RETRY_STATE:
                showFailureState(rxObject.message, true);
                break;
            case RxObject.EVENT_CODE_SHOW_LOADING_DIALOG:
                showLoadingDialog(rxObject.message);
                break;
            case RxObject.EVENT_CODE_DISMISS_LOADING:
                dismissLoadingDialog();
                break;
            case RxObject.EVENT_CODE_DISSMISS_FAILURE:
                dismissLoadingDialog(false, rxObject.message);
                break;
            case RxObject.EVENT_CODE_DISMISS_SUCCESS:
                dismissLoadingDialog(true, rxObject.message);
                break;
            case RxObject.EVENT_CODE_FINISH:
                finish();
                break;
            case RxObject.EVENT_CODE_FINISH_WITHOUT_ANIM:
                finish();
                overridePendingTransition(0, 0);
                break;

        }

    }

    protected IStateViewListener findStateView(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        if (childCount == 0) return null;
        for (int i = 0; i < childCount; i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof IStateViewListener) {
                return (IStateViewListener) v;
            }
            if (v instanceof ViewGroup) {
                IStateViewListener stateView = findStateView((ViewGroup) v);
                if (null != stateView) {
                    return stateView;
                }
            }
        }
        return null;
    }

    public void onRetryListener() {
    }

}
