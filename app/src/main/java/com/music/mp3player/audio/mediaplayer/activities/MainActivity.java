package com.music.mp3player.audio.mediaplayer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.music.mp3player.audio.mediaplayer.Adapter.TabAdapter;
import com.music.mp3player.audio.mediaplayer.AdmobAdManager;
import com.music.mp3player.audio.mediaplayer.CustomViewPager;
import com.music.mp3player.audio.mediaplayer.Fragment.BottomSheetEqualizerFragment;
import com.music.mp3player.audio.mediaplayer.Interface.ActionPlaying;
import com.music.mp3player.audio.mediaplayer.Adapter.CreatedPlaylistAdapter;
import com.music.mp3player.audio.mediaplayer.DataBase.DbHelper;
import com.music.mp3player.audio.mediaplayer.Interface.AdEventListener;
import com.music.mp3player.audio.mediaplayer.ModelClass.AudioModel;
import com.music.mp3player.audio.mediaplayer.R;
import com.music.mp3player.audio.mediaplayer.Service.MusicService;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.music.mp3player.audio.mediaplayer.Adapter.PlaylistAdapter.playlist;
import static com.music.mp3player.audio.mediaplayer.Fragment.AlbumSongsFragment.albumSongs;
import static com.music.mp3player.audio.mediaplayer.Fragment.ArtistSongsFragment.artistSongs;
import static com.music.mp3player.audio.mediaplayer.Fragment.FolderSongsFragment.folderSongsList;
import static com.music.mp3player.audio.mediaplayer.Fragment.GenresSongsFragment.genreSongs;
import static com.music.mp3player.audio.mediaplayer.Fragment.PlaylistSongsFragments.myPlaylist;
import static com.music.mp3player.audio.mediaplayer.GetAllSongs.audioList;
import static com.music.mp3player.audio.mediaplayer.Service.MusicService.mediaPlayer;
//import static com.example.musicplayer.activities.PlayingSongActivity.ivPlayPause;
//import static com.example.musicplayer.activities.PlayingSongActivity.songsList;
import static com.music.mp3player.audio.mediaplayer.activities.SplashScreenActivity.FIRST_OPEN;

public class MainActivity extends AppCompatActivity implements ActionPlaying, ServiceConnection, AdEventListener {

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
    public static List<AudioModel> shuffledList = new ArrayList<>();
    public static List<AudioModel> songsList = new ArrayList<>();
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
    public static MainActivity mainActivity;

    private FrameLayout flMinimizedPlayer;
    public static boolean SHOW_MINI_PLAYER = false;
    public static String name, artist, actName;
    public static int pos;
    public static Uri albumArtUri;
    public static SlidingUpPanelLayout slidingUpPanelLayout;

    private RelativeLayout rlPlayingSong, rlPlayingSongActivityLayout;
    private TextView tvPlayingSongName, tvPlayingSongArtist;
    public static ImageView ivPlayPauseLibrary, ivPlayingSongImage;
    public static int interfacePos;
    private String interfaceActName;

    public static TabLayout topNavigation;
    public static CustomViewPager viewPager;
    public static Toolbar toolbar;
    private ImageView ivSearch;
    public static TextView tvToolbarText;
    public static int r;

    public static boolean CHILD_FRAGMENT = false;

    private Animation animShow, animHide;
    private AdmobAdManager admobAdManager;
    private RelativeLayout rlBannerAds;
    private SharedPreferences ratePref;
    private boolean rateStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Random random = new Random();
        r = random.nextInt(1);*/

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.Transparent));
            window.setBackgroundDrawableResource(R.drawable.bg_3);
        }

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        topNavigation = findViewById(R.id.tlTopNavigation);
        viewPager = findViewById(R.id.view_pager);
        topNavigation.setTabGravity(TabLayout.GRAVITY_START);
        topNavigation.setHorizontalScrollBarEnabled(true);
        flMinimizedPlayer = findViewById(R.id.flMinimizedPlayer);
        toolbar = findViewById(R.id.toolbar);
        tvToolbarText = findViewById(R.id.tvToolbarText);


        final TabAdapter adapter = new TabAdapter(this, getSupportFragmentManager(), topNavigation.getTabCount(), MainActivity.this);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(topNavigation));
        viewPager.setOffscreenPageLimit(6);
        topNavigation.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                super.onTabSelected(tab);
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });

        // setTabLayout();

        createdPlaylist = new ArrayList<>();

        mHandler = new Handler();
        DB = new DbHelper(this);

        mainActivity = MainActivity.this;
        animation = AnimationUtils.loadAnimation(this, R.anim.rotate);

        tvPlayingSongArtist = findViewById(R.id.tvPlayingSongArtist);
        tvPlayingSongName = findViewById(R.id.tvPlayingSongName);
        ivPlayPauseLibrary = findViewById(R.id.ivPlayPause);
        rlPlayingSong = findViewById(R.id.rlPlayingSong1);
        ivPlayingSongImage = findViewById(R.id.ivPlayingSongImage);

        ivBAck = findViewById(R.id.ivBackSong);
        ivSongImage = findViewById(R.id.civSongPhoto);
        tvSongName = findViewById(R.id.tvSongName);
        tvArtistName = findViewById(R.id.tvArtistName);
        tvDuration = findViewById(R.id.tvSongTotalTime);
        tvSongPlayedTime = findViewById(R.id.tvSongPlayedTime);
        ivPlayPause = findViewById(R.id.ivPlayPauseButton);
        ivPrevious = findViewById(R.id.ivPreviousSong);
        ivNext = findViewById(R.id.ivNextSong);
        seekBar = findViewById(R.id.sbSongDuration);
        ivFavouritesSong = findViewById(R.id.ivFavouritesSong);
        ivAddToPlaylist = findViewById(R.id.ivAddToPlaylist);
        ivEqualizer = findViewById(R.id.ivEqualizerSong);
        ivShuffleSong = findViewById(R.id.ivShuffleSong);
        slidingUpPanelLayout = findViewById(R.id.slidingPanel);
        rlPlayingSongActivityLayout = findViewById(R.id.rlPlayingSongActivityLayout);
        ivSearch = findViewById(R.id.ivSearchMain);
        rlBannerAds = findViewById(R.id.rlBannerAds);

        admobAdManager = AdmobAdManager.getInstance(this);
        admobAdManager.setUpProgress(MainActivity.this);
        admobAdManager.showProgress();
        new Thread(() -> {
            while (!admobAdManager.isAdLoad && !admobAdManager.isAdLoadFailed) {
                Log.e("AdLoading : ", "Loading...");
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    admobAdManager.dismissProgress();
                    admobAdManager.loadInterstitialAd(MainActivity.this, getResources().getString(R.string.interstitial_ad_id), 1, new AdmobAdManager.OnAdClosedListener() {
                        @Override
                        public void onAdClosed() {
                            admobAdManager.isAdLoadFailed = false;
                        }
                    });
                }
            });
        }).start();

        /*if(admobAdManager.isAdLoad)
        {
             admobAdManager.loadInterstitialAd(this, getResources().getString(R.string.interstitial_ad_id), 1, new AdmobAdManager.OnAdClosedListener() {
            @Override
            public void onAdClosed() {

                }
            });
        }
        else {

        }*/

        initAnimation();

        pref = getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        ratePref = getSharedPreferences("RatePreference", MODE_PRIVATE);
        rateStatus = ratePref.getBoolean("RateStatus", false);
        //getSharedPreference();

        rlPlayingSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*toolbar.setVisibility(View.GONE);
                rlPlayingSong.setVisibility(View.GONE);
                topNavigation.setVisibility(View.GONE);*/
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.addRule(0, 0);
                slidingUpPanelLayout.setLayoutParams(params);
                rlPlayingSongActivityLayout.setLayoutParams(params);
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                getIntentMethod();
            }
        });

        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();

        ivPlayPauseLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    playPauseButtonClick();
                } else {
                    mainActivity.onClickSong(pos, actName);

                }
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        ivBAck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                slidingUpPanelOperations();
//                rlPlayingSong.setVisibility(View.VISIBLE);
//
//                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                setFragment();
                /*if(!CHILD_FRAGMENT) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toolbar.setVisibility(View.VISIBLE);
                            topNavigation.setVisibility(View.VISIBLE);
                        }
                    }, 500);
                }*/

            }
        });

        ivEqualizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicService != null) {
                    showEqualizerBottomDialog();
//                    Intent equalizerIntent = new Intent(MainActivity.this, EqualizerActivity.class);
//                    startActivity(equalizerIntent);
                } else {
                    Toast.makeText(MainActivity.this, "Media player is not started", Toast.LENGTH_LONG).show();
                }
            }
        });

        ivShuffleSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!IS_SHUFFLED) {
                    shuffledList = songsList;
                    Collections.shuffle(shuffledList);
                    IS_SHUFFLED = true;
                    Toast.makeText(MainActivity.this, "Songs Shuffled", Toast.LENGTH_SHORT).show();
                    ivShuffleSong.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.SelectedTab), android.graphics.PorterDuff.Mode.MULTIPLY);
                } else {
                    ivShuffleSong.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                    IS_SHUFFLED = false;
                    Toast.makeText(MainActivity.this, "Playing in original sequence", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivFavouritesSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor checkUser = DB.getFavouritesFromId(songsList.get(position).getName());

                if (checkUser.getCount() <= 0) {
                    boolean checkAdd = DB.addToFavourites(songPath, songTitle, artistName, songAlbum, songDuration, songId, songAlbumArt.toString());
                    if (checkAdd) {
                        Toast.makeText(MainActivity.this, "Added to Favourites", Toast.LENGTH_SHORT).show();
                        ivFavouritesSong.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.SelectedTab), android.graphics.PorterDuff.Mode.MULTIPLY);
                    }
                } else {
                    boolean checkDelFav = DB.removeFromFavourites(songsList.get(position).getName());
                    if (checkDelFav) {
                        Toast.makeText(MainActivity.this, "Remove from Favourites", Toast.LENGTH_SHORT).show();
                        ivFavouritesSong.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
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
                if (musicService != null && b) {
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

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (musicService != null) {
                    int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                    tvSongPlayedTime.setText(formattedTime(mCurrentPosition));
                    tvDuration.setText(formattedTime(Integer.parseInt(songsList.get(position).getDuration()) / 1000));
                }
                handler.postDelayed(this, 100);
            }
        });

        //adding sliding up panel listener

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
//                toolbar.setVisibility(View.GONE);
//                topNavigation.setVisibility(View.GONE);
                rlPlayingSongActivityLayout.setAlpha(slideOffset);
                rlPlayingSong.setAlpha(1 - slideOffset);
                toolbar.setAlpha(1 - slideOffset);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.addRule(0, 0);
                slidingUpPanelLayout.setLayoutParams(params);
                rlPlayingSongActivityLayout.setLayoutParams(params);


            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

                if (newState.toString().equals("EXPANDED")) {
                    rlBannerAds.setVisibility(View.VISIBLE);
                    admobAdManager.LoadAdaptiveBanner(MainActivity.this, rlBannerAds, getResources().getString(R.string.banner_ad_id), MainActivity.this);
                    slidingUpPanelLayout.getChildAt(1).setOnClickListener(null);
                    rlPlayingSong.setVisibility(View.GONE);

//                    toolbar.setVisibility(View.GONE);
//                    topNavigation.setVisibility(View.GONE);
                    getIntentMethod();

                } else if (newState.toString().equals("COLLAPSED")) {
                    rlBannerAds.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    params.addRule(RelativeLayout.ABOVE, R.id.tlTopNavigation);
                    params.addRule(RelativeLayout.BELOW, R.id.toolbar);
                    slidingUpPanelLayout.setLayoutParams(params);
                    rlPlayingSong.setVisibility(View.VISIBLE);

                    RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    params1.addRule(RelativeLayout.BELOW, R.id.rlPlayingSong1);
                    rlPlayingSongActivityLayout.setLayoutParams(params1);

                    setFragment();

                }
            }
        });

        slidingUpPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }


    public void slidingUpPanelOperations() {
        rlBannerAds.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ABOVE, R.id.tlTopNavigation);
        params.addRule(RelativeLayout.BELOW, R.id.toolbar);
        slidingUpPanelLayout.setLayoutParams(params);

        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params1.addRule(RelativeLayout.BELOW, R.id.rlPlayingSong1);
        rlPlayingSongActivityLayout.setLayoutParams(params1);

        setFragment();
        if (!CHILD_FRAGMENT) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    // toolbar.setVisibility(View.VISIBLE);
                    topNavigation.setVisibility(View.VISIBLE);
                    rlPlayingSong.setVisibility(View.VISIBLE);
                }
            }, 300);
        } else {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    rlPlayingSong.setVisibility(View.VISIBLE);
                }
            }, 300);
        }
    }

    private String formattedTime(int mCurrentPosition) {

        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        String hour = String.valueOf(mCurrentPosition / 3600);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }
    }

    public void getIntentMethod() {
        position = pos;
        activityName = actName;

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
            Intent intent = new Intent(this, MusicService.class);
            intent.putExtra("ServicePosition", position);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
            ivPlayPause.setImageResource(R.drawable.pause);
            ivPlayPauseLibrary.setImageResource(R.drawable.pause);
            ivSongImage.startAnimation(animation);
        } else if (currentSongIndex != position) {
            Intent intent = new Intent(this, MusicService.class);
            intent.putExtra("ServicePosition", position);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
            ivPlayPause.setImageResource(R.drawable.pause);
            ivPlayPauseLibrary.setImageResource(R.drawable.pause);
            ivSongImage.startAnimation(animation);
        }


        /*if( mediaPlayer != null && mediaPlayer.isPlaying() && FIRST_OPEN)
        {
            musicService.pause();
        }*/
        /*else if(!currentActivityName.equals(activityName))
        {
            Intent intent = new Intent(this, MusicService.class);
            intent.putExtra("ServicePosition", position);
            startService(intent);
            ivPlayPause.setImageResource(R.drawable.pause);
        }*/
        setBottomLayout();
        if (musicService != null) {
            musicService.showNotification(R.drawable.ic_baseline_pause_24, "Pause");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        getSharedPreference();

        getIntentMethod();

        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, BIND_AUTO_CREATE);

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (musicService != null) {
                    seekBar.setMax(musicService.getDuration() / 1000);
                    int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                    tvSongPlayedTime.setText(formattedTime(mCurrentPosition));
                    tvDuration.setText(formattedTime(Integer.parseInt(songsList.get(position).getDuration()) / 1000));
                }
                handler.postDelayed(this, 100);
            }
        });

        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();

        //setMinimizePlayer();
    }

    public void getSharedPreference() {
        SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        name = preferences.getString("Name", "");
        artist = preferences.getString("Artist", "");
        pos = preferences.getInt("Position", 0);
        currentSongIndex = pref.getInt("Position", 0);
        actName = preferences.getString("ActivityName", "");
        albumArtUri = Uri.parse(preferences.getString("AlbumArt", ""));
        currentActivityName = pref.getString("ActivityName", "");
        currentSongName = pref.getString("Name", "");
        if (pos > audioList.size()) {
            pos = audioList.size() - 1;
            currentSongIndex = audioList.size() - 1;
            name = audioList.get(audioList.size() - 1).getName();
            artist = audioList.get(audioList.size() - 1).getArtist();
            actName = "";
            albumArtUri = audioList.get(audioList.size() - 1).getAlbumArt();
            currentActivityName = "";
            currentSongName = audioList.get(audioList.size() - 1).getName();
        }


        if (FIRST_OPEN) {
            if (!actName.equals("Playlist") || !actName.equals("")) {
                for (int i = 0; i < audioList.size(); i++) {
                    if (name.equals(audioList.get(i).getName())) {
                        actName = "";
                        pos = i;
                        break;
                    }
                }
            }
        }

        if (name != null || !name.equals("")) {
            SHOW_MINI_PLAYER = true;
            //  flMinimizedPlayer.setVisibility(View.VISIBLE);
        } else {
            SHOW_MINI_PLAYER = false;
            //  flMinimizedPlayer.setVisibility(View.GONE);
        }

        setFragment();
    }

    private void setFragment() {

        tvPlayingSongName.setText(name);
        tvPlayingSongArtist.setText(artist);

        /*tvPlayingSongArtist.setText(artist);
        tvPlayingSongName.setText(name);*/

        Bitmap bit = null;
        try {
            bit = MediaStore.Images.Media.getBitmap(getContentResolver(), albumArtUri);
            // bitmap = ImageDecoder.decodeBitmap(source);
        } catch (FileNotFoundException exception) {
        } catch (IOException e) {

            e.printStackTrace();
        }
        if (bit != null) {
            ivPlayingSongImage.setImageBitmap(bit);
        } else {
            ivPlayingSongImage.setImageResource(R.drawable.music_photo);
        }

    }

    public void setBottomLayout() {
        tvDuration.setText(formattedTime(Integer.parseInt(songsList.get(position).getDuration()) / 1000));
        Log.e("Error", "Position : " + position);
        Uri albumArtUri = songsList.get(position).getAlbumArt();
        bitmap = null;
        //ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(), albumArtUri);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), albumArtUri);
            // bitmap = ImageDecoder.decodeBitmap(source);
        } catch (FileNotFoundException exception) {
        } catch (IOException e) {

            e.printStackTrace();
        }

        songTitle = songsList.get(position).getName();
        songPath = songsList.get(position).getPath();
        songAlbum = songsList.get(position).getAlbum();
        songId = songsList.get(position).getId();
        songDuration = songsList.get(position).getDuration();
        songAlbumArt = songsList.get(position).getAlbumArt();
        artistName = songsList.get(position).getArtist();

        tvSongName.setText(songTitle);

        if (bitmap != null) {
            ivSongImage.setImageBitmap(bitmap);
            Bitmap blurredBitmap = BlurBuilder.blur(MainActivity.this, bitmap);
            rlPlayingSongActivityLayout.setBackground(new BitmapDrawable(this.getResources(), blurredBitmap));
        } else {
            Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.music_photo);
            ivSongImage.setImageResource(R.drawable.music_photo);
            Bitmap blurredBitmap = BlurBuilder.blur(MainActivity.this, icon);
            rlPlayingSongActivityLayout.setBackground(new BitmapDrawable(this.getResources(), blurredBitmap));
        }
        currentSongIndex = position;

        tvArtistName.setText(artistName);

        //ivPlayPause.setImageResource(R.drawable.pause);

        seekBar.setProgress(0);
        seekBar.setMax(100);

        Cursor checkFavUser = DB.getFavouritesFromId(songsList.get(position).getName());
        if (checkFavUser.getCount() > 0) {
            ivFavouritesSong.setColorFilter(ContextCompat.getColor(this, R.color.SelectedTab), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {
            ivFavouritesSong.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        Cursor checkUser = DB.getRecentSongFromId(songsList.get(position).getId());

        if (checkUser.getCount() == 0) {
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

        getSharedPreference();
    }

    public void showEqualizerBottomDialog() {
        BottomSheetEqualizerFragment bottomEqualizer = new BottomSheetEqualizerFragment();
        bottomEqualizer.show(getSupportFragmentManager(), "EqualizerDialog");
        // bottomEqualizer.setCancelable(true);
    }

    public void showBottomSheetDialog() {

        BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this, R.style.AppBottomSheetDialogTheme);
        dialog.setContentView(R.layout.bottom_sheet_dialog);
        dialog.setCancelable(true);

        RelativeLayout rlCreateNewPlaylist = dialog.findViewById(R.id.rlCreateNewPlaylist);
        RelativeLayout rlFavouriteLayout = dialog.findViewById(R.id.rlFavouriteLayout);
        RecyclerView rvNewCreatedPlaylist = dialog.findViewById(R.id.rvNewCreatedPlaylist);
        TextView tvFavPlaylist = dialog.findViewById(R.id.tvFavPlaylist);

        Cursor checkUser = DB.getFavouritesFromId(songsList.get(position).getName());
        if (checkUser.getCount() > 0) {
            tvFavPlaylist.setText("UnFavourites");
        } else {
            tvFavPlaylist.setText("Favourites");
        }

        rlFavouriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor checkUser = DB.getFavouritesFromId(songsList.get(position).getName());

                if (checkUser.getCount() <= 0) {
                    boolean checkAdd = DB.addToFavourites(songsList.get(position).getPath(), songTitle, artistName, songsList.get(position).getAlbum(), songsList.get(position).getDuration(), songsList.get(position).getId(), songsList.get(position).getAlbumArt().toString());
                    if (checkAdd) {

                        Toast.makeText(MainActivity.this, "Added to Favourites", Toast.LENGTH_SHORT).show();
                        ivFavouritesSong.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.SelectedTab), android.graphics.PorterDuff.Mode.MULTIPLY);
                    }
                } else {

                    boolean deleteFav = DB.removeFromFavourites(songTitle);
                    if (deleteFav) {
                        Toast.makeText(MainActivity.this, "Removed from Favourites", Toast.LENGTH_SHORT).show();
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
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                createdPlaylist.add(c.getString(c.getColumnIndex("name")));
            }
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager = linearLayoutManager;
        rvNewCreatedPlaylist.setLayoutManager(layoutManager);

        createdPlaylistAdapter = new CreatedPlaylistAdapter(this, createdPlaylist, new CreatedPlaylistAdapter.addToPlaylistInterface() {
            @Override
            public void addToPlaylist(int pos, String playlistName) {
                Cursor checkUser = DB.getAlreadyAddedSongs(songsList.get(position).getName(), playlistName);

                if (checkUser.getCount() <= 0) {
                    boolean checkAdd = DB.insetIntoCreatedTable(playlistName, songsList.get(position).getPath(), songTitle, artistName, songsList.get(position).getAlbum(), songsList.get(position).getDuration(), songsList.get(position).getId(), songsList.get(position).getAlbumArt().toString());
                    if (checkAdd) {
                        Toast.makeText(MainActivity.this, "Added to Playlist", Toast.LENGTH_SHORT).show();
                        //ivFavouritesSong.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.SelectedTab), android.graphics.PorterDuff.Mode.MULTIPLY);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Added to Favourites", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        rvNewCreatedPlaylist.setAdapter(createdPlaylistAdapter);

        dialog.show();

    }

    public void showCreatePlaylistDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
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
                Toast.makeText(MainActivity.this, "Playlist created", Toast.LENGTH_SHORT).show();
                InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                dialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        dialog.show();
    }

    private void prevThreadBtn() {

        ivPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousButtonClick();
            }
        });
    }

    private void nextThreadBtn() {

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextButtonClick();
            }
        });
    }

    private void playThreadBtn() {

        ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPauseButtonClick();
            }
        });
    }

    public void playPauseButtonClick() {
        if (musicService != null) {

            if (musicService.isPlaying()) {
                musicService.pause();
                ivSongImage.clearAnimation();
                musicService.showNotification(R.drawable.ic_baseline_play_arrow_24, "Play");
                ivPlayPause.setImageResource(R.drawable.play);
                ivPlayPauseLibrary.setImageResource(R.drawable.play);
                seekBar.setMax(musicService.getDuration() / 1000);
                MainActivity.this.runOnUiThread(new Runnable() {
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
                ivPlayPauseLibrary.setImageResource(R.drawable.pause);
                seekBar.setMax(musicService.getDuration() / 1000);
                MainActivity.this.runOnUiThread(new Runnable() {
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
        //  musicService.onComplete();
    }

    public void nextButtonClick() {
        if (musicService.isPlaying() && musicService != null) {
            musicService.stop();
            musicService.release();
            position = ((position + 1) % songsList.size());
            musicService.createMediaPlayer(position);
            setBottomLayout();
            seekBar.setMax(musicService.getDuration() / 1000);
            MainActivity.this.runOnUiThread(new Runnable() {
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
            // musicService.onComplete();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    nextButtonClick();
                }
            });
            ivPlayPause.setImageResource(R.drawable.pause);
            ivPlayPauseLibrary.setImageResource(R.drawable.pause);
            ivSongImage.startAnimation(animation);
            musicService.start();
            musicService.showNotification(R.drawable.ic_baseline_pause_24, "Pause");
        } else {
            musicService.stop();
            musicService.release();
            position = ((position + 1) % songsList.size());
            musicService.createMediaPlayer(position);
            setBottomLayout();
            seekBar.setMax(musicService.getDuration() / 1000);
            MainActivity.this.runOnUiThread(new Runnable() {
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
            // musicService.onComplete();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    nextButtonClick();
                }
            });
            ivPlayPause.setImageResource(R.drawable.pause);
            ivPlayPauseLibrary.setImageResource(R.drawable.pause);
            ivSongImage.clearAnimation();
            musicService.start();
            musicService.showNotification(R.drawable.ic_baseline_pause_24, "Pause");
        }
    }

    public void previousButtonClick() {
        if (musicService.isPlaying()) {
            musicService.stop();
            musicService.release();
            position = ((position - 1) < 0 ? (songsList.size() - 1) : (position - 1));
            musicService.createMediaPlayer(position);
            setBottomLayout();
            seekBar.setMax(musicService.getDuration() / 1000);
            MainActivity.this.runOnUiThread(new Runnable() {
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
            // musicService.onComplete();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    nextButtonClick();
                }
            });
            ivPlayPause.setImageResource(R.drawable.pause);
            musicService.start();
            ivSongImage.startAnimation(animation);
            musicService.showNotification(R.drawable.ic_baseline_pause_24, "Pause");
        } else {
            musicService.stop();
            musicService.release();
            position = ((position - 1) < 0 ? (songsList.size() - 1) : (position - 1));
            musicService.createMediaPlayer(position);
            setBottomLayout();
            seekBar.setMax(musicService.getDuration() / 1000);
            MainActivity.this.runOnUiThread(new Runnable() {
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
            // musicService.onComplete();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    nextButtonClick();
                }
            });
            ivPlayPause.setImageResource(R.drawable.play);
            ivSongImage.clearAnimation();
            musicService.showNotification(R.drawable.ic_baseline_play_arrow_24, "Play");
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder) iBinder;
        musicService = myBinder.getService();

        musicService.setCallBack(this);
        seekBar.setMax(musicService.getDuration() / 1000);

        if (musicService.isPlaying()) {
            ivPlayPauseLibrary.setImageResource(R.drawable.pause);
            ivPlayPause.setImageResource(R.drawable.pause);
            ivSongImage.startAnimation(animation);
        } else {
            ivPlayPauseLibrary.setImageResource(R.drawable.play);
            ivPlayPause.setImageResource(R.drawable.play);
            ivSongImage.clearAnimation();
        }
        musicService.showNotification(R.drawable.ic_baseline_pause_24, "Pause");
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextButtonClick();
            }
        });

        FIRST_OPEN = false;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        //musicService = null;
    }


    @Override
    public void onBackPressed() {

        if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {

//            toolbar.setVisibility(View.VISIBLE);
//            topNavigation.setVisibility(View.VISIBLE);
//            rlPlayingSong.setVisibility(View.VISIBLE);
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            slidingUpPanelOperations();

            /*if(!CHILD_FRAGMENT) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toolbar.setVisibility(View.VISIBLE);
                        topNavigation.setVisibility(View.VISIBLE);
                    }
                }, 500);
            }*/
            Log.e("Pannel", " ::::   " + slidingUpPanelLayout.getPanelState());
        } else if (CHILD_FRAGMENT) {
            toolbar.setVisibility(View.VISIBLE);
            topNavigation.setVisibility(View.VISIBLE);
            CHILD_FRAGMENT = false;
            this.getSupportFragmentManager().popBackStack();
            viewPager.setPagingEnabled(true);
        } else {
            if(rateStatus)
            {
                showExitDialog();
            }
            else {
                showRatingDialog();
            }
        }

    }

    public void showExitDialog()
    {
        Dialog exitDialog = new Dialog(MainActivity.this);
        exitDialog.setContentView(R.layout.exit_dialog);
        exitDialog.setCancelable(false);
        exitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        /*if(background_ring != null && background_ring.isPlaying())
        {
            background_ring.pause();
        }*/
        //backgroundMusic.pauseMusic();

        TextView yes_button = exitDialog.findViewById(R.id.tvYes);
        yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exitDialog.dismiss();
                MainActivity.this.finish();
            }
        });

        TextView no_button =  exitDialog.findViewById(R.id.tvNo);
        no_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                exitDialog.dismiss();
                /*if(background_ring != null && !background_ring.isPlaying())
                {
                    background_ring.start();
                }*/
                //backgroundMusic.startMusic();
            }
        });

        exitDialog.show();
    }

    public void showRatingDialog()
    {
        Dialog rating_dialog = new Dialog(this);
        rating_dialog.setContentView(R.layout.rate_us_dialog);
        rating_dialog.setCancelable(false);
        rating_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        SharedPreferences.Editor edit = ratePref.edit();
        //backgroundMusic.pauseMusic();
        // feedback_edit.setVisibility(View.GONE);
        RatingBar ratingBar = rating_dialog.findViewById(R.id.ratingBar);

        TextView submit_button = rating_dialog.findViewById(R.id.tvSubmit);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();
                if(rating == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please click on 5 Star to give us rating on playstore.", Toast.LENGTH_LONG).show();
                }
                else if(rating <= 3 && rating > 0)
                {
                    edit.putBoolean("RateStatus", true);
                    edit.apply();
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                            Uri.parse("mailto:" + Uri.encode(getResources().getString(R.string.email_id))));

                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));

                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send email via..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(),"There are no email clients installed.", Toast.LENGTH_SHORT)
                                .show();
                    }

                    rating_dialog.dismiss();
                    finish();

                }
                else if(rating > 3)
                {
                    edit.putBoolean("AppFirst", true);
                    edit.apply();
                    String packageName = "com.example.nonograms";
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + packageName)));
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
                    }
                    rating_dialog.dismiss();
                    finish();
                }
            }
        });

        TextView cancel_button = rating_dialog.findViewById(R.id.tvLater);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit.putBoolean("AppFirst", false);
                edit.apply();
                rating_dialog.dismiss();
                finish();
            }
        });
        rating_dialog.show();
    }

    public void onClickSong(int pos1, String actName1) {
        pos = pos1;
        actName = actName1;

        getIntentMethod();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // unbindService(this);
    }

    @Override
    public void onAdLoaded(Object object) {

    }

    @Override
    public void onAdClosed() {

    }

    @Override
    public void onLoadError(String errorCode) {

    }

    public static class BlurBuilder {
        private static final float BITMAP_SCALE = 0.2f;
        private static final float BLUR_RADIUS = 5f;

        public static Bitmap blur(Context context, Bitmap image) {
            int width = Math.round(image.getWidth() * BITMAP_SCALE);
            int height = Math.round(image.getHeight() * BITMAP_SCALE);

            Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
            Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

            RenderScript rs = RenderScript.create(context);
            ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
            theIntrinsic.setRadius(BLUR_RADIUS);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);

            return outputBitmap;
        }
    }

    private void initAnimation() {
        animShow = AnimationUtils.loadAnimation(this, R.anim.view_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.view_hide);
    }
}