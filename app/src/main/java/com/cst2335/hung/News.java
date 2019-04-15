package com.cst2335.hung;

public class News {

    private String title; //article of url
    private String body; //body of article
    private String newsID; //uuid

    /**
     *title body and new id constructor
     * @param title
     * @param body
     * @param newsID
     */
    public News(String title, String body, String newsID){
        this.title = title;
        this.body = body;
        this.newsID = newsID;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public String getBody() {
        return body;
    }

    /**
     *
     * @param body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     *
     * @return
     */
    public String getNewsID() {
        return newsID;
    }

    /**
     *
     * @param newsID
     */
    public void setNewsID(String newsID) {
        this.newsID = newsID;
    }
}
