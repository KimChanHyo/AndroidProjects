package com.example.kimchanhyo.bookmark;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

/**
 * Created by Kim Chan Hyo on 2017-11-10.
 */

public class AddActivity extends AppCompatActivity{
    private EditText editName = null;
    private EditText editURL = null;
    private bookmarkNameManager mNameMgr = new bookmarkNameManager(this);

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        editName = (EditText) findViewById(R.id.editName);
        editURL = (EditText) findViewById(R.id.editURL);
    }

    // 북마크 추가 버튼이 클릭된 경우 북마크를 추가하는 기능
    public void onClick(View view) {
        // EditText에 있는 text를 문자열에 저장
        String strName = editName.getText().toString();
        String strURL = "https://" + editURL.getText().toString();

        // 둘 중 하나라도 비었다면 메소드 종료
        if(strName.isEmpty() || strURL.isEmpty()) return;

        // 북마크 이름 저장
        mNameMgr.save(strName);
        // 북마크 url 저장
        URL_Manager uMgr = new URL_Manager(strName, this);
        uMgr.save(strURL);
        finish();
    }
}
