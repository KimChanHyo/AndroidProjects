package kr.ac.koreatech.swkang.ch10activitylifecyclepause;

import android.app.Activity;
import android.os.Bundle;

public class SmallActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.small_activity);
    }
}
