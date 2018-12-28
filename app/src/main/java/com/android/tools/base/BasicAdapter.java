package com.android.tools.base;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Mouse on 2018/10/19.
 */
public abstract class BasicAdapter<T> extends BaseAdapter {

    protected List<T> list;
    protected Context context;



    public BasicAdapter(Context context, List<T> list){
        this.list = list;
        this.context = context;
    }


    @Override
    public int getCount() {
        return null==list?0:list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
