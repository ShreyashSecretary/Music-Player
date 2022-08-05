package com.music.mp3player.audio.mediaplayer.ModelClass;

public class ModelGenresClass {

    String id;
    String name;
    String noOfSongs;
    String art;

    public ModelGenresClass(String id, String name, String noOfSongs, String art) {
        this.id = id;
        this.name = name;
        this.noOfSongs = noOfSongs;
        this.art = art;
    }

    public ModelGenresClass() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
