package kr.ac.koreatech.swkang.ch14publicdirectory;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "MobileProgramming";
    MediaPlayer mediaPlayer;
    File musicDir;

    public final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //*******************************************************************
        // Runtime permission check
        //*******************************************************************
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

            prepareAccessToMusicDirectory();
        }
        //*********************************************************************


    }

    // Music 디렉토리에 있는 파일들의 이름을 알아내고
    // 그 중 한 파일을을 MediaPlayer 객체의 data source로 지정하는 예제 함수
    public void prepareAccessToMusicDirectory() {

        // Public Directory 중에 Music 디렉토리에 대한 File 객체를 반환한다
        musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        // 즉 여기서 musicDir는 File 객체
        // File 객체는 디렉토리 혹은 파일 모두를 나타낼 수 있다

        // File 클래스에 정의된 메소드를 이용하면 디렉토리 여부, 이름, 절대경로를 알아낼 수 있다
        // isDirectory() 메소드: File 객체가 디렉토리이면 true
        // getName(): File 객체가 나타내는 디렉토리 혹은 파일의 이름 반환
        // getAbsolutePath(): File 객체가 나타내는 디렉토리 혹은 파일의 절대 경로 반환

        if(musicDir.isDirectory()) {
            // 아래는 musicDir가 나타내는 Music 디렉토리의 이름과 절대 경로를 로그로 출력한다
            Log.i(TAG, "name and path: " + musicDir.getName() + ", " + musicDir.getAbsolutePath());
        }

        File files[] = {};
        int num = 0;

        try {
            // listFiles(): 디렉토리에 있는 파일(디렉토리 포함)들을 나타내는 File 객체들의 배열을 반환한다
            files = musicDir.listFiles();
            if(files == null) {
                // listFiles()를 호출한 File 객체가 디렉토리가 아니거나, IOError가 발생한 것
                Log.i(TAG, "this is not be a directory or IOError was occurred");
            } else {
                num = files.length;
                if (num == 0) {
                    Log.i(TAG, "there is no files in the Music directory");
                    // 디렉토리에 파일이 없다
                    // 이 경우는 재생할 수 있는 파일이 없는 것이므로 적절히 대처한다
                }
                // listFiles() 메소드가 반환하는 File 객체 배열을 이용하여
                // musicDir 안에 있는 모든 파일들의 이름을 알아낼 수 있다

                // 아래 코드는 File 객체 배열의 길이만큼 for 루프를 돌면서 파일(혹은 디렉토리)의 이름을 로그로 출력한다
                for (int i = 0; i < num; i++) {
                    Log.i(TAG, "music directory file " + (i + 1) + " : " + files[i].getName());
                }
            }
        } catch(SecurityException e) {
                e.printStackTrace();
        }

        // 아래 코드는 위에서 musicDir.listFiles()의 결과가 null이 아니고 하나 이상의 mp3 파일이 존재하는 것을 전제로 함
        // 즉, files 배열이 null이거나 빈 배열이라면 아래 files[0].getAbsolutePath()에서 에러가 발생할 것임
        // 그러면 당연히 mediaPlayer.setDataSource()도 성공하지 못 할 것임
        // API 사용 예로 참고할 것
        mediaPlayer = new MediaPlayer();
        try {
            Log.i(TAG, "the first file path: " + files[0].getAbsolutePath());

            // 파일의 절대 경로를 이용하여 MediaPlayer가 재생을 할 mp3 파일을 지정할 수 있다
            // 아래는 files 배열의 첫번째 파일의 절대경로를 알아내고
            // 이를 setDatasource() 메소드에 넘겨주어 해당 파일을 재생할 source로 지정하는 예를 보여주고 있다
            // String filepath = files[0].getAbsolutePath();
            String filepath = musicDir.getAbsolutePath();
            filepath += ("/" + files[0].getName());
            Log.i(TAG, "file path: " + filepath);
            mediaPlayer.setDataSource(filepath);

        } catch(IOException e) {
            e.printStackTrace();
        }
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
                    prepareAccessToMusicDirectory();

                } else {
                    // 권한을 얻지 못 하였으므로 파일 읽기를 할 수 없다
                    // 적절히 대처한다

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mediaPlayer.release();
        mediaPlayer = null;
        Log.i("MobileProgramming", "onDestory()");
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.start:
                try {
                    mediaPlayer.prepare();
                } catch(Exception e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
                break;
            case R.id.pause:
                mediaPlayer.pause();
                break;
        }
    }

}
