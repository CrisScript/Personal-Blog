package com.roadmap.personalblog.model;
import java.time.LocalDate;

public class Article {
    private String id;          // slug o uuid
    private String title;
    private String content;
    private LocalDate publishedAt;

    public Article() {}

    public Article(String id, String title, String content, LocalDate publishedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.publishedAt = publishedAt;
    }

    // getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDate getPublishedAt() { return publishedAt; }
    public void setPublishedAt(LocalDate publishedAt) { this.publishedAt = publishedAt; }
}
