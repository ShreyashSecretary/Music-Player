package com.music.mp3player.audio.mediaplayer.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
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

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private Context context;
    public static List<AudioModel> playlist;
  //  private playingSong song;
    DbHelper DB;
    private AddRemoveFavourites addRemove;

    public PlaylistAdapter(Context context, List<AudioModel> playlist, AddRemoveFavourites addRemove) {
        this.context = context;
        this.playlist = playlist;
        this.addRemove = addRemove;
        /*if (this.playlist == null){
            this.playlist = new ArrayList<>();
        }*/
       // this.song = song;
        DB = new DbHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.playlist_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String name = playlist.get(position).getName();
        String artist = playlist.get(position).getArtist();
        String album = playlist.get(position).getAlbum();
        String duration = playlist.get(position).getDuration();
        String id = playlist.get(position).getId();
        String url = playlist.get(position).getPath();
        Uri albumArtUri = playlist.get(position).getAlbumArt();

       // byte[] art = getAlbumArt(playlist.get(position).getPath());
        //Cursor c = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums.Al})
        holder.tvPlaylistCount.setText(artist);
        holder.tvPlaylistName.setText(name);

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
            //holder.ivPlaylistItem.setImageBitmap(bitmap);
            holder.ivPlaylistItem.setImageBitmap(bitmap);
        }
        else
        {
            holder.ivPlaylistItem.setImageResource(R.drawable.music_photo);
        }
       /* File file = new File(albumArt);
        Bitmap bm = BitmapFactory.decodeFile(albumArt);
        holder.ivPlaylistItem.setImageBitmap(BitmapFactory.decodeFile(albumArt));*/
        //holder.ivPlaylistItem.setImageURI(albumArt);

        holder.rlPlaylistItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.onClickSong(position, "Playlist");
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
                            addRemove.onAddRemove(position);
                        }
                        else
                        {
                            AddToPlaylistClass add = new AddToPlaylistClass(context, position, playlist);
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
        return playlist.size();
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

    public void addNewItem(AudioModel model)
    {
        playlist.add(model);
        notifyItemInserted(playlist.indexOf(model));
    }

    public byte[] getAlbumArt(String uri)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    public interface AddRemoveFavourites
    {
        public void onAddRemove(int position);
    }
}
