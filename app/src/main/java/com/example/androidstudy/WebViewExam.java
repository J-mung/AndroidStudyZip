package com.example.androidstudy;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class WebViewExam extends AppCompatActivity {
    private WebView webView;
    private String url = "https://meal-coding.tistory.com/";
    private Map<String, String> urlList = new HashMap<String, String>(){{
        put("IntentExamMain", "https://meal-coding.tistory.com/22");
    }};

    // WebView를 사용하기 위해선 Manifest에 android.permission.INTERNET 허가가 필요하다.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webviewexam_main);

        // 외부로부터 url 요청이 있다면
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            // url 매핑
            url = urlList.get(extras.getString("url"));
        }

        // 매핑한 url이 유효하면
        if(url != null) {
            webView = (WebView) findViewById(R.id.webView_blog);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClientClass());
        } else
            Toast.makeText(this, "You need to set url", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
