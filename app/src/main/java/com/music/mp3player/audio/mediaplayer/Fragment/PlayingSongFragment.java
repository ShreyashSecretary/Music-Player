/*
package com.example.musicplayer.Fragment;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.Interface.ActionPlaying;
import com.example.musicplayer.Adapter.CreatedPlaylistAdapter;
import com.example.musicplayer.DataBase.DbHelper;
import com.example.musicplayer.ModelClass.AudioModel;
import com.example.musicplayer.Service.MusicService;
import com.example.musicplayer.R;
import com.example.musicplayer.Utils.Utilities;
import com.example.musicplayer.activities.EqualizerActivity;
import com.example.musicplayer.activities.PlayingSongActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.example.musicplayer.Adapter.PlaylistAdapter.playlist;
import static com.example.musicplayer.Fragment.AlbumSongsFragment.albumSongs;
import static com.example.musicplayer.Fragment.ArtistSongsFragment.artistSongs;
import static com.example.musicplayer.Fragment.FolderSongsFragment.folderSongsList;
import static com.example.musicplayer.Fragment.GenresSongsFragment.genreSongs;
import static com.example.musicplayer.Fragment.PlaylistSongsFragments.myPlaylist;
import static com.example.musicplayer.GetAllSongs.audioList;
import static com.example.musicplayer.Service.MusicService.mediaPlayer;
import static com.example.musicplayer.activities.SplashScreenActivity.FIRST_OPEN;

public class PlayingSongFragment extends BottomSheetDialogFragment implements ServiceConnection {

    BottomSheetBehavior bottomSheetBehavior;
    private ImageView ivBAck, ivFavouritesSong, ivAddToPlaylist, ivEqualizer, ivShuffleSong;
    private String songTitle, artistName, songPath, songAlbum, songDuration, songId;
    private Uri songAlbumArt;
    private boolean isJustOpen = false;
    public static int position, currentSongIndex;
    private CircularImageView ivSongImage;
    private TextView tvSongName, tvArtistName, tvDuration, tvSongPlayedTime;
    // public static MediaPlayer mp;
    public static ImageView ivPlayPause;
    private ImageView ivPrevious, ivNext;
    private SeekBar seekBar;
    Handler mHandler;
    private Utilities utils;
    public static List<AudioModel> songsList = new ArrayList<>();
    public static List<AudioModel> shuffledList = new ArrayList<>();
    private List<AudioModel> recentList;
    private SharedPreferences pref;
    SharedPreferences.Editor editor;
    DbHelper DB;
    public static List<String> createdPlaylist;
    private CreatedPlaylistAdapter createdPlaylistAdapter;
    private Intent playIntent;
    private boolean musicBound = false;
    Bitmap bitmap = null;
    private Thread playThread, nextThread, prevThread;
    MusicService musicService;
    public static String activityName;
    public Uri uri;
    private Handler handler = new Handler();
    public static boolean IS_SHUFFLED = false;
    private String currentActivityName, currentSongName;
    Animation animation;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(), R.layout.activity_playing_song, null);

        bottomSheet.setContentView(view);

        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));


        //setting Peek at the 16:9 ratio keyline of its parent.
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

       // view.extraSpace.setMinimumHeight((Resources.getSystem().getDisplayMetrics().heightPixels) / 2);

        createdPlaylist = new ArrayList<>();

        mHandler = new Handler();
        utils = new Utilities();
        DB = new DbHelper(getContext());

        ivBAck = view.findViewById(R.id.ivBackSong);
        ivSongImage = view.findViewById(R.id.civSongPhoto);
        tvSongName = view.findViewById(R.id.tvSongName);
        tvArtistName = view.findViewById(R.id.tvArtistName);
        tvDuration = view.findViewById(R.id.tvSongTotalTime);
        tvSongPlayedTime = view.findViewById(R.id.tvSongPlayedTime);
        ivPlayPause = view.findViewById(R.id.ivPlayPauseButton);
        ivPrevious = view.findViewById(R.id.ivPreviousSong);
        ivNext = view.findViewById(R.id.ivNextSong);
        seekBar = view.findViewById(R.id.sbSongDuration);
        ivFavouritesSong = view.findViewById(R.id.ivFavouritesSong);
        ivAddToPlaylist = view.findViewById(R.id.ivAddToPlaylist);
        ivEqualizer = view.findViewById(R.id.ivEqualizerSong);
        ivShuffleSong = view.findViewById(R.id.ivShuffleSong);
        //ivSearch = findViewById(R.id.ivSearch);

        animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);

        pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = pref.edit();

        currentSongIndex = pref.getInt("Position", 0);
        currentActivityName = pref.getString("ActivityName", "");
        currentSongName = pref.getString("Name", "");

        //playSong(position);
        //getIntentMethod();

        ivBAck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // overridePendingTransition( R.anim.slide_in_down, R.anim.slide_out_down );
               // finish();
            }
        });

        */
/*ivEqualizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               *//*
*/
/* Intent intent = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);

                if ((intent.resolveActivity(getPackageManager()) != null)) {
                    startActivityForResult(intent, 0);
                }
                else {
                    Toast.makeText(getContext(, "Equalizer is not available for your device", Toast.LENGTH_SHORT).show();
                }*//*
*/
/*

                if(musicService != null)
                {
                    Intent equalizerIntent = new Intent(getContext(), EqualizerActivity.class);
                    startActivity(equalizerIntent);
                }
                else
                {
                    Toast.makeText(getContext(), "Media player is not started", Toast.LENGTH_LONG).show();
                }
            }
        });

        ivShuffleSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!IS_SHUFFLED) {
                    shuffledList = songsList;
                    Collections.shuffle(shuffledList);
                    IS_SHUFFLED = true;
                    Toast.makeText(getContext(), "Songs Shuffled", Toast.LENGTH_SHORT).show();
                    ivShuffleSong.setColorFilter(ContextCompat.getColor(getContext(), R.color.SelectedTab), android.graphics.PorterDuff.Mode.MULTIPLY);
                }
                else
                {
                    ivShuffleSong.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                    IS_SHUFFLED = false;
                    Toast.makeText(getContext(), "Playing in original sequence", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivFavouritesSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor checkUser = DB.getFavouritesFromId(songsList.get(position).getName());

                if(checkUser.getCount() <= 0) {
                    boolean checkAdd = DB.addToFavourites(songPath, songTitle, artistName, songAlbum, songDuration, songId, songAlbumArt.toString());
                    if(checkAdd)
                    {
                        Toast.makeText(getContext(), "Added to Favourites", Toast.LENGTH_SHORT).show();
                        ivFavouritesSong.setColorFilter(ContextCompat.getColor(getContext(), R.color.SelectedTab), android.graphics.PorterDuff.Mode.MULTIPLY);
                    }
                }
                else
                {
                    boolean checkDelFav = DB.removeFromFavourites(songsList.get(position).getName());
                    if(checkDelFav) {
                        Toast.makeText(getContext(), "Remove from Favourites", Toast.LENGTH_SHORT).show();
                        ivFavouritesSong.setColorFilter(ContextCompat.getColor(getContext(), R.color.countTxt), android.graphics.PorterDuff.Mode.MULTIPLY);
                    }
                }
            }
        });

        ivAddToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(musicService != null && b)
                {
                    musicService.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(musicService != null)
                {
                    int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                    tvSongPlayedTime.setText(formattedTime(mCurrentPosition));
                    tvDuration.setText(formattedTime(Integer.parseInt(songsList.get(position).getDuration()) / 1000));
                }
                handler.postDelayed(this, 1000);
            }
        });*//*


        return bottomSheet;
    }

    private String formattedTime(int mCurrentPosition) {

        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if(seconds.length() == 1)
        {
            return totalNew;
        }
        else
        {
            return totalOut;
        }
    }

    public void getIntentMethod() {
//        position = getIntent().getIntExtra("Position", -1);
//        activityName = getIntent().getStringExtra("ActivityName");
        if (activityName.equals("Album")) {
            songsList = albumSongs;
        } else if (activityName.equals("Artist")) {
            songsList = artistSongs;
        } else if (activityName.equals("Playlist")) {
            songsList = playlist;
        } else if (activityName.equals("Genre")) {
            songsList = genreSongs;
        } else if (activityName.equals("Folder")) {
            songsList = folderSongsList;
        } else if (activityName.equals("CreatedPlayListSongs")) {
            songsList = myPlaylist;
        } else {
            songsList = audioList;
        }

        if (songsList != null) {
            uri = Uri.parse(songsList.get(position).getPath());
        }

        if (currentSongIndex == position && FIRST_OPEN || !currentSongName.equals(songsList.get(position).getName())) {
            Intent intent = new Intent(getContext(), MusicService.class);
            intent.putExtra("ServicePosition", position);
            getContext().startService(intent);
            ivPlayPause.setImageResource(R.drawable.pause);
            ivSongImage.startAnimation(animation);
        } else if (currentSongIndex != position) {
            Intent intent = new Intent(getContext(), MusicService.class);
            intent.putExtra("ServicePosition", position);
            getContext().startService(intent);
            ivPlayPause.setImageResource(R.drawable.pause);
            ivSongImage.startAnimation(animation);
        }
        */
/*else if(!currentActivityName.equals(activityName))
        {
            Intent intent = new Intent(this, MusicService.class);
            intent.putExtra("ServicePosition", position);
            startService(intent);
            ivPlayPause.setImageResource(R.drawable.pause);
        }*//*

        setBottomLayout();
        if (musicService != null) {
            musicService.showNotification(R.drawable.ic_baseline_pause_24, "Pause");
        }
    }

    public void setBottomLayout()
    {
        tvDuration.setText(formattedTime(Integer.parseInt(songsList.get(position).getDuration()) / 1000));
        Uri albumArtUri = songsList.get(position).getAlbumArt();
        bitmap = null;
        //ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(), albumArtUri);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), albumArtUri);
            // bitmap = ImageDecoder.decodeBitmap(source);
        } catch (FileNotFoundException exception) {
        } catch (IOException e) {

            e.printStackTrace();
        }

        */
/*if(tvPlayingSongArtist != null && tvPlayingSongName != null && ivPlayingSongImage != null) {
            tvPlayingSongArtist.setText(songsList.get(position).getArtist());
            tvPlayingSongName.setText(songsList.get(position).getName());
           *//*
*/
/* pos = position;
            actName = activityName;*//*
*/
/*
            if(bitmap != null) {
                ivPlayingSongImage.setImageBitmap(bitmap);
            }
            else
            {
                ivPlayingSongImage.setImageResource(R.drawable.music_photo);
            }
        }*//*


        songTitle = songsList.get(position).getName();
        songPath = songsList.get(position).getPath();
        songAlbum = songsList.get(position).getAlbum();
        songId = songsList.get(position).getId();
        songDuration = songsList.get(position).getDuration();
        songAlbumArt = songsList.get(position).getAlbumArt();
        artistName = songsList.get(position).getArtist();

        tvSongName.setText(songTitle);

        if(bitmap != null) {
            ivSongImage.setImageBitmap(bitmap);
        }
        else
        {
            ivSongImage.setImageResource(R.drawable.music_photo);
        }
        currentSongIndex = position;

        tvArtistName.setText(artistName);

        //ivPlayPause.setImageResource(R.drawable.pause);

        seekBar.setProgress(0);
        seekBar.setMax(100);

        Cursor checkFavUser = DB.getFavouritesFromId(songsList.get(position).getName());
        if(checkFavUser.getCount() > 0)
        {
            ivFavouritesSong.setColorFilter(ContextCompat.getColor(getContext(), R.color.SelectedTab), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        else
        {
            ivFavouritesSong.setColorFilter(ContextCompat.getColor(getContext(), R.color.countTxt), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        Cursor checkUser = DB.getRecentSongFromId(songsList.get(position).getId());

        if(checkUser.getCount() == 0) {
            boolean checkAdd = DB.insertRecentSongs(songsList.get(position).getPath(), songTitle, artistName, songsList.get(position).getAlbum(), songsList.get(position).getDuration(), songsList.get(position).getId(), songsList.get(position).getAlbumArt().toString());
        }

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Name", songTitle);
        editor.putString("Artist", artistName);
        // editor.putString("Duration", String.valueOf(musicService.getDuration()));
        editor.putString("ActivityName", activityName);
        editor.putInt("Position", position);
        editor.putString("AlbumArt", songsList.get(position).getAlbumArt().toString());
        editor.apply();
    }

    public void showBottomSheetDialog() {

        BottomSheetDialog dialog = new BottomSheetDialog(getContext(), R.style.AppBottomSheetDialogTheme);
        dialog.setContentView(R.layout.bottom_sheet_dialog);
        dialog.setCancelable(true);

        RelativeLayout rlCreateNewPlaylist = dialog.findViewById(R.id.rlCreateNewPlaylist);
        RelativeLayout rlFavouriteLayout = dialog.findViewById(R.id.rlFavouriteLayout);
        RecyclerView rvNewCreatedPlaylist = dialog.findViewById(R.id.rvNewCreatedPlaylist);
        TextView tvFavPlaylist = dialog.findViewById(R.id.tvFavPlaylist);

        Cursor checkUser = DB.getFavouritesFromId(songsList.get(position).getName());
        if(checkUser.getCount() > 0)
        {
            tvFavPlaylist.setText("UnFavourites");
        }
        else
        {
            tvFavPlaylist.setText("Favourites");
        }

        rlFavouriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor checkUser = DB.getFavouritesFromId(songsList.get(position).getName());

                if(checkUser.getCount() <= 0) {
                    boolean checkAdd = DB.addToFavourites(songsList.get(position).getPath(), songTitle, artistName, songsList.get(position).getAlbum(), songsList.get(position).getDuration(), songsList.get(position).getId(), songsList.get(position).getAlbumArt().toString());
                    if(checkAdd)
                    {

                        Toast.makeText(getContext(), "Added to Favourites", Toast.LENGTH_SHORT).show();
                        ivFavouritesSong.setColorFilter(ContextCompat.getColor(getContext(), R.color.SelectedTab), android.graphics.PorterDuff.Mode.MULTIPLY);
                    }
                }
                else
                {

                    boolean deleteFav = DB.removeFromFavourites(songTitle);
                    if(deleteFav) {
                        Toast.makeText(getContext(), "Removed from Favourites", Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.dismiss();
            }
        });

        rlCreateNewPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreatePlaylistDialog();
                dialog.dismiss();
            }
        });

        Cursor c = DB.getAllPlaylist();
        createdPlaylist.clear();
        if(c.getCount() > 0)
        {
            while (c.moveToNext())
            {
                createdPlaylist.add(c.getString(c.getColumnIndex("name")));
            }
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager = linearLayoutManager;
        rvNewCreatedPlaylist.setLayoutManager(layoutManager);

        createdPlaylistAdapter = new CreatedPlaylistAdapter(getContext(), createdPlaylist, new CreatedPlaylistAdapter.addToPlaylistInterface() {
            @Override
            public void addToPlaylist(int pos, String playlistName) {
                Cursor checkUser = DB.getAlreadyAddedSongs(songsList.get(position).getName(), playlistName);

                if(checkUser.getCount() <= 0) {
                    boolean checkAdd = DB.insetIntoCreatedTable(playlistName, songsList.get(position).getPath(), songTitle, artistName, songsList.get(position).getAlbum(), songsList.get(position).getDuration(), songsList.get(position).getId(), songsList.get(position).getAlbumArt().toString());
                    if(checkAdd)
                    {
                        Toast.makeText(getContext(), "Added to Playlist", Toast.LENGTH_SHORT).show();
                        //ivFavouritesSong.setColorFilter(ContextCompat.getColor(getContext(), R.color.SelectedTab), android.graphics.PorterDuff.Mode.MULTIPLY);
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Added to Favourites", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        rvNewCreatedPlaylist.setAdapter(createdPlaylistAdapter);

        dialog.show();

    }

    public void showCreatePlaylistDialog()
    {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.new_playlist_name_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvCreate = dialog.findViewById(R.id.tvCreate);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);
        EditText edtNewPlaylist = dialog.findViewById(R.id.edtPlaylistName);

        edtNewPlaylist.setFocusable(true);

        tvCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB.createPlaylist(edtNewPlaylist.getText().toString());
                DB.createTable(edtNewPlaylist.getText().toString());
                DB.insetIntoCreatedTable(edtNewPlaylist.getText().toString(), songsList.get(position).getPath(), songTitle, artistName, songsList.get(position).getAlbum(), songsList.get(position).getDuration(), songsList.get(position).getId(), songsList.get(position).getAlbumArt().toString());
                Toast.makeText(getContext(), "Playlist created", Toast.LENGTH_SHORT).show();
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                dialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        dialog.show();
    }

    @Override
    public void onResume() {
        Intent intent = new Intent(getContext(), MusicService.class);
        getContext().bindService(intent, this, BIND_AUTO_CREATE);
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();

        super.onResume();
    }

    private void prevThreadBtn() {
        prevThread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                ivPrevious.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        previousButtonClick();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void nextThreadBtn() {
        nextThread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                ivNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nextButtonClick();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void playThreadBtn() {

        playThread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                ivPlayPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playPauseButtonClick();
                    }
                });
            }
        };
        playThread.start();
    }

    public void playPauseButtonClick() {
        if(musicService != null) {

            if (musicService.isPlaying()) {
                musicService.pause();
                ivSongImage.clearAnimation();
                musicService.showNotification(R.drawable.ic_baseline_play_arrow_24, "Play");
                ivPlayPause.setImageResource(R.drawable.play);
                seekBar.setMax(musicService.getDuration() / 1000);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (musicService != null) {
                            int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                            seekBar.setProgress(mCurrentPosition);
                            tvDuration.setText(formattedTime(Integer.parseInt(songsList.get(position).getDuration()) / 1000));
                        }
                        handler.postDelayed(this, 100);
                    }
                });
            } else {
                musicService.start();
                ivSongImage.startAnimation(animation);
                musicService.showNotification(R.drawable.ic_baseline_pause_24, "Pause");
                ivPlayPause.setImageResource(R.drawable.pause);
                seekBar.setMax(musicService.getDuration() / 1000);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (musicService != null) {
                            int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                            seekBar.setProgress(mCurrentPosition);
                            tvDuration.setText(formattedTime(Integer.parseInt(songsList.get(position).getDuration()) / 1000));
                        }
                        handler.postDelayed(this, 100);
                    }
                });
            }
        }
        //  //musicService.onComplete();
    }

    public void nextButtonClick()
    {
        if(musicService.isPlaying() && musicService != null)
        {
            musicService.stop();
            musicService.release();
            position = ((position + 1) % songsList.size());
            musicService.createMediaPlayer(position);
            setBottomLayout();
            seekBar.setMax(musicService.getDuration() / 1000);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicService != null)
                    {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                        tvDuration.setText(formattedTime(Integer.parseInt(songsList.get(position).getDuration()) / 1000));
                    }
                    handler.postDelayed(this, 100);
                }
            });
            //musicService.onComplete();
            ivPlayPause.setImageResource(R.drawable.pause);
            ivSongImage.startAnimation(animation);
            musicService.start();
            musicService.showNotification(R.drawable.ic_baseline_pause_24, "Pause");
        }
        else
        {
            musicService.stop();
            musicService.release();
            position = ((position + 1) % songsList.size());
            musicService.createMediaPlayer(position);
            setBottomLayout();
            seekBar.setMax(musicService.getDuration() / 1000);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicService != null)
                    {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                        tvDuration.setText(formattedTime(Integer.parseInt(songsList.get(position).getDuration()) / 1000));
                    }
                    handler.postDelayed(this, 100);
                }
            });
            //musicService.onComplete();
            ivPlayPause.setImageResource(R.drawable.play);
            ivSongImage.clearAnimation();
            musicService.showNotification(R.drawable.ic_baseline_play_arrow_24, "Play");
        }
    }

    public void previousButtonClick()
    {
        */
/*if (currentSongIndex > 0) {
            playSong(currentSongIndex - 1);
            //currentSongIndex = currentSongIndex - 1;
        } else {
            if (songsList.size() > 0) {
                playSong(songsList.size() - 1);
                //currentSongIndex = songsList.size() - 1;
            }
        }
        //musicService.onComplete();*//*


        if(musicService.isPlaying())
        {
            musicService.stop();
            musicService.release();
            position = ((position - 1) < 0 ? (songsList.size() - 1) : (position - 1));
            musicService.createMediaPlayer(position);
            setBottomLayout();
            seekBar.setMax(musicService.getDuration() / 1000);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicService != null)
                    {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                        tvDuration.setText(formattedTime(Integer.parseInt(songsList.get(position).getDuration()) / 1000));
                    }
                    handler.postDelayed(this, 100);
                }
            });
            //musicService.onComplete();
            ivPlayPause.setImageResource(R.drawable.pause);
            musicService.start();
            ivSongImage.startAnimation(animation);
            musicService.showNotification(R.drawable.ic_baseline_pause_24, "Pause");
        }
        else
        {
            musicService.stop();
            musicService.release();
            position = ((position - 1) < 0 ? (songsList.size() - 1) : (position - 1));
            musicService.createMediaPlayer(position);
            setBottomLayout();
            seekBar.setMax(musicService.getDuration() / 1000);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicService != null)
                    {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                        tvDuration.setText(formattedTime(Integer.parseInt(songsList.get(position).getDuration()) / 1000));
                    }
                    handler.postDelayed(this, 100);
                }
            });
            //musicService.onComplete();
            ivPlayPause.setImageResource(R.drawable.play);
            ivSongImage.clearAnimation();
            musicService.showNotification(R.drawable.ic_baseline_play_arrow_24, "Play");
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder)iBinder;
        musicService = myBinder.getService();
       // musicService.setCallBack(this);
//        seekBar.setMax(musicService.getDuration() / 1000);
        // Toast.makeText(this, "Connected :" + musicService, Toast.LENGTH_LONG).show();
//        musicService.showNotification(R.drawable.ic_baseline_pause_24, "Pause");
        ////musicService.onComplete();
        FIRST_OPEN = false;
        */
/*if(mediaPlayer.isPlaying())
        {
            ivPlayPause.setImageResource(R.drawable.pause);
            ivSongImage.startAnimation(animation);
        }
        else
        {
            ivPlayPause.setImageResource(R.drawable.play);
            ivSongImage.clearAnimation();
        }*//*

        // setBottomLayout();
        */
/*Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("ServicePosition", position);
        startService(intent);
        playSong(position);*//*

        */
/*seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                *//*
*/
/*int x = (int) Math.ceil(i / 1000f);

                if (x == 0 && musicService != null && !musicService.isPlaying()) {
                    // clearMediaPlayer();
                    // fab.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, android.R.drawable.ic_media_play));
                    seekBar.setProgress(x);
                }*//*
*/
/*

                if(musicService != null && b)
                {
                    mediaPlayer.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                mHandler.removeCallbacks(mUpdateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = musicService.getDuration();
                int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

                // forward or backward to certain seconds
                musicService.seekTo(currentPosition);

                // update timer progress again
                updateProgressBar();
            }
        });*//*


        // //musicService.onComplete();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        //musicService = null;
    }

    @Override
    public void onBindingDied(ComponentName name) {

    }
}
*/
