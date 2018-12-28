package com.android.tools.media;


public interface IMediaPlayerLogic {

	void play(String name,@WeiciMediaplayer.AudioFileType int type);
	void setOnMediaPlayerListener(OnMediaPlayerListener listener);
	void stop();
	void detroy();
}
