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

public class MainScreenPlaylistAdapter extends RecyclerView.Adapter<MainScreenPlaylistAdapter.ViewHolder> {

    private Context context;
    private List<String> list;
    DbHelper DB;
    private CreatedPlaylistAdapter.addToPlaylistInterface add;

    public MainScreenPlaylistAdapter(Context context, List<String> list, CreatedPlaylistAdapter.addToPlaylistInterface add) {
        this.context = context;
        this.list = list;
        this.add = add;
        DB = new DbHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.playlist_item_layout, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvPlaylistText.setText(list.get(position).toString());

        Cursor c = DB.getPlaylistSongs(list.get(position).toString());

        holder.tvPlaylistSongsCount.setText(" " + c.getCount());

        holder.ivPlaylistItem.setImageResource(R.drawable.music_photo);

        holder.rlPlaylistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add.addToPlaylist(position, list.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPlaylistItem, ivMore;
        private TextView tvPlaylistText;
        private TextView tvPlaylistSongsCount;
        private View vPlaylistDivider;
        private RelativeLayout rlPlaylistLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPlaylistItem = itemView.findViewById(R.id.ivPlayListItem);
            ivMore = itemView.findViewById(R.id.ivMoreOptionPlaylistItem);
            tvPlaylistText = itemView.findViewById(R.id.tvPlaylistName);
            tvPlaylistSongsCount = itemView.findViewById(R.id.tvPlaylistSongCount);
            vPlaylistDivider = itemView.findViewById(R.id.vPlaylistDivider);
            rlPlaylistLayout = itemView.findViewById(R.id.rlPlaylistItem);

            ivMore.setVisibility(View.GONE);
        }
    }
}
