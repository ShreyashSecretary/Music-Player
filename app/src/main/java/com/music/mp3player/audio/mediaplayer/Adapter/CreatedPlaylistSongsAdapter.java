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

public class CreatedPlaylistSongsAdapter extends RecyclerView.Adapter<CreatedPlaylistSongsAdapter.ViewHolder> {

    private Context context;
    public static List<AudioModel> PlaylistSongs;
    //  private playingSong song;
    DbHelper DB;
    private AddRemoveFavourites addRemove;
    private RemovedFromPlaylist songRemove;

    public CreatedPlaylistSongsAdapter(Context context, List<AudioModel> PlaylistSongs, AddRemoveFavourites addRemove, RemovedFromPlaylist songRemove) {
        this.context = context;
        this.PlaylistSongs = PlaylistSongs;
        this.addRemove = addRemove;
        this.songRemove = songRemove;
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

        String name = PlaylistSongs.get(position).getName();
        String artist = PlaylistSongs.get(position).getArtist();
        String album = PlaylistSongs.get(position).getAlbum();
        String duration = PlaylistSongs.get(position).getDuration();
        String id = PlaylistSongs.get(position).getId();
        String url = PlaylistSongs.get(position).getPath();
        Uri albumArtUri = PlaylistSongs.get(position).getAlbumArt();

       // byte[] art = getAlbumArt(PlaylistSongs.get(position).getPath());
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
                /*Intent i = new Intent(context, MainActivity.class);
                i.putExtra("ActivityName", "CreatedPlayListSongs");
                i.putExtra("Position", position);
                context.startActivity(i);*/
                mainActivity.onClickSong(position, "CreatedPlayListSongs");

                //song.onPlaying(position, name, artist);
            }
        });

        holder.rlPlaylistItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                songRemove.removeSong(position);
                return true;
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
                            AddToPlaylistClass add = new AddToPlaylistClass(context, position, PlaylistSongs);
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
        return PlaylistSongs.size();
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
        PlaylistSongs.add(model);
        notifyItemInserted(PlaylistSongs.indexOf(model));
    }

    public interface AddRemoveFavourites
    {
        public void onAddRemove(int position);
    }

    public interface RemovedFromPlaylist
    {
        public void removeSong(int position);
    }
}
