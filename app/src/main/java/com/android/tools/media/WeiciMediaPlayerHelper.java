package com.android.tools.media;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.alan.common.Logger;
import com.android.tools.AndroidToolsConfig;
import com.android.tools.R;
import com.android.tools.widget.audio.AudioTextView;

/**
 * Created by Mouse on 2018/10/23.
 */
public class WeiciMediaPlayerHelper implements OnMediaPlayerListener {

    /**
     * 用于播放按键音
     */
    private static WeiciMediaPlayerHelper instance1;
    /**
     * 用于播放音频文件
     */
    private static WeiciMediaPlayerHelper instance2;


    private Context context;
    private WeiciMediaplayer weiciMediaplayer;

    private View imageView;
    private AudioTextView.OnStateChangeListener onStateChangeListener;

    private WeiciMediaPlayerHelper(Context context) {
        this.context = context.getApplicationContext();
        weiciMediaplayer = new WeiciMediaplayer(this.context);
    }

    public static WeiciMediaPlayerHelper getInstance(Context context) {
        if (null == instance2) {
            instance2 = new WeiciMediaPlayerHelper(context);
        }
        return instance2;
    }

    /**
     * 0 播放按键音 1 播放音频
     *
     * @param context
     * @param type
     * @return
     */
    public static WeiciMediaPlayerHelper getInstance(Context context, int type) {
        if (type == 0) {
            if (instance1 == null) {
                instance1 = new WeiciMediaPlayerHelper(context);
            }
            return instance1;
        }
        return getInstance(context);
    }


    public void play(String name, @WeiciMediaplayer.AudioFileType int type) {
        weiciMediaplayer.setOnMediaPlayerListener(null);
        weiciMediaplayer.play(name, type);
    }

    public void play(String name, @WeiciMediaplayer.AudioFileType int type, OnMediaPlayerListener onMediaPlayerListener) {
        weiciMediaplayer.setOnMediaPlayerListener(onMediaPlayerListener);
        weiciMediaplayer.play(name, type);
    }

    public void play(View imageView) {
        play(imageView, this);
    }

    public void play(View imageView, OnMediaPlayerListener onMediaPlayerListener) {
        resetImageView();
        if (weiciMediaplayer.isPlaying()) {
            weiciMediaplayer.stop();
            resetImageView();
        }
        this.imageView = imageView;
        Logger.d("play");
        int type = WeiciMediaplayer.AUDIO_FILE_TYPE_AUTO;
        Object tag = imageView.getTag(R.id.tag_audio_type);
        if (null != tag && tag instanceof Integer) {
            type = (int) tag;
        }
        String name;
        Object tag2 = imageView.getTag(R.id.tag_audio_path);
        if (tag2 == null || !(tag2 instanceof String)) return;
        name = (String) tag2;
        if (null != onStateChangeListener) {
            onStateChangeListener.prepare(imageView);
        }
        setLoadingAnimation();
        play(name, type, onMediaPlayerListener);
    }

    public void play(View imageView, AudioTextView.OnStateChangeListener onMediaPlayerListener) {
        this.onStateChangeListener = onMediaPlayerListener;
        play(imageView);
    }


    private void setLoadingAnimation() {
        Object tag = this.imageView.getTag(R.id.tag_loading);
        if (null == tag) return;
        int drawable = (int) tag;
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        this.imageView.setBackgroundResource(drawable);
        this.imageView.startAnimation(animation);
    }

    private void resetImageView() {
        if (onStateChangeListener != null) onStateChangeListener.complete(imageView);
        Logger.d("resetImageView");
        if (null == imageView) return;
        imageView.clearAnimation();
        imageView.setAlpha(1.0f);
        int bg = (int) this.imageView.getTag(R.id.tag_audio_default_bg);
        if (this.imageView instanceof ImageView) {
            ((ImageView) this.imageView).setImageResource(bg);
        } else {
            this.imageView.setBackgroundResource(bg);
        }
    }

    private void resetImageView(int hashcode) {
//        if (onStateChangeListener != null) onStateChangeListener.complete(imageView);
        if (null == imageView || hashcode != imageView.hashCode()) return;
        imageView.clearAnimation();
        imageView.setAlpha(1.0f);
        int bg = (int) this.imageView.getTag(R.id.tag_audio_default_bg);
        if (this.imageView instanceof ImageView) {
            ((ImageView) this.imageView).setImageResource(bg);
        } else {
            this.imageView.setBackgroundResource(bg);
        }
    }

    @Override
    public void onCompletionListener() {
        if (null != onStateChangeListener) onStateChangeListener.complete(imageView);
        if (null == imageView) {
            return;
        }
        final int i = imageView.hashCode();
        if (weiciMediaplayer.getState() == WeiciMediaplayer.STATE_IDEL) {
            AndroidToolsConfig.androidToolsConfig.getHandler().post(() -> {
                if (weiciMediaplayer.getState() == WeiciMediaplayer.STATE_IDEL) {
                    resetImageView(i);
                }
                Logger.d("----------onCompletionListener-----------");
            });
        }
    }

    @Override
    public void onError() {
        if (null != onStateChangeListener) onStateChangeListener.error(imageView);
        if (null == imageView) {
            return;
        }
        final int i = imageView.hashCode();
        if (weiciMediaplayer.getState() == WeiciMediaplayer.STATE_IDEL) {
            AndroidToolsConfig.androidToolsConfig.getHandler().post(() -> {
                if (weiciMediaplayer.getState() == WeiciMediaplayer.STATE_IDEL) {
                    resetImageView(i);
                }
                Logger.d("----------onCompletionListener-----------");
            });
        }
    }

    @Override
    public void onStartListener() {
        if (null != onStateChangeListener) onStateChangeListener.start(imageView);
        AndroidToolsConfig.androidToolsConfig.getHandler().post(this::startViewAnimaiton);

        Logger.d("=============onStartListener==========" + weiciMediaplayer.isPlaying());
    }

    public boolean isPlaying(View view) {
        return weiciMediaplayer.isPlaying() && view == imageView;
    }

    public boolean isPlaying() {
        return weiciMediaplayer.isPlaying();
    }

    public void stop(View view) {
        if (view == imageView) {
            resetImageView();
            weiciMediaplayer.stop();
        }
    }

    public void stop(OnMediaPlayerListener onMediaPlayerListener) {
        weiciMediaplayer.setOnMediaPlayerListener(onMediaPlayerListener);
        weiciMediaplayer.stop();
    }

    public void stop() {
        weiciMediaplayer.stop();
    }

    @Override
    public void onPlayerPause() {
        resetImageView();
    }

    private void startViewAnimaiton() {
        Logger.d("iamge:" + (imageView == null));


        if (null == imageView) return;
        imageView.clearAnimation();
        imageView.post(() -> {
            try {
                Object tag3 = imageView.getTag(R.id.tag_anim_type);
                // 0 背景 帧动画
                int animType = 0;
                if (null != tag3 && tag3 instanceof Integer) {
                    animType = (int) tag3;
                }
                Object tag = imageView.getTag(R.id.tag_audio_anim);
                if (null == tag) {
                    return;
                }
                if (animType == 0) {
                    imageView.setBackgroundResource((Integer) tag);
                    AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
                    animationDrawable.start();
                } else if (animType == 1) {
                    Object tag1 = imageView.getTag(R.id.tag_audio_anim_bg);
                    if (null != tag1 && tag1 instanceof Integer) {
                        imageView.setBackgroundResource((Integer) tag1);
                    }
                    imageView.startAnimation(AnimationUtils.loadAnimation(context, (Integer) tag));
                }
            } catch (Exception e) {
                Logger.d("animation failed:" + e.getMessage());
            }
        });

    }

    public void destroy() {
        weiciMediaplayer.detroy();
        imageView = null;
        instance1 = null;
        instance2 = null;
    }


}
