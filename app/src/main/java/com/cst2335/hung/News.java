package com.cst2335.hung;

public class News {
    private String title;
    private String body;
    private int newsID;

    public News(String title, String body, int newsID){
        this.title = title;
        this.body = body;
        this.newsID = newsID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getNewsID() {
        return newsID;
    }

    public void setNewsID(int newsID) {
        this.newsID = newsID;
    }
}
