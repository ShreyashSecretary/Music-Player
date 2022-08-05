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

import com.music.mp3player.audio.mediaplayer.Adapter.FolderSongsAdapter;
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

public class FolderSongsFragment extends Fragment {


    private ImageView ivBackFolderSongs, ivSearch, ivMore;
    private RecyclerView rvFolderSongsSongsList;
    private AudioModel audioModel;
    String folderName;
    public static List<AudioModel> folderSongsList;
    private FolderSongsAdapter adapter;
    private TextView tvFolderSongsText;
    private View view;
    private TextView ivNoData;
    private RelativeLayout rlFolderSongs;

    public FolderSongsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_folder_songs, container, false);

        rlFolderSongs = view.findViewById(R.id.rlFolderSongs);

        rlFolderSongs.setBackgroundResource(R.drawable.bg_3);

        ivBackFolderSongs = view.findViewById(R.id.ivBackFolderSongs);
        ivSearch = view.findViewById(R.id.ivSearch);
        ivMore = view.findViewById(R.id.ivMoreOption);
        rvFolderSongsSongsList = view.findViewById(R.id.rvFolderSongsList);
        tvFolderSongsText = view.findViewById(R.id.tvFolderSongsText);
        ivNoData = view.findViewById(R.id.ivNoData);
        ivNoData.setVisibility(View.GONE);

        CHILD_FRAGMENT = true;

        toolbar.setVisibility(View.GONE);
        topNavigation.setVisibility(View.GONE);
        viewPager.setPagingEnabled(false);
        //setMinimizePlayer();
         folderName = this.getArguments().getString("FolderName");
        /*Intent i = getIntent();
        folderName = i.getStringExtra("FolderName");*/

        tvFolderSongsText.setText(folderName);

        getFolderSongsFromFolderName();

        ivBackFolderSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.flMainFrameLayout);
                /*Fragment fragment = new FolderFragment(getActivity());
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.flMainFrameLayout, fragment);
                transaction.remove(FolderSongsFragment.this);*/
                toolbar.setVisibility(View.VISIBLE);
                topNavigation.setVisibility(View.VISIBLE);
                CHILD_FRAGMENT = false;
                getActivity().getSupportFragmentManager().popBackStack();
                viewPager.setPagingEnabled(true);

               // transaction.commit();
                //folderList.clear();
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

    private void getFolderSongsFromFolderName() {
        folderSongsList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.Media.ALBUM_ID};


        Cursor c = getContext().getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%" + folderName + "%"}, null);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                audioModel = new AudioModel();
                String url = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
                String name = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String songArtist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String songAlbum = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String songid = c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID));
                String duration = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String albumId = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId));

                if(duration != null) {
                    audioModel.setName(name);
                    audioModel.setPath(url);
                    audioModel.setArtist(songArtist);
                    audioModel.setArtist(songArtist);
                    audioModel.setDuration(duration);
                    audioModel.setId(songid);
                    audioModel.setAlbumArt(albumArtUri);

                    folderSongsList.add(audioModel);
                }
            }
        }
        c.close();

        setFolderSongsAdapter();
    }

    private void setFolderSongsAdapter() {

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager = llm;
        rvFolderSongsSongsList.setLayoutManager(layoutManager);

        if(folderSongsList.size() > 0)
        {
            ivNoData.setVisibility(View.GONE);
        }
        else
        {
            ivNoData.setVisibility(View.VISIBLE);
        }

        adapter = new FolderSongsAdapter(getContext(), folderSongsList);
        rvFolderSongsSongsList.setAdapter(adapter);

    }

    /*public void setMinimizePlayer()
    {
        Fragment bottomFragment = new MinimizedPlayerFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //transaction.replace(R.id.flMinimizedPlayer, bottomFragment);
        transaction.add(R.id.flMinimizedPlayer, bottomFragment);

        transaction.commit();
    }*/
}
