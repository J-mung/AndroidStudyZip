package com.example.androidstudy.activitys.server_system;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.androidstudy.AppInfo;

import java.util.HashMap;
import java.util.Map;

public class UpdateSqlRequest extends StringRequest {
    final static private String URL = "http://xorb1198.dothome.co.kr/ServerRequests.php";
    private Map<String, String> map;

    public UpdateSqlRequest(String userID, AppInfo appInfo, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);

        map = new HashMap<>();
        map.put("kindOfRequest", "Update");
        map.put("USER_userID", userID);
        map.put("id", String.valueOf(appInfo.getId()));
        map.put("lecturer", appInfo.getLecturer());
        map.put("content", appInfo.getContent());
        map.put("url", appInfo.getUrl());
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
