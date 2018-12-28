package com.android.tools.widget.softinput;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.android.tools.media.WeiciMediaPlayerHelper;
import com.android.tools.media.WeiciMediaplayer;
import com.android.tools.widget.softinput.listener.OnKeyBoradListener;
import com.android.tools.widget.softinput.listener.OnKeyClickListener;
import com.android.tools.widget.softinput.style.KeyBoardStyle;


/**
 * Created by Mouse on 2018/5/14.
 */

public class Keyboard extends FrameLayout implements OnKeyBoradListener {


    public static final int KEY_CODE_A_LOWER = 1;
    public static final int KEY_CODE_B_LOWER = 2;
    public static final int KEY_CODE_C_LOWER = 3;
    public static final int KEY_CODE_D_LOWER = 4;
    public static final int KEY_CODE_E_LOWER = 5;
    public static final int KEY_CODE_F_LOWER = 6;
    public static final int KEY_CODE_G_LOWER = 7;
    public static final int KEY_CODE_H_LOWER = 8;
    public static final int KEY_CODE_I_LOWER = 9;
    public static final int KEY_CODE_J_LOWER = 10;
    public static final int KEY_CODE_K_LOWER = 11;
    public static final int KEY_CODE_L_LOWER = 12;
    public static final int KEY_CODE_M_LOWER = 13;
    public static final int KEY_CODE_N_LOWER = 14;
    public static final int KEY_CODE_O_LOWER = 15;
    public static final int KEY_CODE_P_LOWER = 16;
    public static final int KEY_CODE_Q_LOWER = 17;
    public static final int KEY_CODE_R_LOWER = 18;
    public static final int KEY_CODE_S_LOWER = 19;
    public static final int KEY_CODE_T_LOWER = 20;
    public static final int KEY_CODE_U_LOWER = 21;
    public static final int KEY_CODE_V_LOWER = 22;
    public static final int KEY_CODE_W_LOWER = 23;
    public static final int KEY_CODE_X_LOWER = 24;
    public static final int KEY_CODE_Y_LOWER = 25;
    public static final int KEY_CODE_Z_LOWER = 26;
    public static final int KEY_CODE_A = 27;
    public static final int KEY_CODE_B = 28;
    public static final int KEY_CODE_C = 29;
    public static final int KEY_CODE_D = 30;
    public static final int KEY_CODE_E = 31;
    public static final int KEY_CODE_F = 32;
    public static final int KEY_CODE_G = 33;
    public static final int KEY_CODE_H = 34;
    public static final int KEY_CODE_I = 35;
    public static final int KEY_CODE_J = 36;
    public static final int KEY_CODE_K = 37;
    public static final int KEY_CODE_L = 38;
    public static final int KEY_CODE_M = 39;
    public static final int KEY_CODE_N = 40;
    public static final int KEY_CODE_O = 41;
    public static final int KEY_CODE_P = 42;
    public static final int KEY_CODE_Q = 43;
    public static final int KEY_CODE_R = 44;
    public static final int KEY_CODE_S = 45;
    public static final int KEY_CODE_T = 46;
    public static final int KEY_CODE_U = 47;
    public static final int KEY_CODE_V = 48;
    public static final int KEY_CODE_W = 49;
    public static final int KEY_CODE_X = 50;
    public static final int KEY_CODE_Y = 51;
    public static final int KEY_CODE_Z = 52;

    public static final int KEY_CODE_SHIFT = 53;
    public static final int KEY_CODE_DEL = 54;
    public static final int KEY_CODE_TO_NUM = 55;
    public static final int KEY_CODE_SPACE = 56;
    public static final int KEY_CODE_TO_CHAR = 57;
    public static final int KEY_CODE_CONFIRM = 58;
    public static final int KEY_CODE_PRE = 59;


    public static final int KEY_CODE_CHAR1 = 101;
    public static final int KEY_CODE_CHAR2 = 102;
    public static final int KEY_CODE_CHAR3 = 103;
    public static final int KEY_CODE_CHAR4 = 104;
    public static final int KEY_CODE_CHAR5 = 105;
    public static final int KEY_CODE_CHAR6 = 106;
    public static final int KEY_CODE_CHAR7 = 107;
    public static final int KEY_CODE_CHAR8 = 108;
    public static final int KEY_CODE_CHAR9 = 109;
    public static final int KEY_CODE_CHAR10 = 110;
    public static final int KEY_CODE_CHAR11 = 111;
    public static final int KEY_CODE_CHAR12 = 112;
    public static final int KEY_CODE_CHAR13 = 113;
    public static final int KEY_CODE_CHAR14 = 114;
    public static final int KEY_CODE_CHAR15 = 115;
    public static final int KEY_CODE_CHAR16 = 116;
    public static final int KEY_CODE_CHAR17 = 117;
    public static final int KEY_CODE_CHAR18 = 118;
    public static final int KEY_CODE_CHAR19 = 119;
    public static final int KEY_CODE_CHAR20 = 120;
    public static final int KEY_CODE_CHAR21 = 121;
    public static final int KEY_CODE_CHAR22 = 122;
    public static final int KEY_CODE_CHAR23 = 123;
    public static final int KEY_CODE_CHAR24 = 124;
    public static final int KEY_CODE_CHAR25 = 125;
    public static final int KEY_CODE_CHAR26 = 126;
    public static final int KEY_CODE_CHAR27 = 127;
    public static final int KEY_CODE_CHAR28 = 128;
    public static final int KEY_CODE_CHAR29 = 129;
    public static final int KEY_CODE_CHAR30 = 130;
    public static final int KEY_CODE_CHAR31 = 131;
    public static final int KEY_CODE_CHAR32 = 132;
    public static final int KEY_CODE_CHAR_TO_NUM = 133;
    public static final int KEY_CODE_CHAR_TO_ABC = 134;
    public static final int KEY_CODE_CHAR_BACK = 135;
    public static final int KEY_CODE_CHAR_CONFIRM = 136;


    public static final int KEY_CODE_NUM1 = 201;
    public static final int KEY_CODE_NUM2 = 202;
    public static final int KEY_CODE_NUM3 = 203;
    public static final int KEY_CODE_NUM4 = 204;
    public static final int KEY_CODE_NUM5 = 205;
    public static final int KEY_CODE_NUM6 = 206;
    public static final int KEY_CODE_NUM7 = 207;
    public static final int KEY_CODE_NUM8 = 208;
    public static final int KEY_CODE_NUM9 = 209;
    public static final int KEY_CODE_NUM_TO_CHAR = 210;
    public static final int KEY_CODE_NUM_TO_ABC = 211;
    public static final int KEY_CODE_NUM_BACK = 212;
    public static final int KEY_CODE_NUM0 = 213;
    public static final int KEY_CODE_NUM_CONFIRM = 214;

    private OnKeyClickListener onWeiciKeyClickListener;

    private boolean enableConfirmButton;
    private String confirmText;

    private KeyBoradCommonCard keyboard1;
    private KeyBoardCharCard keyboard2;
    private KeyBoardNumCard keyboard3;

    private KeyBoardStyle weiciBaseKeyBoardStyle;


    public Keyboard(Context context) {
        super(context);
        init();
    }

    public Keyboard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        keyboard1 = new KeyBoradCommonCard(getContext());
        keyboard1.setOnWeiciKeyBoradListener(this);
        addView(keyboard1);
        enableConfirmButton = true;
        confirmText = "确定";
    }

    public void setOnWeiciKeyClickListener(OnKeyClickListener onWeiciKeyClickListener) {
        this.onWeiciKeyClickListener = onWeiciKeyClickListener;
    }

    public void setEnableKeyConfirm() {


    }

    private void showKeyBoardChar() {
        removeAllViews();
        KeyBoardCharCard keyBoardCharCard = getKeyBoardCharCard();
        addView(keyBoardCharCard);
    }

    private void showKeyBoardABC() {
        removeAllViews();
        KeyBoradCommonCard keyBoardCharCard = getKeyBoardABCCard();
        addView(keyBoardCharCard);
    }

    private void showKeyBoardNum() {
        removeAllViews();
        KeyBoardNumCard keyBoardNumCard = getKeyBoardNumCard();
        addView(keyBoardNumCard);
    }

    @Override
    public void onInputKeyClickListener(BaseKeyBoardCard baseKeyBoardCard, int keyCode, String text) {
        WeiciMediaPlayerHelper.getInstance(getContext(), 0).play("audio_key_m.mp3", WeiciMediaplayer.AUDIO_FILE_TYPE_ASSETS);
        if (keyCode == KEY_CODE_SHIFT) {
            onWeiciKeyClickListener.onInputKeyClickListener(keyCode, "");
        } else if (keyCode == KEY_CODE_TO_NUM || keyCode == KEY_CODE_CHAR_TO_NUM) {
            showKeyBoardNum();
            onWeiciKeyClickListener.onInputKeyClickListener(keyCode, "");
        } else if (keyCode == KEY_CODE_TO_CHAR || keyCode == KEY_CODE_NUM_TO_CHAR) {
            showKeyBoardChar();
            onWeiciKeyClickListener.onInputKeyClickListener(keyCode, "");
        } else if (keyCode == KEY_CODE_CONFIRM || keyCode == KEY_CODE_NUM_CONFIRM || keyCode == KEY_CODE_CHAR_CONFIRM) {
            onWeiciKeyClickListener.onInputKeyClickListener(KEY_CODE_CONFIRM, "");
        } else if (keyCode == KEY_CODE_CHAR_TO_ABC || keyCode == KEY_CODE_NUM_TO_ABC) {
            showKeyBoardABC();
            onWeiciKeyClickListener.onInputKeyClickListener(keyCode, "");
        } else if (keyCode == KEY_CODE_CHAR_BACK || keyCode == KEY_CODE_DEL || keyCode == KEY_CODE_NUM_BACK) {
            onWeiciKeyClickListener.onInputKeyClickListener(KEY_CODE_DEL, text);
        } else if (keyCode == KEY_CODE_PRE) {
            onWeiciKeyClickListener.onInputKeyClickListener(KEY_CODE_PRE, "");
        } else {
            if (null != onWeiciKeyClickListener) {
                onWeiciKeyClickListener.onInputKeyClickListener(keyCode, text);
            }
        }
    }

    public void reset() {
        int childCount = getChildCount();
        if (childCount > 0) {
            View view = getChildAt(0);
            if (view != keyboard1) {
                showKeyBoardABC();
            }
        }
    }

    private KeyBoardCharCard getKeyBoardCharCard() {
        if (keyboard2 == null) {
            keyboard2 = new KeyBoardCharCard(getContext());
            keyboard2.setOnWeiciKeyBoradListener(this);
            keyboard2.setConfirmText(confirmText);
            if (null != weiciBaseKeyBoardStyle) {
                keyboard2.setKeyBoardStyle(weiciBaseKeyBoardStyle);
            }
            keyboard2.setConfirmEnable(enableConfirmButton);
        }
        return keyboard2;
    }

    private KeyBoradCommonCard getKeyBoardABCCard() {
        if (keyboard1 == null) {
            keyboard1 = new KeyBoradCommonCard(getContext());
            keyboard1.setOnWeiciKeyBoradListener(this);

            keyboard1.setConfirmText(confirmText);
            if (null != weiciBaseKeyBoardStyle) {
                keyboard1.setKeyBoardStyle(weiciBaseKeyBoardStyle);
            }
            keyboard1.setConfirmEnable(enableConfirmButton);
        }
        return keyboard1;
    }

    private KeyBoardNumCard getKeyBoardNumCard() {
        if (keyboard3 == null) {
            keyboard3 = new KeyBoardNumCard(getContext());
            keyboard3.setOnWeiciKeyBoradListener(this);
            keyboard3.setConfirmText(confirmText);
            if (null != weiciBaseKeyBoardStyle) {
                keyboard3.setKeyBoardStyle(weiciBaseKeyBoardStyle);
            }
            keyboard3.setConfirmEnable(enableConfirmButton);
        }
        return keyboard3;
    }

    public void setConfirmButtonEnable(boolean enable) {
        this.enableConfirmButton = enable;
        if (keyboard1 != null) {
            keyboard1.setConfirmEnable(enable);
        }
        if (keyboard2 != null) {
            keyboard2.setConfirmEnable(enable);
        }
        if (keyboard3 != null) {
            keyboard3.setConfirmEnable(enable);
        }
    }

    public void setConfirmButtonText(String text) {
        this.confirmText = text;
        if (keyboard1 != null) {
            keyboard1.setConfirmText(text);
        }
        if (keyboard2 != null) {
            keyboard2.setConfirmText(text);
        }
        if (keyboard3 != null) {
            keyboard3.setConfirmText(text);
        }
    }

    public void setWeiciKeyboardStyle(KeyBoardStyle weiciKeyboardStyle) {
        this.weiciBaseKeyBoardStyle = weiciKeyboardStyle;
        setBackgroundColor(weiciKeyboardStyle.getBackgroundRes());
        if (keyboard1 != null) {
            keyboard1.setKeyBoardStyle(weiciKeyboardStyle);
        }

        if (keyboard2 != null) {
            keyboard2.setKeyBoardStyle(weiciKeyboardStyle);
        }

        if (keyboard3 != null) {
            keyboard3.setKeyBoardStyle(weiciKeyboardStyle);
        }
    }


    public void setPreButton(String text, boolean isShow) {
        keyboard1.setPreButtonText(text, isShow);
    }

    public void setPreButtonEnable(boolean enable) {
        keyboard1.setPreButtonEnable(enable);
    }
}
