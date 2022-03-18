package com.example.androidstudy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditTextExample extends AppCompatActivity {
    EditText et_id;
    Button btn_test;
    ImageView img_test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edittext_layout);

        et_id = (EditText) findViewById(R.id.et_id);
        btn_test = (Button) findViewById(R.id.btn_test);

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_id.setText("버튼이 눌러짐");
            }
        });

        img_test = (ImageView) findViewById(R.id.img_test);
        img_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Toast testing...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
