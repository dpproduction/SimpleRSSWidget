package com.dgroup.simplersswidget.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "channel", strict = false)
public class Channel {

    @ElementList(name = "item", inline = true)
    private List<Article> articleList;

    @Element(name = "title", required=false)
    private String title;

    @ElementList(entry="link", inline=true)
    private List<String> link;

    @Element(name = "description")
    private String description;

    public List<Article> getArticleList() {
        return articleList;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }
}