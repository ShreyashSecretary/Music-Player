package com.music.mp3player.audio.mediaplayer.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.music.mp3player.audio.mediaplayer.Adapter.TabAdapter;
import com.music.mp3player.audio.mediaplayer.ModelClass.AudioModel;
import com.music.mp3player.audio.mediaplayer.R;
//import com.example.musicplayer.activities.PlayingSongActivity;
import com.music.mp3player.audio.mediaplayer.activities.SearchActivity;
import com.google.android.material.tabs.TabLayout;

import static android.content.Context.MODE_PRIVATE;
//import static com.example.musicplayer.activities.PlayingSongActivity.ivPlayPause;

public class LibraryFragment extends Fragment {

    private ImageView ivSearch, ivMore, ivBack, ivPlayPauseLibrary, ivPauseSong;
    private TabLayout topNavigation;
    private ViewPager viewPager;
    public TextView tvPlayingSongArtist, tvPlayingSongName;
    private RelativeLayout rlPlayingSong;
    private SharedPreferences pref;
    public static String name, artist, actName;
    public static int pos;
    public static ImageView ivPlayingSongImage;
    private String MY_SHORT_PREF = "SortOrder";
    private View view;
    private Dialog dialog;
    AudioModel audioModel;

    public LibraryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_library, container, false);

        ivBack = view.findViewById(R.id.ivBackLibrary);
        ivSearch = view.findViewById(R.id.ivSearch);
        ivMore = view.findViewById(R.id.ivMoreOption);
        //ivPlayingSongImage = view.findViewById(R.id.ivPlayingSongImage);
        topNavigation = view.findViewById(R.id.tlTopNavigation);
        viewPager = view.findViewById(R.id.view_pager);
       topNavigation.setTabGravity(TabLayout.GRAVITY_START);
       topNavigation.setHorizontalScrollBarEnabled(true);

        /*dialog = new ProgressDialog(getContext(), R.style.DialogStyle);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.progress_dialog_layout);

        dialog.show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
               *//* GetAllSongs allSongs = new GetAllSongs(getContext());
                allSongs.getAudioFiles();*//*
                getAudioFiles();
                dialog.dismiss();
            }
        }, 2000);*/

        final TabAdapter adapter = new TabAdapter(getContext(), getChildFragmentManager(), topNavigation.getTabCount(), getActivity());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(topNavigation));
        viewPager.setOffscreenPageLimit(6);
        topNavigation.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager){
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                super.onTabSelected(tab);
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });
        //viewPager.setCurrentItem(0);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MainScreenFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.flMainFrameLayout, fragment);
                transaction.remove(LibraryFragment.this);
                getActivity().getSupportFragmentManager().popBackStack();
                transaction.commit();
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SearchActivity.class);
                startActivity(i);
            }
        });

        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu();
            }
        });

        return view;
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(), ivMore);
        popupMenu.inflate(R.menu.more_option_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences(MY_SHORT_PREF, MODE_PRIVATE).edit();
                switch (menuItem.getItemId()) {
                    case R.id.by_name:
                        editor.putString("sorting", "sortByName");
                        editor.apply();
                        break;

                    case R.id.by_date:
                        editor.putString("sorting", "sortByDate");
                        editor.apply();
                        break;

                    case R.id.by_size:
                        editor.putString("sorting", "sortBySize");
                        editor.apply();
                        break;

                }
                return true;
            }
        });

        popupMenu.show();
    }

    /*public List<AudioModel> getAudioFiles()
    {
        List<String> folders = new ArrayList<>();
        audioList = new ArrayList<>();
        audioList.clear();
        String[] projection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.Media.ALBUM_ID};
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
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
*//*
                if(musicCursor.isLast())
                {
                    Constants.songList = audioList;
                }*//*
            }
            musicCursor.close();
        }

        return audioList;
    }*/

}
