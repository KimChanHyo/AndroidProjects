package kr.ac.koreatech.swkang.ch14dbcontactpractice;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DBHelper helper;
    private SQLiteDatabase db;
    private Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SQLiteOpenHelper 클래스의 subclass인 DBHelper 클래스 객체 생성
        helper = new DBHelper(this);
        // DBHelper 객체를 이용하여 DB 생성
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException e) {
            db = helper.getReadableDatabase();
        }

        // contacts 테이블에서 모든 레코드를 retrieve
        c = db.rawQuery("SELECT * FROM contacts", null);

        String[] from = {"name", "tel"};
        int[] to = {android.R.id.text1, android.R.id.text2};
        // SimpleCursorAdapter 객체 생성
        // 하나의 리스트 아이템에 2개의 텍스트뷰를 표시하는 레이아웃
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, c, from, to);

        // 레이아웃에 정의된 리스트뷰에 대한 참조 객체 얻음
        ListView list = (ListView)findViewById(R.id.list);
        // 리스트뷰 객체에 어댑터 설정
        list.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        c.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.action_new:
                Log.v("ActionBar", "new button");
                Toast.makeText(getApplicationContext(), "new", Toast.LENGTH_SHORT).show();

                // ContactAddActivity 실행
                startActivity(new Intent(this, ContactAddActivity.class));

                return true;
            case R.id.action_search:
                Log.v("ActionBar", "search button");
                Toast.makeText(getApplicationContext(), "search", Toast.LENGTH_SHORT).show();

                // ContactSearchActivity 실행
                startActivity(new Intent(this, ContactSearchActivity.class));

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
