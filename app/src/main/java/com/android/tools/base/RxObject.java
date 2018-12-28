package com.android.tools.base;

/**
 * Created by Mouse on 2018/10/31.
 */
public class RxObject {

    /**
     * show loading state
     */
    public static final int EVENT_CODE_LOADING_STATE = 1;
    /**
     * show failure state
     */
    public static final int EVENT_CODE_FAILURE_STATE = 2;
    /**
     * show success state
     */
    public static final int EVENT_CODE_SUCCESS_STATE = 3;

    /**
     * show loading dialog
     */
    public static final int EVENT_CODE_SHOW_LOADING_DIALOG = 4;
    /**
     * dismiss dialog
     */
    public static final int EVENT_CODE_DISMISS_LOADING = 5;
    /**
     * dismiss success dialog
     */
    public static final int EVENT_CODE_DISMISS_SUCCESS = 6;
    /**
     * dismiss failure dialog
     */
    public static final int EVENT_CODE_DISSMISS_FAILURE = 7;
    /**
     * finish
     */
    public static final int EVENT_CODE_FINISH = 8;
    /**
     * show failure state with retry
     */
    public static final int EVENT_CODE_FAILURE_RETRY_STATE = 9;

    /**
     * finish
     */
    public static final int EVENT_CODE_FINISH_WITHOUT_ANIM = 10;


    public int code;
    public String message;
    public Object object;

    public RxObject() {

    }

    public RxObject(int code) {
        this(code, "");
    }

    public RxObject(int code, String message) {
        this(code, message, null);
    }

    public RxObject(int code, String message, Object object) {
        this.code = code;
        this.message = message;
        this.object = object;
    }
}
