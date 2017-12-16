package kr.ac.koreatech.swkang.ch14dbcontactpractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ContactAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // layout 지정 - contact_add.xml 파일을 이용
        setContentView(R.layout.contact_add);
    }
}
