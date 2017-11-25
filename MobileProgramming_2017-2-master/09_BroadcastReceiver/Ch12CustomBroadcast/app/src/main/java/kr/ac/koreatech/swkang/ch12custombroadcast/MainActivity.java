package kr.ac.koreatech.swkang.ch12custombroadcast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Button이 클릭됐을 때 호출되는 콜백 메소드
    public void broadcastIntent(View view) {
        // Broadcast 전송을 위한 Intent 객체 생성
        Intent intent = new Intent();

        // Intent action 설정
        intent.setAction("myCUSTOM_INTENT");

        // Broadcast 전송
        sendBroadcast(intent);
    }
}
