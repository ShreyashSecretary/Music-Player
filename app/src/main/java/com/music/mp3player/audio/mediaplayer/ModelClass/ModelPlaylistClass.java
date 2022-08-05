package com.music.mp3player.audio.mediaplayer.ModelClass;

import java.util.List;

public class ModelPlaylistClass {

    private String name;
    private List<AudioModel> playlistSongsList;

    public ModelPlaylistClass(String name, List<AudioModel> playlistSongsList) {
        this.name = name;
        this.playlistSongsList = playlistSongsList;
    }

    public ModelPlaylistClass() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AudioModel> getPlaylistSongsList() {
        return playlistSongsList;
    }

    public void setPlaylistSongsList(List<AudioModel> playlistSongsList) {
        this.playlistSongsList = playlistSongsList;
    }
}
