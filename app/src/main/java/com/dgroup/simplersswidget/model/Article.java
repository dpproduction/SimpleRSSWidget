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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.description != null ? this.description.hashCode() : 0);
        hash = 53 * hash + (this.title != null ? this.title.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Article article = (Article) obj;
        if ((this.title == null) ? (article.title != null) : !this.title.equals(article.title)) {
            return false;
        }
        if ((this.description == null) ? (article.description != null) : !this.description.equals(article.description)) {
            return false;
        }
        if ((this.link == null) ? (article.link != null) : !this.link.equals(article.link)) {
            return false;
        }
        if ((this.author == null) ? (article.author != null) : !this.author.equals(article.author)) {
            return false;
        }
        if ((this.pubDate == null) ? (article.pubDate != null) : !this.pubDate.equals(article.pubDate)) {
            return false;
        }
        return true;
    }

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