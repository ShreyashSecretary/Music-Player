package com.music.mp3player.audio.mediaplayer.Fragment;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.music.mp3player.audio.mediaplayer.Adapter.AlbumSongsAdapter;
import com.music.mp3player.audio.mediaplayer.ModelClass.AudioModel;
import com.music.mp3player.audio.mediaplayer.R;
import com.music.mp3player.audio.mediaplayer.activities.MainActivity;
import com.music.mp3player.audio.mediaplayer.activities.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import static com.music.mp3player.audio.mediaplayer.GetAllSongs.sArtworkUri;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.CHILD_FRAGMENT;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.r;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.toolbar;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.topNavigation;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.viewPager;

public class AlbumSongsFragment extends Fragment {

    private ImageView ivBackAlbum, ivSearch, ivMore;
    private RecyclerView rvAlbumSongsList;
    String albumName;
    private AudioModel audioModel;
    public static List<AudioModel> albumSongs;
    private AlbumSongsAdapter adapter;
    private TextView tvAlbumText;
    private View view;
    private TextView ivNoData;
    private RelativeLayout rlAlbumSongs;

    public AlbumSongsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_album_songs, container, false);

        rlAlbumSongs = view.findViewById(R.id.rlAlbumSongs);

        rlAlbumSongs.setBackgroundResource(R.drawable.bg_3);

        ivBackAlbum = view.findViewById(R.id.ivBackAlbum);
        ivSearch = view.findViewById(R.id.ivSearch);
        ivMore = view.findViewById(R.id.ivMoreOption);
        rvAlbumSongsList = view.findViewById(R.id.rvAlbumSongsList);
        tvAlbumText = view.findViewById(R.id.tvAlbumText);
        ivNoData = view.findViewById(R.id.ivNoData);
        ivNoData.setVisibility(View.GONE);

        CHILD_FRAGMENT = true;

        toolbar.setVisibility(View.GONE);
        topNavigation.setVisibility(View.GONE);
        /*Intent i = getIntent();
        albumName = i.getStringExtra("AlbumName");*/

        viewPager.setPagingEnabled(false);

        albumName = this.getArguments().getString("AlbumName");

        getAlbumSongsFromName(albumName);

        ivBackAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Fragment fragment = new LibraryFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.flMainFrameLayout, fragment);
                transaction.remove(AlbumSongsFragment.this);*/
                toolbar.setVisibility(View.VISIBLE);
                topNavigation.setVisibility(View.VISIBLE);
                CHILD_FRAGMENT = false;
                getActivity().getSupportFragmentManager().popBackStack();
                viewPager.setPagingEnabled(true);
                //transaction.commit();
                //albumList.clear();
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SearchActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    private void getAlbumSongsFromName(String albumName) {
        albumSongs = new ArrayList<>();
        albumSongs.clear();
        String[] songProjection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID};

        String selection = "is_music != 0";
        if (Long.parseLong(albumName) > 0) {
            selection = selection + " and album_id = " + Long.parseLong(albumName);
        }


        Cursor c = getContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songProjection, selection, null, null);

        if (c != null) {
            while (c.moveToNext()) {
                audioModel = new AudioModel();
                String url = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
                String name = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String songAlbum = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String songArtist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String songid = c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID));
                String duration = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String albumId = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId));

                if(duration != null) {
                    audioModel.setName(name);
                    audioModel.setPath(url);
                    audioModel.setAlbum(songAlbum);
                    audioModel.setArtist(songArtist);
                    audioModel.setDuration(duration);
                    audioModel.setId(songid);
                    audioModel.setAlbumArt(albumArtUri);

                    albumSongs.add(audioModel);
                }
            }
        }

        setAlbumSongListAdapter();
    }

    private void setAlbumSongListAdapter() {

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager = llm;
        rvAlbumSongsList.setLayoutManager(layoutManager);

        if (albumSongs.size() > 0)
        {
            ivNoData.setVisibility(View.GONE);
        }
        else
        {
            ivNoData.setVisibility(View.VISIBLE);
        }

        adapter = new AlbumSongsAdapter(getContext(), albumSongs);
        rvAlbumSongsList.setAdapter(adapter);
    }

    /*@Override
    public void onResume() {
        super.onResume();

        albumName = this.getArguments().getString("AlbumName");
        getAlbumSongsFromName(albumName);
    }*/

}
