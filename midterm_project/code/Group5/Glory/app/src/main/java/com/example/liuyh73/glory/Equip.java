package com.example.liuyh73.glory;

import android.media.audiofx.DynamicsProcessing;

/**
 * CREATE TABLE `equip` (
 *   `equip_id` int(11) NOT NULL,
 *   `name` varchar(255) DEFAULT NULL,
 *   `equip_type` int(11) DEFAULT NULL,
 *   `selling_price` int(11) DEFAULT NULL,
 *   `purchase_price` int(11) DEFAULT NULL,
 *   `description` varchar(255) DEFAULT NULL,
 *   `passive` varchar(255) DEFAULT NULL,
 *   `img_url` varchar(255) DEFAULT NULL,
 *   PRIMARY KEY (`equip_id`)
 * );
 */
public class Equip {
    private int equip_id;
    private String name;
    private String equip_type;
    private int selling_price;
    private int purchase_price;
    private String description;
    private String passive;
    private String img_url;

    public Equip(int equip_id, String name, String equip_type, int selling_price, int purchase_price, String description, String passive, String img_url) {
        this.equip_id =equip_id;
        this.name = name;
        this.equip_type = equip_type;
        this.selling_price = selling_price;
        this.purchase_price = purchase_price;
        this.description = description;
        this.passive = passive;
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public int getEquip_id() {
        return equip_id;
    }

    public String getEquip_type() {
        return equip_type;
    }

    public int getPurchase_price() {
        return purchase_price;
    }

    public int getSelling_price() {
        return selling_price;
    }

    public String getDescription() {
        return description;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getPassive() {
        return passive;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEquip_id(int equip_id) {
        this.equip_id = equip_id;
    }

    public void setEquip_type(String equip_type) {
        this.equip_type = equip_type;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setPassive(String passive) {
        this.passive = passive;
    }

    public void setPurchase_price(int purchase_price) {
        this.purchase_price = purchase_price;
    }

    public void setSelling_price(int selling_price) {
        this.selling_price = selling_price;
    }
}
