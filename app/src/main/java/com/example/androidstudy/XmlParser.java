package com.example.androidstudy;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.androidstudy.AppInfo;

public class XmlParser {
    private ArrayList<AppInfo> appInfos;

    public void parseXML(InputStream is) {
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            this.appInfos = processParsing(parser);

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }
    private ArrayList<AppInfo> processParsing(XmlPullParser parser) throws IOException, XmlPullParserException{
        ArrayList<AppInfo> apps = new ArrayList<>();
        int eventType = parser.getEventType();
        AppInfo curApp = null;

        while(eventType != XmlPullParser.END_DOCUMENT) {
            String eltName = null;

            switch(eventType) {
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();

                    if("app".equals(eltName)) {
                        curApp = new AppInfo();
                        apps.add(curApp);
                    } else if(curApp != null) {
                        if("iv_profile".equals(eltName)) {
                            curApp.setIv_profile(R.mipmap.ic_launcher);     // 개별 프로필을 저장할 수 있도록 개선할 것
                        }else if("tv_lecturer".equals(eltName)) {
                            curApp.setTv_lecturer(parser.nextText());
                        }else if("tv_content".equals(eltName)) {
                            curApp.setTv_content(parser.nextText());
                        }else if("url".equals(eltName)) {
                            curApp.setUrl(parser.nextText());
                        }else if("id".equals(eltName)) {
                            curApp.setId(Integer.parseInt(parser.nextText()));
                        }
                    }
                    break;
            }
            eventType = parser.next();
        }
        return apps;
    }

    public ArrayList<AppInfo> getAppInfos() {
        return this.appInfos != null ? this.appInfos : null;
    }

    private void printApps(ArrayList<AppInfo> apps) {
        StringBuilder builder = new StringBuilder();

        for(AppInfo app : apps) {
            builder.append(app.getIv_profile()).append("\n").
                    append(app.getTv_lecturer()).append("\n").
                    append(app.getTv_content()).append("\n").
                    append(app.getUrl()).append("\n").
                    append(app.getId()).append("\n\n");
        }

        Log.e("appInfo", "Info: " + builder.toString());
    }
}
