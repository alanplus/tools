package com.android.tools.collections;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Created by Mouse on 2018/9/8.
 */

public class YxCollections {

    public static String getStrByObject(List<? extends ObjectId> list) {
        if (null == list || list.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (ObjectId objectId : list) {
            sb.append(objectId.getObjectId());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static String getStringByids(List<Integer> list) {

        if (null == list || list.size() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Integer word : list) {
            sb.append(word);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static String getStrids(List<String> list) {

        if (null == list || list.size() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (String word : list) {
            sb.append(word);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 计划使用
     *
     * @param array
     * @return
     */
    public static String getStrWordids(String array) {

        if (TextUtils.isEmpty(array)) {
            return "";
        }
        return array.replace("[", "").replace("]", "");
    }

    public static <T> List<T> copy(List<T> list, T[] array) {
        if (list == null || list.size() == 0) {
            return new ArrayList<>();
        }
        List<T> lists = new ArrayList<>(Arrays.asList(array));
        Collections.copy(lists, list);
        return lists;
    }

    public static <T extends IObject> int indexOf(T object, List<T> list, int n) {
        if (null == list || list.size() == 0) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++) {
            if (object.equalsOnlySome(list.get(i))) {
                n--;
            }
            if (n == 0) {
                return i;
            }
        }
        return -1;
    }

    public static <T extends Object> int indexOfSameObject(T object, List<T> list, int myPosition) {
        if (null == list || list.size() == 0) {
            return -1;
        }
        int n = 0;
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            if (o.equals(object)) {
                n++;
            }
            if (i == myPosition) {
                return n;
            }
        }
        return -1;
    }

    public static <T> T[] toArray(List<T> list, T[] array) {
        list.toArray(array);
        return array;
    }

    public static <T> List<T> toList(T[] array) {
        return Arrays.asList(array);
    }

    public static <T> List<T> toArrayList(T[] array) {
        List<T> list = new ArrayList<>();
        for (T t : array) {
            list.add(t);
        }
        return list;
    }
}
