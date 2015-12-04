package com.dgroup.simplersswidget.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "item", strict = false)
public class Article {

    @Element
    private String title;

    @Element
    private String description;

    @Element
    private String link;

    @Element(required = false)
    private String author;

    @Element(required = false)
    private String pubDate;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getAuthor() {
        return author;
    }

    public String getPubDate() {
        return pubDate;
    }
}