/***
 * Author: Kevin Nghiem
 * Last modified: Mar 25, 2019
 * **/

package com.cst2335.kevin;

public class Article {

    private String title;
    private String body;
    private int articleID;
    /**
     *title body and new id constructor
     * @param title
     * @param body
     * @param articleID
     */

    public Article(String title, String body, int articleID){
        this.title = title;
        this.body = body;
        this.articleID = articleID;
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
    public int getNewsID() {
        return articleID;
    }

    public void setNewsID(int newsID) {
        this.articleID = newsID;
    }
}






