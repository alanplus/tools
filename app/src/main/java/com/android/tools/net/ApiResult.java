package com.android.tools.net;

import java.util.HashMap;

/**
 * Api请求结果类
 * Title: ApiResult
 * Date: 2017/11/6 11:14
 * Company: WeiCi
 */
public class ApiResult {

    public int code;
    public String msg;
    public String url;
    public Object object;

    public HashMap<String, Object> map;

    public int type;

    public ApiResult() {
    }

    public ApiResult(int code) {
        this(code, null);
    }

    public ApiResult(int code, String msg) {
        this(code, msg, null);
    }

    public ApiResult(int code, String msg, String url) {
        this(code, msg, url, null);
    }

    public ApiResult(int code, String msg, String url, Object object) {
        this(code, msg, url, object, null);
    }

    public ApiResult(int code, String msg, String url, Object object, HashMap<String, Object> map) {
        this.code = code;
        this.msg = msg;
        this.object = object;
        this.url = url;
        this.map = map;
    }


    public String getStringFromMap(String key) {
        if (null == map) {
            return null;
        }

        if (map.containsKey(key)) {
            Object o = map.get(key);
            if (o instanceof String) {
                return (String) o;
            }
        }
        return null;
    }

    public int getIntFromMap(String key) {
        if (null == map) {
            return -1;
        }

        if (map.containsKey(key)) {
            Object o = map.get(key);
            if (o instanceof Integer) {
                return (Integer) o;
            }
        }
        return -1;
    }

    public String getErrorMsg() {
        String msg = "请求失败";
        switch (code) {
            case 200:
                msg = "发送成功";
                break;
            case 400:
            case 500:
            case 908:
                msg = "发送失败";
                break;
            case 1000:
            case 1001:
                msg ="该内容已不存在";
                break;
            case 901:
            case 902:
            case 903:
                msg = "验证错误";
                break;
            case 912:
                msg = "无效的用户";
                break;
            case 904:
                msg = "图形验证码错误，请重试";
                break;
            case 905:
                msg = "图形验证码过期，请重新获取";
                break;
            case 911:
                msg = "session不存在";
                break;
            case 906:
                msg = "短信获取太频繁，请稍后再试";
                break;
            case 907:
                msg = "发送的验证码超过5次";
                break;
            case 913:
                msg = "需要申诉";
                break;
            case 914:
                msg = "需要申诉";
                break;
            case 940:
                msg = "账号未认证";
                break;
            case 950:
                msg = "此班级不允许学生加入";
                break;
            case -2:
                msg = "网络无效，请重试";
                break;
            case 201:
                msg = "暂无数据";
                break;
            case 915:
                msg = "激活码不存在";
                break;
            case 990:
                msg = "班级不存在";
                break;
            case -1:
                msg = "网络连接超时，请检测网络连接";
                break;
            default:
                msg = "错误码:" + code;
                break;
        }
        return msg;
    }

    public boolean isNeedAppeal() {
        return code == 913 || code == 914;
    }
}
