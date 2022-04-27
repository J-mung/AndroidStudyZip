package com.example.androidstudy.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidstudy.R;

// background 환경 service가 유지되는 예제

public class ServiceExam extends AppCompatActivity {
    private Button btn_music, btn_stop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servicexam_main);

        btn_music = (Button) findViewById(R.id.btn_music);
        btn_stop = (Button) findViewById(R.id.btn_stop);

        btn_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(getApplicationContext(), MusicService.class));
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(), MusicService.class));
            }
        });
    }
}
