package com.yunwoon.toward_user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView id, pwd;
        Button login;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN); //상태바 공간 사용
        getWindow().setStatusBarColor(Color.parseColor("#ffffff")); // 상태바 흰색 지정
        getWindow().getDecorView().setSystemUiVisibility( //상태바 글자색 검정 변경
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        );

        id = findViewById(R.id.id); pwd = findViewById(R.id.pwd);
        login = findViewById(R.id.loginBtn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getBaseContext(), MainActivity.class);
                startActivity(in);
            }
        });
    }

    public void onHintClicked(View v){
        Toast.makeText(this.getApplicationContext(),"왜 까먹었어 ㅡㅡ",Toast.LENGTH_SHORT).show();
    }
}
