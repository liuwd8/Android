package com.example.liuwd8.httpapi;

public class Repo {
    int id;
    String name;
    String description;
    Boolean has_issues;
    int open_issues;

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHas_issues(Boolean has_issues) {
        this.has_issues = has_issues;
    }

    public void setOpen_issues(int open_issues) {
        this.open_issues = open_issues;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getHas_issues() {
        return has_issues;
    }

    public int getOpen_issues() {
        return open_issues;
    }

    public String getDescription() {
        return description;
    }
}
