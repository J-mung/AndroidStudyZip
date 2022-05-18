package com.example.androidstudy.activitys.server_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.androidstudy.AppInfo;
import com.example.androidstudy.R;
import com.example.androidstudy.activitys.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UpdateitemDialog extends DialogFragment {

    private String TAG = "PopUpdateFormDialogFragment";
    private AutoCompleteTextView actv_update_lecturer;
    private AutoCompleteTextView actv_update_url;
    private Button btn_update_cancle;
    private Button btn_update_confirm;
    private String userID;
    private AppInfo appInfo;
    private Context mContext;

    public UpdateitemDialog(Context mContext, String userID, AppInfo appInfo) {
        this.userID = userID;
        this.appInfo = appInfo;
        this.mContext = mContext;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_item_dialog, container, false);

        actv_update_lecturer = (AutoCompleteTextView) view.findViewById(R.id.actv_update_lecturer);
        actv_update_url = (AutoCompleteTextView) view.findViewById(R.id.actv_update_url);
        btn_update_cancle = (Button) view.findViewById(R.id.btn_update_cancle);
        btn_update_confirm = (Button) view.findViewById(R.id.btn_update_confirm);

        String lecturer = appInfo.getLecturer();
        String url = appInfo.getUrl();
        // 현재 설정된 정보로 set
        actv_update_lecturer.setText(lecturer);
        actv_update_url.setText(url);

        btn_update_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sql update request
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean updateSuccess = jsonObject.getBoolean("success");

                            if(updateSuccess) {
                                Log.d(TAG, "예제 업데이트 완료");
                                String updateLecturer = jsonObject.getString("lecturer");
                                String updateUrl = jsonObject.getString("url");
                                Log.d(TAG, "Lecturer : " + updateLecturer + "\n" + "Url : " + updateUrl);

                                int idx = 0;
                                ArrayList<AppInfo> appInfos = MainActivity.getLoadInfoFromDB();

                                while(!appInfos.get(idx).getLecturer().equals(updateLecturer) && !appInfos.get(idx).getUrl().equals(updateUrl)) {
                                    idx++;
                                }
                                appInfos.get(idx).setLecturer(updateLecturer);
                                appInfos.get(idx).setUrl(updateUrl);
                                MainActivity.setLoadInfoFromDB(appInfos);
                                MainActivity.getMainAdapter().notifyDataSetChanged();
                            } else {
                                Log.d(TAG, "예제 업데이트 실패");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                appInfo.setLecturer(String.valueOf(actv_update_lecturer.getText()));
                appInfo.setUrl(String.valueOf(actv_update_url.getText()));
                UpdateSqlRequest updateSqlRequest = new UpdateSqlRequest(userID, appInfo, responseListener, null);
                RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                requestQueue.add(updateSqlRequest);

                dismiss();
            }
        });

        btn_update_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    // dialog 사이즈 설정
    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
}