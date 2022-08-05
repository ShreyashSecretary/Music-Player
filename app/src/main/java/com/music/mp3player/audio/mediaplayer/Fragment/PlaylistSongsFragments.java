package com.music.mp3player.audio.mediaplayer.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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

import com.music.mp3player.audio.mediaplayer.Adapter.CreatedPlaylistSongsAdapter;
import com.music.mp3player.audio.mediaplayer.DataBase.DbHelper;
import com.music.mp3player.audio.mediaplayer.ModelClass.AudioModel;
import com.music.mp3player.audio.mediaplayer.R;
import com.music.mp3player.audio.mediaplayer.activities.MainActivity;
import com.music.mp3player.audio.mediaplayer.activities.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.CHILD_FRAGMENT;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.r;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.toolbar;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.topNavigation;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.viewPager;

public class PlaylistSongsFragments extends Fragment {

    private ImageView ivBack, ivSearch, ivMore;
    private TextView tvPlaylistName;
    private RecyclerView rvPlaylistList;
    private CreatedPlaylistSongsAdapter adapter;
    private String playlistName;
    public static List<AudioModel> myPlaylist;
    private AudioModel audioModel;
    DbHelper DB;
    private View view;
    private TextView ivNoData;
    private RelativeLayout rlPlaylistSongs;

    public PlaylistSongsFragments() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_playlist_songs, container, false);

        rlPlaylistSongs = view.findViewById(R.id.rlPlaylistSongs);

        rlPlaylistSongs.setBackgroundResource(R.drawable.bg_3);

        ivBack = view.findViewById(R.id.ivBackPlaylist);
        ivSearch = view.findViewById(R.id.ivSearch);
        ivMore = view.findViewById(R.id.ivMoreOption);
        tvPlaylistName = view.findViewById(R.id.tvPlaylistText);
        rvPlaylistList = view.findViewById(R.id.rvPlaylistList);
        ivNoData = view.findViewById(R.id.ivNoData);
        ivNoData.setVisibility(View.GONE);

        CHILD_FRAGMENT = true;
        toolbar.setVisibility(View.GONE);
        topNavigation.setVisibility(View.GONE);
        viewPager.setPagingEnabled(false);

        DB = new DbHelper(getContext());
        /*Intent i = getIntent();
        playlistName = i.getStringExtra("PlaylistName");*/
        playlistName = this.getArguments().getString("PlaylistName");

        tvPlaylistName.setText(playlistName);

        SharedPreferences.Editor editor = getContext().getSharedPreferences("MyPref", MODE_PRIVATE).edit();
        editor.putString("PlaylistName", playlistName);
        editor.apply();


        setPlaylistAdapter();

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

    private void setPlaylistAdapter() {

        myPlaylist = new ArrayList<>();
        myPlaylist.clear();
        Cursor c = DB.getPlaylistSongs(playlistName);
        if(c.getCount() > 0)
        {
            while (c.moveToNext())
            {
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

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager = llm;
        rvPlaylistList.setLayoutManager(layoutManager);

        if(myPlaylist != null) {
            adapter = new CreatedPlaylistSongsAdapter(getContext(), myPlaylist, new CreatedPlaylistSongsAdapter.AddRemoveFavourites() {
                @Override
                public void onAddRemove(int position) {
                    addSongsToFavourites(position);
                }
            }, new CreatedPlaylistSongsAdapter.RemovedFromPlaylist() {
                @Override
                public void removeSong(int position) {
                    showRemoveDialog(position);
                }
            });
            rvPlaylistList.setAdapter(adapter);
        }

        if(myPlaylist.size() > 0)
        {
            ivNoData.setVisibility(View.GONE);
        }
        else
        {
            ivNoData.setVisibility(View.VISIBLE);
            ivNoData.setText("No Songs Found");
        }
    }

    private void showRemoveDialog(int position) {

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.delete_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvYes = dialog.findViewById(R.id.tvYes);
        TextView tvNo = dialog.findViewById(R.id.tvNo);

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor checkUser = DB.getAlreadyAddedSongs(myPlaylist.get(position).getName(), playlistName);

                if(checkUser.getCount() > 0)
                {
                    boolean deleteSong = DB.removeFromCreatedPlaylist(playlistName, myPlaylist.get(position).getName());
                    if(deleteSong)
                    {
                        Toast.makeText(getContext(), "Song Deleted", Toast.LENGTH_SHORT).show();
                    }
                }
                setPlaylistAdapter();
                dialog.dismiss();
            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void addSongsToFavourites(int position)
    {
        Cursor checkUser = DB.getFavouritesFromId(myPlaylist.get(position).getName());

        if(checkUser.getCount() <= 0) {
            boolean checkAdd = DB.addToFavourites(myPlaylist.get(position).getPath(), myPlaylist.get(position).getName(), myPlaylist.get(position).getArtist(), myPlaylist.get(position).getAlbum(), myPlaylist.get(position).getDuration(), myPlaylist.get(position).getId(), myPlaylist.get(position).getAlbumArt().toString());
            if(checkAdd)
            {
                Toast.makeText(getContext(), "Added to Favourites", Toast.LENGTH_SHORT).show();
                //ivFavouritesSong.setColorFilter(ContextCompat.getColor(PlayingSongActivity.getContext(), R.color.SelectedTab), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        }
        else
        {
            boolean checkDelFav = DB.removeFromFavourites(myPlaylist.get(position).getName());
            if(checkDelFav) {
                Toast.makeText(getContext(), "Remove from Favourites", Toast.LENGTH_SHORT).show();
                //ivFavouritesSong.setColorFilter(ContextCompat.getColor(PlayingSongActivity.getContext(), R.color.countTxt), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        }

        //setPlaylistAdapter();
    }
}
