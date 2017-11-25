package com.example.kimchanhyo.photogallery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // thumbnail Image가 클릭됐을 때 수행
    public void onImageViewClicked(View view) {
        // mainImage 객체를 저장
        ImageView mainImage = (ImageView)findViewById(R.id.mainImage);

        // 클릭된 view의 id를 구한 뒤
        // 해당 이미지를 mainImage로 설정(setImageResource 사용)
        switch(view.getId()) {
            case R.id.image1 :
                mainImage.setImageResource(R.drawable.lulu01);
                break;
            case R.id.image2 :
                mainImage.setImageResource(R.drawable.lulu02);
                break;
            case R.id.image3 :
                mainImage.setImageResource(R.drawable.lulu03);
                break;
            case R.id.image4 :
                mainImage.setImageResource(R.drawable.lulu04);
                break;
        }
    }
}
