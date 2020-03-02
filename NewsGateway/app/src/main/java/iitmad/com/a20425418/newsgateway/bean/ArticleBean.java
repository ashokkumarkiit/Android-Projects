package iitmad.com.a20425418.newsgateway.bean;

import java.io.Serializable;

/**
 * Created by Ashok Kumar - A20425418 on 11/22/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class ArticleBean implements Serializable {

    String article_author;
    String article_title;
    String article_description;
    String article_url;
    String article_urlToImage;
    String article_publishedAt;

    public String getArticle_author() {
        return article_author;
    }

    public void setArticle_author(String article_author) {
        this.article_author = article_author;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getArticle_description() {
        return article_description;
    }

    public void setArticle_description(String article_description) {
        this.article_description = article_description;
    }

    public String getArticle_url() {
        return article_url;
    }

    public void setArticle_url(String article_url) {
        this.article_url = article_url;
    }

    public String getArticle_urlToImage() {
        return article_urlToImage;
    }

    public void setArticle_urlToImage(String article_urlToImage) {
        this.article_urlToImage = article_urlToImage;
    }

    public String getArticle_publishedAt() {
        return article_publishedAt;
    }

    public void setArticle_publishedAt(String article_publishedAt) {
        this.article_publishedAt = article_publishedAt;
    }
}
