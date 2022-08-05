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

import com.music.mp3player.audio.mediaplayer.Adapter.ArtistSongsAdapter;
import com.music.mp3player.audio.mediaplayer.ModelClass.AudioModel;
import com.music.mp3player.audio.mediaplayer.R;
import com.music.mp3player.audio.mediaplayer.activities.MainActivity;
import com.music.mp3player.audio.mediaplayer.activities.SearchActivity;

import java.util.ArrayList;
import java.util.List;


import static com.music.mp3player.audio.mediaplayer.GetAllSongs.recentList;
import static com.music.mp3player.audio.mediaplayer.GetAllSongs.sArtworkUri;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.CHILD_FRAGMENT;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.r;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.toolbar;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.topNavigation;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.viewPager;

public class ArtistSongsFragment extends Fragment {

    private ImageView ivBackArtist, ivSearch,ivMore;
    private RecyclerView rvArtistSongsList;
    private AudioModel audioModel;
    String artistName;
    public static List<AudioModel> artistSongs;
    private ArtistSongsAdapter adapter;
    private TextView tvArtistText;
    private View view;
    private TextView ivNoData;
    private RelativeLayout rlArtistSongs;

    public ArtistSongsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_artist, container, false);

        rlArtistSongs = view.findViewById(R.id.rlArtistSongs);

        rlArtistSongs.setBackgroundResource(R.drawable.bg_3);

        ivBackArtist = view.findViewById(R.id.ivBackArtist);
        ivSearch = view.findViewById(R.id.ivSearch);
        ivMore = view.findViewById(R.id.ivMoreOption);
        rvArtistSongsList = view.findViewById(R.id.rvArtistSongsList);
        tvArtistText = view.findViewById(R.id.tvArtistText);
        ivNoData = view.findViewById(R.id.ivNoData);
        ivNoData.setVisibility(View.GONE);

        CHILD_FRAGMENT = true;

        /*Intent i = getIntent();
        artistName = i.getStringExtra("ArtistName");*/

        artistName = this.getArguments().getString("ArtistName");

        getArtistSongsFromName(artistName);

        toolbar.setVisibility(View.GONE);
        topNavigation.setVisibility(View.GONE);
        viewPager.setPagingEnabled(false);

        ivBackArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Fragment fragment = new LibraryFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.flMainFrameLayout, fragment);
                transaction.remove(ArtistSongsFragment.this);*/
                toolbar.setVisibility(View.VISIBLE);
                topNavigation.setVisibility(View.VISIBLE);
                CHILD_FRAGMENT = false;
                getActivity().getSupportFragmentManager().popBackStack();
                viewPager.setPagingEnabled(true);

                //transaction.commit();
              //  artistList.clear();
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

    private void getArtistSongsFromName(String artistName)
    {
        artistSongs = new ArrayList<>();
        artistSongs.clear();
        String[] songProjection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID};

        Cursor c = getContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songProjection, MediaStore.Audio.Media.ARTIST + "=?", new String[]{artistName}, null);

        if (c != null)
        {
            while (c.moveToNext())
            {
                audioModel = new AudioModel();
                String url = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
                String name = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String songArtist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String songAlbum = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String songid = c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID));
                String duration = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String  albumId = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId));

                if(duration != null) {
                    audioModel.setName(name);
                    audioModel.setPath(url);
                    audioModel.setArtist(songArtist);
                    audioModel.setArtist(songArtist);
                    audioModel.setDuration(duration);
                    audioModel.setId(songid);
                    audioModel.setAlbumArt(albumArtUri);

                    artistSongs.add(audioModel);
                }
            }
        }

        setArtistSongListAdapter();
    }

    private void setArtistSongListAdapter() {

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager = llm;
        rvArtistSongsList.setLayoutManager(layoutManager);

        if(artistSongs.size() > 0)
        {
            ivNoData.setVisibility(View.GONE);
        }
        else
        {
            ivNoData.setVisibility(View.VISIBLE);
        }

        if(recentList != null) {
            adapter = new ArtistSongsAdapter(getContext(), artistSongs);
            rvArtistSongsList.setAdapter(adapter);
        }
    }

    /*@Override
    public void onResume() {
        super.onResume();

        artistName = this.getArguments().getString("ArtistName");

        getArtistSongsFromName(artistName);
    }*/
}
