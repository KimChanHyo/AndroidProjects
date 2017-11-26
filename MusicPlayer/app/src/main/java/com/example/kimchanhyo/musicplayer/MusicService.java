package com.example.kimchanhyo.musicplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Kim Chan Hyo on 2017-11-25.
 */

public class MusicService extends Service {
    static final private String TAG = "kchDebug : MusicService";
    static final int fgId = 113;

    private MediaPlayer mediaPlayer;
    private boolean isPause;

    public class musicBinder extends Binder {
        MusicService getService() { return MusicService.this; }
    }
    private final IBinder mBinder = new musicBinder();

    @Override
    public IBinder onBind(Intent intent) { return mBinder; }

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
                .setSmallIcon(R.drawable.launcher_image)
                .setContentIntent(pIntent)
                .build();
        startForeground(fgId, noti);

        startMusic(intent.getStringExtra("fullPath"));
        return START_NOT_STICKY;
    }

    public void onDestroy() {
        stopMusic();
    }

    private void startMusic(String fullPath) {
        if(mediaPlayer.isPlaying() || isPause) return;
        newMusic(fullPath);
    }

    public void playOrPauseMusic(String fullPath) {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
        }
        else {
            mediaPlayer.start();
            isPause = false;
        }
    }
    public void prevOrNextMusic(String fullPath) {

        stopMusic();
        newMusic(fullPath);
    }
    private void stopMusic() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }
    private void newMusic(String fullPath) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(fullPath);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        isPause = false;
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }
}
