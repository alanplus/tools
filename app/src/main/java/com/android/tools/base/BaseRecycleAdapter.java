package com.android.tools.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Mouse on 2018/10/19.
 */
public class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseRecycleAdapter.CommonViewHolder> {

    protected List<T> list;
    protected Context context;

    protected static final int TYPE_HEAD = 100;
    protected static final int TYPE_FOOT = 101;
    protected static final int TYPE_COMMON = 102;

    public BaseRecycleAdapter(Context context, List<T> list){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder commonViewHolder, int position) {

    }

    @Override
    public int getItemCount() {
        return null==list?0:list.size();
    }

    public static class CommonViewHolder extends RecyclerView.ViewHolder{

        public CommonViewHolder(View itemView) {
            super(itemView);
        }
    }
}
