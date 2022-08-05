package com.music.mp3player.audio.mediaplayer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.music.mp3player.audio.mediaplayer.R;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    private Context context;
    public static List<String> folderList;
    private onClickFolderName onFolderClick;

    public FolderAdapter(Context context, List<String> folderList, onClickFolderName onFolderClick) {
        this.context = context;
        this.folderList = folderList;
        this.onFolderClick = onFolderClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.all_folders_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String folder = folderList.get(position);

        if(folder != null) {
            holder.tvAlbumName.setText(folderList.get(position));
            holder.tvartistListCount.setText(folderList.get(position));
        }
        else if(folder.contains(".") || folder.equals("") || folder.equals(" "))
        {
            holder.tvAlbumName.setText("Unknown");
            holder.tvartistListCount.setText("Unknown");
        }

        int height = (int) context.getResources().getDimension(R.dimen._35sdp);
        int width = (int) context.getResources().getDimension(R.dimen._35sdp);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        holder.ivAlbumItem.setLayoutParams(params);

        holder.ivAlbumItem.setImageResource(R.drawable.folder);
        holder.ivAlbumItem.setBackgroundResource(R.color.Transparent);

        holder.rlAlbumItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onFolderClick.getFolderName(folderList.get(position), position);
                /*Intent i = new Intent(context, FolderSongsActivity.class);
                i.putExtra("FolderName", folderList.get(position));
                context.startActivity(i);*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivAlbumItem, ivMore;
        private TextView tvAlbumName, tvartistListCount;
        private RelativeLayout rlAlbumItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAlbumItem = itemView.findViewById(R.id.ivAlbumItem);
            ivMore = itemView.findViewById(R.id.ivMoreOptionAlbumItem);
            tvAlbumName = itemView.findViewById(R.id.tvAlbumName);
            tvartistListCount = itemView.findViewById(R.id.tvAlbumListCount);
            rlAlbumItem = itemView.findViewById(R.id.rlAlbumItem);
        }
    }

    public void addFolders(String folderName)
    {
        folderList.add(folderName);
        notifyItemInserted(folderList.indexOf(folderName));
    }

    public interface onClickFolderName
    {
        public void getFolderName(String folderName, int pos);
    }
}
