// before music service add

package com.example.kimchanhyo.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    File musicDir;
    File files[] = {};

    private String m_string[];
    private ArrayList<String> m_arList;
    private ArrayAdapter<String> m_arAdapter;
    private ListView m_musicList;

    static final String TAG = "LogMessage";
    public final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_arList = new ArrayList<String>();
        m_musicList = findViewById(R.id.musicList);

        // Runtime permission check
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else {
            // READ_EXTERNAL_STORAGE 권한이 있는 것이므로
            // Public Directory에 접근할 수 있고 거기에 있는 파일을 읽을 수 있다
            prepareAccess();
        }

        m_musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(getApplicationContext(), PlayActivity.class);
                in.putExtra("musicDir", musicDir.getAbsolutePath());
                in.putExtra("fileNames", m_string);
                in.putExtra("pos", position);
                startActivity(in);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // READ_EXTERNAL_STORAGE 권한을 얻었으므로
                    // 관련 작업을 수행할 수 있다
                    prepareAccess();
                } else {
                    // 권한을 얻지 못 하였으므로 파일 읽기를 할 수 없다
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    // preapare access to music directory
    // set m_arList, m_arAdapter, m_musicList
    public void prepareAccess() {
        musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        // File 클래스에 정의된 메소드를 이용하면 디렉토리 여부, 이름, 절대경로를 알아낼 수 있다
        // isDirectory() 메소드: File 객체가 디렉토리이면 true
        // getName(): File 객체가 나타내는 디렉토리 혹은 파일의 이름 반환
        // getAbsolutePath(): File 객체가 나타내는 디렉토리 혹은 파일의 절대 경로 반환
        // listFiles(): 디렉토리에 있는 파일(디렉토리 포함)들을 나타내는 File 객체들의 배열을 반환한다

        int num = 0;
        try {
            files = musicDir.listFiles();
            if(files == null)
                Log.i(TAG, "this is not be a directory or IOError was occurred");
            else {
                num = files.length;
                if(num == 0)
                    Log.i(TAG, "there is no files");

                m_string = new String[num];
                for(int i = 0 ; i < num ; ++i) {
                    m_arList.add(files[i].getName());
                    m_string[i] = files[i].getName();
                    Log.i(TAG, "file " + (i + 1) + m_string[i]);
                }
                m_arAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, m_arList);
                m_musicList.setAdapter(m_arAdapter);
            }
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }
}
