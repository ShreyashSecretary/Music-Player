package com.music.mp3player.audio.mediaplayer.ModelClass;

import android.net.Uri;

public class AudioModel {

    private String path;
    private String name;
    private String album;
    private String artist;
    private String duration;
    private String id;
    private Uri albumArt;

    public AudioModel(String path, String name, String album, String artist, String duration, String id, Uri albumArt) {
        this.path = path;
        this.name = name;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
        this.id = id;
        this.albumArt = albumArt;
    }

    public AudioModel() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(Uri albumArt) {
        this.albumArt = albumArt;
    }
}
