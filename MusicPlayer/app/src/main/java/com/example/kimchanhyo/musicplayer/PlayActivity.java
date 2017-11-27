package com.example.kimchanhyo.musicplayer;

import android.content.*;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

/**
 * Created by Kim Chan Hyo on 2017-11-23.
 */

public class PlayActivity extends AppCompatActivity {
    static final private String TAG = "kchDebug : PlayActivity";

    // get : musicDir, fileNames
    static String musicDir = MainActivity.sMusicDir;
    static ArrayList<String> fileNames = MainActivity.m_arList;
    // pos == 현재(이전까지) 재생중인 노래 위치
    static int pos = -1;
    static boolean isPlaying = true;

    // 현재 재생시간, 음악 길이를 나타내는 textview
    TextView playTime;
    // play or pause button
    ImageButton playBtn;

    // display time thread
    // does not act in service state
    playTimeThread dispTime;

    // binding service를 위한 MusicService
    MusicService musicService;
    boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // MainActivity에서 새로운 음악을 시작한 경우에 해당됨
        // 현재(이전까지) 재생중인 음악과 선택한 음악이 다른 경우
        // service를 종료한 뒤 재시작한다
        if(pos != MainActivity.pos)
            stopService(new Intent(this, MusicService.class));

        // pos값 설정
        pos = MainActivity.pos;

        playTime = findViewById(R.id.playTime);
        playBtn = findViewById(R.id.playMusic);

        // musicName을 설정하고 title에 display한다
        setAndDispMusicName();

        // 파일의 fullPath와 현재 pos를 putExtra하고 service를 시작한다.
        // 만약 재생중인 상태에서 같은 노래를 다시 시작하려는 경우에는 음악이 새롭게 시작되면 안되는데
        // 이 부분은 MusicService에서 예외처리를 해주었다.
        Intent serviceIntent = new Intent(this, MusicService.class);
        serviceIntent.putExtra("fullPath", getFullPath());
        serviceIntent.putExtra("fileName", fileNames.get(pos));
        startService(serviceIntent);
        isPlaying = true;

        // service bind
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onStart() {
        super.onStart();
        // 시간 표시 thread start
        if(dispTime == null) {
            dispTime = new playTimeThread();
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
        // 처음에는 onDestroy에서 startActivity를 호출하여 PlayActivity가 종료됬을 때
        // MainActivity가 실행되도록 하였는데, 이 경우 taskManager를 통해 어플을 종료할 경우에도
        // MainActivity가 실행되어버린다. back button을 눌러서 종료하였을 때만 MainActivity를 실행하도록 하는 코드이다.
        // service를 종료할 때에도 PlayActivity가 종료되는데 이 때 MainActivity를 실행하는건 service를 종료하려는 부분에서 따로 처리해준다.
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }


    // prevMusic, nextMusic, startMusic button Listener
    // prev, next Music button 의 처리기가 중복되는 코드가 많아 if-else문으로 구현하였다.
    public void onBtnClicked(View view) {
        int viewId = view.getId();
        // backToList(종료버튼)이 눌린 경우 stopService를 호출하고
        // MainActivity(singleTask)를 실행한다
        // finish()를 이용해 PlayAcitvity를 종료한다.
        if(viewId == R.id.backToList) {
            stopService(new Intent(this, MusicService.class));
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        // use bound service
        // fullPath를 전달하여 알맞은 음악을 play or pause
        // isPlaying() 메소드를 이용하여 isPlaying 변수값을 갱신해주고, 알맞은 button image로 설정해준다.
        else if(viewId == R.id.playMusic) {
            musicService.playOrPauseMusic(getFullPath());
            isPlaying = musicService.isPlaying();
            setPlayBtnImage();
        }
        // prevMusic && nextMusic, use bound service
        // prev인 경우 pos - 1 을 해주고, next인 경우 pos + 1 을 해준다.
        // fullPath를 전달하여 알맞은 음악을 재생한다.
        // notification bar에 음악 이름을 새로운 음악으로 변경해준다.
        // PlayActivity의 title을 새로운 음악 이름으로 변경하고, 재생(or일시정지) 버튼을 알맞게 설정해준다.
        else {
            if(viewId == R.id.prevMusic)    MainActivity.pos = pos = (pos - 1 + fileNames.size()) % fileNames.size();
            else    MainActivity.pos = pos = (pos + 1) % fileNames.size();
            musicService.prevOrNextMusic(getFullPath());
            musicService.updateNofi(fileNames.get(pos));
            isPlaying = true;
            setAndDispMusicName();
            setPlayBtnImage();
        }
    }

    // musicDir, fileNames, pos를 이용하여 fullPath를 구하고 반환
    public String getFullPath() { return musicDir + "/" + fileNames.get(pos); }

    // isPlaying 값을 참조하여 버튼을 알맞은 이미지로 설정.
    public void setPlayBtnImage() {
        if(isPlaying)   playBtn.setImageResource(android.R.drawable.ic_media_pause);
        else            playBtn.setImageResource(android.R.drawable.ic_media_play);
    }
    public void setAndDispMusicName() {
        setTitle(fileNames.get(pos));
    }


    // 1초마다 현재 재생중인 시간과 전체 음악 시간을 표시해준다.
    // PlayActivity가 살아있을 때만 작동한다.(구현하다가 포기했습니다.)
    class playTimeThread extends Thread {
        public void run() {
            while(true) {
                playTime.post(new Runnable() {
                    @Override
                    public void run() {
                        String cur = toMinSec(musicService.curPos());
                        String duration = toMinSec(musicService.duration());
                        playTime.setText(cur + "    " + duration);
                        if(cur.equals(duration))
                            findViewById(R.id.nextMusic).performClick();
                    }
                });
                try { Thread.sleep(1000); }
                catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }
    // 현재 시간(millisecond)을 분, 초(String)로 변경
    public String toMinSec(int milli) {
        int min, sec;
        min = milli / (1000 * 60);
        milli %= (1000 * 60);
        sec = milli / 1000;
        return min + ":" + sec;
    }
}
