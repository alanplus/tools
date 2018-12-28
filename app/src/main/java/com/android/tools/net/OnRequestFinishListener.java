package com.android.tools.net;


import org.json.JSONObject;

/**
 * Created by Mouse on 2018/7/27.
 */

public interface OnRequestFinishListener {

    void parse(JSONObject jsonObject, ApiResult apiResult);
}
