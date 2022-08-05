package com.music.mp3player.audio.mediaplayer.ModelClass;

public class ModelAlbumClass {

    String id;
    String album;
    String artist;
    String art;
    String noOfSongs;

    public ModelAlbumClass(String id, String album, String artist, String art, String noOfSongs) {
        this.id = id;
        this.album = album;
        this.artist = artist;
        this.art = art;
        this.noOfSongs = noOfSongs;
    }

    public ModelAlbumClass() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public String getNoOfSongs() {
        return noOfSongs;
    }

    public void setNoOfSongs(String noOfSongs) {
        this.noOfSongs = noOfSongs;
    }

    /*public List<AudioModel> getSongList() {
        return songList;
    }

    public void setSongList(List<AudioModel> songList) {
        this.songList = songList;
    }*/
}
