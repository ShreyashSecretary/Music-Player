package com.music.mp3player.audio.mediaplayer;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.music.mp3player.audio.mediaplayer.Adapter.CreatedPlaylistAdapter;
import com.music.mp3player.audio.mediaplayer.DataBase.DbHelper;
import com.music.mp3player.audio.mediaplayer.ModelClass.AudioModel;
//import com.example.musicplayer.activities.PlayingSongActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class AddToPlaylistClass {

    private List<AudioModel> songsList;
    private DbHelper DB;
    private Context context;
    private CreatedPlaylistAdapter createdPlaylistAdapter;
    public static List<String> createdPlaylist;
    private int position;

    public AddToPlaylistClass(Context context, int position, List<AudioModel> songsList) {
        this.context = context;
        this.position = position;
        this.songsList = songsList;
        DB = new DbHelper(context);
    }


    public void showBottomSheetDialog() {

        createdPlaylist = new ArrayList<>();
        BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.AppBottomSheetDialogTheme);
        dialog.setContentView(R.layout.bottom_sheet_dialog);
        dialog.setCancelable(true);

        RelativeLayout rlCreateNewPlaylist = dialog.findViewById(R.id.rlCreateNewPlaylist);
        RelativeLayout rlFavouriteLayout = dialog.findViewById(R.id.rlFavouriteLayout);
        RecyclerView rvNewCreatedPlaylist = dialog.findViewById(R.id.rvNewCreatedPlaylist);

        rlFavouriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor checkUser = DB.getFavouritesFromId(songsList.get(position).getName());

                if(checkUser.getCount() <= 0) {
                    boolean checkAdd = DB.addToFavourites(songsList.get(position).getPath(), songsList.get(position).getName(), songsList.get(position).getArtist(), songsList.get(position).getAlbum(), songsList.get(position).getDuration(), songsList.get(position).getId(), songsList.get(position).getAlbumArt().toString());
                    if(checkAdd)
                    {
                        Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show();
                        //ivFavouritesSong.setColorFilter(ContextCompat.getColor(PlayingSongActivity.this, R.color.SelectedTab), android.graphics.PorterDuff.Mode.MULTIPLY);
                    }
                }
                else
                {
                    Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        rlCreateNewPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreatePlaylistDialog();
                dialog.dismiss();
            }
        });

        Cursor c = DB.getAllPlaylist();
        createdPlaylist.clear();
        if(c.getCount() > 0)
        {
            while (c.moveToNext())
            {
                createdPlaylist.add(c.getString(c.getColumnIndex("name")));
            }
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        RecyclerView.LayoutManager layoutManager = linearLayoutManager;
        rvNewCreatedPlaylist.setLayoutManager(layoutManager);

        createdPlaylistAdapter = new CreatedPlaylistAdapter(context, createdPlaylist, new CreatedPlaylistAdapter.addToPlaylistInterface() {
            @Override
            public void addToPlaylist(int pos, String playlistName) {
                Cursor checkUser = DB.getAlreadyAddedSongs(songsList.get(position).getName(), playlistName);

                if(checkUser.getCount() <= 0) {
                    boolean checkAdd = DB.insetIntoCreatedTable(playlistName, songsList.get(position).getPath(), songsList.get(position).getName(), songsList.get(position).getArtist(), songsList.get(position).getAlbum(), songsList.get(position).getDuration(), songsList.get(position).getId(), songsList.get(position).getAlbumArt().toString());
                    if(checkAdd)
                    {
                        Toast.makeText(context, "Added to Playlist", Toast.LENGTH_SHORT).show();
                        //ivFavouritesSong.setColorFilter(ContextCompat.getColor(PlayingSongActivity.this, R.color.SelectedTab), android.graphics.PorterDuff.Mode.MULTIPLY);
                    }
                }
                else
                {
                    Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        rvNewCreatedPlaylist.setAdapter(createdPlaylistAdapter);

        dialog.show();

    }

    public void showCreatePlaylistDialog()
    {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.new_playlist_name_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvCreate = dialog.findViewById(R.id.tvCreate);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);
        EditText edtNewPlaylist = dialog.findViewById(R.id.edtPlaylistName);

        edtNewPlaylist.setFocusable(true);

        tvCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB.createPlaylist(edtNewPlaylist.getText().toString());
                DB.createTable(edtNewPlaylist.getText().toString());
                DB.insetIntoCreatedTable(edtNewPlaylist.getText().toString(), songsList.get(position).getPath(), songsList.get(position).getName(), songsList.get(position).getArtist(), songsList.get(position).getAlbum(), songsList.get(position).getDuration(), songsList.get(position).getId(), songsList.get(position).getAlbumArt().toString());
                Toast.makeText(context, "Playlist created", Toast.LENGTH_SHORT).show();
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                dialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        dialog.show();
    }
}
