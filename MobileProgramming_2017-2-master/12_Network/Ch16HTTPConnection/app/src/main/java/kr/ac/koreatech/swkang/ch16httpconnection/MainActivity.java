package kr.ac.koreatech.swkang.ch16httpconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // URL로부터 데이터를 전송받는 작업을 수행하기 위한
    // AsyncTask 클래스 정의
    private class DownloadTask extends AsyncTask<String, Void, String> {
        String s = null;

        // 작업 스레드에서 수행되는 코드
        // String 객체로 주어진 URL에 대해서 HttpURLConnection을 생성하고 데이터를 받음
        // 결과를 String 객체로 반환
        @Override
        protected String doInBackground(String... url) {
            try {
                s = downloadUrl(url[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return s;
        }

        // 작업 스레드 작업이 완료된 이후 수행되는 코드
        // 결과를 String 객체로 전달 받고 이를 TextView 객체에 텍스트 내용으로 설정
        @Override
        protected void onPostExecute(String result) {
            TextView text = (TextView)findViewById(R.id.text);
            text.setText(result);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // 버튼을 클릭하면 네트워크 상태를 확인하고, 네트워크 연결이 이루어진 상태인 경우
    // AsyncTask인 DownloadTask 객체를 생성하고 실행
    // URL은 사용자가 EditText에 입력한 내용을 String 객체로 변환하여 전달해 주고 있음
    public void onClick(View v) {
        if(isNetworkAvailable()) {
            EditText url = (EditText)findViewById(R.id.url);

            DownloadTask task = new DownloadTask();
            task.execute(url.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Network is not available", Toast.LENGTH_LONG).show();
        }
    }

    // 현재 active network의 상태가 connected 상태이면 true 반환
    private boolean isNetworkAvailable() {
        boolean available = false;

        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        if(info != null && info.isConnected())
            available = true;

        return available;
    }

    // 입력된 URL에 대해서 HttpURLConnection을 이용하여 서버에 연결
    // 데이터를 읽고 String 객체로 변환 후 반환한다
    private String downloadUrl(String strUrl) throws IOException {
        String s = null;
        byte[] buffer = new byte[1024];
        InputStream iStream = null;

        URL url = new URL(strUrl);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();

        try {
            iStream = conn.getInputStream();
            iStream.read(buffer);
            s = new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            iStream.close();
            conn.disconnect();
        }
        return s;
    }
}
