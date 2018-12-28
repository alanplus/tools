package com.android.tools.base;

import android.content.Context;
import android.content.Intent;

import com.android.tools.rx.RxManager;
import com.android.tools.widget.ToastManager;


/**
 * Created by Mouse on 2018/7/23.
 */

public class RxBasePresenter {

    protected RxManager rxManager;
    protected Context context;

    protected String EVENT_MESSAGE;

    public RxBasePresenter(Context context, RxManager rxManager) {
        this.context = context;
        this.rxManager = rxManager;
        EVENT_MESSAGE = "event" + rxManager.hashCode();


    }

    public void init(Intent intent) {
    }


    protected void showLoadState(String text) {
        rxManager.post(EVENT_MESSAGE, new RxObject(RxObject.EVENT_CODE_LOADING_STATE, text));
    }

    protected void showLoadState() {
        showLoadState("");
    }

    protected void showSuccess() {
        showSuccess("");
    }

    protected void showSuccess(Object object) {
        rxManager.post(EVENT_MESSAGE, new RxObject(RxObject.EVENT_CODE_SUCCESS_STATE, "", object));
    }

    protected void showFailure() {
        showFailure("加载失败，请重试");
    }

    protected void showFailure(String text) {
        showFailure(text, false);
    }

    protected void showFailure(String text, boolean retry) {
        rxManager.post(EVENT_MESSAGE, new RxObject(retry ? RxObject.EVENT_CODE_FAILURE_RETRY_STATE : RxObject.EVENT_CODE_FAILURE_STATE, text));
    }

    protected void showLoadDialog() {
        rxManager.post(EVENT_MESSAGE, new RxObject(RxObject.EVENT_CODE_SHOW_LOADING_DIALOG));
    }

    protected void dissmissSuccessLoadingDialog() {
        rxManager.post(EVENT_MESSAGE, new RxObject(RxObject.EVENT_CODE_DISMISS_SUCCESS));
    }

    protected void dissmissSuccessLoadingDialog(String text) {
        rxManager.post(EVENT_MESSAGE, new RxObject(RxObject.EVENT_CODE_DISMISS_SUCCESS, text));
    }

    protected void dissmissDialog() {
        rxManager.post(EVENT_MESSAGE, new RxObject(RxObject.EVENT_CODE_DISMISS_LOADING));
    }

    protected void dismissFailureLoadingDialog() {
        rxManager.post(EVENT_MESSAGE, new RxObject(RxObject.EVENT_CODE_DISSMISS_FAILURE));
    }

    protected void dismissFailureLoadingDialog(String text) {
        rxManager.post(EVENT_MESSAGE, new RxObject(RxObject.EVENT_CODE_DISSMISS_FAILURE, text));
    }

    protected void finish() {
        rxManager.post(EVENT_MESSAGE, new RxObject(RxObject.EVENT_CODE_FINISH));
    }

    protected void finishWithoutAnim() {
        rxManager.post(EVENT_MESSAGE, new RxObject(RxObject.EVENT_CODE_FINISH_WITHOUT_ANIM));
    }

    protected void showToast(String message) {
        ToastManager.getInstance().showToast(context, message);
    }
}
