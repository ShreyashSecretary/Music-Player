package com.music.mp3player.audio.mediaplayer.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.music.mp3player.audio.mediaplayer.ModelClass.ModelArtistClass;
import com.music.mp3player.audio.mediaplayer.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    private Context context;
    private List<ModelArtistClass> artistList;
    private GoToArtistSongs goTo;

    public ArtistAdapter(Context context, List<ModelArtistClass> artistList, GoToArtistSongs goTo) {
        this.context = context;
        this.artistList = artistList;
        this.goTo = goTo;
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

        Uri image = Uri.parse(artistList.get(position).getArt());
        holder.tvAlbumName.setText(artistList.get(position).getArtistName());
        holder.tvartistListCount.setText(artistList.get(position).getNoOfSongs());

        Bitmap bitmap = null;

        //ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(), albumArtUri);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), image);
            // bitmap = ImageDecoder.decodeBitmap(source);
        } catch (FileNotFoundException exception) {
        } catch (IOException e) {

            e.printStackTrace();
        }

        if (bitmap != null) {
            holder.ivAlbumItem.setImageBitmap(bitmap);
        }
        else
        {
            holder.ivAlbumItem.setImageResource(R.drawable.music_photo);
        }

       // Bitmap bm= BitmapFactory.decodeFile(image);
//        ImageView image=(ImageView)findViewById(R.id.image);
//        image.setImageBitmap(bm);
        //holder.ivAlbumItem.setImageBitmap(bm);

        holder.rlAlbumItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goTo.artistSongs(artistList.get(position).getArtistName());
                /*Intent i = new Intent(context, ArtistSongsActivity.class);
                i.putExtra("ArtistName", artistList.get(position).getArtistName());
                context.startActivity(i);*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return artistList.size();
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

    public void addArtistItem(ModelArtistClass artistClass)
    {
        artistList.add(artistClass);
        notifyItemInserted(artistList.indexOf(artistClass));
    }

    public interface GoToArtistSongs
    {
        public void artistSongs(String name);
    }
}
