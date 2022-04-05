package com.example.androidstudy;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;

public class AppInfoXmlParser {
    private final static String ns = null;
    private ArrayList<AppInfo> appInfos;
    private Context mContext;

    private static final String XML_PATH = "app/src/main/assets/appinfo.xml";

    public AppInfoXmlParser() {
        mContext = null;
    }

    public AppInfoXmlParser(Context mContext) {
        this.mContext = mContext;
    }

    public void parseXML(InputStream is) {
        try {
            // parser instance
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            //this.appInfos = processParsing(parser);
            parser.nextTag();
            this.appInfos = readFeed(parser);

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
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
            if(name.equals("tv_lecturer")) {
                tv_lecturer = readTarget(parser, "tv_lecturer");
            } else if(name.equals("tv_content")) {
                tv_content = readTarget(parser, "tv_content");
            } else if(name.equals("url")) {
                url = readTarget(parser, "url");
            } else {
                skip(parser);
            }
        }

        return (new AppInfo(R.mipmap.ic_launcher, tv_lecturer, tv_content, url, Integer.parseInt(id)));
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
                            curApp.setProfile(R.mipmap.ic_launcher);     // 개별 프로필을 저장할 수 있도록 개선할 것
                        }else if("tv_lecturer".equals(eltName)) {
                            curApp.setLecturer(parser.nextText());
                        }else if("tv_content".equals(eltName)) {
                            curApp.setContent(parser.nextText());
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

    // 변경하고 싶은 app의 id값
    // 변경하고 싶은 내용(현재는 url만 고려함)
    public void writeAppInfo(InputStream is, int target, String editContent) throws IOException, ParserConfigurationException, SAXException, TransformerConfigurationException {
        //FileInputStream is = mContext.openFileInput("appinfo.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(is);
        NodeList apps = document.getElementsByTagName("app");

        for(int i = 0; i < apps.getLength(); i++) {
            // attributes들을 map 형태로 추출
            NamedNodeMap map = apps.item(i).getAttributes();
            // attribute id 가져오기
            Node node = map.getNamedItem("id");
            // 가져온 id의 값이 target과 동일하면
            if(node.getNodeValue().equals(String.valueOf(target))) {
                NodeList childNode = apps.item(i).getChildNodes();
                for(int j = 0; j < childNode.getLength(); j++) {

                    findElement(apps.item(i).getChildNodes(), "url");

                    break;
                }
                break;
                /*
                Log.e("editXml", childNode.item(1).getNodeName());
                childNode.item(1).setTextContent("google.com");
                Log.e("editXml", childNode.item(1).getNodeName());
                */
            }
            //txt.setText(node.getNodeValue());
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        /*
        StreamResult result = new StreamResult(mContext.getAssets().open("appinfo.xml"), Context.MODE_PRIVATE));
        try {
            transformer.transform(domSource, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        */
    }
    public void findElement(NodeList appChild, String element) {
        for(int i = 0; i < appChild.getLength(); i++) {
            if (appChild.item(i).getNodeName().equals(element)) {
                appChild.item(i+1).setNodeValue("google");
                String str = appChild.item(i+1).getNodeValue();
            }
        }
    }
    public void writeUrl(Node node, String input) {
        node.setTextContent(input);
    }

    private void printApps(ArrayList<AppInfo> apps) {
        StringBuilder builder = new StringBuilder();

        for(AppInfo app : apps) {
            builder.append(app.getProfile()).append("\n").
                    append(app.getLecturer()).append("\n").
                    append(app.getContent()).append("\n").
                    append(app.getUrl()).append("\n").
                    append(app.getId()).append("\n\n");
        }

        Log.e("appInfo", "Info: " + builder.toString());
    }
}
