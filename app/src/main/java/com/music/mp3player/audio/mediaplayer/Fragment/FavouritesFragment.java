package com.music.mp3player.audio.mediaplayer.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

import static com.music.mp3player.audio.mediaplayer.GetAllSongs.favouritesList;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.CHILD_FRAGMENT;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.r;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.toolbar;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.topNavigation;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.viewPager;

public class FavouritesFragment extends Fragment {

    private RecyclerView rvFavouritesList;
    private PlaylistAdapter adapter;
    private ImageView ivBack, ivSearch;
    DbHelper DB;
    private View view;
    private TextView ivNoData;
    private RelativeLayout rlFavouritesSongs;

    public FavouritesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_favourites, container, false);

        rlFavouritesSongs = view.findViewById(R.id.rlFavouritesSongs);

        rlFavouritesSongs.setBackgroundResource(R.drawable.bg_3);

        rvFavouritesList = view.findViewById(R.id.rvFavouritesList);
        ivBack = view.findViewById(R.id.ivBackFavourites);
        ivSearch = view.findViewById(R.id.ivSearch);
        ivNoData = view.findViewById(R.id.ivNoData);
        ivNoData.setVisibility(View.GONE);
        DB = new DbHelper(getContext());

        CHILD_FRAGMENT = true;
        toolbar.setVisibility(View.GONE);
        topNavigation.setVisibility(View.GONE);
        viewPager.setPagingEnabled(false);

        setFavouritesAdapter();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Fragment fragment = new MainScreenFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.flMainFrameLayout, fragment);
                transaction.remove(FavouritesFragment.this);*/
                toolbar.setVisibility(View.VISIBLE);
                topNavigation.setVisibility(View.VISIBLE);
                CHILD_FRAGMENT = false;
                getActivity().getSupportFragmentManager().popBackStack();
                viewPager.setPagingEnabled(true);
                //transaction.commit();
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

    public void setFavouritesAdapter()
    {
        GetAllSongs allSongs = new GetAllSongs(getContext());
        allSongs.getFavouritesSongs();

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager = llm;
        rvFavouritesList.setLayoutManager(layoutManager);

        if(favouritesList.size() > 0)
        {
            ivNoData.setVisibility(View.GONE);
        }
        else
        {
            ivNoData.setVisibility(View.VISIBLE);
            ivNoData.setText("No Favourits Found");
        }

        if(favouritesList != null) {
            adapter = new PlaylistAdapter(getContext(), favouritesList, new PlaylistAdapter.AddRemoveFavourites() {
                @Override
                public void onAddRemove(int position) {
                    addSongsToFavourites(position);
                }
            });
            rvFavouritesList.setAdapter(adapter);
        }

    }

    public void addSongsToFavourites(int position)
    {
        Cursor checkUser = DB.getFavouritesFromId(favouritesList.get(position).getName());

        if(checkUser.getCount() <= 0) {
            boolean checkAdd = DB.addToFavourites(favouritesList.get(position).getPath(), favouritesList.get(position).getName(), favouritesList.get(position).getArtist(), favouritesList.get(position).getAlbum(), favouritesList.get(position).getDuration(), favouritesList.get(position).getId(), favouritesList.get(position).getAlbumArt().toString());
            if(checkAdd)
            {
                Toast.makeText(getContext(), "Added to Favourites", Toast.LENGTH_SHORT).show();
                //ivFavouritesSong.setColorFilter(ContextCompat.getColor(PlayingSongActivity.getContext(, R.color.SelectedTab), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        }
        else
        {
            boolean checkDelFav = DB.removeFromFavourites(favouritesList.get(position).getName());
            if(checkDelFav) {
                Toast.makeText(getContext(), "Remove from Favourites", Toast.LENGTH_SHORT).show();
                setFavouritesAdapter();
                //ivFavouritesSong.setColorFilter(ContextCompat.getColor(PlayingSongActivity.getContext(, R.color.countTxt), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        }

    }
}
