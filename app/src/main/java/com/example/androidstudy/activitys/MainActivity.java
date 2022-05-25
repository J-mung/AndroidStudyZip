package com.example.androidstudy.activitys;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.androidstudy.AppInfo;
import com.example.androidstudy.AppInfoXmlParser;
import com.example.androidstudy.ItemMoveCallback;
import com.example.androidstudy.MainAdapter;
import com.example.androidstudy.R;
import com.example.androidstudy.StartDragListener;
import com.example.androidstudy.activitys.server_system.AddDataDialog;
import com.example.androidstudy.activitys.server_system.LoginActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements StartDragListener{

    private static MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private static ArrayList<AppInfo> appInfos;
    private static ArrayList<AppInfo> loadInfoFromDB;
    private static String userID = null;
    private long backBtnTime = 0;

    private Button btn_addExam;
    private Button btn_login;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Intent loginIntent;
    private ItemTouchHelper touchHelper;

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

        // add item with DialogFragment
        btn_addExam = (Button) findViewById(R.id.btn_addExam);
        btn_addExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDataDialog addf = new AddDataDialog();
                addf.show(getSupportFragmentManager(), "PopAddFormDialogFragment");
            }
        });

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveLoginActivity();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if(userID != null) {
            btn_login.setVisibility(View.GONE);
            btn_addExam.setVisibility(View.VISIBLE);
            // 리사이클러뷰에 LinearLayoutManager 객체 지정
            recyclerView = (RecyclerView) findViewById(R.id.recycler_main);
            linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);

            // get appinfos from DB
            Bundle args = loginIntent.getBundleExtra("appInfos");
            loadInfoFromDB = (ArrayList<AppInfo>) args.getSerializable("ARRAYLIST");

            // using AppInfoResParser object
            AppInfoXmlParser resParser = new AppInfoXmlParser();

            /*
            // get user's info from XML file (테스트용도로 사용해볼 것을 고려)
            resParser.parseXML(getApplicationContext(), getResources().getXml(R.xml.appinfores));
            appInfos = resParser.getAppInfos();
            */

            // 리사이클러뷰에 mainAdapter 객체 지정
            mainAdapter = new MainAdapter(MainActivity.this, loadInfoFromDB, this);

            // Set TouchHelper for drag and drop
            ItemTouchHelper.Callback callback = new ItemMoveCallback(mainAdapter);
            touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(recyclerView);

            recyclerView.setAdapter(mainAdapter);
            mainAdapter.notifyDataSetChanged();

            fragmentManager = getSupportFragmentManager();

            transaction = fragmentManager.beginTransaction();
        } else {
            btn_login.setVisibility(View.VISIBLE);
            btn_addExam.setVisibility(View.GONE);
        }
    }

    // check for app termination from user
    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if(0 <= gapTime && 2000 >= gapTime) {
            //super.onBackPressed();
            finish();
            System.exit(0);
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
    public static String getUserID() {
        return userID;
    }

    ActivityResultLauncher<Intent> getLoginResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        // get user's ID
                        loginIntent = result.getData();
                        userID = loginIntent.getStringExtra("userID");
                    }
                }
            });

    private void moveLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        getLoginResult.launch(intent);
    }

    // In order to use a specific handle view to drag and drop
    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }
}