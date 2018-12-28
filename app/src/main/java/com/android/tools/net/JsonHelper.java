package com.android.tools.net;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Mouse on 2018/7/23.
 */

public class JsonHelper {

    public static int getInt(JSONObject jsonObject, String key){
        if(null==jsonObject){
            return 0;
        }
        if(jsonObject.isNull(key)){
            return 0;
        }
        return jsonObject.optInt(key,0);
    }

    public static String getString(JSONObject jsonObject, String key){
        if(null==jsonObject){
            return "";
        }
        if(jsonObject.isNull(key)){
            return "";
        }
        return jsonObject.optString(key,"");
    }

    public static boolean getBoolean(JSONObject jsonObject, String key){
        if(null==jsonObject){
            return false;
        }
        if(jsonObject.isNull(key)){
            return false;
        }
        return jsonObject.optBoolean(key,false);
    }

    public static JSONArray getJsonArray(JSONObject jsonObject, String key){

        if(null==jsonObject){
            return null;
        }
        if(jsonObject.isNull(key)){
            return null;
        }
        return jsonObject.optJSONArray(key);
    }

    public static JSONObject getJsonObject(JSONObject jsonObject, String key){

        if(null==jsonObject){
            return null;
        }
        if(jsonObject.isNull(key)){
            return null;
        }
        return jsonObject.optJSONObject(key);
    }


}
