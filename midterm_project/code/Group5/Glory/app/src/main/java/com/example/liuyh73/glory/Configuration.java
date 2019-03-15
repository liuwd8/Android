package com.example.liuyh73.glory;

public class Configuration {
    public static final String DB_PATH = "schema";
    public static final String DB_NAME = "glory.db";
    public static final int DB_VERSION = 1;
    public static int oldVersion = -1;
    public static final String HERO_TABLE_NAME = "hero";
    public static final String EQUIP_TABLE_NAME = "equip";
    public static final String HERO_EQUIP_TABLE_NAME = "hero_equip";
    public static final String SKILL_TABLE_NAME = "skill";
    public static final String SKIN_TABLE_NAME = "skin";
    public static final String PRESENT_TABLE_NAME = "present";
    public static final String RELATION_TABLE_NAME = "relation";
    public static final String[] HERO_TYPE = { "", "战士", "法师", "坦克", "刺客", "射手", "辅助" };
    public static final String[] EQUIP_TYPE = { "", "攻击", "法术", "防御", "移动", "打野", "", "辅助" };
}
