package com.example.androidstudy.activitys.login_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.androidstudy.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_userID, et_userPassword, et_userName, et_userAge;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_userID = (EditText) findViewById(R.id.et_userID);
        et_userPassword = (EditText) findViewById(R.id.et_userPassword);
        et_userName = (EditText) findViewById(R.id.et_userName);
        et_userAge = (EditText) findViewById(R.id.et_userAge);

        // 회원가입 버튼 클릭 시 수행
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 현재 입력되어 있는 값을 get
                String userID = et_userID.getText().toString();
                String userPassword = et_userPassword.getText().toString();
                String userName = et_userName.getText().toString();
                int userAge = Integer.parseInt(et_userAge.getText().toString());

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if(success) { // 회원등록이 성공한 경우
                                Toast.makeText(getApplicationContext(), "회원 등록이 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else { // 회원등록이 실패한 경우
                                Toast.makeText(getApplicationContext(), "회원 등록이 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                // 서버로 Volley를 이용해서 요청을 함
                RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, userName, userAge, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });
    }
}