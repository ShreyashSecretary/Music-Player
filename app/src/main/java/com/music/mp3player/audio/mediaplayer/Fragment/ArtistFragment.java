package com.music.mp3player.audio.mediaplayer.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.music.mp3player.audio.mediaplayer.Adapter.ArtistAdapter;
import com.music.mp3player.audio.mediaplayer.GetAllSongs;
import com.music.mp3player.audio.mediaplayer.ModelClass.ModelArtistClass;
import com.music.mp3player.audio.mediaplayer.R;

import java.util.ArrayList;
import java.util.List;

import static com.music.mp3player.audio.mediaplayer.GetAllSongs.sArtworkUri;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.CHILD_FRAGMENT;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.tvToolbarText;

public class ArtistFragment extends Fragment {

    private View view;
    private RecyclerView rvArtistList;
    private ArtistAdapter adapter;
    private GetAllSongs allSongs;
    private TextView ivNoData;
    ModelArtistClass artistClass;
    private List<ModelArtistClass> myList = new ArrayList<>();
    private List<ModelArtistClass> artistList = new ArrayList<>();
    private Dialog dialog;
    private Activity context;
    FrameLayout flArtistSongFragment;

    public ArtistFragment(Activity context) {
        this.context = context;
        tvToolbarText.setText("Artists");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_artist, container, false);

        rvArtistList = view.findViewById(R.id.rvArtist);
        ivNoData = view.findViewById(R.id.ivNoData);
        flArtistSongFragment = view.findViewById(R.id.flArtistSongs);
        ivNoData.setVisibility(View.GONE);



        dialog = new ProgressDialog(getContext(), R.style.DialogStyle);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.progress_dialog_layout);

        CHILD_FRAGMENT = false;
       // dialog.show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                myList = getSongsArtistWise();
               // dialog.dismiss();
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setArtistAdapter();
                    }
                });
            }
        },100);

       // setArtistAdapter();
       // init();

        return view;
    }

    private void init() {

        OtherDataTask dataTask = new OtherDataTask();
        dataTask.execute();
    }

    public void setArtistAdapter()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager = linearLayoutManager;
        rvArtistList.setLayoutManager(layoutManager);

        if(myList.size() > 0)
        {
            ivNoData.setVisibility(View.GONE);
        }
        else
        {
            ivNoData.setVisibility(View.VISIBLE);
            ivNoData.setText("No Artists Found");
        }

            adapter = new ArtistAdapter(getContext(), myList, new ArtistAdapter.GoToArtistSongs() {
                @Override
                public void artistSongs(String name) {
                    Fragment fragment = new ArtistSongsFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.flArtistSongs, fragment);
                    transaction.addToBackStack("Artist");
                    transaction.commitAllowingStateLoss();
                   // artistList.clear();

                    Bundle args = new Bundle();
                    args.putString("ArtistName", name);
                    fragment.setArguments(args);
                }
            });
            rvArtistList.setAdapter(adapter);

           // getSongsArtistWise();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /*    public void getSongsArtistWise()
    {
//        artistList = new ArrayList<>();
//        artistList.clear();
        String[] projection = new String[]{MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Artists.NUMBER_OF_ALBUMS, MediaStore.Audio.Artists.NUMBER_OF_TRACKS};
        Cursor artistCursor = getContext().getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, projection, null ,null, null);

        if(artistCursor != null)
        {
            while (artistCursor.moveToNext())
            {
                artistClass = new ModelArtistClass();
                String artistId = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists._ID));
                String artistName = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                String artistNoOfSongs = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
                //String image = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.Albums.ALBUM_ID));

                artistClass.setId(artistId);
                artistClass.setArtistName(artistName);
                artistClass.setNoOfSongs(artistNoOfSongs);

                Cursor c = getContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.ARTIST + "=?", new String[]{artistName}, null);
                if (c.getCount() > 0)
                {
                    while (c.moveToNext())
                    {
                        String  albumId = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                        String albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId)).toString();

                        if (c.isLast())
                        {
                            artistClass.setArt(albumArtUri);
                        }
                    }
                }

                //artistList.add(artistClass);
                adapter.addArtistItem(artistClass);

                if(artistList.size() > 0)
                {
                    ivNoData.setVisibility(View.GONE);
                }
                else
                {
                    ivNoData.setVisibility(View.VISIBLE);
                }
            }
        }
        artistCursor.close();
    }*/

    class OtherDataTask extends AsyncTask<Void, Void, Void> {

        public OtherDataTask() {

        }

        @Override
        protected Void doInBackground(Void... voids) {
            setArtistAdapter();
            return null;
        }
    }

    /*@Override
    public void onResume() {
        super.onResume();

        setArtistAdapter();
    }*/

    public List<ModelArtistClass> getSongsArtistWise()
    {
        artistList = new ArrayList<>();
        artistList.clear();
        String[] projection = new String[]{MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Artists.NUMBER_OF_ALBUMS, MediaStore.Audio.Artists.NUMBER_OF_TRACKS};
        Cursor artistCursor = context.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, projection, null ,null, null);

        if(artistCursor != null)
        {
            while (artistCursor.moveToNext())
            {
                artistClass = new ModelArtistClass();
                String artistId = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists._ID));
                String artistName = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                String artistNoOfSongs = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
                //String image = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.Albums.ALBUM_ID));

                artistClass.setId(artistId);
                artistClass.setArtistName(artistName);
                artistClass.setNoOfSongs(artistNoOfSongs);

                Cursor c = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.ARTIST + "=?", new String[]{artistName}, null);
                if (c.getCount() > 0)
                {
                    while (c.moveToNext())
                    {
                        String  albumId = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                        String albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId)).toString();

                        if (c.isLast())
                        {
                            artistClass.setArt(albumArtUri);
                        }
                    }
                }

                artistList.add(artistClass);
            }
        }
        artistCursor.close();
        return artistList;
    }
}
