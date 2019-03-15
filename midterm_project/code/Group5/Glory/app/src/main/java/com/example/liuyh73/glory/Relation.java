package com.example.liuyh73.glory;

import android.widget.RelativeLayout;

/**
 *     `hero_id` int(11) NOT NULL,
 *     `name` varchar(255) NOT NULL,
 *     `partner1` varchar(255) NOT NULL,
 *     `partner1description` varchar(1024) NOT NULL,
 *     `partner2` varchar(255) NOT NULL,
 *     `partner2description` varchar(1024) NOT NULL,
 *     `repress1` varchar(255) NOT NULL,
 *     `repress1description` varchar(1024) NOT NULL,
 *     `repress2` varchar(255) NOT NULL,
 *     `repress2description` varchar(1024) NOT NULL,
 *     `repressed1` varchar(255) NOT NULL,
 *     `repressed1description` varchar(1024) NOT NULL,
 *     `repressed2` varchar(255) NOT NULL,
 *     `repressed2description` varchar(1024) NOT NULL,
 */
public class Relation {
    private int hero_id;
    private String name;
    private String partner1;
    private String partner1description;
    private String partner2;
    private String partner2description;
    private String repress1;
    private String repress1description;
    private String repress2;
    private String repress2description;
    private String repressed1;
    private String repressed1description;
    private String repressed2;
    private String repressed2description;

    public Relation(int hero_id, String name, String partner1,String partner1description, String partner2, String partner2description, String repress1, String repress1description,
                    String repress2, String repress2description, String repressed1, String repressed1description, String repressed2, String repressed2description){
        this.hero_id = hero_id;
        this.name = name;
        this.partner1 = partner1;
        this.partner1description = partner1description;
        this.partner2 = partner2;
        this.partner2description = partner2description;
        this.repress1 = repress1;
        this.repress1description = repress1description;
        this.repress2 = repress2;
        this.repress2description = repress2description;
        this.repressed1 = repressed1;
        this.repressed1description = repressed1description;
        this.repressed2 = repressed2;
        this.repressed2description = repressed2description;
    }

    public int getHero_id() {
        return hero_id;
    }

    public String getName() {
        return name;
    }

    public String getPartner1() {
        return partner1;
    }

    public String getPartner1description() {
        return partner1description;
    }

    public String getPartner2() {
        return partner2;
    }

    public String getPartner2description() {
        return partner2description;
    }

    public String getRepress1() {
        return repress1;
    }

    public String getRepress1description() {
        return repress1description;
    }

    public String getRepress2() {
        return repress2;
    }

    public String getRepress2description() {
        return repress2description;
    }

    public String getRepressed1() {
        return repressed1;
    }

    public String getRepressed1description() {
        return repressed1description;
    }

    public String getRepressed2() {
        return repressed2;
    }

    public String getRepressed2description() {
        return repressed2description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHero_id(int hero_id) {
        this.hero_id = hero_id;
    }

    public void setPartner1(String partner1) {
        this.partner1 = partner1;
    }

    public void setPartner1description(String partner1description) {
        this.partner1description = partner1description;
    }

    public void setPartner2(String partner2) {
        this.partner2 = partner2;
    }

    public void setPartner2description(String partner2description) {
        this.partner2description = partner2description;
    }

    public void setRepress1(String repress1) {
        this.repress1 = repress1;
    }

    public void setRepress1description(String repress1description) {
        this.repress1description = repress1description;
    }

    public void setRepress2(String repress2) {
        this.repress2 = repress2;
    }

    public void setRepress2description(String repress2description) {
        this.repress2description = repress2description;
    }

    public void setRepressed1(String repressed1) {
        this.repressed1 = repressed1;
    }

    public void setRepressed1description(String repressed1description) {
        this.repressed1description = repressed1description;
    }

    public void setRepressed2(String repressed2) {
        this.repressed2 = repressed2;
    }

    public void setRepressed2description(String repressed2description) {
        this.repressed2description = repressed2description;
    }

    public String getDescriptionByName(String heroName){
        if(partner1.equals(heroName)){
            return partner1description;
        }
        if(partner2.equals(heroName)){
            return partner2description;
        }
        if(repress1.equals(heroName)){
            return repress1description;
        }
        if(repress2.equals(heroName)){
            return repress2description;
        }
        if(repressed1.equals(heroName)){
            return repressed1description;
        }
        if(repressed2.equals(heroName)){
            return repressed2description;
        }
        return "";
    }
}
