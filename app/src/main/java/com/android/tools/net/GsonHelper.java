package com.android.tools.net;

import com.alan.common.Logger;
import com.alan.common.reflect.ObjectFactory;
import com.android.tools.ArrayUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mouse on 2018/1/12.
 */

public class GsonHelper {

    private static Gson mGson = new Gson();

    /**
     * 将json字符串转化成实体对象
     *
     * @param json
     * @param classOfT
     * @return
     */
    public static Object stringToObject(String json, Class classOfT) {
        return mGson.fromJson(json, classOfT);
    }

    /**
     * 将对象准换为json字符串 或者 把list 转化成json
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String objectToString(T object) {
        return mGson.toJson(object);
    }

    /**
     * 把json 字符串转化成list
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> stringToList(String json, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(mGson.fromJson(elem, cls));
        }
        return list;
    }

    public static <T> HashMap<String, T> stringTHashMap(String string, Class<T> clazz) {
        Type type = new TypeToken<HashMap<String, T>>() {
        }.getType();
        HashMap<String, T> map = mGson.fromJson(string, type);
        return map;
    }

    public static <T> String hashmapToString(HashMap<String, T> map) {
        Type type = new TypeToken<HashMap<String, T>>() {
        }.getType();
        return mGson.toJson(map, type);
    }

    public static <T> JSONObject toJsonObject(T t) {
        Class<?> aClass = t.getClass();
        Field[] fields = aClass.getDeclaredFields();
        try {
            JSONObject jsonObject = new JSONObject();
            for (Field field : fields) {
                SerializedName annotation = field.getAnnotation(SerializedName.class);
                if (null != annotation) {
                    String key = annotation.value();
                    field.setAccessible(true);
                    Object o = field.get(t);
                    if (o instanceof Integer || o instanceof Boolean || o instanceof String || o instanceof Long || o instanceof Short || o instanceof Float || o instanceof Double) {
                        jsonObject.put(key, o);
                    } else if (!(o instanceof List) && !(o instanceof Map)) {
                        jsonObject.put(key, toJsonObject(o));
                    }
                }
            }
            return jsonObject;
        } catch (Exception e) {
            Logger.error(e);
        }
        return new JSONObject();
    }

    public static <T> JSONArray toJsonArray(List<T> list) {
        if (ArrayUtil.isListNull(list)) {
            return new JSONArray();
        }
        JSONArray jsonArray = new JSONArray();
        for (T o : list) {
            if (o instanceof Integer || o instanceof Boolean || o instanceof String || o instanceof Long || o instanceof Short || o instanceof Float || o instanceof Double) {
                jsonArray.put(o);
            } else {
                jsonArray.put(toJsonObject(o));
            }
        }
        return jsonArray;
    }

    public static <T> T toObject(String string, Class<T> tClass) {

        ObjectFactory<T> objectFactory = new ObjectFactory<>(tClass);
        try {
            T t = objectFactory.newInstance();
            return toObject(string, t);
        } catch (Exception e) {
            Logger.error(e);
        }
        return null;
    }

    public static <T> List<T> toObjectList(String string, Class<T> tClass) {

        List<T> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(string);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                T t = toObject(jsonObject.toString(), tClass);
                if(null!=t){
                    list.add(t);
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        }
        return list;
    }

    public static <T> T toObject(String string, T t) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            Class<?> tClass = t.getClass();
            Field[] fields = tClass.getDeclaredFields();
            for (Field field : fields) {
                SerializedName annotation = field.getAnnotation(SerializedName.class);
                if (null != annotation) {
                    field.setAccessible(true);
                    if (jsonObject.has(annotation.value())) {
                        field.set(t, jsonObject.opt(annotation.value()));
                    }
                }
            }
            return t;
        } catch (Exception e) {
            Logger.error(e);
        }
        return null;
    }

}
