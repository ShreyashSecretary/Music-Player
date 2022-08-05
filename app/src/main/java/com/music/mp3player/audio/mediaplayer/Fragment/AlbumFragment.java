package com.music.mp3player.audio.mediaplayer.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Albums;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.music.mp3player.audio.mediaplayer.Adapter.AlbumAdapter;
import com.music.mp3player.audio.mediaplayer.AdmobAdManager;
import com.music.mp3player.audio.mediaplayer.GetAllSongs;
import com.music.mp3player.audio.mediaplayer.ModelClass.ModelAlbumClass;
import com.music.mp3player.audio.mediaplayer.R;

import java.util.ArrayList;
import java.util.List;

import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.CHILD_FRAGMENT;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.tvToolbarText;

public class AlbumFragment extends Fragment {

    private View view;
    private RecyclerView rvAlbumList;
    private AlbumAdapter adapter;
    private TextView ivNoData;
    private GetAllSongs allSongs;
    private ModelAlbumClass albumClass;
    private List<ModelAlbumClass> myList = new ArrayList<>();
    private List<ModelAlbumClass> albumList = new ArrayList<>();
    private Dialog dialog;
    private Activity context;
    private AdmobAdManager admobAdManager;

    public AlbumFragment(Activity context) {
        this.context = context;
        tvToolbarText.setText("Albums");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container, false);

        rvAlbumList = view.findViewById(R.id.rvAlbumList);
        ivNoData = view.findViewById(R.id.ivNoData);
        ivNoData.setVisibility(View.GONE);

        dialog = new ProgressDialog(getContext(), R.style.DialogStyle);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.progress_dialog_layout);

        CHILD_FRAGMENT = false;
       // dialog.show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                myList = getSongAlbumWise();
               // dialog.dismiss();
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAlbumAdapter();
                    }
                });
            }
        },100);

//        setAlbumAdapter();


        return view;
    }

    private void init() {

        OtherDataTask dataTask = new OtherDataTask();
        dataTask.execute();
    }

    public void setAlbumAdapter()
    {
//        allSongs = new GetAllSongs(getContext());
//        allSongs.getSongAlbumWise();
        GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
        RecyclerView.LayoutManager lm = glm;
        rvAlbumList.setLayoutManager(lm);

        if(myList.size() > 0)
        {
            ivNoData.setVisibility(View.GONE);
        }
        else
        {
            ivNoData.setVisibility(View.VISIBLE);
            ivNoData.setText("No Albums Found");
        }

        adapter = new AlbumAdapter(getContext(), myList, new AlbumAdapter.goToSongsFragment() {
            @Override
            public void goToSongs(int pos, String name) {
                Fragment fragment = new AlbumSongsFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.flAlbumSongs, fragment);
                transaction.addToBackStack("Album");
                transaction.commitAllowingStateLoss();
                //albumList.clear();

                Bundle args = new Bundle();
                args.putString("AlbumName", name);
                fragment.setArguments(args);
            }
        });
        rvAlbumList.setAdapter(adapter);

        //getSongAlbumWise();
    }

   /* public void getSongAlbumWise() {

//        albumList = new ArrayList<>();
//        albumList.clear();
        String[] projection = new String[]{Albums._ID, Albums.ALBUM, Albums.ARTIST, Albums.ALBUM_ART, Albums.NUMBER_OF_SONGS};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = MediaStore.Audio.Media.ALBUM + " ASC";
        Cursor cursor = getContext().getContentResolver().query(Albums.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, sortOrder);

        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                albumClass = new ModelAlbumClass();
                String id = cursor.getString(cursor.getColumnIndex(Albums._ID));
                String album = cursor.getString(cursor.getColumnIndex(Albums.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(Albums.ARTIST));
                String art = cursor.getString(cursor.getColumnIndex(Albums.ALBUM_ART));
                String noofSong = cursor.getString(cursor.getColumnIndex(Albums.NUMBER_OF_SONGS));

                albumClass.setId(id);
                albumClass.setAlbum(album);
                albumClass.setArtist(artist);
                albumClass.setArt(art);
                albumClass.setNoOfSongs(noofSong);

                //albumList.add(albumClass);
                adapter.addAlbumItem(albumClass);

                if(albumList.size() > 0)
                {
                    ivNoData.setVisibility(View.GONE);
                }
                else
                {
                    ivNoData.setVisibility(View.VISIBLE);
                }
            }
        }
        cursor.close();
    }*/

    class OtherDataTask extends AsyncTask<Void, Void, Void> {

        public OtherDataTask() {

        }

        @Override
        protected Void doInBackground(Void... voids) {
            setAlbumAdapter();
            return null;
        }
    }

    /*@Override
    public void onResume() {
        super.onResume();
        setAlbumAdapter();
    }*/

    public List<ModelAlbumClass> getSongAlbumWise() {

        albumList = new ArrayList<>();
        albumList.clear();
        String[] projection = new String[]{Albums._ID, Albums.ALBUM, Albums.ARTIST, Albums.ALBUM_ART, Albums.NUMBER_OF_SONGS};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = MediaStore.Audio.Media.ALBUM + " ASC";
        Cursor cursor = context.getContentResolver().query(Albums.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, sortOrder);

        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                albumClass = new ModelAlbumClass();
                String id = cursor.getString(cursor.getColumnIndex(Albums._ID));
                String album = cursor.getString(cursor.getColumnIndex(Albums.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(Albums.ARTIST));
                String art = cursor.getString(cursor.getColumnIndex(Albums.ALBUM_ART));
                String noofSong = cursor.getString(cursor.getColumnIndex(Albums.NUMBER_OF_SONGS));

                albumClass.setId(id);
                albumClass.setAlbum(album);
                albumClass.setArtist(artist);
                albumClass.setArt(art);
                albumClass.setNoOfSongs(noofSong);

                albumList.add(albumClass);
            }
        }
        cursor.close();
        return albumList;
    }
}
