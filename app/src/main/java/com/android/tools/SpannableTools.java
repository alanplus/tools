package com.android.tools;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;


/**
 * Created by Mouse on 2018/1/27.
 */

public class SpannableTools {

    public static void setSpannableTextColor(SpannableString string, int start, int len, int color) {
        string.setSpan(new ForegroundColorSpan(color), start, start + len, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    public static void setSpannableTextSize(SpannableString string, int start, int len, int size) {
        string.setSpan(new AbsoluteSizeSpan(size,true), start, start + len, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    public static void setSpannableTextFont(SpannableString string, int start, int len, Typeface typeface){
        string.setSpan(new MyTypefaceSpan(typeface),start,start+len,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    public static void setSpannableTextBold(SpannableString string, int start, int len){
        string.setSpan(new StyleSpan(Typeface.BOLD),start,start+len,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    public static void setSpannableTextItalic(SpannableString string, int start, int len){
        string.setSpan(new StyleSpan(Typeface.ITALIC),start,start+len,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    public static void setSpannableTextItalicAndBold(SpannableString string, int start, int len){
        string.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),start,start+len,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }
}
