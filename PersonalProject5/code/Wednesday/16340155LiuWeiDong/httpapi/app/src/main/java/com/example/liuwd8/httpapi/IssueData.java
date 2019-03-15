package com.example.liuwd8.httpapi;

public class IssueData {
    String title;
    String state;
    String created_at;
    String body;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTitle() {
        return title;
    }

    public String getState() {
        return state;
    }

    public String getBody() {
        return body;
    }

    public String getCreated_at() {
        return created_at;
    }
}
