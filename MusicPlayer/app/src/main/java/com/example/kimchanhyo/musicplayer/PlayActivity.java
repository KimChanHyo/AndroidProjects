package com.example.kimchanhyo.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kim Chan Hyo on 2017-11-23.
 */

public class PlayActivity extends AppCompatActivity {
    static final private String TAG = "kchDebug : PlayActivity";

    static String musicDir = MainActivity.sMusicDir;
    static ArrayList<String> fileNames = MainActivity.m_arList;
    static int pos = -1;
    static boolean isPlaying = true;

    TextView playTime;
    ImageButton playBtn;

    DispCurPosThread dispTime;

    MusicService musicService;
    boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "bind?");
            MusicService.musicBinder binder = (MusicService.musicBinder)service;
            musicService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        if(pos != MainActivity.pos)
            stopService(new Intent(this, MusicService.class));

        pos = MainActivity.pos;

        playTime = findViewById(R.id.playTime);
        playBtn = findViewById(R.id.playMusic);

        setAndDispMusicName();

        isPlaying = true;
        Intent serviceIntent = new Intent(this, MusicService.class);
        serviceIntent.putExtra("fullPath", getFullPath());
        serviceIntent.putExtra("fileName", fileNames.get(pos));
        startService(serviceIntent);

        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(dispTime == null) {
            dispTime = new DispCurPosThread();
            dispTime.start();
        }

        setPlayBtnImage();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onBtnClicked(View view) {
        int viewId = view.getId();
        if(viewId == R.id.backToList) {
            stopService(new Intent(this, MusicService.class));
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else if(viewId == R.id.playMusic) {
            musicService.playOrPauseMusic(getFullPath());
            isPlaying = musicService.isPlaying();
            setPlayBtnImage();
        }
        else {  // prevMusic && nextMusic
            if(viewId == R.id.prevMusic)    MainActivity.pos = pos = (pos - 1 + fileNames.size()) % fileNames.size();
            else    MainActivity.pos = pos = (pos + 1) % fileNames.size();
            musicService.prevOrNextMusic(getFullPath());
            musicService.updateNofi(fileNames.get(pos));
            isPlaying = true;
            setAndDispMusicName();
            setPlayBtnImage();
        }
    }

    public String getFullPath() {
        return musicDir + "/" + fileNames.get(pos);
    }

    public void setPlayBtnImage() {
        if(isPlaying)   playBtn.setImageResource(android.R.drawable.ic_media_pause);
        else            playBtn.setImageResource(android.R.drawable.ic_media_play);
    }
    public void setAndDispMusicName() {
        setTitle(fileNames.get(pos));
    }

    class DispCurPosThread extends Thread {
        public void run() {
            while(true) {
                playTime.post(new Runnable() {
                    @Override
                    public void run() {
                        playTime.setText(toMinSec(musicService.curPos())
                                + "    " + toMinSec(musicService.duration()));
                    }
                });
                try { Thread.sleep(1000); }
                catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }
    public String toMinSec(int milli) {
        int min, sec;
        min = milli / (1000 * 60);
        milli %= (1000 * 60);
        sec = milli / 1000;
        return min + ":" + sec;
    }
}
