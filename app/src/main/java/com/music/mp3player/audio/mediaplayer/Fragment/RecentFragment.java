package com.music.mp3player.audio.mediaplayer.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.music.mp3player.audio.mediaplayer.Adapter.PlaylistAdapter;
import com.music.mp3player.audio.mediaplayer.DataBase.DbHelper;
import com.music.mp3player.audio.mediaplayer.GetAllSongs;
import com.music.mp3player.audio.mediaplayer.R;
import com.music.mp3player.audio.mediaplayer.activities.MainActivity;
import com.music.mp3player.audio.mediaplayer.activities.SearchActivity;

import static com.music.mp3player.audio.mediaplayer.GetAllSongs.recentList;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.CHILD_FRAGMENT;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.r;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.toolbar;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.topNavigation;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.viewPager;

public class RecentFragment extends Fragment {

    private RecyclerView rvRecentList;
    private PlaylistAdapter adapter;
    private ImageView ivBack, ivSearch;
    DbHelper DB;
    private View view;
    private TextView ivNoData;
    private RelativeLayout rlRecentSongsMain;

    public RecentFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_recent_songs, container, false);

        rlRecentSongsMain = view.findViewById(R.id.rlRecentSongsMain);

        rlRecentSongsMain.setBackgroundResource(R.drawable.bg_3);

        rvRecentList = view.findViewById(R.id.rvRecentList);
        ivBack = view.findViewById(R.id.ivBackRecent);
        ivSearch = view.findViewById(R.id.ivSearch);
        ivNoData = view.findViewById(R.id.ivNoData);
        ivNoData.setVisibility(View.GONE);

        CHILD_FRAGMENT = true;
        toolbar.setVisibility(View.GONE);
        topNavigation.setVisibility(View.GONE);
        viewPager.setPagingEnabled(false);

        DB = new DbHelper(getContext());

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                setRecentAdapter();
            }
        },100);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Fragment fragment = new MainScreenFragment();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.flMainFrameLayout, fragment);
                toolbar.setVisibility(View.VISIBLE);
                topNavigation.setVisibility(View.VISIBLE);
                CHILD_FRAGMENT = false;
                getActivity().getSupportFragmentManager().popBackStack();
                viewPager.setPagingEnabled(true);
//                transaction.remove(RecentFragment.this);
//
//                transaction.commit();
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

    public void setRecentAdapter()
    {
        GetAllSongs allSongs = new GetAllSongs(getContext());
        allSongs.getRecentList();

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager = llm;
        rvRecentList.setLayoutManager(layoutManager);

        if(recentList.size() > 0)
        {
            ivNoData.setVisibility(View.GONE);
        }
        else
        {
            ivNoData.setVisibility(View.VISIBLE);
            ivNoData.setText("No Recent Songs Found");
        }

        if(recentList != null) {
            adapter = new PlaylistAdapter(getContext(), recentList, new PlaylistAdapter.AddRemoveFavourites() {
                @Override
                public void onAddRemove(int position) {
                    addSongsToFavourites(position);
                }
            });
            rvRecentList.setAdapter(adapter);
        }

    }

    public void addSongsToFavourites(int position)
    {
        Cursor checkUser = DB.getFavouritesFromId(recentList.get(position).getName());

        if(checkUser.getCount() <= 0) {
            boolean checkAdd = DB.addToFavourites(recentList.get(position).getPath(), recentList.get(position).getName(), recentList.get(position).getArtist(), recentList.get(position).getAlbum(), recentList.get(position).getDuration(), recentList.get(position).getId(), recentList.get(position).getAlbumArt().toString());
            if(checkAdd)
            {
                Toast.makeText(getContext(), "Added to Favourites", Toast.LENGTH_SHORT).show();
                //ivFavouritesSong.setColorFilter(ContextCompat.getColor(PlayingSongActivity.getContext(), R.color.SelectedTab), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        }
        else
        {
            boolean checkDelFav = DB.removeFromFavourites(recentList.get(position).getName());
            if(checkDelFav) {
                Toast.makeText(getContext(), "Remove from Favourites", Toast.LENGTH_SHORT).show();
                //ivFavouritesSong.setColorFilter(ContextCompat.getColor(PlayingSongActivity.getContext(), R.color.countTxt), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        }

        //setRecentAdapter();
    }
}
