package com.music.mp3player.audio.mediaplayer.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.music.mp3player.audio.mediaplayer.R;
import com.music.mp3player.audio.mediaplayer.activities.MainActivity;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.music.mp3player.audio.mediaplayer.Service.MusicService.mediaPlayer;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.SHOW_MINI_PLAYER;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.actName;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.albumArtUri;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.artist;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.ivPlayPause;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.name;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.pos;
//import static com.example.musicplayer.activities.PlayingSongActivity.ivPlayPause;
//import static com.example.musicplayer.activities.SplashScreenActivity.FIRST_OPEN;

public class MinimizedPlayerFragment extends Fragment {

    private View view;
    private RelativeLayout rlPlayingSong;
    private TextView tvPlayingSongName, tvPlayingSongArtist;
    private ImageView ivPlayPauseLibrary, ivPlayingSongImage;

    public MinimizedPlayerFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        SharedPreferences pref = getContext().getSharedPreferences("MyPref" , Context.MODE_PRIVATE);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.minimized_player_fragment, container, false);

        tvPlayingSongArtist = view.findViewById(R.id.tvPlayingSongArtist);
        tvPlayingSongName = view.findViewById(R.id.tvPlayingSongName);
        ivPlayPauseLibrary = view.findViewById(R.id.ivPlayPause);
        rlPlayingSong = view.findViewById(R.id.rlPlayingSong);
        ivPlayingSongImage = view.findViewById(R.id.ivPlayingSongImage);

        /*rlPlayingSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent i = new Intent(getContext(), PlayingSongActivity.class);
//                i.putExtra("Position", pos);
//                i.putExtra("ActivityName", actName);
//                startActivity(i);
                PlayingSongFragment bottomSheet = new PlayingSongFragment();
                bottomSheet.show(getActivity().getSupportFragmentManager(),bottomSheet.getTag());
            }
        });*/

       /* rlPlayingSong.setOnTouchListener(new OnSwipeTouchListener(getContext()){
            @Override
            public void onSwipeUp() {
                super.onSwipeUp();
                PlayingSongFragment bottomSheet = new PlayingSongFragment();
                bottomSheet.show(getActivity().getSupportFragmentManager(),bottomSheet.getTag());
            }

            @Override
            public void onSwipeDown() {
                super.onSwipeDown();
                PlayingSongFragment bottomSheet = new PlayingSongFragment();
                bottomSheet.show(getActivity().getSupportFragmentManager(),bottomSheet.getTag());
            }
        });*/


        ivPlayPauseLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null && mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    ivPlayPauseLibrary.setImageResource(R.drawable.play);
                    ivPlayPause.setImageResource(R.drawable.play);
                }
                else if(mediaPlayer != null)
                {
                    mediaPlayer.start();
                    ivPlayPauseLibrary.setImageResource(R.drawable.pause);
                    ivPlayPause.setImageResource(R.drawable.pause);
                }
                else
                {
                    Intent i = new Intent(getContext(), MainActivity.class);
                    i.putExtra("Position", pos);
                    i.putExtra("ActivityName", actName);
                    startActivity(i);
                }
            }
        });

        return view;
    }

    /*public void clickEvents()
    {
        rlPlayingSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                *//*Intent i = new Intent(getContext(), PlayingSongActivity.class);
                i.putExtra("Position", pos);
                i.putExtra("ActivityName", actName);
                startActivity(i);*//*

                PlayingSongFragment bottomSheet = new PlayingSongFragment();
                bottomSheet.show(getActivity().getSupportFragmentManager(),bottomSheet.getTag());
            }
        });*/

        /*rlPlayingSong.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                Intent i = new Intent(getContext(), PlayingSongActivity.class);
                i.putExtra("Position", pos);
                i.putExtra("ActivityName", actName);
                startActivity(i);
                return true;
            }
        });*/


        /*ivPlayPauseLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null && mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    ivPlayPauseLibrary.setImageResource(R.drawable.play);
                    ivPlayPause.setImageResource(R.drawable.play);
                }
                else if(mediaPlayer != null)
                {
                    mediaPlayer.start();
                    ivPlayPauseLibrary.setImageResource(R.drawable.pause);
                    ivPlayPause.setImageResource(R.drawable.pause);
                }
                else
                {
                    Intent i = new Intent(getContext(), PlayingSongActivity.class);
                    i.putExtra("Position", pos);
                    i.putExtra("ActivityName", actName);
                    startActivity(i);
                }
            }
        });
    }*/

    private void setFragment() {

        tvPlayingSongName.setText(name);
        tvPlayingSongArtist.setText(artist);

        /*tvPlayingSongArtist.setText(artist);
        tvPlayingSongName.setText(name);*/

        Bitmap bit = null;
        try {
            bit = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), albumArtUri);
            // bitmap = ImageDecoder.decodeBitmap(source);
        } catch (FileNotFoundException exception) {
        } catch (IOException e) {

            e.printStackTrace();
        }
        if(bit != null) {
            ivPlayingSongImage.setImageBitmap(bit);
        }
        else
        {
            ivPlayingSongImage.setImageResource(R.drawable.music_photo);
        }

        if(mediaPlayer != null && mediaPlayer.isPlaying())
        {
            ivPlayPauseLibrary.setImageResource(R.drawable.pause);
        }
        else
        {
            ivPlayPauseLibrary.setImageResource(R.drawable.play);
        }
       // clickEvents();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(SHOW_MINI_PLAYER) {
            setFragment();
        }
    }
}
