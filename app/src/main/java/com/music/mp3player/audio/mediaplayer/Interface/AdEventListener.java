package com.music.mp3player.audio.mediaplayer.Interface;

public interface AdEventListener {

    void onAdLoaded(Object object);

    void onAdClosed();

    void onLoadError(String errorCode);
}
