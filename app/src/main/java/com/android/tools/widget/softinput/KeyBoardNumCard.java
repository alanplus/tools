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

public class KeyBoardNumCard extends BaseKeyBoardCard implements View.OnClickListener {

    private View[] keys;

    private static final int[] ids = new int[]{R.id.key_num1, R.id.key_num2, R.id.key_num3,
            R.id.key_num4, R.id.key_num5, R.id.key_num6, R.id.key_num7, R.id.key_num8, R.id.key_num9, R.id.key_num11, R.id.key_num12,
            R.id.key_num13, R.id.key_num14, R.id.key_num15};

    private static final String[] strings = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "符",
            "ABC", "0", "", "确定"};

    public KeyBoardNumCard(Context context) {
        super(context);
    }

    public KeyBoardNumCard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView() {
        super.initView();
        LayoutInflater.from(getContext()).inflate(R.layout.view_weici_keyboard_num, this);
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

        if (id == R.id.key_num1) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_NUM1, "1");
        } else if (id == R.id.key_num2) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_NUM2, "2");
        } else if (id == R.id.key_num3) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_NUM3, "3");
        } else if (id == R.id.key_num4) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_NUM4, "4");
        } else if (id == R.id.key_num5) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_NUM5, "5");
        } else if (id == R.id.key_num6) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_NUM6, "6");
        } else if (id == R.id.key_num7) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_NUM7, "7");
        } else if (id == R.id.key_num8) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_NUM8, "8");
        } else if (id == R.id.key_num9) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_NUM9, "9");
        } else if (id == R.id.key_num11) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_NUM_TO_CHAR, "");
        } else if (id == R.id.key_num12) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_NUM_TO_ABC, "");
        } else if (id == R.id.key_num13) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_NUM0, "0");
        } else if (id == R.id.key_num14) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_NUM_BACK, "");
        } else if (id == R.id.key_num15) {
            onWeiciKeyBoradListener.onInputKeyClickListener(this, Keyboard.KEY_CODE_NUM_CONFIRM, "");
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
                    if(view.getId()== R.id.key_num15){
                        ((TextView)view).setTextColor(keyBoardStyle.getItemConfirmTextColor());
                    }else{
                        ((TextView)view).setTextColor(keyBoardStyle.getItemTextColor());
                    }
                }else if(view.getId()==R.id.key_num14){
                    ImageView imageView = (ImageView) ((RelativeLayout)view).getChildAt(0);
                    imageView.setImageResource(keyBoardStyle.getItemBackIcon());
                }
            }
        }
    }
}
