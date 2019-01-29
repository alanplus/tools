package com.android.tools.media.download.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.android.tools.media.download.IDownloadConfig;

/**
 * Created by Mouse on 2019/1/29.
 */
public class AudioDownloadView extends android.support.v7.widget.AppCompatTextView implements IAudioDownloadView {

    private AudioDownloadViewHelper audioDownloadViewHelper;

    public AudioDownloadView(Context context) {
        this(context, null);
    }

    public AudioDownloadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(getContext(), this, attrs);
    }

    @Override
    public void init(Context context, View view, AttributeSet attributeSet) {
        audioDownloadViewHelper = new AudioDownloadViewHelper();
        audioDownloadViewHelper.init(context, view, attributeSet);
    }

    @Override
    public void onClickEvent() {
        audioDownloadViewHelper.onClickEvent();
    }

    @Override
    public void setAudioDownloadConfig(IDownloadConfig iDownloadConfig) {
        audioDownloadViewHelper.setAudioDownloadConfig(iDownloadConfig);
    }
}
