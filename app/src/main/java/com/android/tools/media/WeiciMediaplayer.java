package com.android.tools.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.IntDef;
import android.text.TextUtils;

import com.android.tools.AndroidTools;
import com.android.tools.Logger;
import com.android.tools.widget.ToastManager;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

public class WeiciMediaplayer
        implements IMediaPlayerLogic, OnCompletionListener, OnErrorListener, OnPreparedListener {

    public static final int AUDIO_FILE_TYPE_AUTO = 0;
    public static final int AUDIO_FILE_TYPE_ASSETS = 1;
    public static final int AUDIO_FILE_TYPE_FILE = 2;
    public static final int AUDIO_FILE_TYPE_NET = 3;

    @IntDef({AUDIO_FILE_TYPE_AUTO, AUDIO_FILE_TYPE_ASSETS, AUDIO_FILE_TYPE_FILE, AUDIO_FILE_TYPE_NET})
    public @interface AudioFileType {
    }

    private MediaPlayer mMediaPlayer;
    private MusicRadioHandlerThread mHandlerThread;
    private Handler mThreadHandler;
    private OnMediaPlayerListener listener;
    private Context context;

    private int state;

    public static final int STATE_IDEL = 0;
    public static final int STATE_PLAYING = 1;
    public static final int STATE_PREPARE = 2;


    public WeiciMediaplayer(Context context) {
        this.context = context.getApplicationContext();
        WeakReference<WeiciMediaplayer> logic = new WeakReference<>(this);
        mHandlerThread = new MusicRadioHandlerThread("musicradio", logic);
        mHandlerThread.start();
        mThreadHandler = new Handler(mHandlerThread.getLooper(), mHandlerThread);
        state = STATE_IDEL;
    }


    private void playByAssets(String name) {
        try {
            MediaPlayer mediaPlayer = getMediaPlayer();
            AssetManager am = context.getAssets();
            AssetFileDescriptor afd = am.openFd(name);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepareAsync();
            state = STATE_PREPARE;
        } catch (Exception e) {
            Logger.d("playByAssets play error:" + e.getMessage());
            if (null != listener) {
                listener.onError();
            }
        }
    }

    private void playByFile(String name) {
        try {
            MediaPlayer mediaPlayer = getMediaPlayer();
            mediaPlayer.setDataSource(name);
            mediaPlayer.prepareAsync();
            state = STATE_PREPARE;
        } catch (Exception e) {
            Logger.d("playByFile play error:" + e.getMessage());
            if (null != listener) {
                listener.onError();
            }
        }
    }

    public boolean isPlaying() {
        return state == STATE_PLAYING || state == STATE_PREPARE;
    }


    @Override
    public void stop() {
        if (state == STATE_PREPARE) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            state = STATE_IDEL;
            return;
        }
        state = STATE_IDEL;
        try {
            if (null != mMediaPlayer && mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                if (null != listener) {
                    listener.onCompletionListener();
                }
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void detroy() {
        if (listener != null) listener.onCompletionListener();
        if (null != mMediaPlayer) {
            stop();
            mMediaPlayer.release();
            mThreadHandler.removeMessages(0);
            mMediaPlayer = null;
            mHandlerThread = null;
            mThreadHandler = null;
            listener = null;
            state = STATE_IDEL;
        }
    }

    private MediaPlayer getMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            return mMediaPlayer;
        }
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        return mMediaPlayer;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        state = STATE_IDEL;
        if (null != listener) {
            listener.onCompletionListener();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Logger.d("what:" + what + "---------extra:" + extra);
        state = STATE_IDEL;
        if (what == -38) {
            return false;
        }

        if (null != listener) {
            listener.onError();
        }
        return false;
    }

    public static class MusicRadioHandlerThread extends HandlerThread implements Callback {

        WeakReference<WeiciMediaplayer> mLogic;

        MusicRadioHandlerThread(String name, WeakReference<WeiciMediaplayer> logic) {
            super(name);
            this.mLogic = logic;
        }

        @Override
        public boolean handleMessage(Message msg) {
            if (null == msg) {
                return false;
            }
            WeiciMediaplayer logic = mLogic.get();
            switch (msg.what) {
                case AUDIO_FILE_TYPE_ASSETS:
                    logic.playByAssets((String) msg.obj);
                    break;
                case AUDIO_FILE_TYPE_FILE:
                case AUDIO_FILE_TYPE_NET:
                    logic.playByFile((String) msg.obj);
                    break;
            }
            return false;
        }
    }

    @Override
    public void play(String name, int type) {

        int t = canPlay(name, type);
        if (t == -1) {
            state = STATE_IDEL;
            if (null != listener) listener.onError();
            return;
        }
        if (t == AUDIO_FILE_TYPE_NET && !AndroidTools.isNetworkAvailable(context)) {
            ToastManager.getInstance().showToast(context, "网络无效，请重试");
            state = STATE_IDEL;
            if (null != listener) listener.onCompletionListener();
            return;
        }

        Message msg = new Message();
        msg.what = t;
        msg.obj = t == 1 ? "audio/" + name : name;
        mThreadHandler.sendMessage(msg);
    }

    @Override
    public void setOnMediaPlayerListener(OnMediaPlayerListener listener) {
        this.listener = listener;
    }


    public int canPlay(String name, @AudioFileType int type) {
        if (TextUtils.isEmpty(name) || null == mThreadHandler) return -1;
        if (type == AUDIO_FILE_TYPE_AUTO) {
            if (canPlayByAssets(name)) return AUDIO_FILE_TYPE_ASSETS;
            if (canPlayByFile(name)) return AUDIO_FILE_TYPE_FILE;
            if (canPlayByNet(name)) return AUDIO_FILE_TYPE_NET;
        } else if (type == AUDIO_FILE_TYPE_ASSETS && canPlayByAssets(name)) {
            return AUDIO_FILE_TYPE_ASSETS;
        } else if (type == AUDIO_FILE_TYPE_FILE && canPlayByFile(name)) {
            return AUDIO_FILE_TYPE_FILE;
        } else if (type == AUDIO_FILE_TYPE_NET && canPlayByNet(name)) {
            return AUDIO_FILE_TYPE_NET;
        }
        return -1;
    }

    private boolean canPlayByAssets(String name) {
        AssetManager am = context.getAssets();
        try {
            String[] list = am.list("audio");
            List<String> list1 = Arrays.asList(list);
            return list1.contains(name);
        } catch (IOException e) {
            return false;
        }
    }

    private boolean canPlayByFile(String name) {
        return new File(name).exists();
    }

    private boolean canPlayByNet(String name) {
        return !TextUtils.isEmpty(name) && name.startsWith("http://");
    }


    @Override
    public void onPrepared(MediaPlayer mp) {

        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            state = STATE_PLAYING;
            if (null != listener) listener.onStartListener();
        } else {
            if (null != listener) listener.onCompletionListener();
        }
    }

    public int getState() {
        return state;
    }
}
