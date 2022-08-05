package com.music.mp3player.audio.mediaplayer.Adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.music.mp3player.audio.mediaplayer.AddToPlaylistClass;
import com.music.mp3player.audio.mediaplayer.DataBase.DbHelper;
import com.music.mp3player.audio.mediaplayer.ModelClass.AudioModel;
import com.music.mp3player.audio.mediaplayer.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.mainActivity;

//import static com.example.musicplayer.Service.MusicService.mediaPlayer;


public class SearchSongsAdapter extends RecyclerView.Adapter<SearchSongsAdapter.ViewHolder> {

    private Context context;
    private List<AudioModel> searchList;
    private DbHelper DB;

    public SearchSongsAdapter(Context context, List<AudioModel> searchList) {
        this.context = context;
        this.searchList = searchList;
        DB = new DbHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.playlist_item_layout, parent, false);
        return new ViewHolder(view);
    }

    public void filterList(ArrayList<AudioModel> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        searchList = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String name = searchList.get(position).getName();
        String artist = searchList.get(position).getArtist();
        String album = searchList.get(position).getAlbum();
        String duration = searchList.get(position).getDuration();
        String id = searchList.get(position).getId();
        String url = searchList.get(position).getPath();
        Uri albumArtUri = searchList.get(position).getAlbumArt();

        //Cursor c = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums.Al})
        Bitmap bitmap = null;

        //ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(), albumArtUri);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), albumArtUri);
            // bitmap = ImageDecoder.decodeBitmap(source);
        } catch (FileNotFoundException exception) {
        } catch (IOException e) {

            e.printStackTrace();
        }

        if (bitmap != null) {
            holder.ivPlaylistItem.setImageBitmap(bitmap);
        }
        else
        {
            holder.ivPlaylistItem.setImageResource(R.drawable.music_photo);
        }

        holder.tvPlaylistCount.setText(artist);
        holder.tvPlaylistName.setText(name);

        holder.rlPlaylistItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(context, MainActivity.class);
                i.putExtra("ActivityName", "list");
                i.putExtra("Position", position);*/
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(holder.rlPlaylistItem.getWindowToken(), 0);
//                context.startActivity(i);
                mainActivity.onClickSong(position, "list");
                ((Activity)context).finish();
                //song.onPlaying(position, name, artist);
            }
        });

        holder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.ivMore);
                popupMenu.inflate(R.menu.popup_menu);
                Menu opt = popupMenu.getMenu();

                Cursor checkFav = DB.getFavouritesFromId(name);
                if(checkFav.getCount() > 0)
                {
                    opt.getItem(0).setTitle("Remove From Favourites");
                }
                else
                {
                    opt.getItem(0).setTitle("Add to Favourites");
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getTitle().equals("Add to Favourites") || menuItem.getTitle().equals("Remove From Favourites")) {
                            addSongsToFavourites(position);
                        }
                        else
                        {
                            AddToPlaylistClass add = new AddToPlaylistClass(context, position, searchList);
                            add.showBottomSheetDialog();
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPlaylistItem, ivMore;
        private TextView tvPlaylistName, tvPlaylistCount;
        private View vPlaylistDivider;
        private RelativeLayout rlPlaylistItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            ivPlaylistItem = itemView.findViewById(R.id.ivPlayListItem);
            ivMore = itemView.findViewById(R.id.ivMoreOptionPlaylistItem);
            tvPlaylistName = itemView.findViewById(R.id.tvPlaylistName);
            tvPlaylistCount = itemView.findViewById(R.id.tvPlaylistSongCount);
            vPlaylistDivider = itemView.findViewById(R.id.vPlaylistDivider);
            rlPlaylistItem = itemView.findViewById(R.id.rlPlaylistItem);
        }
    }

    public void addSongsToFavourites(int position)
    {
        Cursor checkUser = DB.getFavouritesFromId(searchList.get(position).getName());

        if(checkUser.getCount() <= 0) {
            boolean checkAdd = DB.addToFavourites(searchList.get(position).getPath(), searchList.get(position).getName(), searchList.get(position).getArtist(), searchList.get(position).getAlbum(), searchList.get(position).getDuration(), searchList.get(position).getId(), searchList.get(position).getAlbumArt().toString());
            if(checkAdd)
            {
                Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show();
                //ivFavouritesSong.setColorFilter(ContextCompat.getColor(PlayingSongActivity.this, R.color.SelectedTab), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        }
        else
        {
            boolean checkDelFav = DB.removeFromFavourites(searchList.get(position).getName());
            if(checkDelFav) {
                Toast.makeText(context, "Remove from Favourites", Toast.LENGTH_SHORT).show();
                //ivFavouritesSong.setColorFilter(ContextCompat.getColor(PlayingSongActivity.this, R.color.countTxt), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        }
    }
}
