package com.example.androidstudy.activitys.server_system;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.androidstudy.AppInfo;
import com.example.androidstudy.R;
import com.example.androidstudy.activitys.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class AddDataDialog extends DialogFragment {

    private String TAG = "PopAddFormDialogFragment";
    private AutoCompleteTextView actv_content;
    private AutoCompleteTextView actv_lecturer;
    private AutoCompleteTextView actv_url;
    private Button btn_dialog_confirm;
    private Button btn_dialog_cancle;
    private String userID;

    public AddDataDialog(String userID) {
        this.userID = userID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_data_dialog, container, false);

        actv_content = (AutoCompleteTextView) view.findViewById(R.id.actv_content);
        actv_lecturer = (AutoCompleteTextView) view.findViewById(R.id.actv_lecturer);
        actv_url = (AutoCompleteTextView) view.findViewById(R.id.actv_url);
        btn_dialog_confirm = (Button) view.findViewById(R.id.btn_dialog_confirm);
        btn_dialog_cancle = (Button) view.findViewById(R.id.btn_dialog_cancle);

        String[] items = getResources().getStringArray(R.array.exam_content_name);
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>((MainActivity)getActivity(), R.layout.content_list, items);
        actv_content.setAdapter(itemAdapter);

        btn_dialog_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppInfo addInfo = new AppInfo();
                addInfo.setContent(String.valueOf(actv_content.getText()));
                addInfo.setLecturer(String.valueOf(actv_lecturer.getText()));
                addInfo.setUrl(String.valueOf(actv_url.getText()));

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if(success) {
                                //Toast.makeText((MainActivity)getActivity(), "예제 추가 완료", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "예제 추가 완료");
                            } else {
                                //Toast.makeText((MainActivity)getActivity(), "예제 추가 실패", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "예제 추가 실패");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                AddDataRequest addDataRequest = new AddDataRequest("test1", addInfo, responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue((MainActivity)getActivity());
                requestQueue.add(addDataRequest);

                dismiss();
            }
        });
        btn_dialog_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}
