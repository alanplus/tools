package com.android.tools.net;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.tools.AndroidToolsConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Api控制类
 * Title: ApiBody
 * Date: 2017/11/13 10:09
 * Company: WeiCi
 */
public class ApiBody {
    public HashMap<String, String> params;
    public String URL;
    public boolean Encoder = true;
    public OkHttpUtil.TYPE type = OkHttpUtil.TYPE.GET;
    public File file;

    public JSONObject parse(String text, ApiResult result) {
        return parseOnlyCode(text, result);
    }


    public static class ApiBodyBuilder {

        private ApiBody apiBody;

        public ApiBodyBuilder create() {
            apiBody = new ApiBody();
            apiBody.type = OkHttpUtil.TYPE.GET;
            return this;
        }

        public ApiBodyBuilder create(@NonNull final OnRequestFinishListener onRequestFinishListener) {
            apiBody = new ApiBody() {
                @Override
                public JSONObject parse(String text, ApiResult result) {
                    onRequestFinishListener.parse(super.parse(text, result), result);
                    return null;
                }
            };
            return this;
        }

        public ApiBodyBuilder createWithJson() {
            apiBody = new ApiBody() {
                @Override
                public JSONObject parse(String text, ApiResult result) {
                    result.object = super.parse(text, result);
                    return null;
                }
            };
            return this;
        }

        public ApiBodyBuilder type(OkHttpUtil.TYPE type) {
            apiBody.type = type;
            return this;
        }

        public <T> ApiBodyBuilder createWithJsonList(final Class<T> c) {
            apiBody = new ApiBody() {
                @Override
                public JSONObject parse(String text, ApiResult result) {
                    JSONObject jsonObject = super.parse(text, result);
                    if (result.code == 200) {
                        JSONArray data = JsonHelper.getJsonArray(jsonObject, "data");
                        if (data != null)
                            result.object = GsonHelper.stringToList(data.toString(), c);
                    }
                    return null;
                }
            };
            return this;
        }

        public <T> ApiBodyBuilder createWithJsonList(final Class<T> c, String name) {
            apiBody = new ApiBody() {
                @Override
                public JSONObject parse(String text, ApiResult result) {
                    JSONObject jsonObject = super.parse(text, result);
                    if (result.code == 200) {
                        JSONArray data = JsonHelper.getJsonArray(jsonObject, name);
                        if (data != null)
                            result.object = GsonHelper.stringToList(data.toString(), c);
                    }
                    return null;
                }
            };
            return this;
        }

        public <T> ApiBodyBuilder createWithJsonObject(final Class<T> c, String name) {
            apiBody = new ApiBody() {
                @Override
                public JSONObject parse(String text, ApiResult result) {
                    JSONObject jsonObject = super.parse(text, result);
                    if (result.code == 200) {
                        JSONObject data = JsonHelper.getJsonObject(jsonObject, name);
                        if (data != null)
                            result.object = GsonHelper.stringToObject(data.toString(), c);
                    }
                    return null;
                }
            };
            return this;
        }


        public <T> ApiBodyBuilder createWithFullJsonList(final Class<T> c) {
            apiBody = new ApiBody() {
                @Override
                public JSONObject parse(String text, ApiResult result) {
                    super.parse(text, result);
                    if (result.code == 200) {
                        result.object = GsonHelper.stringToObject(text, c);
                    }
                    return null;
                }
            };
            return this;
        }

        public ApiBodyBuilder buildBaseParams() {
            apiBody.params = AndroidToolsConfig.androidToolsConfig.getBaseParams();
            return this;
        }

        public ApiBodyBuilder buildUserBaseParams() {
            apiBody.params = AndroidToolsConfig.androidToolsConfig.getUserBaseParams();
            return this;
        }

        public ApiBodyBuilder add(String key, String value) {
            if (null == apiBody.params)
                apiBody.params = AndroidToolsConfig.androidToolsConfig.getUserBaseParams();
            apiBody.params.put(key, value);
            return this;
        }

        public ApiBodyBuilder add(String key, int value) {
            if (null == apiBody.params)
                apiBody.params = AndroidToolsConfig.androidToolsConfig.getUserBaseParams();
            apiBody.params.put(key, String.valueOf(value));
            return this;
        }

        public ApiBodyBuilder url(String url) {
            apiBody.URL = url;
            return this;
        }

        public ApiResult executeOnGet() {
            return ApiRequest.executeOnGet(apiBody);
        }

        public ApiResult executeOnPost() {
            return ApiRequest.executeOnPost(apiBody);
        }

        public void execute(ApiRequest.OnCallBackListener onCallBackListener) {
            ApiRequest.execute(apiBody, onCallBackListener);
        }
    }


    public JSONObject parseOnlyCode(String text, ApiResult result) {
        if (!TextUtils.isEmpty(text)) {
            try {
                JSONObject json = new JSONObject(text);
                result.code = json.optInt("result_code");
                AndroidToolsConfig.androidToolsConfig.onErrorCode(result.code);
                return json;
            } catch (Exception e) {
            }
        }
        return null;
    }

}
