package com.android.tools.media.download;

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
import com.android.tools.net.OkHttpUtil;
import com.android.tools.rx.RxSchedulers;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

public class YxMediaplayer
        implements OnCompletionListener, OnErrorListener, OnPreparedListener, MediaPlayer.OnBufferingUpdateListener {

    public static final int AUDIO_FILE_TYPE_AUTO = 0;
    public static final int AUDIO_FILE_TYPE_ASSETS = 1;
    public static final int AUDIO_FILE_TYPE_FILE = 2;
    public static final int AUDIO_FILE_TYPE_NET = 3;

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Logger.d("percent:" + percent);
        Logger.d("total:" + mMediaPlayer.getDuration());

    }

    @IntDef({AUDIO_FILE_TYPE_AUTO, AUDIO_FILE_TYPE_ASSETS, AUDIO_FILE_TYPE_FILE, AUDIO_FILE_TYPE_NET})
    @interface AudioFileType {
    }


    private MediaPlayer mMediaPlayer;
    private MusicRadioHandlerThread mHandlerThread;
    private Handler mThreadHandler;
    private IMediaStateChangeListener iMediaStateChangeListener;
    private Context context;

    private int state;

    public YxMediaplayer(Context context, IMediaStateChangeListener iMediaStateChangeListener) {
        this.context = context.getApplicationContext();
        WeakReference<YxMediaplayer> logic = new WeakReference<>(this);
        mHandlerThread = new MusicRadioHandlerThread("music_radio", logic);
        mHandlerThread.start();
        mThreadHandler = new Handler(mHandlerThread.getLooper(), mHandlerThread);
        this.iMediaStateChangeListener = iMediaStateChangeListener;
        setState(IMediaStateChangeListener.STATE_IDLE, 0);
    }

    public YxMediaplayer(Context context) {
        this(context, null);
    }


    private void playByAssets(String name) {
        try {
            MediaPlayer mediaPlayer = getMediaPlayer();
            AssetManager am = context.getAssets();
            AssetFileDescriptor afd = am.openFd(name);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepareAsync();
            setState(IMediaStateChangeListener.STATE_PREPARE, 0);
        } catch (Exception e) {
            Logger.d("playByAssets play error:" + e.getMessage());
            setState(IMediaStateChangeListener.STATE_ERROR, IMediaStateChangeListener.ERROR_CANNOT_PLAY);
        }
    }

    private void playByFile(String name) {
        try {
            MediaPlayer mediaPlayer = getMediaPlayer();
            mediaPlayer.setDataSource(name);
            mediaPlayer.prepareAsync();
            setState(IMediaStateChangeListener.STATE_PREPARE, 0);
        } catch (Exception e) {
            Logger.d("playByFile play error:" + e.getMessage());
            setState(IMediaStateChangeListener.STATE_ERROR, IMediaStateChangeListener.ERROR_CANNOT_PLAY);
        }
    }

    public boolean isPlaying() {
        return state == IMediaStateChangeListener.STATE_LOADING || state == IMediaStateChangeListener.STATE_LOADFINISH || state == IMediaStateChangeListener.STATE_PREPARE || state == IMediaStateChangeListener.STATE_START;
    }


    public void stop() {
        if (state == IMediaStateChangeListener.STATE_LOADING || state == IMediaStateChangeListener.STATE_LOADFINISH || state == IMediaStateChangeListener.STATE_PREPARE) {
            setState(IMediaStateChangeListener.STATE_STOP, 0);
        } else if (state == IMediaStateChangeListener.STATE_START) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
            } catch (Exception ignore) {

            }
            setState(IMediaStateChangeListener.STATE_IDLE, 0);
        } else if (state == IMediaStateChangeListener.STATE_STOP || state == IMediaStateChangeListener.STATE_PAUSE || state == IMediaStateChangeListener.STATE_DESTROY) {
            setState(IMediaStateChangeListener.STATE_IDLE, 0);
        }
    }

    public void destroy() {
        stop();
        setState(IMediaStateChangeListener.STATE_DESTROY, 0);
        if (null != mMediaPlayer) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mMediaPlayer.release();
                    mThreadHandler.removeMessages(0);
                    mMediaPlayer = null;
                    mHandlerThread = null;
                    mThreadHandler = null;
                }
            }).start();
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
        mMediaPlayer.setOnBufferingUpdateListener(this);
        return mMediaPlayer;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        setState(IMediaStateChangeListener.STATE_STOP, 0);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Logger.d("what:" + what + "---------extra:" + extra);
        if (what == -38) {
            return false;
        }
        setState(IMediaStateChangeListener.STATE_ERROR, IMediaStateChangeListener.ERROR_MEDIA);
        return false;
    }

    public static class MusicRadioHandlerThread extends HandlerThread implements Callback {

        WeakReference<YxMediaplayer> mLogic;

        MusicRadioHandlerThread(String name, WeakReference<YxMediaplayer> logic) {
            super(name);
            this.mLogic = logic;
        }

        @Override
        public boolean handleMessage(Message msg) {
            if (null == msg) {
                YxMediaplayer logic = mLogic.get();
                logic.setState(IMediaStateChangeListener.STATE_ERROR, IMediaStateChangeListener.ERROR_OTHER);
                return false;
            }
            YxMediaplayer logic = mLogic.get();
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

    public void play(String name, int type) {

        int t = canPlay(name, type);
        if (t == -1) {
            setState(IMediaStateChangeListener.STATE_ERROR, IMediaStateChangeListener.ERROR_CANNOT_PLAY);
            return;
        }
        if (t == AUDIO_FILE_TYPE_NET && !AndroidTools.isNetworkAvailable(context)) {
            setState(IMediaStateChangeListener.STATE_ERROR, IMediaStateChangeListener.ERROR_NO_NET);
            return;
        }
        Message msg = new Message();
        msg.what = t;
        msg.obj = t == 1 ? "audio/" + name : name;
        mThreadHandler.sendMessage(msg);
    }

    public void setiMediaStateChangeListener(IMediaStateChangeListener iMediaStateChangeListener) {
        this.iMediaStateChangeListener = iMediaStateChangeListener;
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
        return true;
//        AssetManager am = context.getAssets();
//        try {
//            String[] list = am.list("audio");
//            List<String> list1 = Arrays.asList(list);
//            return list1.contains(name);
//        } catch (IOException e) {
//            return false;
//        }
    }

    public void play(String name, IDownloadConfig iDownloadConfig, IMediaStateChangeListener iMediaStateChangeListener) {
        setiMediaStateChangeListener(iMediaStateChangeListener);
        if (null == iDownloadConfig || TextUtils.isEmpty(name)) {
            setState(IMediaStateChangeListener.STATE_ERROR, IMediaStateChangeListener.ERROR_OTHER);
            return;
        }
        String destName = iDownloadConfig.getDestName(name);
        if (new File(destName).exists()) {
            play(destName, AUDIO_FILE_TYPE_FILE);
            return;
        }

        if (TextUtils.isEmpty(iDownloadConfig.getUrl())) {
            if (canPlayByAssets(name)) {
                play(name, AUDIO_FILE_TYPE_ASSETS);
            } else {
                setState(IMediaStateChangeListener.STATE_ERROR, IMediaStateChangeListener.ERROR_CANNOT_PLAY);
            }
            return;
        }

        if (!AndroidTools.isNetworkAvailable(context)) {
            setState(IMediaStateChangeListener.STATE_ERROR, IMediaStateChangeListener.ERROR_NO_NET);
            return;
        }

        setState(IMediaStateChangeListener.STATE_LOADING, 0);
        Observable.create((ObservableOnSubscribe<String>) e -> {
            String file = download(name, iDownloadConfig);
            e.onNext(file);
        }).compose(RxSchedulers.threadMain()).doOnNext(s -> {
            if (TextUtils.isEmpty(s)) {
                setState(IMediaStateChangeListener.STATE_ERROR, IMediaStateChangeListener.ERROR_CANNOT_PLAY);
                return;
            }
            setState(IMediaStateChangeListener.STATE_LOADFINISH, 0);
            play(s, AUDIO_FILE_TYPE_FILE);
        }).subscribe();
    }

    private boolean canPlayByFile(String name) {
        return new File(name).exists();
    }

    private boolean canPlayByNet(String name) {
        return !TextUtils.isEmpty(name) && name.startsWith("http://");
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        if (state != IMediaStateChangeListener.STATE_PREPARE) {
            setState(IMediaStateChangeListener.STATE_IDLE, 0);
            return;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            setState(IMediaStateChangeListener.STATE_START, 0);
        } else {
            setState(IMediaStateChangeListener.STATE_STOP, 0);
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state, int error) {
        this.state = state;
        if (null != iMediaStateChangeListener) {
            switch (state) {
                case IMediaStateChangeListener.STATE_IDLE:
                    iMediaStateChangeListener.onIdleStateListener();
                    break;
                case IMediaStateChangeListener.STATE_LOADING:
                    iMediaStateChangeListener.onLoadingStateListener();
                    break;
                case IMediaStateChangeListener.STATE_LOADFINISH:
                    iMediaStateChangeListener.onLoadFinishStateListener();
                    break;
                case IMediaStateChangeListener.STATE_PREPARE:
                    iMediaStateChangeListener.onPrepareStateListener();
                    break;
                case IMediaStateChangeListener.STATE_START:
                    iMediaStateChangeListener.onStartStateListener();
                    break;
                case IMediaStateChangeListener.STATE_PAUSE:
                    iMediaStateChangeListener.onPauseStateListener();
                    break;
                case IMediaStateChangeListener.STATE_STOP:
                    iMediaStateChangeListener.onStopStateListener();
                    this.state = IMediaStateChangeListener.STATE_IDLE;
                    break;
                case IMediaStateChangeListener.STATE_DESTROY:
                    iMediaStateChangeListener.onDestroyStateListener();
                    this.state = IMediaStateChangeListener.STATE_IDLE;
                    break;
                case IMediaStateChangeListener.STATE_ERROR:
                    iMediaStateChangeListener.onErrorStateListener(error);
                    this.state = IMediaStateChangeListener.STATE_IDLE;
                    break;
            }
        }
    }

    private String download(String name, IDownloadConfig iDownloadConfig) {
        String destName = iDownloadConfig.getDestName(name);
        boolean b = OkHttpUtil.downloadFile(context, iDownloadConfig.getUrl(), destName);
        return b ? destName : "";
    }
}
