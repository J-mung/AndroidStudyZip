package com.example.androidstudy.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.androidstudy.R;

public class BroadCastReceiverExam extends AppCompatActivity {

    public static TextView tv_state;
    private NetworkReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_cast_receiver_exam);

        tv_state = (TextView) findViewById(R.id.tv_state);

        // 브로드 캐스트 리시버 객체 생성
        IntentFilter filter = new IntentFilter();
        receiver = new NetworkReceiver();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        // 브로드 캐스트 리시버 등록
        registerReceiver(receiver, filter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 브로드 캐스트 리시버 해제
        unregisterReceiver(receiver);
    }
}