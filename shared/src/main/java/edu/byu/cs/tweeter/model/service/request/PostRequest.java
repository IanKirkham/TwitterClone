package edu.byu.cs.tweeter.model.service.request;

import java.time.LocalDateTime;

import edu.byu.cs.tweeter.model.domain.User;

public class PostRequest {
    private User author;
    private String content;
    private LocalDateTime timePublished;

    public PostRequest(User author, String content, LocalDateTime timePublished) {
        this.author = author;
        this.content = content;
        this.timePublished = timePublished;
    }

    public PostRequest() {}

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimePublished() {
        return timePublished;
    }

    public void setTimePublished(LocalDateTime timePublished) {
        this.timePublished = timePublished;
    }
}
