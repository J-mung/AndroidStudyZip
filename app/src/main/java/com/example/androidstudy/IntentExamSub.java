package com.example.androidstudy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class IntentExamSub extends AppCompatActivity {
    private TextView tv_sub;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intentexam_sub);

        tv_sub = (TextView) findViewById(R.id.tv_sub);

        Intent intent = getIntent();
        String str = intent.getStringExtra("str");

        tv_sub.setText(str);
    }
}
