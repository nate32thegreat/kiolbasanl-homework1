package com.cybr406.post;

import javax.persistence.*;

@Entity
public class Post
{
    String author;

    @Lob
    String content;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
