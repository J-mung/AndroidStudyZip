package com.example.androidstudy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnItemClick {

    private ArrayList<MainData> appList;
    private MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private String[] actNames = {
            "EditTextExample", "IntentExamMain", "ServiceExam", "WebViewExam", "SpinnerExam",
            "LoadingAniExam"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정
        recyclerView = (RecyclerView) findViewById(R.id.recycler_main);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        appList = new ArrayList<>();

        // activity들의 class 이름을 따와서 appList에 저장
        ActivityInfo[] activityInfos;
        try {
            activityInfos = getActivityList();
            for(int i = 0; i < activityInfos.length; i++) {
                String[] appInfos = activityInfos[i].name.toString().split("\\.");
                String appName = appInfos[appInfos.length-1];

                // Main 화면에서 표시하고 싶은 app이 맞다면 MainData를 생성하고 appList에 저장
                if(confirmName(appName)) {
                    MainData mainData = new MainData(R.mipmap.ic_launcher, "홍드로이드", appName);
                    appList.add(mainData);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // 리사이클러뷰에 mainAdapter 객체 지정
        mainAdapter = new MainAdapter(MainActivity.this, appList, this);
        recyclerView.setAdapter(mainAdapter);
        mainAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(String appName) throws ClassNotFoundException {
        // 전달받은 appName을 통해 Intent를 생성하고 화면 전환
        Log.e("test", "appName: " + appName);
        Intent intent = createAppIntent(appName);

        if(intent != null)
            startActivity(intent);
    }

    // activity 이름 확인
    public boolean confirmName(String appName) {
        boolean token = false;

        for(int i = 0; i < actNames.length; i++) {
            if(actNames[i].equals(appName))  {
                token = true;
            }
        }

        return token;
    }

    // activity 이름에 맞처서 intent 생성
    public Intent createAppIntent(String appName) throws ClassNotFoundException {
        Intent intent = null;

        for(int i = 0; i < actNames.length; i++) {
            if(actNames[i].equals(appName)) {
                intent = new Intent(MainActivity.this, Class.forName("com.example.androidstudy."+appName));
            }
        }

        return intent;
    }

    public ActivityInfo[] getActivityList() throws PackageManager.NameNotFoundException {
        PackageManager pm = this.getPackageManager();
        PackageInfo info = pm.getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_ACTIVITIES);
        ActivityInfo[] list = info.activities;

        return list;
    }
}