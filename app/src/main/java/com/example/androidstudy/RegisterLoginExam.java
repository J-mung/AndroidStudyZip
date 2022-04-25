package com.example.androidstudy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// 서버 : http://xorb1198.dothome.co.kr/myadmin

public class RegisterLoginExam extends AppCompatActivity {

    private TextView tv_userID, tv_userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerlogin);

        tv_userID = (TextView) findViewById(R.id.tv_userID);
        tv_userPassword = (TextView) findViewById(R.id.tv_userPassword);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("loginID");
        String userPassword = intent.getStringExtra("loginPassword");

        tv_userID.setText(userID);
        tv_userPassword.setText(userPassword);
    }
}
