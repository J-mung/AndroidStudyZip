package com.example.androidstudy;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WebViewExam extends AppCompatActivity {
    private WebView webView;
    private String url = "https://meal-coding.tistory.com/";
    private Button btn_url;

    // WebView를 사용하기 위해선 Manifest에 android.permission.INTERNET 허가가 필요하다.

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webviewexam_main);

        btn_url = (Button) findViewById(R.id.btn_url);

        btn_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView = (WebView)findViewById(R.id.webView_blog);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(url);
                webView.setWebChromeClient(new WebChromeClient());
                webView.setWebViewClient(new WebViewClientClass());
            }
        });
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
