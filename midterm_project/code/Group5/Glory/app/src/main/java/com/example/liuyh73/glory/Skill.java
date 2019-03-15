package com.example.liuyh73.glory;

import android.graphics.Bitmap;

public class Skill {
    private int skill_id;   // 技能id
    private String name;    // 技能名称
    private int cool;       // 冷却时间
    private int waste;      // 消耗蓝
    private String description; // 技能描述
    private String tips;       // 小技巧
    private String effectUrl;

    public Skill(int _skill_id, String _name, int _cool, int _waste, String _description, String _tips, String _effectUrl) {
        skill_id=_skill_id;
        name = _name;
        cool = _cool;
        waste = _waste;
        description = _description;
        tips = _tips;
        effectUrl = _effectUrl;
    }

    public String getName(){ return name; }
    public int getCool() { return cool; }
    public int getWaste() { return waste; }
    public String getDescription() { return description; }
    public String getTips() { return tips; }
    public String getEffectUrl() {return effectUrl;}

    public void setCool(int cool) {
        this.cool = cool;
    }
    public void setWaste(int waste) {
        this.waste = waste;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setTips(String tips) {
        this.tips = tips;
    }
}
