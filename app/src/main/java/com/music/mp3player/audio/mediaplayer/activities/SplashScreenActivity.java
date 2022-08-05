package com.music.mp3player.audio.mediaplayer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.music.mp3player.audio.mediaplayer.AdmobAdManager;
import com.music.mp3player.audio.mediaplayer.GetAllSongs;
import com.music.mp3player.audio.mediaplayer.R;

import java.util.ArrayList;
import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {

    GetAllSongs allSongs;
    public static boolean FIRST_OPEN = true;
    private AdmobAdManager admobAdManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if(Build.VERSION.SDK_INT >= 21)
        {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.Transparent));
            window.setBackgroundDrawableResource(R.drawable.bg_4);
        }

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        admobAdManager = AdmobAdManager.getInstance(this);
        admobAdManager.loadInterstitialAd(this, getResources().getString(R.string.interstitial_ad_id));

        if(isPermissionGranted()) {
            init();
        }
        else {
            Intent intent = new Intent(SplashScreenActivity.this, PermissionActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void init() {

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                String actName = pref.getString("ActivityName", "");
                String playlistName = pref.getString("PlaylistName", "");
                allSongs = new GetAllSongs(SplashScreenActivity.this);
                allSongs.getAudioFiles();
//                allSongs.getSongAlbumWise();
//                allSongs.getSongsArtistWise();
                allSongs.getFavouritesSongs();
                allSongs.getRecentList();
                if(actName.equals("CreatedPlayListSongs") && !playlistName.equals(""))
                {
                    allSongs.getPlayListSongs(playlistName);
                }
//                allSongs.getGenresList();
               // allSongs.getAllFolder();
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 1500);

    }

    private void GetPermissions() {

        List<String> permissionsNeeded = new ArrayList<>();
        final List<String> permissionsList = new ArrayList<>();

        if(!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read External Storage");

        if(!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write External Storage");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                for (int i = 0; i < 1; i++)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), 1);
                    }

                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), 1);
            }
            return;
        }

        if (isPermissionGranted())
            init();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }

        return true;
    }

    public boolean isPermissionGranted() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(SplashScreenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                return true;
            }
            else {

                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (permissions.length >= 1)
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                }
        }
        else
        {
            GetPermissions();
        }


    }
}