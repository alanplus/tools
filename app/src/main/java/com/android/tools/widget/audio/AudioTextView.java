package com.android.tools.widget.audio;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Mouse on 2018/10/23.
 */
public class AudioTextView extends android.support.v7.widget.AppCompatTextView implements IAudioView {

    private AudioViewHelper audioViewHelper;

    public AudioTextView(Context context) {
        this(context, null);
    }

    public AudioTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        audioViewHelper = new AudioViewHelper();
        audioViewHelper.init(getContext(), attributeSet, this);
    }

    @Override
    public boolean isPlaying() {
        return audioViewHelper.isPlaying();
    }

    @Override
    public void setClickAndPlayingState(boolean pauseOnClickAndPlaying) {
        audioViewHelper.setClickAndPlayingState(pauseOnClickAndPlaying);
    }

    @Override
    public void setAnimType(int animType) {
        audioViewHelper.setAnimType(animType);
    }

    @Override
    public void setAnimBackground(int resourceId) {
        audioViewHelper.setAnimBackground(resourceId);
    }

    @Override
    public void setDefaultBackground(int resourceId) {
        audioViewHelper.setDefaultBackground(resourceId);
    }

    @Override
    public void setAudioType(int type) {
        audioViewHelper.setAudioType(type);
    }

    @Override
    public void setLoadingAnimationDrawabe(int res) {
        audioViewHelper.setLoadingAnimationDrawabe(res);
    }

    @Override
    public void setAudio(String audio) {
        audioViewHelper.setAudio(audio);
    }

    @Override
    public void play(String name) {
        audioViewHelper.play(name);
    }

    @Override
    public void play() {
        audioViewHelper.play();
    }

    @Override
    public void stop() {
        audioViewHelper.stop();
    }

    @Override
    public void setEnable(boolean enable) {
        audioViewHelper.setEnable(enable);
    }

    @Override
    public void onClickEvent(View view) {
        audioViewHelper.onClickEvent(view);
    }

    @Override
    public void setOnAudioViewClickListener(OnClickListener onClickListener) {
        audioViewHelper.setOnAudioViewClickListener(onClickListener);
    }

    @Override
    public void setOnStateChangedListener(OnStateChangeListener onStateChangedListener) {
        audioViewHelper.setOnStateChangedListener(onStateChangedListener);
    }

    @Override
    public boolean isAudioEnable() {
        return audioViewHelper.isAudioEnable();
    }

    public interface OnStateChangeListener {
        void prepare(View v);

        void start(View v);

        void complete(View v);

        void error(View v);
    }
}
