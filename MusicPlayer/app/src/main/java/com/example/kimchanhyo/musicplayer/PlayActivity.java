package com.example.kimchanhyo.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Kim Chan Hyo on 2017-11-23.
 */

public class PlayActivity extends AppCompatActivity {
    static final String TAG = "LogMessage";

    String filepath;
    String fileNames[];
    int pos;

    TextView nameTxtView;
    ImageButton playBtn;
    MediaPlayer mediaPlayer;

    MusicService musicService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Intent in = getIntent();
        filepath = in.getStringExtra("musicDir");
        fileNames = in.getStringArrayExtra("fileNames");
        pos = in.getIntExtra("pos", 0);

        nameTxtView = findViewById(R.id.musicName);
        playBtn = findViewById(R.id.playMusic);
        mediaPlayer = new MediaPlayer();
        try {
            String musicPath = filepath + "/" + fileNames[pos];
            nameTxtView.setText(fileNames[pos]);
            setTitle(fileNames[pos]);
            mediaPlayer.setDataSource(musicPath);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

    public void onBtnClicked(View view) {
        switch(view.getId()) {
            case R.id.backToList :
                finish();
                break;
            case R.id.prevMusic :
                break;
            case R.id.playMusic :
                if(mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                else {
                    try { mediaPlayer.prepare(); }
                    catch (Exception e) { e.printStackTrace(); }
                    mediaPlayer.start();
                }
                setPlayBtnImage();
                break;
            case R.id.nextMusic :
                break;
        }
    }

    public void setPlayBtnImage() {
        if(mediaPlayer.isPlaying())
            playBtn.setImageResource(android.R.drawable.ic_media_pause);
        else
            playBtn.setImageResource(android.R.drawable.ic_media_play);
    }

}
