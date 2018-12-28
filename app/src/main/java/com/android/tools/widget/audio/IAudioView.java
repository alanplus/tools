package com.android.tools.widget.audio;

import android.support.annotation.AnimRes;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.android.tools.media.WeiciMediaplayer;

/**
 * Created by Mouse on 2018/10/24.
 */
public interface IAudioView {

    boolean isPlaying();

    void setClickAndPlayingState(boolean pauseOnClickAndPlaying);

    void setAnimType(int animType);

    void setAnimBackground(@AnimRes int resourceId);

    void setDefaultBackground(@DrawableRes int resourceId);

    void setAudioType(@WeiciMediaplayer.AudioFileType int type);

    void setLoadingAnimationDrawabe(int res);

    void setAudio(String audio);

    void play(String name);

    void play();

    void stop();

    void setEnable(boolean enable);

    void onClickEvent(View view);

    void setOnAudioViewClickListener(View.OnClickListener onClickListener);

    void setOnStateChangedListener(AudioTextView.OnStateChangeListener onStateChangedListener);

    boolean isAudioEnable();
}
