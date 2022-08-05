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

import com.music.mp3player.audio.mediaplayer.Adapter.GenreSongsAdapter;
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

public class GenresSongsFragment extends Fragment {

    private ImageView ivBackGenre, ivSearch,ivMore;
    private RecyclerView rvGenreSongsList;
    private AudioModel audioModel;
    String genreId, genreName;
    public static List<AudioModel> genreSongs;
    private GenreSongsAdapter adapter;
    private TextView tvGenreText;
    private View view;
    private RelativeLayout rlGenreSongs;

    public GenresSongsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_genres_songs, container, false);

        rlGenreSongs = view.findViewById(R.id.rlGenreSongs);

        rlGenreSongs.setBackgroundResource(R.drawable.bg_3);

        ivBackGenre = view.findViewById(R.id.ivBackGenre);
        ivSearch = view.findViewById(R.id.ivSearch);
        ivMore = view.findViewById(R.id.ivMoreOption);
        rvGenreSongsList = view.findViewById(R.id.rvGenreSongsList);
        tvGenreText = view.findViewById(R.id.tvGenreText);

        CHILD_FRAGMENT = true;
        toolbar.setVisibility(View.GONE);
        topNavigation.setVisibility(View.GONE);
        viewPager.setPagingEnabled(false);
        /*Intent i = getIntent();
        genreId = i.getStringExtra("GenreId");
        genreName = i.getStringExtra("GenreName");*/

        genreId = this.getArguments().getString("GenreId");
        genreName = this.getArguments().getString("GenreName");

        getGenreSongsFromId(genreId);

        ivBackGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Fragment fragment = new LibraryFragment();
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.flMainFrameLayout, fragment);
//                transaction.remove(GenresSongsFragment.this);
                toolbar.setVisibility(View.VISIBLE);
                topNavigation.setVisibility(View.VISIBLE);
                CHILD_FRAGMENT = false;
                getActivity().getSupportFragmentManager().popBackStack();
                viewPager.setPagingEnabled(true);
                //transaction.commit();
               // genreList.clear();
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

    public void getGenreSongsFromId(String id)
    {
        genreSongs = new ArrayList<>();
        genreSongs.clear();
        long genreId = Long.parseLong(id);
        String[] projection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID};
        Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreId);

        Cursor c = getContext().getContentResolver().query(uri, projection, null,null,null);

        if(c.getCount() > 0)
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

                    genreSongs.add(audioModel);
                }
            }
        }

        setGenreSongsAdapter();
    }

    public void setGenreSongsAdapter()
    {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager = llm;
        rvGenreSongsList.setLayoutManager(layoutManager);

        if(recentList != null) {
            adapter = new GenreSongsAdapter(getContext(), genreSongs);
            rvGenreSongsList.setAdapter(adapter);
        }
    }
}
