package com.example.kimchanhyo.musicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
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
        startForeground(fgId, buildNoti(intent.getStringExtra("fileName")));

        startMusic(intent.getStringExtra("fullPath"));
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMusic();
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
    public void updateNofi(String fileName) {
        ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(fgId, buildNoti(fileName));
    }
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }



    private void startMusic(String fullPath) {
        if(mediaPlayer.isPlaying() || isPause) return;
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

    private Notification buildNoti(String fileName) {
        Intent intent = new Intent(this, PlayActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return new Notification.Builder(this)
                .setContentTitle("Music Player")
                .setContentText("Now Playing : " + fileName)
                .setSmallIcon(R.drawable.launcher_image)
                .setContentIntent(pIntent)
                .build();
    }
}
