package com.music.mp3player.audio.mediaplayer.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
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

import com.music.mp3player.audio.mediaplayer.Adapter.FolderAdapter;
import com.music.mp3player.audio.mediaplayer.ModelClass.AudioModel;
import com.music.mp3player.audio.mediaplayer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.music.mp3player.audio.mediaplayer.Adapter.FolderAdapter.folderList;
import static com.music.mp3player.audio.mediaplayer.GetAllSongs.bucketList;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.CHILD_FRAGMENT;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.tvToolbarText;

public class FolderFragment extends Fragment {

    private TextView ivNoData;
    private RecyclerView rvFolderSongsList;
    String FolderName;
    private AudioModel audioModel;
    public static List<String> folderNameList = new ArrayList<>();
    private FolderAdapter adapter;
    private TextView tvFolderText;
    ProgressDialog progressDialog;
    private View view;
    private Activity activity;
    private int a=0;

    private ArrayList<String> myList = new ArrayList<>();

    public FolderFragment(Activity activity) {
        this.activity = activity;
        tvToolbarText.setText("Folders");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_folder, container, false);

     //   ivBackFolder = view.findViewById(R.id.ivBackFolder);
//        ivSearch = view.findViewById(R.id.ivSearch);
//        ivMore = view.findViewById(R.id.ivMoreOption);
        rvFolderSongsList = view.findViewById(R.id.rvFolderSongsList);
      //  tvFolderText = view.findViewById(R.id.tvFolderText);
        ivNoData = view.findViewById(R.id.ivNoData);
        ivNoData.setVisibility(View.GONE);



        CHILD_FRAGMENT = false;

       // setMinimizePlayer();

       // tvFolderText.setText("Folders");

        progressDialog = new ProgressDialog(getContext(), R.style.DialogStyle);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setContentView(R.layout.progress_dialog_layout);

       // progressDialog.show();
       // Toast.makeText(getContext(), "Folder Fragment call", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                folderNameList = bucketList;
                setFolderAdapter();
               // progressDialog.dismiss();
            }
        },100);

        return view;
    }

    private void setFolderAdapter() {

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager lm = llm;
        rvFolderSongsList.setLayoutManager(lm);

        adapter = new FolderAdapter(getContext(), folderNameList, new FolderAdapter.onClickFolderName() {

            @Override
            public void getFolderName(String folderName, int pos) {
                Fragment fragment = new FolderSongsFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
              // transaction.add(R.id.flMainFrameLayout, "Fragment");
                 transaction.add(R.id.flFolderSongs, fragment);
                 transaction.addToBackStack("Folder");
//                transaction.remove(FolderFragment.this);
//                getActivity().getSupportFragmentManager().popBackStack();
                transaction.commitAllowingStateLoss();
               // folderList.clear();

                Bundle args = new Bundle();
                args.putString("FolderName", folderName);
                fragment.setArguments(args);

            }
        });
        rvFolderSongsList.setAdapter(adapter);

       // getAllFolder();
    }

    public void getAllStuff(File file, String name) {
        File list[] = file.listFiles();
        boolean folderFound = false;
        File mFile = null;
        String directoryName = "";
        if(list != null) {
            for (int i = 0; i < list.length; i++) {
                mFile = new File(file, list[i].getName());
                if (mFile.isDirectory()) {
                    directoryName = list[i].getName();
                    getAllStuff(mFile, directoryName);
                } else {
                    if (list[i].getName().toLowerCase(Locale.getDefault()).endsWith(".mp3") || list[i].getName().toLowerCase(Locale.getDefault()).endsWith(".mp4") || list[i].getName().toLowerCase(Locale.getDefault()).endsWith(".m4a")
                            || list[i].getName().toLowerCase(Locale.getDefault()).endsWith(".rtx") || list[i].getName().toLowerCase(Locale.getDefault()).endsWith(".wav") || list[i].getName().toLowerCase(Locale.getDefault()).endsWith(".mkv")) {
                        myList.add(list[i].getName());
                        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        String[] projection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
                                MediaStore.Audio.Media.DURATION, MediaStore.Audio.AudioColumns.DATA};


                        Cursor c = activity.getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%" + name + "%"}, null);
                        if (c.getCount() > 0) {
                            folderFound = true;
                        }
                    }
                }
            }
        }
        if (folderFound) {
            if(!name.contains(".")) {
                adapter.addFolders(name);
            }
                Log.e("Folder Name", name);
            a++;

            if(folderList.size() > 0)
            {
                ivNoData.setVisibility(View.GONE);
            }
            else
            {
                ivNoData.setVisibility(View.VISIBLE);
            }
        }

    }

    public void getAllFolder() {

        String root_sd = Environment.getExternalStorageDirectory().toString();
        File file = new File(root_sd);
        //list content of root sd
        getAllStuff(file, " ");
    }

    /*public void setMinimizePlayer()
    {
        Fragment bottomFragment = new MinimizedPlayerFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //transaction.replace(R.id.flMinimizedPlayer, bottomFragment);
        transaction.add(R.id.flMinimizedPlayer, bottomFragment);

        transaction.commit();
    }*/
}
