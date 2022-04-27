package com.example.androidstudy.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.androidstudy.R;

public class VideoViewExam extends AppCompatActivity {

    private Button btn_exoPlayer;
    private VideoView videoView;                // 미디어를 실행할 수 있게 도와주는 뷰
    private MediaController mediaController;    // 재생이나 정지와 같은 미디어 제어 버튼부를 담당
    private String videoURL = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view_exam);

        btn_exoPlayer = (Button) findViewById(R.id.btn_exoPlayer);
        btn_exoPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExoPlayerActivity.class);
                startActivity(intent);
                finish();
            }
        });

        videoView = (VideoView) findViewById(R.id.videoView);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        // 앱 내에 존재하는 영상 URL
        String localVideo = "android.resource://"+getPackageName() + "/" + R.raw.big_buck_bunny;
        Uri uri = Uri.parse(localVideo);

        videoView.setMediaController(mediaController);  // 미디어 제어 버튼부 세팅
        videoView.setVideoURI(uri);                     // 비디오 뷰의 주소를 설정

        // 동영상을 불러오는 데에 시간이 걸리므로
        // 동영상 로딩 준비가 끝났을 때 실행하도록 리스너 설정
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });
    }   // onCreate()

    @Override
    protected void onPause() {
        super.onPause();

        // 동영상 일시 정지
        if(videoView != null && videoView.isPlaying())
            videoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(videoView != null)
            videoView.stopPlayback();
    }
}