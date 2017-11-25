package com.example.kimchanhyo.pizzaapp;

        import android.support.annotation.IdRes;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.TextView;
        import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // totalPrice = doughPrice + toppingPrice
    int doughPrice = 0, toppingPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // radio button이 클릭됐을 때 수행
    public void onRadioButtonClicked(View view) {
        // 클릭된 view의 id(dough)를 구한 뒤
        // 해당 dough의 가격만큼 doughPrice를 설정한다
        switch(view.getId()) {
            case R.id.original  : doughPrice = 10000; break;
            case R.id.napoli    : doughPrice = 15000; break;
            case R.id.thin      : doughPrice = 13000; break;
        }

        // totalPrice의 id를 이용하여 view 객체를 찾고
        // setText 메소드를 이용하여 바뀐 가격을 화면에 띄워준다.
        ((TextView)findViewById(R.id.totalPrice)).
                setText(String.valueOf(doughPrice + toppingPrice));
    }

    // check box가 클릭됐을 때 수행
    public void onCheckBoxClicked(View view) {
        // 클릭된 view의 체크 상태를 checked 변수에 저장
        boolean checked = ((CheckBox)view).isChecked();

        // 클릭된 view의 id(topping)를 구한 뒤
        // 해당 topping의 가격만큼 toppingPrice를 설정한다
        // (체크된 경우 가격만큼 +, 해제된 경우 가격만큼 -)
        switch(view.getId()) {
            case R.id.pepperoni :   toppingPrice += (checked ? 1000 : -1000);    break;
            case R.id.potato    :   toppingPrice += (checked ? 2000 : -2000);    break;
            case R.id.cheese    :   toppingPrice += (checked ? 2000 : -2000);    break;
        }

        // radioGroup(dough)에서 선택된 radio button이 없는 경우
        // dough를 먼저 선택하라는 메시지를 띄운다
       RadioGroup radioG = (RadioGroup)findViewById(R.id.radioGroup);
        if(radioG.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(),
                    "Please select dough first", Toast.LENGTH_SHORT).show();
        }
        // 이외의 경우(dough가 잘 선택된 경우)
        // totalPrice의 id를 이용하여 view 객체를 찾고
        // setText 메소드를 이용하여 바뀐 가격을 화면에 띄워준다.
        else {
            ((TextView) findViewById(R.id.totalPrice)).
                    setText(String.valueOf(doughPrice + toppingPrice));
        }
    }
}
