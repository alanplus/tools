package com.android.tools.media;

import android.media.MediaPlayer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Alan
 * 时 间：2020-01-19
 * 简 述：<功能简述>
 */
public class MediaProgressHelper {

    private Timer timer;

    private static MediaProgressHelper mMediaPregressHelper;


    private MediaProgressHelper() {
        timer = new Timer();
    }

    public static MediaProgressHelper getInstance() {
        if (mMediaPregressHelper == null) {
            mMediaPregressHelper = new MediaProgressHelper();
        }
        return mMediaPregressHelper;
    }

    public void execute(TimerTask timerTask) {
        timer.schedule(timerTask, 0, 800);
    }

    public void cancel() {
        timer.cancel();
    }

    public void destroy() {
        timer.cancel();
        timer = null;
        mMediaPregressHelper = null;
    }

    public static boolean hasInstance(){
        return mMediaPregressHelper!=null;
    }


}
