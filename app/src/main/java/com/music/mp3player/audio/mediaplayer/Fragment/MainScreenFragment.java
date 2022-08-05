package com.music.mp3player.audio.mediaplayer.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.nativead.NativeAd;
import com.music.mp3player.audio.mediaplayer.Adapter.CreatedPlaylistAdapter;
import com.music.mp3player.audio.mediaplayer.Adapter.MainScreenPlaylistAdapter;
import com.music.mp3player.audio.mediaplayer.AdmobAdManager;
import com.music.mp3player.audio.mediaplayer.DataBase.DbHelper;
import com.music.mp3player.audio.mediaplayer.GetAllSongs;
import com.music.mp3player.audio.mediaplayer.Interface.AdEventListener;
import com.music.mp3player.audio.mediaplayer.R;

import java.util.ArrayList;
import java.util.List;

import static com.music.mp3player.audio.mediaplayer.GetAllSongs.favouritesList;
import static com.music.mp3player.audio.mediaplayer.GetAllSongs.recentList;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.CHILD_FRAGMENT;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.tvToolbarText;

public class MainScreenFragment extends Fragment implements View.OnClickListener, AdEventListener {

   // private ImageView  ivRecent, ivFavourite;
    private TextView tvRecentCount, tvFavouriteCount, tvPlaylist;
    private RecyclerView rvPlaylist;
    private MainScreenPlaylistAdapter createdPlaylistAdapter;
    private List<String> userPlaylist;
    GetAllSongs allSongs;
    DbHelper DB;

    private TextView ivNoData;
    private RelativeLayout rlRecent, rlFavourites;
    private FrameLayout flMinimizedPlayer;
    private View view;
    private FrameLayout flSmallNativeAd;
    private AdmobAdManager admobAdManager;

    public MainScreenFragment() {
        tvToolbarText.setText("Playlist");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.main_fragment_layout, container, false);

        DB = new DbHelper(getContext());

       // ivDrawer = view.findViewById(R.id.ivDrawer);
        tvRecentCount = view.findViewById(R.id.tvRecentSongsCount);
        tvFavouriteCount = view.findViewById(R.id.tvFavouritesCount);
        rvPlaylist = view.findViewById(R.id.rvPlaylist);
        tvPlaylist = view.findViewById(R.id.tvPlaylist);
        ivNoData = view.findViewById(R.id.ivNoData);

        rlRecent = view.findViewById(R.id.rlRecentSongs);
        rlFavourites = view.findViewById(R.id.rlFavourites);
        flMinimizedPlayer = view.findViewById(R.id.flMinimizedPlayer);
        flSmallNativeAd = view.findViewById(R.id.flSmallNativeAd);
        //drawerLayout = findViewById(R.id.drawerLayout);



        CHILD_FRAGMENT = false;

       /* tvPlayingSongArtist = findViewById(R.id.tvPlayingSongArtist);
        tvPlayingSongName = findViewById(R.id.tvPlayingSongName);
        ivPlayingSongImage = findViewById(R.id.ivPlayingSongImage);*/

        ivNoData.setVisibility(View.GONE);
        setPlaylistAdapter();

        rlFavourites.setOnClickListener(this);
        rlRecent.setOnClickListener(this);


        admobAdManager = AdmobAdManager.getInstance(getContext());
        admobAdManager.LoadNativeAd(getContext(), getContext().getResources().getString(R.string.native_ad_id), this);

        return view;
    }

    private void setPlaylistAdapter() {

        allSongs = new GetAllSongs(getContext());
        allSongs.getAudioFiles();
        allSongs.getRecentList();
        allSongs.getFavouritesSongs();

        userPlaylist = new ArrayList<>();
        userPlaylist.clear();

        if(recentList != null) {
            tvRecentCount.setText("(" + recentList.size() + ")");
        }
        else
        {
            tvRecentCount.setText("(0)");
        }

        if(favouritesList != null)
        {
            tvFavouriteCount.setText("(" +favouritesList.size()+ ")");
        }
        else
        {
            tvFavouriteCount.setText("(0)");
        }

        Cursor c = DB.getAllPlaylist();
        if (c.getCount() > 0)
        {
            while (c.moveToNext())
            {
                userPlaylist.add(c.getString(c.getColumnIndex("name")));
            }
        }

        if (userPlaylist.size() == 0)
        {
            ivNoData.setVisibility(View.VISIBLE);
            ivNoData.setText("No Playlist Found");
        }
        else
        {
            ivNoData.setVisibility(View.GONE);
        }

        tvPlaylist.setText("Playlist (" + c.getCount() + ")");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager = linearLayoutManager;
        rvPlaylist.setLayoutManager(layoutManager);

        createdPlaylistAdapter = new MainScreenPlaylistAdapter(getContext(), userPlaylist, new CreatedPlaylistAdapter.addToPlaylistInterface() {
            @Override
            public void addToPlaylist(int pos, String playlistName) {
                Fragment fragment = new PlaylistSongsFragments();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.flMinimizedPlayer, fragment);
                transaction.addToBackStack("Playlist");
                transaction.commitAllowingStateLoss();

                Bundle bundle = new Bundle();
                bundle.putString("PlaylistName", playlistName);
                fragment.setArguments(bundle);
            }
        });
        rvPlaylist.setAdapter(createdPlaylistAdapter);

    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId())
        {

            case R.id.rlRecentSongs :
                fragment = new RecentFragment();
                replaceFragment(fragment);
                break;

            case R.id.rlFavourites :
                fragment = new FavouritesFragment();
                replaceFragment(fragment);

                break;

        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.flMinimizedPlayer, fragment);
        transaction.addToBackStack("Playlist");
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onResume() {
        super.onResume();

        setPlaylistAdapter();
    }

    @Override
    public void onAdLoaded(Object object) {
        flSmallNativeAd.setVisibility(View.VISIBLE);
        admobAdManager.populateUnifiedNativeAdView(getContext(), flSmallNativeAd, (NativeAd) object,false,false);
    }

    @Override
    public void onAdClosed() {

    }

    @Override
    public void onLoadError(String errorCode) {

    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();

        FIRST_OPEN = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        setPlaylistAdapter();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // FIRST_OPEN = false;
    }*/

    /*public void startService() {
        *//*Intent serviceIntent = new Intent(getContext(, NotificationService.class);
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(serviceIntent);*//*


        notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(getContext());

        remoteViews = new RemoteViews(getPackageName(),R.layout.status_bar_expanded);
        remoteViews.setImageViewResource(R.id.status_bar_album_art,R.mipmap.ic_launcher);
        remoteViews.setTextViewText(R.id.status_bar_track_name,"TEXT");
        remoteViews.setProgressBar(R.id.status_bar_artist_name,100,40,true);

        notification_id = (int) System.currentTimeMillis();

        Intent button_intent = new Intent("button_click");
        button_intent.putExtra("id",notification_id);
        PendingIntent button_pending_event = PendingIntent.getBroadcast(getContext(),notification_id,
                button_intent,0);

        remoteViews.setOnClickPendingIntent(R.id.status_bar_collapse,button_pending_event);

        Intent notification_intent = new Intent(getContext(),MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(),0,notification_intent,0);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setCustomBigContentView(remoteViews)
                .setContentIntent(pendingIntent);

        notificationManager.notify(notification_id,builder.build());

    }

    public void getNotification()
    {

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        remoteViews = new RemoteViews(getPackageName(),R.layout.status_bar_expanded);
        remoteViews.setImageViewResource(R.id.status_bar_album_art,R.mipmap.ic_launcher);
        remoteViews.setTextViewText(R.id.status_bar_track_name,"TEXT");
        //remoteViews.setProgressBar(R.id.status_bar_artist_name,100,40,true);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getContext(),CHANNEL_ID);
        notification.setSmallIcon(R.drawable.play);
        notification.setContentTitle("Music Player");
        notification.setContentText("Music is currently playing");
        notification.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notification.setAutoCancel(true);
        notification.setOnlyAlertOnce(true);

        Intent intent = new Intent(getContext(),MainActivity.class);
        PendingIntent pending = PendingIntent.getActivity(getContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pending);

        NotificationManagerCompat mnc = NotificationManagerCompat.from(getContext());
        mnc.notify(1,notification.build());
    }*/

    /*public void setMinimizePlayer()
    {
        Fragment bottomFragment = new MinimizedPlayerFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //transaction.replace(R.id.flMinimizedPlayer, bottomFragment);
        transaction.add(R.id.flMinimizedPlayer, bottomFragment);

        transaction.commit();
    }*/
}
