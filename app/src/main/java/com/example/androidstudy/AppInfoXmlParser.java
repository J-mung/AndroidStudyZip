package com.example.androidstudy;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class MyXmlParser {
    private ArrayList<AppInfo> appInfos;
    private Context mContext;

    private static final String XML_PATH = "app/src/main/assets/appinfo.xml";

    public MyXmlParser() {
        mContext = null;
    }

    public MyXmlParser(Context mContext) {
        this.mContext = mContext;
    }

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

    public void editXml(String selection, String url, Context mContext) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try(InputStream inputStream = new FileInputStream(XML_PATH)) {
            Log.e("editXml", "xml file was opend");
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputStream);
            NodeList appList = doc.getElementsByTagName("app");
            Log.e("editXml", "editing xml file");
            for(int i = 0; i < appList.getLength(); i++) {
                Node app = appList.item(i);
                if(app.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList childeNodes = app.getChildNodes();
                    for(int j = 0; j < childeNodes.getLength(); j++) {
                        Node item = childeNodes.item(j);
                        Log.e("editXml", "Tag Name: " + item.getNodeName().toString());
                        if(item.getNodeType() == Node.ELEMENT_NODE) {
                            if("tv_content".equalsIgnoreCase(item.getNodeName())){
                                if(selection.equals(item.getTextContent())) {
                                    item.setTextContent(url);
                                }
                            }
                        }
                    }
                }
            }

            try(FileOutputStream outputStream = new FileOutputStream(mContext.getAssets().open("appinfo.xml").toString())) {
                writeXml(doc, outputStream);
            }
        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            e.printStackTrace();
        }
        /*DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(context.openFileInput("appinfo.xml"));

        NodeList nodeList = doc.getElementsByTagName(tag);

        for(int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            NamedNodeMap att = node.getAttributes();
            int h = 0;
            boolean isTitle = false;
            while(h < att.getLength()) {
                Node app = att.item(h);
                if(app.getNodeValue().equals(title))
                    isTitle = true;
                if(h == 3 && isTitle)
                    app.setNodeValue(content);
                h += 1;
            }
        }

        Transformer transformer = null;
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        DOMSource dSource = new DOMSource(doc);
        StreamResult result = new StreamResult(context.openFileOutput("appinfo.xml", Context.MODE_PRIVATE));
        if(transformer != null) {
            try {
                transformer.transform(dSource, result);
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }*/
    }

    // 변경하고 싶은 app의 id값
    // 변경하고 싶은 내용(현재는 url만 고려함)
    private void writeXml(int target, String editContent) {
        // 파일 수정하는 과정
        // 파일을 열어서 수정할 위치를 찾는다

        // 파일을 연다
        XmlPullParserFactory factory;
        FileInputStream is = null;
        try {
            StringBuilder sb = new StringBuilder();
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xmlPullParser = factory.newPullParser();
            is = mContext.openFileInput("appinfo.xml");

            xmlPullParser.setInput(is, null);

            int eventType = xmlPullParser.getEventType();
            int atriNum = -1;
            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG: {
                        if(Integer.parseInt(xmlPullParser.getAttributeValue(0)) == target)
                            while(xmlPullParser.getName().equals("id")) { atriNum++; }
                        if(atriNum > -1) {

                        }

                        break;
                    }
                }
                eventType = xmlPullParser.next();
            }

        } catch (XmlPullParserException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 파일의 끝까지 반복
            // 스타트 태그를 읽는다.
            // 만약 태그에 attribute가 있다면
                // attribute id를 찾고, 수정하고자 하는 id와 동일하다면
                    // 해당 태그의 자식들 중 url 태그를 찾을 때까지 반복, url 태그를 찾았다면
                        // 변경되는 url로 수정한다
                        // loop 탈출
            // 만약 잘못된 접근(다른 이름의 파일, 없는 파일, 존재하지 않는 태그 등)이면 오류문을 출력하고 파일을 닫는다.
        // 파일을 성공적으로 수정했으면, re-parsing 과정을 거쳐서 arrayList appInfos에 반영한다

        XmlSerializer serializer = Xml.newSerializer();
        // xml file formating func 만들기
        StringWriter writer = new StringWriter();

        try {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "apps");

            for(AppInfo app: appInfos) {
                serializer.startTag()
            }
        }

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
