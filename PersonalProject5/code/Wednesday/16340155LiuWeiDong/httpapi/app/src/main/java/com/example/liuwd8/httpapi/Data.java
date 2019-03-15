package com.example.liuwd8.httpapi;

public class Data {
    private int aid;
    private int state;
    private String cover;
    private String title;
    private String content;
    private int play;
    private String duration;
    private int vedioReview;
    private String create;
    private String rec;
    private int count;

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getAid() {
        return aid;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCover() {
        return cover;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public String getCreate() {
        return create;
    }

    public void setPlay(int play) {
        this.play = play;
    }

    public int getPlay() {
        return play;
    }

    public void setRec(String rec) {
        this.rec = rec;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getVedioReview() {
        return vedioReview;
    }

    public void setVedioReview(int vedioReview) {
        this.vedioReview = vedioReview;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRec() {
        return rec;
    }

    public String getTitle() {
        return title;
    }
}
