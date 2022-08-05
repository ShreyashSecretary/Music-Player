package com.music.mp3player.audio.mediaplayer.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.music.mp3player.audio.mediaplayer.Adapter.GenreAdapter;
import com.music.mp3player.audio.mediaplayer.GetAllSongs;
import com.music.mp3player.audio.mediaplayer.ModelClass.ModelGenresClass;
import com.music.mp3player.audio.mediaplayer.R;

import java.util.ArrayList;
import java.util.List;

import static com.music.mp3player.audio.mediaplayer.GetAllSongs.sArtworkUri;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.CHILD_FRAGMENT;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.tvToolbarText;

public class GenreFragment extends Fragment {


    private View view;
    private RecyclerView rvGenreList;
    private GenreAdapter adapter;
    private GetAllSongs allSongs;
    private TextView ivNoData;
    private ModelGenresClass genresClass;
    private List<ModelGenresClass> myList = new ArrayList<>();
    private List<ModelGenresClass> genresLists = new ArrayList<>();
    private Dialog dialog;
    private Activity context;

    public GenreFragment(Activity context) {
        this.context = context;
        tvToolbarText.setText("Genres");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_genre, container, false);

        rvGenreList = view.findViewById(R.id.rvGenre);
        ivNoData = view.findViewById(R.id.ivNoData);
        ivNoData.setVisibility(View.GONE);

        dialog = new ProgressDialog(getContext(), R.style.DialogStyle);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.progress_dialog_layout);



        CHILD_FRAGMENT = false;
        //dialog.show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                myList = getGenresList();
                //dialog.dismiss();
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setGenresAdapter();
                    }
                });
            }
        },100);

//       setGenresAdapter();

        return view;
    }

    private void init() {
        OtherDataTask dataTask = new OtherDataTask();
        dataTask.execute();
    }

    private void setGenresAdapter() {

//        allSongs = new GetAllSongs(getContext());
//        allSongs.getGenresList();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager = linearLayoutManager;
        rvGenreList.setLayoutManager(layoutManager);

        if (myList.size() > 0) {
            ivNoData.setVisibility(View.GONE);
        } else {
            ivNoData.setVisibility(View.VISIBLE);
            ivNoData.setText("No Genres Founds");
        }

        adapter = new GenreAdapter(getContext(), myList, new GenreAdapter.GoToGenreSongs() {
            @Override
            public void genreSongs(String id, String name) {
                Fragment fragment = new GenresSongsFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.flGenreSongs, fragment);
                transaction.addToBackStack("Genre");
                transaction.commitAllowingStateLoss();

                Bundle bundle = new Bundle();
                bundle.putString("GenreName", name);
                bundle.putString("GenreId", id);
                fragment.setArguments(bundle);
            }
        });
        rvGenreList.setAdapter(adapter);

       // getGenresList();
    }

 /*   public void getGenresList()
    {
//        genresLists = new ArrayList<>();
//        genresLists.clear();
//        folderName.clear();
        int index;
        long genreId;
        int count;
        Uri uri;
        Cursor tempcursor;
        String[] proj1 = {MediaStore.Audio.Genres.NAME, MediaStore.Audio.Genres._ID};
        String[] proj2={MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.ALBUM_ID};

        Cursor genrecursor = getContext().getContentResolver().query(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,proj1,null, null, null);
        if(genrecursor.getCount() > 0)
        {
            while (genrecursor.moveToNext()){
                index = genrecursor.getColumnIndex(MediaStore.Audio.Genres.NAME);
                String z = genrecursor.getString(index);
                index = genrecursor.getColumnIndex(MediaStore.Audio.Genres._ID);
                String genreIdString = genrecursor.getString((index));
                if(genreIdString != null) {
                    genreId = Long.parseLong(genreIdString);
                    uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreId);

                    tempcursor = getContext().getContentResolver().query(uri, proj2, null, null, null);
                    //String b = tempcursor.getCount();
                    if (tempcursor.getCount() > 0) {
                        String a = genrecursor.getString(genrecursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME));
                        String b = genrecursor.getString(genrecursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID));
                        String c = String.valueOf(tempcursor.getCount());

                        genresClass = new ModelGenresClass();

                        while (tempcursor.moveToNext()) {
                            String albumId = tempcursor.getString(tempcursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                            String albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId)).toString();

                            if (tempcursor.isLast()) {
                                genresClass.setArt(albumArtUri);
                            }
                        }

                        genresClass.setId(b);
                        genresClass.setName(a);
                        genresClass.setNoOfSongs(c);

                        adapter.addGenreItem(genresClass);

                        if (genreList.size() > 0) {
                            ivNoData.setVisibility(View.GONE);
                        } else {
                            ivNoData.setVisibility(View.VISIBLE);
                        }
                        //genresLists.add(genresClass);
                    *//*do{

                        index=tempcursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                        String b = tempcursor.getString(index);
                        //String c = tempcursor.getString(get)
                        String c = "";

                    }while(tempcursor.moveToNext());*//*
                    }
                    tempcursor.close();
                }
            }
            genrecursor.close();
        }

    }*/

    class OtherDataTask extends AsyncTask<Void, Void, Void> {

        public OtherDataTask() {

        }

        @Override
        protected Void doInBackground(Void... voids) {
            setGenresAdapter();
            return null;
        }
    }

    public List<ModelGenresClass> getGenresList() {
        genresLists = new ArrayList<>();
        genresLists.clear();
        int index;
        long genreId;
        int count;
        Uri uri;
        Cursor tempcursor;
        String[] proj1 = {MediaStore.Audio.Genres.NAME, MediaStore.Audio.Genres._ID};
        String[] proj2 = {MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.ALBUM_ID};

        Cursor genrecursor = context.getContentResolver().query(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI, proj1, null, null, null);
        if (genrecursor.getCount() > 0) {
            while (genrecursor.moveToNext()) {
                index = genrecursor.getColumnIndex(MediaStore.Audio.Genres.NAME);
                String z = genrecursor.getString(index);
                index = genrecursor.getColumnIndex(MediaStore.Audio.Genres._ID);
                String genreIdString = genrecursor.getString((index));
                if(genreIdString != null ){
                    genreId = Long.parseLong(genreIdString);
                    uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreId);

                    tempcursor = context.getContentResolver().query(uri, proj2, null, null, null);
                    //String b = tempcursor.getCount();
                    if (tempcursor.getCount() > 0) {
                        String a = genrecursor.getString(genrecursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME));
                        String b = genrecursor.getString(genrecursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID));
                        String c = String.valueOf(tempcursor.getCount());

                        genresClass = new ModelGenresClass();

                        while (tempcursor.moveToNext()) {
                            String albumId = tempcursor.getString(tempcursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                            String albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId)).toString();

                            if (tempcursor.isLast()) {
                                genresClass.setArt(albumArtUri);
                            }
                        }

                        genresClass.setId(b);
                        genresClass.setName(a);
                        genresClass.setNoOfSongs(c);

                        genresLists.add(genresClass);
                    /*do{

                    index=tempcursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                    String b = tempcursor.getString(index);
                    //String c = tempcursor.getString(get)
                    String c = "";

                }while(tempcursor.moveToNext());*/
                    }
                    tempcursor.close();
                }
            }
            genrecursor.close();
        }
        return genresLists;
    }
}
