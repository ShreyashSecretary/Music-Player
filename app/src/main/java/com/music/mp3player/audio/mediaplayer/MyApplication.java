package com.music.mp3player.audio.mediaplayer;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

//import com.google.android.gms.ads.MobileAds;
//import com.google.android.gms.ads.initialization.InitializationStatus;
//import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class MyApplication extends Application {

    public static final String CHANNEL_ID1 = "channel1";
    public static final String CHANNEL_ID2 = "channel2";
    public static final String ACTION_PREVIOUS = "actionPrevious";
    public static final String ACTION_NEXT = "actionNext";
    public static final String ACTION_PLAY = "actionPlay";
    public static boolean SHOW_NOTIFICATION = false;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

        /*MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });/*/

        /*OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(MyApplication.this);
        OneSignal.setAppId(getResources().getString(R.string.onesignal));

        YandexMetricaConfig config =YandexMetricaConfig.newConfigBuilder(getResources().getString(R.string.appmatrica)).build();
        //Initializing AppMetricaSDK
        YandexMetrica.activate(getApplicationContext(), config);
        //Automatic tracking of your activity
        YandexMetrica.enableActivityAutoTracking(this);*/

    }

    private void createNotificationChannel() {

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
        {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID1, "Channel(1)", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Channel 1 desc....");
            channel1.setShowBadge(true);
            channel1.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_ID2, "Channel(2)", NotificationManager.IMPORTANCE_LOW);
            channel2.setDescription("Channel 2 desc...");
            channel2.setShowBadge(true);
            channel2.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
           // notificationManager.createNotificationChannel(channel1);
            notificationManager.createNotificationChannel(channel2);
        }
    }


}
