package com.example.kimchanhyo.musicplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Kim Chan Hyo on 2017-11-25.
 */

public class MusicService extends Service {
    static final String TAG = "kchDebug : MusicService";
    static final int fgId = 113;

    MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int started) {
        Intent notiIn = new Intent(this, PlayActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, notiIn, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification noti = new Notification.Builder(this)
                .setContentTitle("Music Service")
                .setContentText("Music is playing. Click to start an activity")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .build();
        startForeground(fgId, noti);

        playMusic(intent.getStringExtra("fullPath"));
        return START_STICKY;
    }

    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void playMusic(String fullPath) {
        if(mediaPlayer.isPlaying()) return;

        try {
            mediaPlayer.setDataSource(fullPath);
            mediaPlayer.prepare();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }
}
