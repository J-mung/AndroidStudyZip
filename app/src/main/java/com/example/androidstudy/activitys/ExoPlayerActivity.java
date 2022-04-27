package com.example.androidstudy.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.example.androidstudy.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

public class ExoPlayerActivity extends AppCompatActivity {

    // video URL
    private String videoURL = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
    private PlayerView playerView;
    private ExoPlayer exoPlayer;  // 실질적으로 영상을 플레이하는 객체의 참조 변수
    private PlayerControlView exoController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);

        playerView = (PlayerView) findViewById(R.id.playerView);
        exoController = (PlayerControlView) findViewById(R.id.exoController);
    }

    // 화면에 영상이 보이기 시작할 때
    @Override
    protected void onStart() {
        super.onStart();

        // Remove ExoPlayerFactory. Use ExoPlayer.Builder instead. (from https://github.com/google/ExoPlayer/releases)
        //exoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
        exoPlayer = new ExoPlayer.Builder(this).build();
        // playerView에게 player 설정
        playerView.setPlayer(exoPlayer);
        // 플레이어 컨트롤뷰와 플레이어 연동
        exoController.setPlayer(exoPlayer);

        // 비디오데이터 소스를 관리하는 DataSource 객체를 만들어주는 팩토리 객체 생성
        DataSource.Factory factory = new DefaultDataSourceFactory(this, "Ex89VideoAndExoPlayer");
        Uri videoUri = Uri.parse(videoURL);
        // 비디오데이터를 Uri로 부터 추출해서 DataSourc 객체 (CD or LP판 같은) 생성
        ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(factory).createMediaSource(MediaItem.fromUri(videoUri));

        // 생성된 비디오데이터 소스객체인 mediaSource를
        // 플레이어 객체에게 전달하여 준비하도록 지시
        exoPlayer.prepare(mediaSource);

        // 로딩이 완료되면 자동 실행
        //exoPlayer.setPlayWhenReady(true);
    }

    @Override
    protected void onStop() {
        super.onStop();

        playerView.setPlayer(null);
        exoPlayer.release();
        exoPlayer=null;
    }
}