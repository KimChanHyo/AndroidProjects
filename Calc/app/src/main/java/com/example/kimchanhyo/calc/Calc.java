package com.example.kimchanhyo.calc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Calc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);
    }

    public void onClick(View view) {
        String str = "init";
        switch(view.getId()) {
            case R.id.clear :
                str = "Clear";
                break;
            case R.id.add :
                str = "Add";
                break;
        }
        Toast.makeText(getApplicationContext(), str + " Button Pressed!", Toast.LENGTH_SHORT).show();
    }
}
