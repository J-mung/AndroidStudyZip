package com.example.androidstudy.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.androidstudy.AppInfo;
import com.example.androidstudy.AppInfoXmlParser;
import com.example.androidstudy.MainAdapter;
import com.example.androidstudy.R;
import com.example.androidstudy.activitys.server_system.AddDataForm;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    ArrayList<AppInfo> appInfos;
    private long backBtnTime = 0;
    private Button btn_addExam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent= getIntent();
        String userID = intent.getStringExtra("userID");

        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("파일 쓰기 권한이 필요합니다.")
                .setDeniedMessage("권한을 거부하였습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

        // 리사이클러뷰에 LinearLayoutManager 객체 지정
        recyclerView = (RecyclerView) findViewById(R.id.recycler_main);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // using AppInfoResParser object
        AppInfoXmlParser resParser = new AppInfoXmlParser();

        resParser.parseXML(getApplicationContext(), getResources().getXml(R.xml.appinfores));
        appInfos = resParser.getAppInfos();

        // 리사이클러뷰에 mainAdapter 객체 지정
        mainAdapter = new MainAdapter(MainActivity.this, appInfos);
        recyclerView.setAdapter(mainAdapter);
        mainAdapter.notifyDataSetChanged();

        btn_addExam = (Button) findViewById(R.id.btn_addExam);
        btn_addExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDataForm addDataForm = new AddDataForm( MainActivity.this, userID);
                addDataForm.showCustomDialog();

            }
        });
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if(0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();

        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(getApplicationContext(), "권한이 허용됨", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "권한이 거부됨", Toast.LENGTH_SHORT).show();
        }
    };
}