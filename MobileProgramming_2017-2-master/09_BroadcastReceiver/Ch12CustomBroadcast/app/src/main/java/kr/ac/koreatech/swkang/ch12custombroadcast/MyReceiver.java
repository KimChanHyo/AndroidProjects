package kr.ac.koreatech.swkang.ch12custombroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;


public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "브로드캐스트 수신", Toast.LENGTH_SHORT).show();
    }
}
