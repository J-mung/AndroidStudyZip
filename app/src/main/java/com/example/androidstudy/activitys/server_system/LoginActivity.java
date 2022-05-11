package com.example.androidstudy.activitys.server_system;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidstudy.AppInfo;
import com.example.androidstudy.R;
import com.example.androidstudy.activitys.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText et_loginID, et_loginPassword;
    private Button btn_login, btn_register;
    private boolean loginSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_loginID = (EditText) findViewById(R.id.et_loginID);
        et_loginPassword = (EditText) findViewById(R.id.et_loginPassword);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginID = et_loginID.getText().toString();
                String loginPassword = et_loginPassword.getText().toString();

                Response.Listener<String> loginListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            loginSuccess = jsonObject.getBoolean("success");

                            if(loginSuccess) { // 로그인이 성공한 경우
                                Toast.makeText(getApplicationContext(), "로그인이 성공하였습니다.", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("loginID", loginID);
                                intent.putExtra("loginPassword", loginPassword);
                                startActivity(intent);
                            } else { // 로그인이 실패한 경우
                                Toast.makeText(getApplicationContext(), "로그인이 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Response.Listener<String> loadExamListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(loginSuccess) {
                            ArrayList<AppInfo> loadInfos = new ArrayList<>();
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                Log.d("TAG", response);

                                JSONArray jsonArray = jsonObject.getJSONArray("exam_table");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    AppInfo newinfo = new AppInfo();
                                    JSONObject item = jsonArray.getJSONObject(i);
                                    newinfo.setId(Integer.parseInt(item.getString("id")));
                                    newinfo.setLecturer(item.getString("lecturer"));
                                    newinfo.setContent(item.getString("content"));
                                    newinfo.setUrl(item.getString("url"));
                                    // profile 이미지를 설정해두지 않으면 recyclerview에서 item을 표시하지 못하는 버그
                                    // 따라서 임의로 이미지 지정
                                    newinfo.setProfile(R.mipmap.ic_launcher_round);
                                    loadInfos.add(newinfo);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            Bundle args = new Bundle();
                            args.putSerializable("ARRAYLIST", (Serializable) loadInfos);
                            intent.putExtra("appInfos", args);
                            startActivity(intent);
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(loginID, loginPassword, loginListener);
                LoadDataRequest loadExamRequest = new LoadDataRequest(loginID, loadExamListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
                queue.add(loadExamRequest);
            }
        });

    }
}
