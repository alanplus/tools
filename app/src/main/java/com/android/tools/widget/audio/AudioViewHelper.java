package com.android.tools.widget.audio;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.android.tools.R;
import com.android.tools.media.WeiciMediaPlayerHelper;

/**
 * Created by Mouse on 2018/10/24.
 */
public class AudioViewHelper implements IAudioView {

    private String audio;
    private boolean pauseOnClickAndPlaying;
    private View view;
    private boolean enable = true;
    private AudioTextView.OnStateChangeListener onStateChangeListener;

    public void init(Context context, AttributeSet attributeSet, View view) {
        pauseOnClickAndPlaying = true;
        this.view = view;
        if (attributeSet == null) return;
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.AudioViewStyle);
        int type = typedArray.getInt(R.styleable.AudioViewStyle_audio_type, 0);
        view.setTag(R.id.tag_audio_type, type);
        int resourceId = typedArray.getResourceId(R.styleable.AudioViewStyle_default_backgroud, 0);
        view.setTag(R.id.tag_audio_default_bg, resourceId);
        if (resourceId != 0) view.setBackgroundResource(resourceId);
        int animRes = typedArray.getResourceId(R.styleable.AudioViewStyle_anim, 0);
        view.setTag(R.id.tag_audio_anim, animRes);
        int animType = typedArray.getInt(R.styleable.AudioViewStyle_anim_type, 0);
        view.setTag(R.id.tag_anim_type, animType);
        audio = typedArray.getString(R.styleable.AudioViewStyle_audio);

        int loadDrawable = typedArray.getResourceId(R.styleable.AudioViewStyle_loading_drawable, 0);
        if (loadDrawable != 0) {
            view.setTag(R.id.tag_loading, loadDrawable);
        }

        int animBg = typedArray.getResourceId(R.styleable.AudioViewStyle_anim_backgroud, 0);
        if (animBg != 0) view.setTag(R.id.tag_audio_anim_bg, animBg);
        view.setOnClickListener(v -> {
            onClickEvent(v);
        });
    }

    @Override
    public boolean isPlaying() {
        return WeiciMediaPlayerHelper.getInstance(view.getContext()).isPlaying(view);
    }

    @Override
    public void setClickAndPlayingState(boolean pauseOnClickAndPlaying) {
        this.pauseOnClickAndPlaying = pauseOnClickAndPlaying;
    }

    /**
     * type 0 默认 播放帧动画 1 背景做透明度变化
     *
     * @param animType
     */
    @Override
    public void setAnimType(int animType) {
        view.setTag(R.id.tag_anim_type, animType);
    }

    @Override
    public void setAnimBackground(int resourceId) {
        view.setTag(R.id.tag_audio_anim_bg, resourceId);
    }

    @Override
    public void setDefaultBackground(int resourceId) {
        view.setBackgroundResource(resourceId);
        view.setTag(R.id.tag_audio_default_bg, resourceId);
    }

    @Override
    public void setAudioType(int type) {
        view.setTag(R.id.tag_audio_type, type);
    }

    @Override
    public void setLoadingAnimationDrawabe(int res) {
        if (res == 0) {
            view.setTag(R.id.tag_loading, null);
            return;
        }
        view.setTag(R.id.tag_loading, res);
    }

    @Override
    public void setAudio(String audio) {
        this.audio = audio;
    }

    @Override
    public void play(String name) {
        this.audio = name;
        play();
    }

    @Override
    public void play() {
        if(WeiciMediaPlayerHelper.getInstance(view.getContext()).isPlaying()){
            WeiciMediaPlayerHelper.getInstance(view.getContext()).stop();
        }
        view.setTag(R.id.tag_audio_path, audio);
        WeiciMediaPlayerHelper.getInstance(view.getContext()).play(view, onStateChangeListener);
    }

    @Override
    public void stop() {
        WeiciMediaPlayerHelper.getInstance(view.getContext()).stop(view);
    }

    @Override
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public void onClickEvent(View v) {
        WeiciMediaPlayerHelper weiciMediaPlayerHelper = WeiciMediaPlayerHelper.getInstance(v.getContext());
        if (weiciMediaPlayerHelper.isPlaying(view) && pauseOnClickAndPlaying) {
            weiciMediaPlayerHelper.stop(view);
        } else {
            if (!enable) return;
            play();
        }
    }

    @Override
    public void setOnAudioViewClickListener(View.OnClickListener onClickListener) {
        view.setOnClickListener(onClickListener);
    }

    @Override
    public void setOnStateChangedListener(AudioTextView.OnStateChangeListener onStateChangedListener) {
        this.onStateChangeListener = onStateChangedListener;
    }

    @Override
    public boolean isAudioEnable() {
        return enable;
    }

}
