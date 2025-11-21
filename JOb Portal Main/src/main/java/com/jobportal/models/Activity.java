package com.jobportal.models;

import java.time.LocalDateTime;

public class Activity {
    private int id;
    private int userId;
    private String userName;
    private String content;
    private LocalDateTime createdAt;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return userName + " • " + content;
    }
}
