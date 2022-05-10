package com.example.androidstudy.activitys.server_system;

import static com.google.common.io.Resources.getResource;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.androidstudy.AppInfo;
import com.example.androidstudy.AppInfoXmlParser;
import com.example.androidstudy.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/*
* dialog에서 tv_content_item.setAdapter(itemAdapter); 안 먹힘
* Activity로 수정할 필요가 보임
* */

public class AddDataForm extends Dialog {

    private String userID;
    private AutoCompleteTextView tv_content_item;
    private AutoCompleteTextView tv_lecturer;
    private AutoCompleteTextView tv_url;
    private Context myContext;

    public AddDataForm(@NonNull Context context, String userID) {
        super(context);
        this.userID = userID;

    }

    public void showCustomDialog() {
        //final Dialog dialog = new Dialog(super.getContext());
        final AlertDialog.Builder ad = new AlertDialog.Builder(super.getContext());
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ad.setCancelable(true);
        //dialog.setContentView(R.layout.add_data_dialog);
        ad.setView(R.layout.add_data_dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  // delete dialog's title bar

        tv_content_item = (AutoCompleteTextView) findViewById(R.id.tv_content_item);
        tv_lecturer = (AutoCompleteTextView) findViewById(R.id.tv_lecturer);
        tv_url = (AutoCompleteTextView) findViewById(R.id.tv_url);
        String[] items = AddDataForm.super.getContext().getResources().getStringArray(R.array.exam_content_name);
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(AddDataForm.super.getContext(), R.layout.content_list, items);
        tv_content_item.setAdapter(itemAdapter);

        ad.setPositiveButton("확인", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppInfo addInfo = new AppInfo();
                addInfo.setContent(String.valueOf(tv_content_item.getText()));
                addInfo.setLecturer(String.valueOf(tv_lecturer.getText()));
                addInfo.setUrl(String.valueOf(tv_url.getText()));

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if(success) {
                                Toast.makeText(AddDataForm.super.getContext(), "예제 추가 완료", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddDataForm.super.getContext(), "예제 추가 실패", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                AddDataRequest addDataRequest = new AddDataRequest(userID, addInfo, responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(AddDataForm.super.getContext());
                requestQueue.add(addDataRequest);
            }
        });

        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        ad.show();
    }
}
