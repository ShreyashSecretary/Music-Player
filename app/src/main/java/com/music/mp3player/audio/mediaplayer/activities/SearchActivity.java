package com.music.mp3player.audio.mediaplayer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.music.mp3player.audio.mediaplayer.Adapter.SearchSongsAdapter;
import com.music.mp3player.audio.mediaplayer.GetAllSongs;
import com.music.mp3player.audio.mediaplayer.ModelClass.AudioModel;
import com.music.mp3player.audio.mediaplayer.R;

import java.util.ArrayList;

import static com.music.mp3player.audio.mediaplayer.GetAllSongs.audioList;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.r;

public class SearchActivity extends AppCompatActivity {

    private ImageView ivBackSearch, search_input_hint_icon, search_input_close_icon;
    private EditText search_input;
    private RecyclerView rvSearchList;
    SearchSongsAdapter searchAdapter;
    GetAllSongs allSongs;
    private TextView ivNoData;
    private RelativeLayout rlSearchActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if(Build.VERSION.SDK_INT >= 21)
        {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.Transparent));
            window.setBackgroundDrawableResource(R.drawable.bg_3);
        }

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


        rlSearchActivity = findViewById(R.id.rlSearchActivity);

        rlSearchActivity.setBackgroundResource(R.drawable.bg_3);
      //  StatusBarUtil.setTransparent(SearchActivity.this);

        InputMethodManager inputMethodManager =(InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        ivBackSearch = findViewById(R.id.ivBackSearch);
        search_input_hint_icon = findViewById(R.id.search_input_hint_icon);
        search_input_close_icon = findViewById(R.id.search_input_close_icon);
        search_input = findViewById(R.id.search_input);
        rvSearchList = findViewById(R.id.rvSearchList);
        ivNoData = findViewById(R.id.ivNoData);
        ivNoData.setVisibility(View.GONE);
        search_input_close_icon.setVisibility(View.INVISIBLE);
        search_input.setFocusable(true);
        search_input.setCursorVisible(true);
        search_input.requestFocus();

        setSearchAdapter();

        ivBackSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
              //  InputMethodManager inputMethodManager1 = (InputMethodManager) SearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        search_input_close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_input.setText("");
                search_input_hint_icon.setVisibility(View.VISIBLE);
               // InputMethodManager inputMethodManager1 = (InputMethodManager) SearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                setSearchAdapter();
            }
        });

        search_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // if (s.toString().length() > 0) {
                if(s.length() > 0)
                {
                    search_input_close_icon.setVisibility(View.VISIBLE);
                }
                else
                {
                    search_input_close_icon.setVisibility(View.INVISIBLE);
                }
                searchAdapter = new SearchSongsAdapter(SearchActivity.this, audioList);
                filter(s.toString());
                rvSearchList.setAdapter(searchAdapter);
                        /*} else {
                            recyclerView.setAdapter(searchContactAdapter);
                        }*/
            }
        });
    }

    private void setSearchAdapter() {

        allSongs = new GetAllSongs(SearchActivity.this);
        allSongs.getAudioFiles();
        searchAdapter = new SearchSongsAdapter(SearchActivity.this, audioList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager = linearLayoutManager;
        rvSearchList.setLayoutManager(layoutManager);

        if(audioList.size() > 0)
        {
            ivNoData.setVisibility(View.GONE);
        }
        else
        {
            ivNoData.setVisibility(View.VISIBLE);
        }

        rvSearchList.setAdapter(searchAdapter);
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<AudioModel> filteredlist = new ArrayList<>();

        if (audioList.size() > 0) {
            for (AudioModel item : audioList) {
                if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredlist.add(item);
                }
            }
        }
        searchAdapter.filterList(filteredlist);
        //}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
//        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManagerhodManager.hideSoftInputFromWindow(, 0);
        finish();
    }
}