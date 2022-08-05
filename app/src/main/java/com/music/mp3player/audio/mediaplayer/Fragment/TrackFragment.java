package com.music.mp3player.audio.mediaplayer.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.music.mp3player.audio.mediaplayer.Adapter.PlaylistAdapter;
import com.music.mp3player.audio.mediaplayer.Adapter.TrackAdapter;
import com.music.mp3player.audio.mediaplayer.DataBase.DbHelper;
import com.music.mp3player.audio.mediaplayer.ModelClass.AudioModel;
import com.music.mp3player.audio.mediaplayer.R;

import java.util.ArrayList;
import java.util.List;

import static com.music.mp3player.audio.mediaplayer.GetAllSongs.audioList;
import static com.music.mp3player.audio.mediaplayer.GetAllSongs.sArtworkUri;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.tvToolbarText;

public class TrackFragment extends Fragment{

    private View view;
    private RecyclerView rvTrackList;
    private TrackAdapter trackAdapter;
    private List<AudioModel> songList = new ArrayList<>();
    private TextView ivNoData;
    DbHelper DB;
    private AudioModel audioModel;
    private ProgressDialog dialog;
    private Activity context;

    public TrackFragment(Activity context) {
        this.context = context;
        tvToolbarText.setText("Tracks");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_track, container, false);

        rvTrackList = view.findViewById(R.id.rvTrackList);
        ivNoData = view.findViewById(R.id.ivNoData);
        ivNoData.setVisibility(View.GONE);

        DB = new DbHelper(getContext());

        dialog = new ProgressDialog(getContext(), R.style.DialogStyle);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.progress_dialog_layout);

        //dialog.show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
               songList = getAudioFiles();
               // dialog.dismiss();

                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setTrackAdapter();
                    }
                });
            }
        }, 500);



        return view;
    }

    public void setTrackAdapter() {
//        GetAllSongs allSongs = new GetAllSongs(getContext());
//        allSongs.getAudioFiles();
        //songList = audioList;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager = linearLayoutManager;
        rvTrackList.setLayoutManager(layoutManager);

        trackAdapter = new TrackAdapter(getContext(), audioList, new TrackAdapter.AddRemoveFavourites() {
            @Override
            public void onAddRemove(int position) {
                addSongsToFavourites(position);
            }
        });
        rvTrackList.setAdapter(trackAdapter);

       // new Thread(this::getAudioFiles).start();
        //getAudioFiles();
    }

    public void addSongsToFavourites(int position)
    {
        Cursor checkUser = DB.getFavouritesFromId(audioList.get(position).getName());

        if(checkUser.getCount() <= 0) {
            boolean checkAdd = DB.addToFavourites(audioList.get(position).getPath(), audioList.get(position).getName(), audioList.get(position).getArtist(), audioList.get(position).getAlbum(), audioList.get(position).getDuration(), audioList.get(position).getId(), audioList.get(position).getAlbumArt().toString());
            if(checkAdd)
            {
                Toast.makeText(getContext(), "Added to Favourites", Toast.LENGTH_SHORT).show();
                //ivFavouritesSong.setColorFilter(ContextCompat.getColor(PlayingSongActivity.this, R.color.SelectedTab), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        }
        else
        {
            boolean checkDelFav = DB.removeFromFavourites(audioList.get(position).getName());
            if(checkDelFav) {
                Toast.makeText(getContext(), "Remove from Favourites", Toast.LENGTH_SHORT).show();
                //ivFavouritesSong.setColorFilter(ContextCompat.getColor(PlayingSongActivity.this, R.color.countTxt), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        }


    }

    public List<AudioModel> getAudioFiles()
    {
        List<String> folders = new ArrayList<>();
        audioList = new ArrayList<>();
        audioList.clear();
        String[] projection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.Media.ALBUM_ID};
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = context.getContentResolver().query(uri, projection, null, null, null);
        if(musicCursor != null)
        {
            while (musicCursor.moveToNext())
            {
                audioModel = new AudioModel();
                String url = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
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

}
