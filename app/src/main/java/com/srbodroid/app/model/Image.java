package com.srbodroid.app.model;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table IMAGE.
 */
public class Image {

    private Long id;
    private String url;
    private Long news_id;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Image() {
    }

    public Image(Long id) {
        this.id = id;
    }

    public Image(Long id, String url, Long news_id) {
        this.id = id;
        this.url = url;
        this.news_id = news_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getNews_id() {
        return news_id;
    }

    public void setNews_id(Long news_id) {
        this.news_id = news_id;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
