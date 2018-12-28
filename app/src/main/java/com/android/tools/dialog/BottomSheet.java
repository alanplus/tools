package com.android.tools.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.android.tools.AndroidTools;
import com.android.tools.R;


/**
 * Created by Mouse on 2017/10/11.
 */

public class BottomSheet extends Dialog {

    // 动画时长
    private final static int mAnimationDuration = 200;
    // 持有 ContentView，为了做动画
    private View mContentView;
    private boolean mIsAnimating = false;

    private OnBottomSheetShowListener mOnBottomSheetShowListener;

    public BottomSheet(@NonNull Context context) {
        super(context, R.style.BottomSheet);
    }

    private void initWindow() {

        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        // 在底部，宽度撑满
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM | Gravity.CENTER;

        int[] screenSize = AndroidTools.getScreenSize(getContext());
        int screenWidth = screenSize[0];
        int screenHeight = screenSize[1];
        params.width = screenWidth < screenHeight ? screenWidth : screenHeight;
        getWindow().setAttributes(params);
        setCanceledOnTouchOutside(true);
    }

    public void setOnBottomSheetShowListener(OnBottomSheetShowListener onBottomSheetShowListener) {
        mOnBottomSheetShowListener = onBottomSheetShowListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
    }

    @Override
    public void setContentView(int layoutResID) {
        mContentView = LayoutInflater.from(getContext()).inflate(layoutResID, null);
        super.setContentView(mContentView);
    }

    @Override
    public void setContentView(@NonNull View view) {
        mContentView = view;
        super.setContentView(view);
    }

    @Override
    public void setContentView(@NonNull View view, ViewGroup.LayoutParams params) {
        mContentView = view;
        super.setContentView(view, params);
    }

    public View getContentView() {
        return mContentView;
    }

    /**
     * BottomSheet升起动画
     */
    private void animateUp() {
        if (mContentView == null) {
            return;
        }
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f
        );
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translate);
        set.addAnimation(alpha);
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(mAnimationDuration);
        set.setFillAfter(true);
        mContentView.startAnimation(set);
    }

    /**
     * BottomSheet降下动画
     */
    private void animateDown() {
        if (mContentView == null) {
            return;
        }
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f
        );
        AlphaAnimation alpha = new AlphaAnimation(1, 0);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translate);
        set.addAnimation(alpha);
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(mAnimationDuration);
        set.setFillAfter(true);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsAnimating = false;
                /**
                 * Bugfix： Attempting to destroy the window while drawing!
                 */
                mContentView.post(() -> {
                    try {
                        BottomSheet.super.dismiss();
                    } catch (Exception e) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mContentView.startAnimation(set);
    }

    @Override
    public void show() {
        super.show();
        animateUp();
        if (mOnBottomSheetShowListener != null) {
            mOnBottomSheetShowListener.onShow();
        }
    }

    @Override
    public void dismiss() {
        if (mIsAnimating) {
            return;
        }
        animateDown();
    }

    public void dismissNoAnim() {
        dismiss();
    }


    public interface OnBottomSheetShowListener {
        void onShow();
    }

    protected int getContentViewLayout() {
        return R.layout.bottom_listview;
    }

    protected boolean isOnlyListView() {
        return true;
    }

    public static class BottomListSheetBuilder {

        private Context mContext;
        private BottomSheet mDialog;
        private BaseAdapter adapter;
        private OnDismissListener onDismissListener;

        public BottomListSheetBuilder(Context mContext) {
            this.mContext = mContext;
        }

        public BottomListSheetBuilder setAdapter(BaseAdapter adapter) {
            this.adapter = adapter;
            return this;
        }

        public BottomListSheetBuilder setOnDismissListener(OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
            return this;
        }

        public BottomSheet build() {

            mDialog = new BottomSheet(mContext) {
                @Override
                public void dismiss() {
                    super.dismiss();
                    BottomListSheetBuilder.this.dismiss();
                }
            };
            if (null != adapter) {
                View contentView = buildViews();
                mDialog.setContentView(contentView);
                if (null != onDismissListener) {
                    mDialog.setOnDismissListener(onDismissListener);
                }
            }
            return mDialog;
        }

        public void dismiss() {

        }

        private boolean needToScroll() {
            int itemHeight = AndroidTools.dip2px(mContext, 56);
            int totalHeight = adapter.getCount() * itemHeight;
            return totalHeight > getListMaxHeight();
        }

        private int getListMaxHeight() {
            int[] screenSize = AndroidTools.getScreenSize(mContext);
            return (int) (screenSize[1] * 0.5);
        }

        private View buildViews() {
            ListView listView = (ListView) LayoutInflater.from(mContext).inflate(mDialog.getContentViewLayout(), null);
            if (mDialog.isOnlyListView() && needToScroll() && listView.getLayoutParams() != null) {
                listView.getLayoutParams().height = getListMaxHeight();
            }
            listView.setAdapter(adapter);
            {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (mOnItemClickListener != null)
                            mOnItemClickListener.onItemClick(i, mDialog);
                        if (mOnItemClickedListener != null)
                            mOnItemClickedListener.onItemClick(i, adapter.getItem(i), mDialog);
                    }
                });
            }
            if (mSelector != 0)
                listView.setSelector(mSelector);
            return listView;
        }

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            mOnItemClickListener = listener;
        }

        private OnItemClickedListener mOnItemClickedListener;

        public void setOnItemClickedListener(OnItemClickedListener listener) {
            mOnItemClickedListener = listener;
        }

        private int mSelector;

        public void setSelector(int resId) {
            mSelector = resId;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Dialog dialog);
    }

    public interface OnItemClickedListener {
        void onItemClick(int position, Object object, Dialog dialog);
    }
}
