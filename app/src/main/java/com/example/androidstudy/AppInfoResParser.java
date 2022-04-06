package com.example.androidstudy;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class AppInfoResParser {
    private final static String ns = null;
    private final static String FILENAME = "appinfo.xml";
    private ArrayList<AppInfo> appInfos;

    public void parseXML(Context mContext, XmlPullParser parser) {
        try {
            //parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            //parser.nextTag();
            //this.appInfos = readFeed(parser);
            this.appInfos = processParse(parser);
            writeFile(mContext, null, appInfos);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<AppInfo> editXmlFile(Context mContext, int targetId, String editText) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            FileInputStream fiStream = mContext.openFileInput(FILENAME);
            parser.setInput(fiStream, null);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        parser.nextTag();
        appInfos = readFeed(parser);
        appInfos.get(targetId).setUrl(editText);
        writeFile(mContext, null, appInfos);

        return appInfos;
    }

    // 확장성이 좋게 개선 필요
    private ArrayList<AppInfo> processParse(XmlPullParser parser) throws XmlPullParserException, IOException {
        String lecturer = null;
        String content = null;
        String url = null;
        int id = 0;
        int profile = 0;
        ArrayList<AppInfo> apps = new ArrayList<>();

        while(parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if(parser.getEventType() == XmlPullParser.START_TAG) {
                if(parser.getName().equals("app")) {
                    apps.add(new AppInfo());
                    id = Integer.parseInt(parser.getAttributeValue(0));
                    //profile = Integer.parseInt(parser.getAttributeValue(1));
                    apps.get(id-1).setId(id);
                    //apps.get(id-1).setIv_profile(profile);
                    apps.get(id-1).setProfile(R.mipmap.ic_launcher_round);
                } else if(parser.getName().equals("lecturer")) {
                    parser.next();
                    lecturer = parser.getText();
                    apps.get(id-1).setLecturer(lecturer);
                } else if(parser.getName().equals("content")) {
                    parser.next();
                    content = parser.getText();
                    apps.get(id-1).setContent(content);
                } else if(parser.getName().equals("url")) {
                    parser.next();
                    url = parser.getText();
                    if(url == null)
                        url = "";
                    apps.get(id-1).setUrl(url);
                }
            }
            parser.next();
        }
        return apps;
    }

    private ArrayList<AppInfo> readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        ArrayList<AppInfo> apps = new ArrayList<>();

        // Below statment will initially queue your cursor to the feed element's start tag : <feed>
        parser.require(XmlPullParser.START_TAG, ns, "feed");
        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the app tag
            if (name.equals("app")) {
                apps.add(readApp(parser));
            } else {
                skip(parser);
            }
        }
        return apps;
    }

    private AppInfo readApp(XmlPullParser parser) throws  XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "app");
        String iv_profile = null;
        String tv_lecturer = null;
        String tv_content = null;
        String url = null;
        String id = null;

        // get attribute
        for(int i = 0 ; i < parser.getAttributeCount(); i++) {
            String atrName = parser.getAttributeName(i);
            if(atrName.equals("id"))
                id = parser.getAttributeValue(i);
            else if(atrName.equals("profile"))
                iv_profile = parser.getAttributeValue(null, "iv_profile");
        }
        // get app's children info
        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("lecturer")) {
                tv_lecturer = readTarget(parser, "lecturer");
            } else if(name.equals("content")) {
                tv_content = readTarget(parser, "content");
            } else if(name.equals("url")) {
                url = readTarget(parser, "url");
            } else {
                skip(parser);
            }
        }

        return (new AppInfo(R.mipmap.ic_launcher, tv_lecturer, tv_content, url, Integer.parseInt(id)));
        //return (new AppInfo(profile, lecturer, content, url, id));
    }

    private String readTarget(XmlPullParser parser, String target) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, target);
        String text = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, target);
        return text;
    }

    private String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        if(parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while(depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    public ArrayList<AppInfo> getAppInfos() {
        return this.appInfos;
    }

    public void setXmlUrl(FileInputStream fis, int target, String editStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        Document doc = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(fis);
        } catch (ParserConfigurationException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        NodeList child = null;
        NodeList childchild = null;
        NodeList node = doc.getElementsByTagName("app");        // app 태그들의 목록
        for(int i = 0; i < node.getLength(); i++) {
            if(node.item(i).getAttributes().equals(String.valueOf(target))) {
                child = node.item(i).getChildNodes();
            }
        }
        for(int i = 0; i < child.getLength(); i++) {
            if(child.item(i).getNodeName().equals("url")) {
                childchild = child.item(i).getChildNodes();
            }
        }

        child = node.item(1).getChildNodes();          // app 태그들의 목록 중 하나를 선택하고 하위 태그들의 리스트 추출
        childchild = child.item(1).getChildNodes();    // 하위 태그 리스트들 중 하나 선택
        String childchildStr = childchild.item(0).getNodeValue();  // 하위 태그의 text추출
        childchild.item(0).setTextContent(editStr);
        childchildStr = childchild.item(0).getNodeValue();
        node.item(1).getNodeName();
    }

    public void writeFile(Context mContext, String fileName, ArrayList<AppInfo> apps) {
        if(fileName == null)
            fileName = FILENAME;
        if(isExternalStorageWritable() && checkPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            try {
                FileOutputStream fos = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
                //FileOutputStream fos = new FileOutputStream(file, MODE_PRIVATE);

                XmlSerializer serializer = Xml.newSerializer();
                StringWriter writer = new StringWriter();
                try{
                    serializer.setOutput(writer);
                    serializer.startDocument("UTF-8", true);
                    serializer.startTag("", "feed");

                    for(AppInfo appinfo : apps) {
                        serializer.startTag("", "app");
                        serializer.attribute("", "id", String.valueOf(appinfo.getId()));
                        serializer.attribute("", "profile", String.valueOf(appinfo.getProfile()));

                        serializer.startTag("", "lecturer");
                        serializer.text(String.valueOf(appinfo.getLecturer()));
                        serializer.endTag("", "lecturer");

                        serializer.startTag("", "content");
                        serializer.text(String.valueOf(appinfo.getContent()));
                        serializer.endTag("", "content");

                        serializer.startTag("", "url");
                        serializer.text(String.valueOf(appinfo.getUrl()));
                        serializer.endTag("", "url");

                        serializer.endTag("", "app");
                    }
                    serializer.endTag("", "feed");
                    serializer.endDocument();
                    serializer.flush();
                    String result = writer.toString();
                    fos.write(result.getBytes());
                    fos.close();
                    //txt.setText(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mContext, "Cannot Write to External Storage", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isExternalStorageWritable() {
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i("State", "Yes, it's writable");
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermission(Context mContext, String permission) {
        int check = ContextCompat.checkSelfPermission(mContext, permission);
        if (check == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }
}
