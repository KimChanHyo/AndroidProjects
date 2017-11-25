package com.example.kimchanhyo.bookmark;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ActionMode.Callback {

    ActionMode mActionMode;

    private ArrayList<String> m_arList;     // 저장된 북마크의 이름
    private ArrayAdapter<String> m_adapter; // (m_arList - ListView) 데이터를 리스트 뷰에 제공하는 어댑터
    private ListView m_list;                // 저장된 북마크의 이름을 보여주는 리스트

    // 북마크의 이름을 관리하는 클래스(file input, output)
    private bookmarkNameManager mNameMgr = new bookmarkNameManager(this);
    // 지우려는 북마크의 리스트 뷰 position
    private int delPos;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 북마크의 이름을 저장하는 파일 생성
        // (아무런 동작도 하지 않고, 단순히 파일을 (MODE_APPEND)생성해준다.)
        // (파일이 없을 때, load하는 상황 방지)
        mNameMgr.create();

        // 저장된 북마크의 이름을 load
        m_arList = mNameMgr.load();

        // m_adapter, m_list 상태 업데이트
        updateState();

        // list(ListView)에 itemClickListener 등록
        // 해당하는 북마크(주소) 사이트를 엶.
        m_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // 해당 북마크 이름을 fileName에 저장
               String fileName = m_adapter.getItem(position);

               // 북마크에 해당하는 url을 (fileName).txt 파일에서 불러옴
               URL_Manager uMgr = new URL_Manager(fileName, getApplicationContext());
               String url = uMgr.load();
               // url 페이지 접속
               Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
               startActivity(in);
               return;
           }
        });

        // list(ListView)에 itemLongClickListener 등록
        // 해당하는 북마크를 list에서 지움.
        m_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 컨텍스트 액션 모드 활성화
                mActionMode = startSupportActionMode(MainActivity.this);
                // 지우려는 북마크의 위치 저장
                delPos = position;
                return true;
            }
        }
        );
    }

    // 북마크 추가가 완료된 경우 list(ListView) 업데이트
    @Override
    protected void onStart() {
        super.onStart();
        m_arList = mNameMgr.load();
        updateState();
    }

    // 북마크 추가 메뉴 등록
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // 북마크 추가 버튼을 누른 경우 AddActivity 호출
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add:
                Intent in = new Intent(this, AddActivity.class);
                startActivity(in);
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }


    // Context Action Mode 사용을 위한 @Override start
    // ActionMode 등록(delete bookmark)
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.del, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }

    // 삭제 contextActionMode 기능 구현
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch(item.getItemId()) {
            case R.id.del :
                // 해당 북마크의 url을 가지고 있는 파일 삭제
                this.deleteFile(m_arList.get(delPos) + ".txt");
                // m_arList에서 해당 북마크의 이름 삭제
                m_arList.remove(delPos);
                // 변경된 북마크의 이름 리스트를 파일에 새로 저장
                mNameMgr.save(m_arList);

                // m_adapter, m_list 상태 업데이트
                updateState();
                // 액션모드 종료
                mode.finish();
                return true;
            default :
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) { mActionMode = null; }
    // Context Action Mode 사용을 위한 @Override end

    // m_arList가 변경된 경우 m_adapter, m_list 상태 업데이트
    public void updateState() {
        m_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, m_arList);
        m_list = (ListView) findViewById(R.id.list);
        m_list.setAdapter(m_adapter);
    }
}
