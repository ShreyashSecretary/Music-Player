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

import com.music.mp3player.audio.mediaplayer.ModelClass.ModelGenresClass;
import com.music.mp3player.audio.mediaplayer.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {

    private Context context;
    private List<ModelGenresClass> genreList;
    private GoToGenreSongs goTo;

    public GenreAdapter(Context context, List<ModelGenresClass> genreList, GoToGenreSongs goTo) {
        this.context = context;
        this.genreList = genreList;
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

        Uri image = Uri.parse(genreList.get(position).getArt());
        holder.tvAlbumName.setText(genreList.get(position).getName());
        holder.tvartistListCount.setText(genreList.get(position).getNoOfSongs());

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

                goTo.genreSongs(genreList.get(position).getId(), genreList.get(position).getName());
                /*Intent i = new Intent(context, GenresSongsActivity.class);
                i.putExtra("GenreId", genreList.get(position).getId());
                i.putExtra("GenreName", genreList.get(position).getName());
                context.startActivity(i);*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return genreList.size();
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

    public void addGenreItem(ModelGenresClass genresClass)
    {
        genreList.add(genresClass);
        notifyItemInserted(genreList.indexOf(genresClass));
    }

    public interface GoToGenreSongs
    {
        public void genreSongs(String id, String name);
    }
}
