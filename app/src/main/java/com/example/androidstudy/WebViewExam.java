package com.example.androidstudy;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class WebViewExam extends AppCompatActivity {
    private WebView webView;
    private String url = "https://meal-coding.tistory.com/";
    private Map<String, String> urlList = new HashMap<String, String>(){{
        put("IntentExamMain", "https://meal-coding.tistory.com/22");
    }};
    private XmlParser xmlParser;
    private ArrayList<AppInfo> appInfos;

    // WebView를 사용하기 위해선 Manifest에 android.permission.INTERNET 허가가 필요하다.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webviewexam_main);

        try {
            xmlParser = new XmlParser();
            xmlParser.parseXML(getAssets().open("appinfo.xml"));
            appInfos = xmlParser.getAppInfos();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 외부로부터 url 요청이 있다면
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            // url 매핑
            //url = urlList.get(extras.getString("url"));

            for(AppInfo curApp : appInfos) {
                if(curApp.getName().equals(extras.getString("url")))
                    url = curApp.getUrl();
            }
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
