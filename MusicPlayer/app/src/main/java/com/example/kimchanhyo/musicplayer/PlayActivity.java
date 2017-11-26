package com.example.kimchanhyo.musicplayer;

import android.content.ComponentName;
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
    static final String TAG = "kchDebug : PlayActivity";

    String musicDir;
    ArrayList<String> fileNames;
    static int pos = -1;
    boolean isPlaying;

    TextView nameTxtView;
    ImageButton playBtn;

    MusicService musicService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.musicBinder binder = (MusicService.musicBinder)service;
            musicService = binder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            ;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Log.d(TAG, String.valueOf(pos));
        Log.d(TAG, String.valueOf(MainActivity.pos));
        if(pos != MainActivity.pos)
            stopService(new Intent(this, MusicService.class));

        musicDir = MainActivity.sMusicDir;
        fileNames = MainActivity.m_arList;
        pos = MainActivity.pos;
        isPlaying = true;

        nameTxtView = findViewById(R.id.musicName);
        playBtn = findViewById(R.id.playMusic);

        setAndDispMusicName();

        Intent serviceIntent = new Intent(this, MusicService.class);
        serviceIntent.putExtra("fullPath", musicDir + "/" + fileNames.get(pos));
        startService(serviceIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onBtnClicked(View view) {
        switch(view.getId()) {
            case R.id.backToList :
                stopService(new Intent(this, MusicService.class));
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.prevMusic :
                break;
            case R.id.playMusic :
                if(isPlaying)
                    ;
                    // pause;
                    // mediaPlayer.pause();
                else {

//                    try { mediaPlayer.prepare(); }
//                    catch (Exception e) { e.printStackTrace(); }
//                    mediaPlayer.start();
                }
                setPlayBtnImage();
                break;
            case R.id.nextMusic :
                break;
        }
    }

    public void setPlayBtnImage() {
        if(isPlaying)   playBtn.setImageResource(android.R.drawable.ic_media_pause);
        else            playBtn.setImageResource(android.R.drawable.ic_media_play);
    }
    public void setAndDispMusicName() {
        nameTxtView.setText(fileNames.get(pos));
        setTitle(fileNames.get(pos));
    }
}
