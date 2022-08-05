package com.music.mp3player.audio.mediaplayer;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.music.mp3player.audio.mediaplayer.DataBase.DbHelper;
import com.music.mp3player.audio.mediaplayer.ModelClass.AudioModel;
import com.music.mp3player.audio.mediaplayer.ModelClass.ModelAlbumClass;
import com.music.mp3player.audio.mediaplayer.ModelClass.ModelArtistClass;
import com.music.mp3player.audio.mediaplayer.ModelClass.ModelGenresClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.music.mp3player.audio.mediaplayer.Fragment.PlaylistSongsFragments.myPlaylist;

public class GetAllSongs {

    private ModelArtistClass artistClass;
    private ModelGenresClass genresClass;
    private AudioModel audioModel;
    private ModelAlbumClass albumClass;
    public static List<AudioModel> audioList;
    public static List<AudioModel> recentList;
    public static List<AudioModel> favouritesList;
    //public static List<ModelAlbumClass> albumList;
   // public static List<ModelArtistClass> artistList;
   // public static List<ModelGenresClass> genresLists;
    //public static ArrayList<String> folderName = new ArrayList<>();
    private Context context;
    DbHelper DB;
    String actName;
    public static List<String> bucketList = new ArrayList<>();

    public static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");


    public GetAllSongs(Context context) {
        this.context = context;

        DB = new DbHelper(context);
    }

    //ArrayList<HashMap<String,String>> songList = getPlayList(Environment.getExternalStorageDirectory().getAbsolutePath() );
        /*if(songList!=null){
        for(int i=0;i<songList.size();i++){
            String fileName=songList.get(i).get("file_name");
            String filePath=songList.get(i).get("file_path");
            //here you will get list of file name and file path that present in your device
            log.e("file details "," name ="+fileName +" path = "+filePath);
        }
    }*/

    public List<AudioModel> getAudioFiles()
    {
        List<String> folders = new ArrayList<>();
        audioList = new ArrayList<>();
        audioList.clear();
        bucketList.clear();
        String[] projection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.Media.ALBUM_ID/*, MediaStore.Audio.Media.BUCKET_DISPLAY_NAME*/};
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = context.getContentResolver().query(uri, projection, null, null, null);
        if(musicCursor != null)
        {
            while (musicCursor.moveToNext())
            {
                audioModel = new AudioModel();
               // String bucket = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME));
              //  Log.e("Bucket" , " ::: " + bucket);
                String url = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
                File file = new File(url);
               // file.getParentFile().getPath();
                Log.e("PAth", ""+file.getParentFile().getName()+ " ::: "+ url);
                String folderName = file.getParentFile().getName();
                if(!bucketList.contains(folderName) && !folderName.startsWith("."))
                {
                    bucketList.add(folderName);
                }
                String name = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String album = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String albumId = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String artist = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String id = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String duration = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                Uri albumArtUri;
                if(albumId != null) {
                    albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId));
                }
                else
                {
                    albumArtUri = null;
                }

                if(duration != null) {
                    audioModel.setName(name);
                    audioModel.setPath(url);
                    audioModel.setAlbum(album);
                    audioModel.setArtist(artist);
                    audioModel.setArtist(artist);
                    audioModel.setDuration(duration);
                    audioModel.setId(id);
                    audioModel.setAlbumArt(albumArtUri);

                    Log.e("Name : " + name, "Album : " + album);
                    Log.e("Path : " + url, "Artist : " + artist);

                    audioList.add(audioModel);
                }
/*
                if(musicCursor.isLast())
                {
                    Constants.songList = audioList;
                }*/
            }
            musicCursor.close();
        }

        return audioList;
    }

    public List<AudioModel> getRecentList()
    {
        recentList = new ArrayList<>();
        Cursor c = DB.getRecentSongs();
        if(c != null)
        {
            while (c.moveToNext())
            {
                audioModel = new AudioModel();
                audioModel.setPath(c.getString(c.getColumnIndex("path")));
                audioModel.setName(c.getString(c.getColumnIndex("name")));
                audioModel.setArtist(c.getString(c.getColumnIndex("artist")));
                audioModel.setAlbum(c.getString(c.getColumnIndex("album")));
                audioModel.setDuration(c.getString(c.getColumnIndex("duration")));
                audioModel.setId(c.getString(c.getColumnIndex("id")));
                audioModel.setAlbumArt(Uri.parse(c.getString(c.getColumnIndex("image"))));

                recentList.add(audioModel);
            }
        }
        c.close();
        return recentList;
    }

    public void getPlayListSongs(String playlistName) {
        myPlaylist = new ArrayList<>();
        myPlaylist.clear();
        Cursor c = DB.getPlaylistSongs(playlistName);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                audioModel = new AudioModel();
                String url = c.getString(c.getColumnIndex("path"));
                String name = c.getString(c.getColumnIndex("name"));
                String album = c.getString(c.getColumnIndex("album"));
                String artist = c.getString(c.getColumnIndex("artist"));
                String id = c.getString(c.getColumnIndex("id"));
                String duration = c.getString(c.getColumnIndex("duration"));
                Uri albumArtUri = Uri.parse(c.getString(c.getColumnIndex("image")));

                audioModel.setName(name);
                audioModel.setPath(url);
                audioModel.setAlbum(album);
                audioModel.setArtist(artist);
                audioModel.setArtist(artist);
                audioModel.setDuration(duration);
                audioModel.setId(id);
                audioModel.setAlbumArt(albumArtUri);

                myPlaylist.add(audioModel);
            }
        }
    }
    /*public void getSongAlbumWise() {

        albumList = new ArrayList<>();
        albumList.clear();
        String[] projection = new String[]{Albums._ID, Albums.ALBUM, Albums.ARTIST, Albums.ALBUM_ART, Albums.NUMBER_OF_SONGS};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = MediaStore.Audio.Media.ALBUM + " ASC";
        Cursor cursor = context.getContentResolver().query(Albums.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, sortOrder);

        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                albumClass = new ModelAlbumClass();
                String id = cursor.getString(cursor.getColumnIndex(Albums._ID));
                String album = cursor.getString(cursor.getColumnIndex(Albums.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(Albums.ARTIST));
                String art = cursor.getString(cursor.getColumnIndex(Albums.ALBUM_ART));
                String noofSong = cursor.getString(cursor.getColumnIndex(Albums.NUMBER_OF_SONGS));

                albumClass.setId(id);
                albumClass.setAlbum(album);
                albumClass.setArtist(artist);
                albumClass.setArt(art);
                albumClass.setNoOfSongs(noofSong);

                albumList.add(albumClass);
            }
        }
        cursor.close();
    }*/

   /* public void getSongsArtistWise()
    {
        artistList = new ArrayList<>();
        artistList.clear();
        String[] projection = new String[]{MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Artists.NUMBER_OF_ALBUMS, MediaStore.Audio.Artists.NUMBER_OF_TRACKS};
        Cursor artistCursor = context.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, projection, null ,null, null);

        if(artistCursor != null)
        {
            while (artistCursor.moveToNext())
            {
                artistClass = new ModelArtistClass();
                String artistId = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists._ID));
                String artistName = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                String artistNoOfSongs = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
                //String image = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.Albums.ALBUM_ID));

                artistClass.setId(artistId);
                artistClass.setArtistName(artistName);
                artistClass.setNoOfSongs(artistNoOfSongs);

                Cursor c = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.ARTIST + "=?", new String[]{artistName}, null);
                if (c.getCount() > 0)
                {
                    while (c.moveToNext())
                    {
                        String  albumId = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                        String albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId)).toString();

                        if (c.isLast())
                        {
                            artistClass.setArt(albumArtUri);
                        }
                    }
                }

                artistList.add(artistClass);
            }
        }
        artistCursor.close();
    }*/
    public List<AudioModel> getFavouritesSongs()
    {
        favouritesList = new ArrayList<>();
        Cursor c = DB.getFavouritesSongs();
        if(c.getCount() > 0)
        {
            while (c.moveToNext())
            {
                audioModel = new AudioModel();
                audioModel.setPath(c.getString(c.getColumnIndex("path")));
                audioModel.setName(c.getString(c.getColumnIndex("name")));
                audioModel.setArtist(c.getString(c.getColumnIndex("artist")));
                audioModel.setAlbum(c.getString(c.getColumnIndex("album")));
                audioModel.setDuration(c.getString(c.getColumnIndex("duration")));
                audioModel.setId(c.getString(c.getColumnIndex("id")));
                audioModel.setAlbumArt(Uri.parse(c.getString(c.getColumnIndex("image"))));

                favouritesList.add(audioModel);
            }
        }
        c.close();
        return favouritesList;
    }
}
