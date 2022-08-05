package com.music.mp3player.audio.mediaplayer.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.music.mp3player.audio.mediaplayer.Interface.ActionPlaying;
import com.music.mp3player.audio.mediaplayer.Reciever.NotificationReceiver;
import com.music.mp3player.audio.mediaplayer.R;
import com.music.mp3player.audio.mediaplayer.activities.MainActivity;

import static com.music.mp3player.audio.mediaplayer.MyApplication.ACTION_NEXT;
import static com.music.mp3player.audio.mediaplayer.MyApplication.ACTION_PLAY;
import static com.music.mp3player.audio.mediaplayer.MyApplication.ACTION_PREVIOUS;
import static com.music.mp3player.audio.mediaplayer.MyApplication.CHANNEL_ID2;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.pos;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.shuffledList;
import static com.music.mp3player.audio.mediaplayer.activities.MainActivity.songsList;
import static com.music.mp3player.audio.mediaplayer.activities.PlayingSongActivity.IS_SHUFFLED;
//import static com.example.musicplayer.activities.PlayingSongActivity.shuffledList;
//import static com.example.musicplayer.activities.PlayingSongActivity.songsList;
import static com.music.mp3player.audio.mediaplayer.activities.SplashScreenActivity.FIRST_OPEN;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {

    IBinder mBinder = new MyBinder();
    public static MediaPlayer mediaPlayer;
    public static boolean NOTIFICATION_VISIBLE = false;
   // List<AudioModel> myList = new ArrayList<>();
    Uri uri;
    int position = -1;
    ActionPlaying actionPlaying;
    MediaSessionCompat mediaSessionCompat;

    @Override
    public void onCreate() {
        super.onCreate();

        mediaSessionCompat = new MediaSessionCompat(getBaseContext(), "My Audio");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.e("Bind", "Service");
        return mBinder;
    }

    public class MyBinder extends Binder
    {
        public MusicService getService()
        {
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int myPosition = intent.getIntExtra("ServicePosition", -1);
        String actionName = intent.getStringExtra("ActionName");
        if(myPosition != -1) {
            playMedia(myPosition);
        }

        if(actionName != null)
        {
            switch (actionName)
            {
                case "playPause":
                   // Toast.makeText(this, "PlayPause", Toast.LENGTH_LONG).show();
                    if(actionPlaying != null) {
                        Log.e("Msg", "PlayPause");
                        actionPlaying.playPauseButtonClick();
                    }
                    break;

                case "next":
                  //  Toast.makeText(this, "Next", Toast.LENGTH_LONG).show();
                    if(actionPlaying != null) {
                        Log.e("Msg", "PlayPause");
                        actionPlaying.nextButtonClick();
                    }
                    break;

                case "previous":
                   // Toast.makeText(this, "Previous", Toast.LENGTH_LONG).show();
                    if(actionPlaying != null) {
                        Log.e("Msg", "PlayPause");
                        actionPlaying.previousButtonClick();
                    }
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    private void playMedia(int startPosition) {

        position = startPosition;
        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            if(songsList != null)
            {
                createMediaPlayer(position);
                mediaPlayer.start();
            }
        }
        else
        {
            createMediaPlayer(position);
            if(!NOTIFICATION_VISIBLE)
            {

            }
            else
            {
                mediaPlayer.start();
            }

        }
    }


    public void start()
    {
        mediaPlayer.start();
        Log.e("Start", "FIRST " +FIRST_OPEN);
    }

    public boolean isPlaying()
    {
        return mediaPlayer.isPlaying();
    }

    public void stop()
    {
        mediaPlayer.stop();
    }

    public void release()
    {
        mediaPlayer.release();
    }

    public int getDuration()
    {
        return mediaPlayer.getDuration();
    }

    public void seekTo(int position)
    {
        mediaPlayer.seekTo(position);
    }

    public void createMediaPlayer(int positionInner)
    {
        position = positionInner;
        if(IS_SHUFFLED)
        {
            uri = Uri.parse(shuffledList.get(position).getPath());
        }
        else
        {
            uri = Uri.parse(songsList.get(position).getPath());
        }

        mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
        Log.e("Start", "FIRST " +positionInner);
    }

    public int getCurrentPosition()
    {
        return mediaPlayer.getCurrentPosition();
    }

    public void pause()
    {
        mediaPlayer.pause();
    }

    public void reset()
    {
        mediaPlayer.reset();
    }

    public void prepare()
    {
        mediaPlayer.prepareAsync();
    }

    public void onComplete()
    {
        mediaPlayer.setOnCompletionListener(this);
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if(actionPlaying != null)
        {
            actionPlaying.nextButtonClick();
            if(mediaPlayer != null)
            {
                createMediaPlayer(position);
                start();
               // onComplete();
            }
        }

        /*createMediaPlayer(position);
        start();*/
    }

    public void setCallBack(ActionPlaying actionPlaying)
    {
        this.actionPlaying = actionPlaying;
    }

    public void showNotification(int playPauseButton, String playPause)
    {
       // tvDuration.setText(formattedTime(Integer.parseInt(songsList.get(position).getDuration()) / 1000));
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent prevIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_PREVIOUS);
        PendingIntent prevPending = PendingIntent.getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_NEXT);
        PendingIntent nextPending = PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_PLAY);
        PendingIntent pausePending = PendingIntent.getBroadcast(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        byte[] picture = null;
        picture = getAlbumArt(songsList.get(pos).getPath());
        Bitmap thumb = null;

        if(picture != null)
        {
            thumb = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        }
        else
        {
            thumb = BitmapFactory.decodeResource(getResources(), R.drawable.music_photo);
        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID2)
                .setSmallIcon(R.drawable.play)
                .setLargeIcon(thumb)
                .setContentTitle(songsList.get(pos).getName())
                .setContentText(songsList.get(pos).getArtist())
                .addAction(R.drawable.ic_baseline_skip_previous_24, "previous", prevPending)
                .addAction(playPauseButton, playPause, pausePending)
                .addAction(R.drawable.ic_baseline_skip_next_24, "Next", nextPending)
                /*.setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSessionCompat.getSessionToken()))*/
                .setPriority(NotificationCompat.PRIORITY_LOW)
                //.setChannelId(CHANNEL_ID2)
                //.setOnlyAlertOnce(true)
                .setContentIntent(contentIntent)
                //.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build();
                NOTIFICATION_VISIBLE = true;

       /* NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);*/

        startForeground(2, notification);
        if(!isPlaying()) {
            stopForeground(false);
            NOTIFICATION_VISIBLE = false;
            //stopSelf();
        }
    }

    public byte[] getAlbumArt(String uri)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

}
