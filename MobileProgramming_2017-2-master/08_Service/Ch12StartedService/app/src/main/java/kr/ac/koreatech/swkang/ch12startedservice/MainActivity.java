package kr.ac.koreatech.swkang.ch12startedservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ActivityForMusicService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // button이 눌렸을 때 호출되는 onClick 메소드 구현
    public void onClick(View view) {
        // view의 id가 무엇인지에 따라 MusicService 시작/중지
        switch(view.getId()) {
            case R.id.start :
                Log.d(TAG, "onClick() start");
                // startService 호출
                // 실행할 Service 클래스에 대한 Intent 객체를 생성하여 startService 호출 시 넘겨준다
                startService(new Intent(this, MusicService.class));

                break;
            case R.id.stop :
                Log.d(TAG, "onClick() stop");
                // stopService 호출
                // 중지할 Service 클래스에 대한 Intent 객체를 생성하여 stopService 호출 시 넘겨준다
                stopService(new Intent(this, MusicService.class));

                break;
        }
    }
}
