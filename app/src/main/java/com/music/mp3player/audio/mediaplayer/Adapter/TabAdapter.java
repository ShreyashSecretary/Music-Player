package com.music.mp3player.audio.mediaplayer.Adapter;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.music.mp3player.audio.mediaplayer.Fragment.AlbumFragment;
import com.music.mp3player.audio.mediaplayer.Fragment.ArtistFragment;
import com.music.mp3player.audio.mediaplayer.Fragment.FolderFragment;
import com.music.mp3player.audio.mediaplayer.Fragment.GenreFragment;
import com.music.mp3player.audio.mediaplayer.Fragment.MainScreenFragment;
import com.music.mp3player.audio.mediaplayer.Fragment.TrackFragment;

public class TabAdapter extends FragmentPagerAdapter {

    private Context context;
    private Activity activity;
    int totalTabs;

    public TabAdapter(Context context, FragmentManager fm, int totalTabs, Activity activity) {
        super(fm, totalTabs);
        this.context = context;
        this.activity = activity;
        this.totalTabs = totalTabs;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if (position == 0)
        {
            fragment = new TrackFragment(activity);
        }
        else if (position == 1)
        {
            fragment = new ArtistFragment(activity);
        }
        else if (position == 2)
        {
            fragment = new AlbumFragment(activity);
        }
        else if (position == 3)
        {
            fragment = new MainScreenFragment();
        }
        else if (position == 4)
        {
            fragment = new GenreFragment(activity);
        }
        else
        {
            fragment = new FolderFragment(activity);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }
}
