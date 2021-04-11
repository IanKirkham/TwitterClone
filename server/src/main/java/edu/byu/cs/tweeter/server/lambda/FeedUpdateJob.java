package edu.byu.cs.tweeter.server.lambda;

import java.util.List;

public class FeedUpdateJob {
    List<String> userAliases;
    String author;
    String timePublished;
    String content;

    public FeedUpdateJob(List<String> userAliases, String author, String timePublished, String content) {
        this.userAliases = userAliases;
        this.author = author;
        this.timePublished = timePublished;
        this.content = content;
    }

    public FeedUpdateJob() {}

    public List<String> getUserAliases() {
        return userAliases;
    }

    public void setUserAliases(List<String> userAliases) {
        this.userAliases = userAliases;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTimePublished() {
        return timePublished;
    }

    public void setTimePublished(String timePublished) {
        this.timePublished = timePublished;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
