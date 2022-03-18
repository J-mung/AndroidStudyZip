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
                    appList.get(i).setTv_content(appInfos[appInfos.length-1]);
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
    public void onClick(String appName) {
        // 전달받은 appName을 통해 Intent를 생성하고 화면 전환
        Log.e("test", "appName: " + appName);
        Intent intent = createAppIntent(appName);

        if(intent != null)
            startActivity(intent);
    }

    public boolean confirmName(String appName) {
        boolean token = false;
        if(appName.equals("EditTextExample"))
            token = true;
        else if(appName.equals("IntentExamMain"))
            token = true;
        return token;
    }

    public Intent createAppIntent(String appName) {
        Intent intent = null;
        if(appName.equals("EditTextExample"))
            intent = new Intent(MainActivity.this, EditTextExample.class);
        else if(appName.equals("IntentExamMain"))
            intent = new Intent(MainActivity.this, IntentExamMain.class);

        return intent;
    }

    public ActivityInfo[] getActivityList() throws PackageManager.NameNotFoundException {
        PackageManager pm = this.getPackageManager();
        PackageInfo info = pm.getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_ACTIVITIES);
        ActivityInfo[] list = info.activities;

        return list;
    }
}