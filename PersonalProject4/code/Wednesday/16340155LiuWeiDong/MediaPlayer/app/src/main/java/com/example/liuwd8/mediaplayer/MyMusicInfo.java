package com.example.liuwd8.mediaplayer;

import java.io.Serializable;

public class MyMusicInfo implements Serializable {
    public String name;
    public String singer;
    public int duration;
    public int position;

    public MyMusicInfo() {}

    public MyMusicInfo(String name, String singer, int duration, int position) {
        this.name = name;
        this.singer = singer;
        this.duration = duration;
        this.position = position;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public String getSinger() {
        return singer;
    }

    public int getDuration() {
        return duration;
    }

    public int getPosition() {
        return position;
    }
}
