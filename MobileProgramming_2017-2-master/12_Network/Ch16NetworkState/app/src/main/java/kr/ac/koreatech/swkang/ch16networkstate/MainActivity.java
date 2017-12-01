package kr.ac.koreatech.swkang.ch16networkstate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText display;
    TextView mobileState;
    TextView wifiState;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                // 브로드캐스트가 disconnect 이벤트에 의해 발생되고, 연결된 네트워크가 전혀 없는 경우
                // EXTRA_NO_CONNECTIVITY가 true로 설정됨
                if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false))
                    Toast.makeText(context, "Network: not connected", Toast.LENGTH_LONG).show();

                ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = manager.getActiveNetworkInfo();

                if(info != null && info.isConnected()) {
                    if(info.getType() == ConnectivityManager.TYPE_WIFI)
                        Toast.makeText(context, "WiFi Network: connected", Toast.LENGTH_LONG).show();
                    else if(info.getType() == ConnectivityManager.TYPE_MOBILE)
                        Toast.makeText(context, "Mobile Network: connected", Toast.LENGTH_LONG).show();
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = (EditText)findViewById(R.id.edit);
        mobileState = (TextView)findViewById(R.id.mobile_state);
        wifiState = (TextView)findViewById(R.id.wifi_state);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
    }

    public void onClick(View view) {
        try {
            // 네트워크 상태를 조회할 수 있는 ConnectivityManager 객체를 얻음
            ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

            // 현재 활성화된(active) 네트워크의 정보를 얻음
            NetworkInfo info = manager.getActiveNetworkInfo();

            // NetworkInfo 객체의 내용을 String으로 변환하여 EditText 객체인 display에 텍스트 내용으로 설정
            display.setText(info.toString());

            if(info != null) {
                if(info.getType() == ConnectivityManager.TYPE_WIFI) {
                    if(info.isAvailable() && info.isConnected()) {
                        wifiState.setText("WiFi state: connected");
                    } else if(info.isAvailable() && !info.isConnected()) {
                        wifiState.setText("WiFi state: available, but not connected");
                    }
                } else if(info.getType() == ConnectivityManager.TYPE_MOBILE) {
                    if(info.isAvailable() && info.isConnected()) {
                        mobileState.setText("Mobile state: connected");
                    } else if(info.isAvailable() && !info.isConnected()) {
                        mobileState.setText("Mobile state: available, but not connected");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Network online", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
    }
}
