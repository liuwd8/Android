package com.example.liuyh73.glory;

public class Skin {
    private int hero_id;
    private String name;
    private String img_url;
    private String avatar_url;
    public Skin(int hero_id, String name, String img_url, String avatar_url){
        this.hero_id = hero_id;
        this.name = name;
        this.img_url = img_url;
        this.avatar_url = avatar_url;
    }

    public int getHero_id() {
        return hero_id;
    }

    public String getName() {
        return name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
