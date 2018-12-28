package com.android.tools.base;

import android.content.Context;

import com.android.tools.rx.RxManager;

import java.util.List;

/**
 * Created by Mouse on 2018/7/23.
 */

public class RxApiBasePresenter<T> extends RxBasePresenter{

    protected List<T> list;

    public RxApiBasePresenter(Context context, RxManager rxManager) {
        super(context, rxManager);
    }

    public int getListCount(){
        return list==null?0:list.size();
    }

    public T getItem(int position){
        return list.get(position);
    }

    public List<T> getList() {
        return list;
    }
}
