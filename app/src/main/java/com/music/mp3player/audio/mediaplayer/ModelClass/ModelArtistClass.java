package com.music.mp3player.audio.mediaplayer.ModelClass;

public class ModelArtistClass {

    private String id;
    private String artistName;
    private String noOfSongs;
    private String art;

    public ModelArtistClass(String id, String artistName, String noOfSongs, String art) {
        this.id = id;
        this.artistName = artistName;
        this.noOfSongs = noOfSongs;
        this.art = art;
    }

    public ModelArtistClass() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getNoOfSongs() {
        return noOfSongs;
    }

    public void setNoOfSongs(String noOfSongs) {
        this.noOfSongs = noOfSongs;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }
}
