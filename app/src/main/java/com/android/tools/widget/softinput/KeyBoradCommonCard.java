package com.android.tools.widget.softinput;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.tools.R;


/**
 * Created by Mouse on 2018/5/18.
 */

public class KeyBoradCommonCard extends BaseKeyBoardCard implements View.OnClickListener {


    private boolean isShift;

    private View[] keys;

    private final static int[] ids = new int[]{R.id.key_A, R.id.key_B, R.id.key_C, R.id.key_D, R.id.key_E, R.id.key_F, R.id.key_G, R.id.key_H,
            R.id.key_I, R.id.key_J, R.id.key_K, R.id.key_L, R.id.key_M, R.id.key_N, R.id.key_O, R.id.key_P, R.id.key_Q,
            R.id.key_R, R.id.key_S, R.id.key_T, R.id.key_U, R.id.key_V, R.id.key_W, R.id.key_X, R.id.key_Y, R.id.key_Z,
            R.id.key_shift, R.id.key_back, R.id.key_to_num, R.id.key_to_space, R.id.key_to_char, R.id.key_to_pre, R.id.key_to_confirm};

    private final static String[] strings = new String[]{
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "", "", "123", "空格", "符", "上一题", "确定"};


    public KeyBoradCommonCard(Context context) {
        super(context);
    }

    public KeyBoradCommonCard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView() {
        super.initView();
        LayoutInflater.from(getContext()).inflate(R.layout.view_weici_keyboard, this);
        int len = ids.length;
        keys = new View[len];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = findViewById(ids[i]);
            keys[i].setOnClickListener(this);
            String s = strings[i];
            if (!TextUtils.isEmpty(s)) {
                s = isShift ? s.toUpperCase() : s.toLowerCase();
            }
            if (keys[i] instanceof TextView) {
                TextView button = (TextView) keys[i];
                button.setText(s);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (null == onWeiciKeyBoradListener) {
            return;
        }
        if (id == R.id.key_A) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_A : Keyboard.KEY_CODE_A_LOWER, isShift ? "A" : "a");
        } else if (id == R.id.key_B) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_B : Keyboard.KEY_CODE_B_LOWER, isShift ? "B" : "b");
        } else if (id == R.id.key_C) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_C : Keyboard.KEY_CODE_C_LOWER, isShift ? "C" : "c");
        } else if (id == R.id.key_D) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_D : Keyboard.KEY_CODE_D_LOWER, isShift ? "D" : "d");
        } else if (id == R.id.key_E) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_E : Keyboard.KEY_CODE_E_LOWER, isShift ? "E" : "e");
        } else if (id == R.id.key_F) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_F : Keyboard.KEY_CODE_F_LOWER, isShift ? "F" : "f");
        } else if (id == R.id.key_G) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_G : Keyboard.KEY_CODE_G_LOWER, isShift ? "G" : "g");
        } else if (id == R.id.key_H) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_H : Keyboard.KEY_CODE_H_LOWER, isShift ? "H" : "h");
        } else if (id == R.id.key_I) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_I : Keyboard.KEY_CODE_I_LOWER, isShift ? "I" : "i");
        } else if (id == R.id.key_J) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_J : Keyboard.KEY_CODE_J_LOWER, isShift ? "J" : "j");
        } else if (id == R.id.key_K) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_K : Keyboard.KEY_CODE_K_LOWER, isShift ? "K" : "k");
        } else if (id == R.id.key_L) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_L : Keyboard.KEY_CODE_L_LOWER, isShift ? "L" : "l");
        } else if (id == R.id.key_M) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_M : Keyboard.KEY_CODE_M_LOWER, isShift ? "M" : "m");
        } else if (id == R.id.key_N) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_N : Keyboard.KEY_CODE_N_LOWER, isShift ? "N" : "n");
        } else if (id == R.id.key_O) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_O : Keyboard.KEY_CODE_O_LOWER, isShift ? "O" : "o");
        } else if (id == R.id.key_P) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_P : Keyboard.KEY_CODE_P_LOWER, isShift ? "P" : "p");
        } else if (id == R.id.key_Q) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_Q : Keyboard.KEY_CODE_Q_LOWER, isShift ? "Q" : "q");
        } else if (id == R.id.key_R) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_R : Keyboard.KEY_CODE_R_LOWER, isShift ? "R" : "r");
        } else if (id == R.id.key_S) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_S : Keyboard.KEY_CODE_S_LOWER, isShift ? "S" : "s");
        } else if (id == R.id.key_T) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_T : Keyboard.KEY_CODE_T_LOWER, isShift ? "T" : "t");
        } else if (id == R.id.key_U) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_U : Keyboard.KEY_CODE_U_LOWER, isShift ? "U" : "u");
        } else if (id == R.id.key_V) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_V : Keyboard.KEY_CODE_V_LOWER, isShift ? "V" : "v");
        } else if (id == R.id.key_W) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_W : Keyboard.KEY_CODE_W_LOWER, isShift ? "W" : "w");
        } else if (id == R.id.key_X) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_X : Keyboard.KEY_CODE_X_LOWER, isShift ? "X" : "x");
        } else if (id == R.id.key_Y) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_Y : Keyboard.KEY_CODE_Y_LOWER, isShift ? "Y" : "y");
        } else if (id == R.id.key_Z) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, isShift ? Keyboard.KEY_CODE_Z : Keyboard.KEY_CODE_Z_LOWER, isShift ? "Z" : "z");
        } else if (id == R.id.key_shift) {
            onClickShitEvent();
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_SHIFT, "");
        } else if (id == R.id.key_back) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_DEL, "");
        } else if (id == R.id.key_to_num) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_TO_NUM, "");
        } else if (id == R.id.key_to_space) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_SPACE, " ");
        } else if (id == R.id.key_to_char) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_TO_CHAR, "");
        } else if (id == R.id.key_to_confirm) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CONFIRM, "");
        }else if(id==R.id.key_to_pre){
            onWeiciKeyBoradListener.onInputKeyClickListener(this,Keyboard.KEY_CODE_PRE,"");
        }
    }


    private void onClickShitEvent() {
        isShift = !isShift;
        for (int i = 0; i < keys.length; i++) {
            String s = strings[i];
            if (!TextUtils.isEmpty(s)) {
                s = isShift ? s.toUpperCase() : s.toLowerCase();
            }
            if (keys[i] instanceof TextView) {
                TextView button = (TextView) keys[i];
                button.setText(s);
            }
        }
    }

    @Override
    public TextView getConfirmButton() {
        return (TextView) keys[keys.length - 1];
    }

    @Override
    public void onWeiciKeyBoradStyleChanged() {
        super.onWeiciKeyBoradStyleChanged();
        if (null != keys && keyBoardStyle != null) {
            int itemBackground = keyBoardStyle.getItemBackground();
            for (View view : keys) {
                view.setBackgroundResource(itemBackground);
                if (view instanceof TextView) {
                    if (view.getId() == R.id.key_to_confirm) {
                        ((TextView) view).setTextColor(keyBoardStyle.getItemConfirmTextColor());
                    } else {
                        ((TextView) view).setTextColor(keyBoardStyle.getItemTextColor());
                    }

                } else if (view.getId() == R.id.key_back) {
                    ImageView imageView = (ImageView) ((RelativeLayout) view).getChildAt(0);
                    imageView.setImageResource(keyBoardStyle.getItemBackIcon());
                } else if (view.getId() == R.id.key_shift) {
                    ImageView imageView = (ImageView) ((RelativeLayout) view).getChildAt(0);
                    imageView.setImageResource(keyBoardStyle.getItemShiftIcon());
                }
            }
        }
    }

    public void setPreButtonText(String text, boolean isShow) {
        TextView textView = (TextView) keys[keys.length - 2];
        TextView spaceView = findViewById(R.id.key_to_space);
        LinearLayout.LayoutParams layoutParams = (LayoutParams) spaceView.getLayoutParams();
        textView.setText(text);
        if (isShow) {
            textView.setVisibility(View.VISIBLE);
            layoutParams.weight = 2;
        } else {
            textView.setVisibility(View.GONE);
            layoutParams.weight = 4;
        }
    }

    public void setPreButtonEnable(boolean enable) {
        TextView textView = (TextView) keys[keys.length - 2];
        textView.setEnabled(enable);
        textView.setBackgroundResource(enable ? R.drawable.bg_key_1_style2 : R.drawable.bg_key_gray);
    }
}
