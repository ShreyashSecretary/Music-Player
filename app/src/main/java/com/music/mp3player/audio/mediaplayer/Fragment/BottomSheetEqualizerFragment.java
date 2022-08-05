package com.music.mp3player.audio.mediaplayer.Fragment;

import androidx.annotation.NonNull;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;

import com.bullhead.equalizer.EqualizerFragment;
import com.bullhead.equalizer.EqualizerModel;
import com.bullhead.equalizer.Settings;
import com.music.mp3player.audio.mediaplayer.EqualizerSettings;
import com.music.mp3player.audio.mediaplayer.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import static com.music.mp3player.audio.mediaplayer.Service.MusicService.mediaPlayer;

public class BottomSheetEqualizerFragment extends BottomSheetDialogFragment {

    private int sessionId;

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_equalizer, null);

        dialog.setContentView(view);
        loadEqualizerSettings();

        if(mediaPlayer != null) {
            sessionId = mediaPlayer.getAudioSessionId();
        }

        EqualizerFragment equalizerFragment = EqualizerFragment.newBuilder()
                .setAccentColor(Color.parseColor("#ffd240"))
                .setAudioSessionId(sessionId)
                .build();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.eqFrame, equalizerFragment)
                .commit();
    }

    private void saveEqualizerSettings(){
        if (Settings.equalizerModel != null){
            EqualizerSettings settings = new EqualizerSettings();
            settings.bassStrength = Settings.equalizerModel.getBassStrength();
            settings.presetPos = Settings.equalizerModel.getPresetPos();
            settings.reverbPreset = Settings.equalizerModel.getReverbPreset();
            settings.seekbarpos = Settings.equalizerModel.getSeekbarpos();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

            Gson gson = new Gson();
            preferences.edit()
                    .putString(PREF_KEY, gson.toJson(settings))
                    .apply();
        }
    }

    private void loadEqualizerSettings(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        Gson gson = new Gson();
        EqualizerSettings settings = gson.fromJson(preferences.getString(PREF_KEY, "{}"), EqualizerSettings.class);
        EqualizerModel model = new EqualizerModel();
        model.setBassStrength(settings.bassStrength);
        model.setPresetPos(settings.presetPos);
        model.setReverbPreset(settings.reverbPreset);
        model.setSeekbarpos(settings.seekbarpos);

        Settings.isEqualizerEnabled = true;
        Settings.isEqualizerReloaded = true;
        Settings.bassStrength = settings.bassStrength;
        Settings.presetPos = settings.presetPos;
        Settings.reverbPreset = settings.reverbPreset;
        Settings.seekbarpos = settings.seekbarpos;
        Settings.equalizerModel = model;
    }

    public static final String PREF_KEY = "equalizer";

    @Override
    public void onPause() {
        super.onPause();
        saveEqualizerSettings();
    }
}