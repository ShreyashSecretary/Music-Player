package com.music.mp3player.audio.mediaplayer.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.music.mp3player.audio.mediaplayer.DataBase.DbHelper;
import com.music.mp3player.audio.mediaplayer.R;

import java.util.List;

public class CreatedPlaylistAdapter extends RecyclerView.Adapter<CreatedPlaylistAdapter.ViewHolder> {

    private Context context;
    private List<String> list;
    DbHelper DB;
    private addToPlaylistInterface add;

    public CreatedPlaylistAdapter(Context context, List<String> list, addToPlaylistInterface add) {
        this.context = context;
        this.list = list;
        this.add = add;
        DB = new DbHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.created_playlist_list, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvPlaylistText.setText(list.get(position).toString());

        Cursor c = DB.getPlaylistSongs(list.get(position).toString());

        holder.tvPlaylistSongsCount.setText(" " + c.getCount());

        holder.ivPlaylistImage.setImageResource(R.drawable.music_photo);

        holder.rlPlaylistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add.addToPlaylist(position, list.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        if(list != null) {
            return list.size();
        }
        else
        {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rlPlaylistLayout;
        private TextView tvPlaylistText, tvPlaylistSongsCount;
        private ImageView ivPlaylistImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPlaylistText = itemView.findViewById(R.id.tvPlaylistText);
            rlPlaylistLayout = itemView.findViewById(R.id.rlPlaylistLayout);
            tvPlaylistSongsCount = itemView.findViewById(R.id.tvPlaylistSongsCount);
            ivPlaylistImage = itemView.findViewById(R.id.ivPlaylistImage);

        }
    }

    public interface addToPlaylistInterface
    {
        public void addToPlaylist(int pos, String playlistName);
    }
}
