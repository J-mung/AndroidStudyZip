package com.example.androidstudy.activitys.server_system;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoadDataRequest extends StringRequest {

    // 서버 URL 설정 (FileZilla에 업로드한 AddExamRequest.php 연동)
    final static private String URL = "http://xorb1198.dothome.co.kr/LoadExam.php";
    private Map<String, String> map;

    public LoadDataRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("USER_userID", userID);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}