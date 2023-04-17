package com.example.globalcredenceapp.notice;

public class NoticeModelClass {

    private String title,date,body;
    private boolean expandable;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public NoticeModelClass(String title, String date, String body) {
        this.title = title;
        this.date = date;
        this.body = body;
        this.expandable = false;
    }

    public NoticeModelClass(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "NoticeModelClass{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
