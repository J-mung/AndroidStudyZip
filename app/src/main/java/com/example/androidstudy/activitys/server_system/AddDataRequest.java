package com.example.androidstudy.activitys.server_system;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.androidstudy.AppInfo;

import java.util.HashMap;
import java.util.Map;

public class AddDataRequest extends StringRequest {
    final static private String URL = "http://xorb1198.dothome.co.kr/AddExam.php";
    private Map<String, String> map;

    public AddDataRequest(String userID, AppInfo appinfo, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("USER_userID", userID);
        map.put("id", String.valueOf(appinfo.getId()));
        map.put("lecturer", appinfo.getLecturer());
        map.put("content", appinfo.getContent());
        map.put("url", appinfo.getUrl());
    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
