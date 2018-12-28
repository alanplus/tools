package com.android.tools.widget.softinput;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.tools.R;


/**
 * Created by Mouse on 2018/5/18.
 */

public class KeyBoardCharCard extends BaseKeyBoardCard implements View.OnClickListener {

    private View[] keys;

    private static final int[] ids = new int[]{R.id.key_11, R.id.key_12, R.id.key_13, R.id.key_14,
            R.id.key_15, R.id.key_16, R.id.key_17, R.id.key_18, R.id.key_19, R.id.key_20, R.id.key_21, R.id.key_22,
            R.id.key_23, R.id.key_24, R.id.key_25, R.id.key_26, R.id.key_27, R.id.key_28, R.id.key_29, R.id.key_30,
            R.id.key_31, R.id.key_32, R.id.key_33, R.id.key_34, R.id.key_35, R.id.key_36, R.id.key_37, R.id.key_38,
            R.id.key_39, R.id.key_40, R.id.key_41, R.id.key_42, R.id.key_43, R.id.key_44, R.id.key_45, R.id.key_46};

    private static final String[] strings = new String[]{"~", "`", "!", "@", "#", "$", "%", "^", "&", "*",
            "(", ")", "_", "-", "+", "=", "{", "}", "[", "]", "|",
            "\\", ":", ";", "\"", "'", "<", ",", ">", ".", "?", "/", "123", "ABC", "", "确定"};

    public KeyBoardCharCard(Context context) {
        super(context);
    }

    public KeyBoardCharCard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView() {
        super.initView();
        LayoutInflater.from(getContext()).inflate(R.layout.view_weici_keyboard_char, this);
        int len = ids.length;
        keys = new View[len];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = findViewById(ids[i]);
            keys[i].setOnClickListener(this);
            String s = strings[i];
            if (keys[i] instanceof TextView) {
                TextView button = (TextView) keys[i];
                button.setText(s);
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (null == onWeiciKeyBoradListener) {
            return;
        }
        int id = v.getId();

        if (id == R.id.key_11) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR1, "~");
        } else if (id == R.id.key_12) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR2, "`");
        } else if (id == R.id.key_13) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR3, "!");
        } else if (id == R.id.key_14) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR4, "@");
        } else if (id == R.id.key_15) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR5, "#");
        } else if (id == R.id.key_16) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR6, "$");
        } else if (id == R.id.key_17) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR7, "%");
        } else if (id == R.id.key_18) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR8, "^");
        } else if (id == R.id.key_19) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR9, "&");
        } else if (id == R.id.key_20) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR10, "*");
        } else if (id == R.id.key_21) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR11, "(");
        } else if (id == R.id.key_22) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR12, ")");
        } else if (id == R.id.key_23) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR13, "_");
        } else if (id == R.id.key_24) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR14, "-");
        } else if (id == R.id.key_25) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR15, "+");
        } else if (id == R.id.key_26) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR16, "+");
        } else if (id == R.id.key_27) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR17, "{");
        } else if (id == R.id.key_28) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR18, "}");
        } else if (id == R.id.key_29) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR19, "[");
        } else if (id == R.id.key_30) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR20, "]");
        } else if (id == R.id.key_31) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR21, "|");
        } else if (id == R.id.key_32) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR22, "\\");
        } else if (id == R.id.key_33) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR23, ":");
        } else if (id == R.id.key_34) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR24, ";");
        } else if (id == R.id.key_35) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR25, "\"");
        } else if (id == R.id.key_36) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR26, "'");
        } else if (id == R.id.key_37) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR27, "<");
        }else if (id == R.id.key_38) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR28, ",");
        }else if (id == R.id.key_39) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR29, ">");
        }else if (id == R.id.key_40) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR30, ".");
        }else if (id == R.id.key_41) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR31, "?");
        }else if (id == R.id.key_42) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR32, "/");
        }else if (id == R.id.key_43) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR_TO_NUM, "123");
        }else if (id == R.id.key_44) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR_TO_ABC, "ABC");
        }else if (id == R.id.key_45) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR_BACK, "");
        }else if (id == R.id.key_46) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_CHAR_CONFIRM, "确定");
        }
    }

    @Override
    public TextView getConfirmButton() {
        return (TextView) keys[keys.length-1];
    }

    @Override
    public void onWeiciKeyBoradStyleChanged() {
        super.onWeiciKeyBoradStyleChanged();
        if(null!=keys&& keyBoardStyle !=null){
            int itemBackground = keyBoardStyle.getItemBackground();
            for(View view:keys){
                view.setBackgroundResource(itemBackground);
                if(view instanceof TextView){
                    if(view.getId()==R.id.key_46){
                        ((TextView)view).setTextColor(keyBoardStyle.getItemConfirmTextColor());
                    }else{
                        ((TextView)view).setTextColor(keyBoardStyle.getItemTextColor());
                    }

                }else if(view.getId()==R.id.key_45){
                    ImageView imageView = (ImageView) ((RelativeLayout)view).getChildAt(0);
                    imageView.setImageResource(keyBoardStyle.getItemBackIcon());
                }
            }
        }
    }
}
