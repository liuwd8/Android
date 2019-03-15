package com.example.liuyh73.glory;

import android.graphics.Bitmap;
import android.media.audiofx.DynamicsProcessing;

import java.lang.reflect.Field;
public class HeroEquip {
    private Equip equip1;
    private Equip equip2;
    private Equip equip3;
    private Equip equip4;
    private Equip equip5;
    private Equip equip6;
    private String description;
    public HeroEquip() {}

    public HeroEquip(Equip equip1, Equip equip2, Equip equip3, Equip equip4, Equip equip5, Equip equip6, String description) {
        this.description = description;
        this.equip1 = equip1;
        this.equip2 = equip2;
        this.equip3 = equip3;
        this.equip4 = equip4;
        this.equip5 = equip5;
        this.equip6 = equip6;
    }

    public void setEquip(int index, Equip _equip){
        String fieldName = "equip"+index;
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            field.set(this, _equip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Equip getEquip(int index) {
        String fieldName = "equip"+index;
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            return (Equip)field.get(this);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getDescription() {return this.description;}

    public void setDescription(String description) {
        this.description = description;
    }
}
