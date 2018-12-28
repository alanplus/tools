package com.android.tools.media;

public interface OnMediaPlayerListener {
	
	void onCompletionListener();
	void onError();
	void onStartListener();
	void onPlayerPause();
}
