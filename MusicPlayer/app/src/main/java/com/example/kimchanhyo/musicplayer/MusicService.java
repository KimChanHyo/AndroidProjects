package com.example.kimchanhyo.musicplayer;

import android.app.*;
import android.content.*;
import android.media.MediaPlayer;
import android.os.*;

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
        // foreground 시작, notification bar 설정
        startForeground(fgId, buildNoti(intent.getStringExtra("fileName")));

        // fullPath를 이용하여 music start
        startMusic(intent.getStringExtra("fullPath"));
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMusic();
    }



    // playButton을 눌렀을 때 실행되는 메소드
    // 재생중이였으면 일시정지, 아니였으면 재생한다.
    public void playOrPauseMusic(String fullPath) {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            PlayActivity.isPlaying = false;
            isPause = true;
        }
        else {
            mediaPlayer.start();
            PlayActivity.isPlaying = true;
            isPause = false;
        }
    }
    // prev or next button을 눌렀을 때 실행되는 메소드
    // 기존 음악을 멈추고, 새로운 음악(fullPath)을 시작한다.
    public void prevOrNextMusic(String fullPath) {
        stopMusic();
        newMusic(fullPath);
    }
    // notification bar의 정보를 update해야되는 경우 실행됨
    // fileName(음악이름)을 이용하여 notification을 새로 등록합니다.
    // goo.gl/5wDbaj (stackOverFlow) 참고하여 작성하였습니다.
    public void updateNofi(String fileName) {
        ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(fgId, buildNoti(fileName));
    }
    // timeDispThread에서 쓰이는 메소드
    public int curPos() {
        if(mediaPlayer == null) return 0;
        else return mediaPlayer.getCurrentPosition();
    }
    public int duration() {
        if(mediaPlayer == null) return 0;
        else return mediaPlayer.getDuration();
    }



    // startService 호출시 실행되는 메소드
    // 이미 재생중이거나, 일시정지인 경우는 아무 동작도 하지않고
    // 아닌 경우(stopService를 호출하였어야한다) 새로운 음악을 재생한다.
    private void startMusic(String fullPath) {
        if(mediaPlayer.isPlaying() || isPause) return;
        newMusic(fullPath);
    }
    // 소멸자 역할을 한다.
    // 혹은 새로운 음악을 재생하기 전에 호출된다.
    private void stopMusic() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }
    // 새로운 음악을 실행하는 메소드
    private void newMusic(String fullPath) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(fullPath);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        PlayActivity.isPlaying = true;
        isPause = false;
    }

    // fileName을 음악 이름으로 하는 notification을 생성해줌.
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
