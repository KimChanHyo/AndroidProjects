package kr.ac.koreatech.swkang.ch16webview;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // 버튼을 클릭하면 네트워크 상태를 확인하고, 네트워크 연결이 이루어진 상태인 경우
    // WebView를 이용하여 입력된 URL의 웹 페이지를 표시
    // URL은 사용자가 EditText에 입력한 내용을 String 객체로 변환하여 전달해 주고 있음
    public void onClick(View v) {
        if(isNetworkAvailable()) {
            EditText url = (EditText)findViewById(R.id.url);
            WebView web = (WebView)findViewById(R.id.web);

            web.setWebViewClient(new WebViewClient());
            web.loadUrl(url.getText().toString());
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
}
