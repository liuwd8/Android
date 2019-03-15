package com.example.liuyh73.glory;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * CREATE TABLE `hero` (
 *   `hero_id` integer NOT NULL,
 *   `name` text DEFAULT NULL,
 *   `pay_type` integer DEFAULT NULL,
 *   `new_type` integer DEFAULT NULL,
 *   `hero_type` integer DEFAULT NULL,
 *   `hero_type2` integer DEFAULT NULL,
 *   `skin_name` text DEFAULT NULL,
 *   `img_url` text  DEFAULT NULL,
 *   `live` integer DEFAULT NULL,
 *   `attack` integer DEFAULT NULL,
 *   `skill` integer DEFAULT NULL,
 *   `difficulty` integer DEFAULT NULL,
 *   PRIMARY KEY (`hero_id`)
 * );
 */
public class Hero {
    private int hero_id;            // 英雄id
    private String name;            // 英雄名字
    private String title;           // 人物称号
    private String hero_type;       // 分类
    private String hero_type2;      // 分类2
    private String skin_name;       // 皮肤名称
    private String avaterUrl;       // 人物头像url
    private int live;               // 生存能力值
    private int attack;             // 攻击伤害值
    private int difficulty;         // 上手难度值
    private int skill;              // 技能效果

    private List<Skill> skills;         // 技能
    private List<HeroEquip> heroEquips; // 推荐装备
    private List<Skin> skins;      // 皮肤

    public Hero(int hero_id, String name,  String title, String hero_type, String hero_type2, String skin_name, String avatarUrl, int live, int attack, int skill, int difficulty){
        this.hero_id = hero_id;
        this.name = name;
        this.title = title;
        this.hero_type = hero_type;
        this.hero_type2 = hero_type2;
        this.skin_name = skin_name;
        this.avaterUrl = avatarUrl;
        this.live = live;
        this.attack = attack;
        this.skill = skill;
        this.difficulty = difficulty;
        this.skills = new ArrayList<>();
        this.heroEquips = new ArrayList<>();
        this.skins = new ArrayList<>();
    }

    public int getSkill() {
        return skill;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public int getHero_id() {
        return hero_id;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAvaterUrl() {
        return avaterUrl;
    }

    public void setAvaterUrl(String avaterUrl) {
        this.avaterUrl = avaterUrl;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getHero_type() { return hero_type; }
    public void setHero_type(String hero_type) { this.hero_type = hero_type; }

    public String getHero_type2() { return hero_type2; }
    public void setHero_type2(String hero_type2) { this.hero_type2 = hero_type2; }

    public int getLive() { return live; }
    public void setLive(int live) { this.live = live; }

    public int getAttack() { return attack; }
    public void setAttack(int attack) { this.attack = attack; }

    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }

    public List<Skill> getSkills() { return skills; }
    public void setSkills(List<Skill> skills) { this.skills = skills; }

    public List<HeroEquip> getHeroEquips() { return heroEquips; }
    public void setHeroEquips(List<HeroEquip> heroEquips) { this.heroEquips = heroEquips; }

    public List<Skin> getSkins() { return skins; }

    public void setSkins(List<Skin> skins) {
        this.skins = skins;
    }
}
