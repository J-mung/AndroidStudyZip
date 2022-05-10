package com.example.androidstudy;

public class AppInfo {
    private int profile;
    private String lecturer;
    private String content;
    private String url;
    private int id;

    public AppInfo() {
        profile = 0;
        lecturer = null;
        content = null;
        url = null;
        id = 0;
    }

    public AppInfo(int profile, String lecturer, String content, String url, int id) {
        this.profile = profile;
        this.lecturer = lecturer;
        this.content = content;
        this.url = url;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if(url == null) {
            url = "";
        }
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProfile() { return profile; }

    public void setProfile(int iv_profile) {
        this.profile = iv_profile;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        if(lecturer == null) {
            lecturer = "HONG_DROID";
        }
        this.lecturer = lecturer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if(content == null) {
            content = "Sample";
        }
        this.content = content;
    }
}
