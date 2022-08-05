package com.music.mp3player.audio.mediaplayer.Adapter;

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
import java.util.List;

import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.mainActivity;

//import static com.example.musicplayer.Service.MusicService.mediaPlayer;

public class FolderSongsAdapter extends RecyclerView.Adapter<FolderSongsAdapter.ViewHolder> {

    private Context context;
    private List<AudioModel> folderSongList;
    //private folderSongListAdapter.playingSong song;
    private DbHelper DB;

    public FolderSongsAdapter(Context context, List<AudioModel> folderSongList) {
        this.context = context;
        this.folderSongList = folderSongList;
        //this.song = song;
        DB = new DbHelper(context);
    }
    
    @NonNull
    @Override
    public FolderSongsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.playlist_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderSongsAdapter.ViewHolder holder, int position) {

        String name = folderSongList.get(position).getName();
        String artist = folderSongList.get(position).getArtist();
        Uri albumArtUri = folderSongList.get(position).getAlbumArt();

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
            holder.ivfolderSongListItem.setImageBitmap(bitmap);
        }
        else
        {
            holder.ivfolderSongListItem.setImageResource(R.drawable.music_photo);
        }
        holder.tvfolderSongListCount.setText(artist);
        holder.tvfolderSongListName.setText(name);

        holder.rlfolderSongListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i = new Intent(context, MainActivity.class);
                i.putExtra("ActivityName", "Folder");
                i.putExtra("Position", position);
                context.startActivity(i);*/
                mainActivity.onClickSong(position, "Folder");

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
                            AddToPlaylistClass add = new AddToPlaylistClass(context, position, folderSongList);
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
        return folderSongList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivfolderSongListItem, ivMore;
        private TextView tvfolderSongListName, tvfolderSongListCount;
        private View vfolderSongListDivider;
        private RelativeLayout rlfolderSongListItem;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivfolderSongListItem = itemView.findViewById(R.id.ivPlayListItem);
            ivMore = itemView.findViewById(R.id.ivMoreOptionPlaylistItem);
            tvfolderSongListName = itemView.findViewById(R.id.tvPlaylistName);
            tvfolderSongListCount = itemView.findViewById(R.id.tvPlaylistSongCount);
            vfolderSongListDivider = itemView.findViewById(R.id.vPlaylistDivider);
            rlfolderSongListItem = itemView.findViewById(R.id.rlPlaylistItem);
        }
    }


    public void addSongsToFavourites(int position)
    {
        Cursor checkUser = DB.getFavouritesFromId(folderSongList.get(position).getName());

        if(checkUser.getCount() <= 0) {
            boolean checkAdd = DB.addToFavourites(folderSongList.get(position).getPath(), folderSongList.get(position).getName(), folderSongList.get(position).getArtist(), folderSongList.get(position).getAlbum(), folderSongList.get(position).getDuration(), folderSongList.get(position).getId(), folderSongList.get(position).getAlbumArt().toString());
            if(checkAdd)
            {
                Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show();
                //ivFavouritesSong.setColorFilter(ContextCompat.getColor(PlayingSongActivity.this, R.color.SelectedTab), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        }
        else
        {
            boolean checkDelFav = DB.removeFromFavourites(folderSongList.get(position).getName());
            if(checkDelFav) {
                Toast.makeText(context, "Remove from Favourites", Toast.LENGTH_SHORT).show();
                //ivFavouritesSong.setColorFilter(ContextCompat.getColor(PlayingSongActivity.this, R.color.countTxt), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        }
    }
}
