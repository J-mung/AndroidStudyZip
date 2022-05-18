package com.example.androidstudy.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.androidstudy.activitys.server_system.AddDataDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private static ArrayList<AppInfo> appInfos;
    private static ArrayList<AppInfo> loadInfoFromDB;
    private long backBtnTime = 0;
    private Button btn_addExam;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check permission
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

        // get user's ID
        Intent intent= getIntent();
        String userID = intent.getStringExtra("userID");
        // get appinfos from DB
        Bundle args = intent.getBundleExtra("appInfos");
        loadInfoFromDB = (ArrayList<AppInfo>) args.getSerializable("ARRAYLIST");

        // using AppInfoResParser object
        AppInfoXmlParser resParser = new AppInfoXmlParser();

        // get user's info from XML file (테스트용도로 사용해볼 것을 고려)
        resParser.parseXML(getApplicationContext(), getResources().getXml(R.xml.appinfores));
        appInfos = resParser.getAppInfos();

        // 리사이클러뷰에 mainAdapter 객체 지정
        mainAdapter = new MainAdapter(MainActivity.this, loadInfoFromDB, userID);
        recyclerView.setAdapter(mainAdapter);
        mainAdapter.notifyDataSetChanged();

        fragmentManager = getSupportFragmentManager();

        transaction = fragmentManager.beginTransaction();

        // add item with DialogFragment
        btn_addExam = (Button) findViewById(R.id.btn_addExam);
        btn_addExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDataDialog addf = new AddDataDialog(userID);
                addf.show(getSupportFragmentManager(), "PopAddFormDialogFragment");
            }
        });
    }

    // check for app termination from user
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

    public static MainAdapter getMainAdapter() {
        return mainAdapter;
    }

    public static ArrayList<AppInfo> getLoadInfoFromDB() {
        return loadInfoFromDB;
    }

    public static void setLoadInfoFromDB(ArrayList<AppInfo> loadInfoFromDB) {
        MainActivity.loadInfoFromDB = loadInfoFromDB;
    }
}