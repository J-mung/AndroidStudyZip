package com.example.androidstudy.activitys.server_system;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.androidstudy.AppInfo;

import java.util.HashMap;
import java.util.Map;

public class DeleteDateRequest extends StringRequest {

    final static private String URL = "http://xorb1198.dothome.co.kr/ServerRequests.php";
    private Map<String, String> map;

    public DeleteDateRequest(String userID, String content, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("kindOfRequest", "Delete");
        map.put("USER_userID", userID);
        map.put("content", content);
    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
