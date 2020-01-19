package com.android.tools.media.download;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.alan.common.Logger;
import com.android.tools.R;
import com.android.tools.media.MediaProgressHelper;
import com.android.tools.media.OnMediaProgressListener;
import com.android.tools.widget.ToastManager;

import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Mouse on 2019/1/29.
 */
public class MediaplayerManager implements IMediaStateChangeListener {

    private YxMediaplayer yxMediaplayer;
    private View mView;

    private static MediaplayerManager mediaplayerManager;
    private Context context;

    private IMediaStateChangeListener iMediaStateChangeListener;

    private OnMediaProgressListener onMediaProgressListener;


    private MediaplayerManager(Context context) {
        this.context = context.getApplicationContext();
        yxMediaplayer = new YxMediaplayer(this.context);
    }

    public static MediaplayerManager getInstance(Context context) {
        if (mediaplayerManager == null) {
            mediaplayerManager = new MediaplayerManager(context);
        }
        return mediaplayerManager;
    }

    public void play(View view, IDownloadConfig iDownloadConfig) {
        play(view, iDownloadConfig, null);
    }

    public void play(View view, IDownloadConfig iDownloadConfig, IMediaStateChangeListener iMediaStateChangeListener) {
        this.iMediaStateChangeListener = iMediaStateChangeListener;
        String name = iDownloadConfig.getAudioName();
        if (mView == view) {
            if (yxMediaplayer.isPlaying()) {
                yxMediaplayer.stop();
            } else {
                yxMediaplayer.play(name, iDownloadConfig, this);
            }
        } else {
            if (yxMediaplayer.getState() == IMediaStateChangeListener.STATE_IDLE) {
                this.mView = view;
                yxMediaplayer.play(name, iDownloadConfig, this);
            } else {
                yxMediaplayer.destroy();
                yxMediaplayer.setiMediaStateChangeListener(null);
                resetView();
                this.mView = view;
                yxMediaplayer = new YxMediaplayer(context);
                yxMediaplayer.play(name, iDownloadConfig, this);
            }
        }
    }

    public void play(IDownloadConfig iDownloadConfig) {
        play(iDownloadConfig, null);
    }

    public void play(IDownloadConfig iDownloadConfig, IMediaStateChangeListener iMediaStateChangeListener) {
        this.iMediaStateChangeListener = iMediaStateChangeListener;
        String name = iDownloadConfig.getAudioName();
        if (yxMediaplayer.isPlaying()) {
            yxMediaplayer.stop();
        } else {
            yxMediaplayer.play(name, iDownloadConfig, this);
        }
    }

    public void play(View view, String name) {
        if (mView == view) {
            if (yxMediaplayer.isPlaying()) {
                yxMediaplayer.stop();
            } else {
                yxMediaplayer.play(name, YxMediaplayer.AUDIO_FILE_TYPE_ASSETS);
            }
        } else {
            if (yxMediaplayer.getState() == IMediaStateChangeListener.STATE_IDLE) {
                this.mView = view;
                yxMediaplayer.play(name, YxMediaplayer.AUDIO_FILE_TYPE_ASSETS);
            } else {
                yxMediaplayer.destroy();
                yxMediaplayer.setiMediaStateChangeListener(null);
                resetView();
                this.mView = view;
                yxMediaplayer = new YxMediaplayer(context);
                yxMediaplayer.play(name, YxMediaplayer.AUDIO_FILE_TYPE_ASSETS);
            }
        }
    }

    /**
     * 不依赖View 播放
     *
     * @param name
     */
    public void play(String name) {
        resetView();
        mView = null;
        yxMediaplayer.setiMediaStateChangeListener(this);
        if (yxMediaplayer.isPlaying()) {
            yxMediaplayer.stop();
        } else {
            yxMediaplayer.play(name, YxMediaplayer.AUDIO_FILE_TYPE_AUTO);
        }
    }

    public void play(String name, IDownloadConfig iDownloadConfig) {
        resetView();
        mView = null;
        if (yxMediaplayer.isPlaying()) {
            yxMediaplayer.stop();
        } else {
            yxMediaplayer.play(name, iDownloadConfig, this);
        }
    }


    public void stop() {
        if (null != yxMediaplayer && yxMediaplayer.isPlaying()) {
            yxMediaplayer.stop();
        }
    }

    private void resetView() {
        if (null == mView) return;
        mView.clearAnimation();
        mView.setAlpha(1.0f);
        int bg = (int) this.mView.getTag(R.id.tag_audio_default_bg);
        if (this.mView instanceof ImageView) {
            ((ImageView) this.mView).setImageResource(bg);
        } else {
            this.mView.setBackgroundResource(bg);
        }
    }

    private void startViewAnimation() {
        if (null == mView) {
            return;
        }
        try {
            mView.clearAnimation();
            Object tag3 = mView.getTag(R.id.tag_anim_type);
            // 0 背景 帧动画
            int animType = 0;
            if (tag3 instanceof Integer) {
                animType = (int) tag3;
            }
            Object tag = mView.getTag(R.id.tag_audio_anim);
            if (null == tag) {
                return;
            }
            if (animType == 0) {
                mView.setBackgroundResource((Integer) tag);
                AnimationDrawable animationDrawable = (AnimationDrawable) mView.getBackground();
                animationDrawable.start();
            } else if (animType == 1) {
                Object tag1 = mView.getTag(R.id.tag_audio_anim_bg);
                if (tag1 instanceof Integer) {
                    mView.setBackgroundResource((Integer) tag1);
                }
                mView.startAnimation(AnimationUtils.loadAnimation(context, (Integer) tag));
            }
        } catch (Exception e) {
            Logger.d("animation failed:" + e.getMessage());
        }

    }

    @Override
    public void onLoadingStateListener() {
        if (null != iMediaStateChangeListener) {
            iMediaStateChangeListener.onLoadingStateListener();
        }
        if (null == mView) {
            return;
        }
        Object tag = this.mView.getTag(R.id.tag_loading);
        if (null == tag) return;
        int drawable = (int) tag;
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        animation.setInterpolator(new LinearInterpolator());
        this.mView.setBackgroundResource(drawable);
        this.mView.startAnimation(animation);
    }

    @Override
    public void onLoadFinishStateListener() {
        if (null != iMediaStateChangeListener) {
            iMediaStateChangeListener.onLoadFinishStateListener();
        }
    }

    @Override
    public void onPrepareStateListener() {
        if (null != iMediaStateChangeListener) {
            iMediaStateChangeListener.onPrepareStateListener();
        }
    }

    @Override
    public void onStartStateListener() {
        startTime();
        if (null == mView && null == iMediaStateChangeListener) return;
        AndroidSchedulers.mainThread().scheduleDirect(() -> {
            if (null != iMediaStateChangeListener) {
                iMediaStateChangeListener.onStartStateListener();
            }
            startViewAnimation();
        });

    }

    @Override
    public void onPauseStateListener() {
        stopTime();
        if (null == mView && null == iMediaStateChangeListener) return;
        AndroidSchedulers.mainThread().scheduleDirect(() -> {
            if (null != iMediaStateChangeListener) {
                iMediaStateChangeListener.onPauseStateListener();
            }
            resetView();
        });
    }

    @Override
    public void onStopStateListener() {
        stopTime();
        if (null == mView && null == iMediaStateChangeListener) return;
        AndroidSchedulers.mainThread().scheduleDirect(() -> {
            if (null != iMediaStateChangeListener) {
                iMediaStateChangeListener.onStopStateListener();
            }
            resetView();
        });
    }

    @Override
    public void onDestroyStateListener() {
        stopTime();
        if (null == mView && null == iMediaStateChangeListener) return;
        AndroidSchedulers.mainThread().scheduleDirect(() -> {
            if (null != iMediaStateChangeListener) {
                iMediaStateChangeListener.onDestroyStateListener();
            }
            resetView();
        });
    }

    @Override
    public void onIdleStateListener() {
        stopTime();
        if (null == mView && null == iMediaStateChangeListener) return;
        AndroidSchedulers.mainThread().scheduleDirect(() -> {
            if (null != iMediaStateChangeListener) {
                iMediaStateChangeListener.onIdleStateListener();
            }
            resetView();
        });
    }

    @Override
    public void onErrorStateListener(int error) {
        if (null == mView && null == iMediaStateChangeListener) return;
        AndroidSchedulers.mainThread().scheduleDirect(() -> {
            if (null != iMediaStateChangeListener) {
                iMediaStateChangeListener.onErrorStateListener(error);
            }

            if (mView == null) {
                return;
            }
            resetView();
            if (error == IMediaStateChangeListener.ERROR_NO_NET) {
                ToastManager.getInstance().showToast(context, "网络无效，请重试");
            } else {
                ToastManager.getInstance().showToast(context, "播放失败");
            }
        });
    }

    @Override
    public void onComplete() {
        if (null != onMediaProgressListener) {
            onMediaProgressListener.onComplete();
        }
    }

    public void setMediaStateChangeListener(IMediaStateChangeListener iMediaStateChangeListener) {
        this.iMediaStateChangeListener = iMediaStateChangeListener;
    }


    public void setOnMediaProgressListener(OnMediaProgressListener onMediaProgressListener) {
        this.onMediaProgressListener = onMediaProgressListener;
    }

    private void startTime() {
        if (null != onMediaProgressListener) {
            MediaProgressHelper.getInstance().execute(timerTask);
        }

    }

    private void stopTime() {
        if (MediaProgressHelper.hasInstance()) {
            MediaProgressHelper.getInstance().cancel();
        }
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Logger.d("executor timer task");
            if (yxMediaplayer.getState() == IMediaStateChangeListener.STATE_START && null != onMediaProgressListener) {
                try {
                    int currentPosition = yxMediaplayer.getCurrentPosition();
                    int duration = yxMediaplayer.getDuration();
                    Logger.d("currentPosition:" + currentPosition);
                    Logger.d("duration:" + duration);
                    if (null != onMediaProgressListener) {
                        onMediaProgressListener.onMediaProgressListener(currentPosition, duration);
                    }
                } catch (Exception e) {
                    Logger.error(e);
                }
            }
        }
    };

    public void destroy() {
        yxMediaplayer.destroy();
        onMediaProgressListener = null;
        mediaplayerManager = null;
        if (MediaProgressHelper.hasInstance()) {
            MediaProgressHelper.getInstance().destroy();
        }
    }

    public void seek(int position) {
        yxMediaplayer.seek(position);
    }

    public int getState() {
        return yxMediaplayer.getState();
    }
}
