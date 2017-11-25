package kr.ac.koreatech.swkang.ch12batterybr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    BatteryBroadcastReceiver mReceiver;
    TextView mBatteryLevelText;
    ProgressBar mBatteryLevelProgress;

    // BroadcastReceiver 클래스 정의
    // onReceive 콜백 메소드 구현
    private class BatteryBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            // 수신한 Broadcast intent의 action이 ACTION_BATTERY_CHANGED인 경우 아래 코드 실행

            // 매개변수로 넘어온 Intent 객체를 이용해서 battery level 정보를 얻는다
            // getIntExtra() 메소드 이용, key는 BatteryManager.EXTRA_LEVEL, default 값은
            // int level =

            // 해당 level 값으로 TextView와 PrograssBar를 업데이트 한다
            // mBatteryLevelText.setText("Battery level: " + level);
            // mBatteryLevelProgress.setProgress(level);

            // battery level 값에 따라 메시지를 Toast 메시지로 만들어 표시한다

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBatteryLevelText = (TextView) findViewById(R.id.textview);
        mBatteryLevelProgress = (ProgressBar) findViewById(R.id.progressBar);

        // 위에서 정의한 BroadcastReceiver 클래스의 객체를 생성한다
        // mReceiver
    }

    // onStart()에서 BroadcastReceiver를 등록한다
    @Override
    protected void onStart() {
        super.onStart();

        // Intent filter 객체 생성

        // Intent action 설정 : Intent.ACTION_BATTERY_CHANGED

        // BroadcastReceiver 등록한다


    }

    // onStop()에서 BroadcastReceiver를 해제한다
    @Override
    protected void onStop() {
        super.onStop();

        // 해제한다

    }


}
