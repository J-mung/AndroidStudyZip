package com.example.androidstudy.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidstudy.R;

public class IntentExamMain extends AppCompatActivity {

    private Button btn_move;
    private EditText et_test;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intentexam_main);

        et_test = findViewById(R.id.et_test);

        btn_move = findViewById(R.id.btn_move);
        btn_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str = et_test.getText().toString();

                Intent intent = new Intent(IntentExamMain.this, IntentExamSub.class);
                intent.putExtra("str", str);
                startActivity(intent); // 액티비티 이동

            }
        });
    }
}
