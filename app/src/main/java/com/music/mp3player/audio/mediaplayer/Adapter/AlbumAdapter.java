package com.music.mp3player.audio.mediaplayer.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.nativead.NativeAd;
import com.music.mp3player.audio.mediaplayer.AdmobAdManager;
import com.music.mp3player.audio.mediaplayer.Interface.AdEventListener;
import com.music.mp3player.audio.mediaplayer.ModelClass.ModelAlbumClass;
import com.music.mp3player.audio.mediaplayer.R;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ModelAlbumClass> albumList;
    private goToSongsFragment goTo;
    private final int ADS_LAYOUT = 1;
    private final int LIST_ITEM = 2;
    private AdmobAdManager admobAdManager;

    public AlbumAdapter(Context context, List<ModelAlbumClass> albumList, goToSongsFragment goTo) {
        this.context = context;
        this.albumList = albumList;
        this.goTo = goTo;
        admobAdManager = AdmobAdManager.getInstance(context);
    }

    @Override
    public int getItemViewType(int position) {

        try {
            if (position == 2) {
                return ADS_LAYOUT;
            } else {
                return LIST_ITEM;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.getItemViewType(position);


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ADS_LAYOUT:
                View view = inflater.inflate(R.layout.nativ_ad_layout, parent, false);
                return new AdsClass(view);

            case LIST_ITEM:
                View view1 = inflater.inflate(R.layout.album_item_layout, parent, false);
                return new ViewHolder(view1);

            default:
                return null;
        }
        /*View view = inflater.inflate(R.layout.album_item_layout, parent, false);
        return new ViewHolder(view);*/
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int finalWidth = (width / 2) - (int) context.getResources().getDimension(R.dimen._15sdp);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(finalWidth, ViewGroup.LayoutParams.WRAP_CONTENT);

        switch (getItemViewType(position)) {

            case LIST_ITEM :
                ((ViewHolder) holder).rlAlbumItem.setLayoutParams(params);

                String image = albumList.get(position).getArt();
                ((ViewHolder) holder).tvAlbumName.setText(albumList.get(position).getAlbum());
                ((ViewHolder) holder).tvAlbumListCount.setText(albumList.get(position).getNoOfSongs());

                Bitmap bm = BitmapFactory.decodeFile(image);

                if (bm != null) {
                    Glide.with(context)
                            .load(bm)
                            .into(((ViewHolder) holder).ivAlbumItem);
                } else {
                    ((ViewHolder) holder).ivAlbumItem.setImageResource(R.drawable.music_photo);
                }
    //        ImageView image=(ImageView)findViewById(R.id.image);
    //        image.setImageBitmap(bm);
                //((ViewHolder)holder).ivAlbumItem.setImageBitmap(bm);

                ((ViewHolder) holder).rlAlbumItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        goTo.goToSongs(position, albumList.get(position).getId());
                    /*Intent i = new Intent(context, AlbumSongsActivity.class);
                    i.putExtra("AlbumName", albumList.get(position).getAlbum());
                    context.startActivity(i);*/
                    }
                });
                break;

            case ADS_LAYOUT:
                admobAdManager.LoadNativeAd(context, context.getResources().getString(R.string.native_ad_id), new AdEventListener() {
                    @Override
                    public void onAdLoaded(Object object) {
                        admobAdManager.populateUnifiedNativeAdView(context, ((AdsClass)holder).flSquareNative, (NativeAd) object,true,false);
                    }

                    @Override
                    public void onAdClosed() {

                    }

                    @Override
                    public void onLoadError(String errorCode) {

                    }
                });
        }
    }

   /* @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int finalWidth = (width / 2) - (int) context.getResources().getDimension(R.dimen._15sdp);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(finalWidth, ViewGroup.LayoutParams.WRAP_CONTENT);

        holder.rlAlbumItem.setLayoutParams(params);

        String image = albumList.get(position).getArt();
        holder.tvAlbumName.setText(albumList.get(position).getAlbum());
        holder.tvAlbumListCount.setText(albumList.get(position).getNoOfSongs());

        Bitmap bm = BitmapFactory.decodeFile(image);

        if(bm != null) {
            Glide.with(context)
                    .load(bm)
                    .into(holder.ivAlbumItem);
        }
        else
        {
            holder.ivAlbumItem.setImageResource(R.drawable.music_photo);
        }
//        ImageView image=(ImageView)findViewById(R.id.image);
//        image.setImageBitmap(bm);
        //holder.ivAlbumItem.setImageBitmap(bm);

        holder.rlAlbumItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goTo.goToSongs(position, albumList.get(position).getId());
                *//*Intent i = new Intent(context, AlbumSongsActivity.class);
                i.putExtra("AlbumName", albumList.get(position).getAlbum());
                context.startActivity(i);*//*
            }
        });

    }*/

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivAlbumItem;
        private TextView tvAlbumName, tvAlbumListCount;
        private RelativeLayout rlAlbumItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAlbumItem = itemView.findViewById(R.id.ivAlbumItem);
            tvAlbumName = itemView.findViewById(R.id.tvAlbumName);
            tvAlbumListCount = itemView.findViewById(R.id.tvAlbumListCount);
            rlAlbumItem = itemView.findViewById(R.id.rlAlbumItem);
        }
    }

    public class AdsClass extends RecyclerView.ViewHolder {

        private FrameLayout flSquareNative;

        public AdsClass(@NonNull View itemView) {
            super(itemView);

            flSquareNative = itemView.findViewById(R.id.flSquareNative);
        }
    }

    public void addAlbumItem(ModelAlbumClass albumClass)
    {
        albumList.add(albumClass);
        notifyItemInserted(albumList.indexOf(albumClass));
    }

    public interface goToSongsFragment
    {
        public void goToSongs(int pos, String name);
    }

    /*@Override
    public void onAdLoaded(Object object) {
        admobAdManager.populateUnifiedNativeAdView(context, ((AdsClass)).flSquareNative, (NativeAd) object,false,false);
    }

    @Override
    public void onAdClosed() {

    }

    @Override
    public void onLoadError(String errorCode) {

    }*/
}
