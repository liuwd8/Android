package com.example.liuyh73.glory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.media.audiofx.DynamicsProcessing;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RelativeLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MyDB extends SQLiteOpenHelper {
    private Context mContext;
//    private static MyDB myDB;
//    public MyDB getInstance() {
//        if (myDB == null) {
//
//        }
//    }

    public MyDB(Context context) {
        super(context, Configuration.DB_NAME, null, Configuration.DB_VERSION);
        mContext = context;
        // TODO Auto-generated constructor stub
    }

    /**
     * 第一次调用 getWritableDatabase() 或 getReadableDatabase() 时调用
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        executeAssetsSQL(sqLiteDatabase, "tables.sql");
        executeAssetsSQL(sqLiteDatabase, "heros.sql");
        executeAssetsSQL(sqLiteDatabase, "equips.sql");
        executeAssetsSQL(sqLiteDatabase, "heroEquips.sql");
        executeAssetsSQL(sqLiteDatabase, "skills.sql");
        executeAssetsSQL(sqLiteDatabase, "skins.sql");
        executeAssetsSQL(sqLiteDatabase, "presents.sql");
        executeAssetsSQL(sqLiteDatabase, "relations.sql");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int ii) {
        /**
         * 更新数据库版本（这里不使用）
         * db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
         * onCreate(db);
         */
    }

    /**
     * 读取数据库文件（.sql），并执行sql语句
     */
    private void executeAssetsSQL(SQLiteDatabase db, String schemaName) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(mContext.getAssets()
                    .open(Configuration.DB_PATH + "/" + schemaName)));

            System.out.println("路径:" + Configuration.DB_PATH + "/" + schemaName);
            String line;
            String buffer = "";
            while ((line = in.readLine()) != null) {
                buffer += line;
                if (line.trim().endsWith(";")) {
                    db.execSQL(buffer.replace(";", ""));
                    buffer = "";
                }
            }
        } catch (IOException e) {
            Log.e("db-error", e.toString());
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                Log.e("db-error", e.toString());
            }
        }
    }

    /**
     * 构造函数参数属性：
     *  int hero_id;            // 英雄id
     *  String name;            // 英雄名字
     *  String title;           // 人物称号
     *  String hero_type;       // 分类
     *  String hero_type2;      // 分类2
     *  String skin_name;       // 皮肤名称
     *  String avaterUrl;       // 人物头像url
     *  int live;               // 生存能力值
     *  int attack;             // 攻击伤害值
     *  int skill               // 技能效果
     *  int difficulty;         // 上手难度值
     * 其他属性：
     *  List<Skill> skills;         // 技能
     *  List<HeroEquip> heroEquips; // 推荐装备
     *  List<Skin> skins;      // 皮肤
     * @param name
     * @return
     */
    public Hero getHeroInfoByName(String name) {
        Hero hero = null;
        SQLiteDatabase db = getReadableDatabase();
        String heroSelection = "name = ?";
        String[] heroSelectionArgs = { name };
        Cursor cursor =db.query(Configuration.HERO_TABLE_NAME, null, heroSelection,heroSelectionArgs, null, null, null);
        if(cursor.moveToNext()){
            hero = new Hero(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(6).split("\\|")[0],
                    Configuration.HERO_TYPE[cursor.getInt(4)],
                    Configuration.HERO_TYPE[cursor.getInt(5)],
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getInt(8),
                    cursor.getInt(9),
                    cursor.getInt(10),
                    cursor.getInt(11)
            );
        }
        List<HeroEquip>heroEquipList = getHeroEquipsByHeroId(hero.getHero_id());
        hero.setHeroEquips(heroEquipList);
        List<Skill>skillList = getSkillsByHeroId(hero.getHero_id());
        hero.setSkills(skillList);
        List<Skin>skinList = getSkinByHeroId(hero.getHero_id());
        hero.setSkins(skinList);
        cursor.close();
        return hero;
    }

    public List<HeroEquip> getHeroEquipsByHeroId(int hero_id) {
        SQLiteDatabase db = getReadableDatabase();
        String heroEquipSelection = "hero_id = ?";
        String [] heroEquipSelectionArgs = {String.valueOf(hero_id)};
        Cursor cursor = db.query(Configuration.HERO_EQUIP_TABLE_NAME, null, heroEquipSelection, heroEquipSelectionArgs, null, null, null);
        List<HeroEquip> heroEquipList = new ArrayList<>();
        if(cursor.moveToNext()) {
            String []recommend1 = cursor.getString(1).split(",");
            String description1 = cursor.getString(2);
            HeroEquip heroEquip1 = new HeroEquip();
            for(int i=0; i<recommend1.length; i++) {
                heroEquip1.setEquip(i+1, getEquipByEquipId(Integer.parseInt(recommend1[i])));
            }
            heroEquip1.setDescription(description1);
            heroEquipList.add(heroEquip1);
            String []recommend2 = cursor.getString(3).split(",");
            String description2 = cursor.getString(4);
            HeroEquip heroEquip2 = new HeroEquip();
            for(int i=0; i<recommend2.length; i++) {
                heroEquip2.setEquip(i+1, getEquipByEquipId(Integer.parseInt(recommend2[i])));
            }
            heroEquip2.setDescription(description2);
            heroEquipList.add(heroEquip2);
        }
        cursor.close();
        return heroEquipList;
    }

    /**
     *  int equip_id;
     *  String name;
     *  int equip_type;
     *  int selling_price;
     *  int purchase_price;
     *  String description;
     *  String passive;
     *  String img_url;
     * @param equip_id
     * @return
     */
    public Equip getEquipByEquipId(int equip_id) {
        SQLiteDatabase db = getReadableDatabase();
        String equipSelection = "equip_id = ?";
        String [] equipSelectionArgs = {String.valueOf(equip_id)};
        Cursor cursor = db.query(Configuration.EQUIP_TABLE_NAME, null, equipSelection, equipSelectionArgs, null, null, null);
        Equip equip = null;
        if(cursor.moveToNext()){
            equip = new Equip(
                    cursor.getInt(0),
                    cursor.getString(1),
                    Configuration.EQUIP_TYPE[cursor.getInt(2)],
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7)
            );
        }
        cursor.close();
        return equip;
    }

    /**
     * int skill_id;   // 技能id
     * String name;    // 技能名称
     * int cool;       // 冷却时间
     * int waste;      // 消耗蓝
     * String description; // 技能描述
     * String tips;       // 小技巧
     * String effectUrl;
     * @param hero_id
     * @return
     */
    public List<Skill> getSkillsByHeroId(int hero_id) {
        SQLiteDatabase db = getReadableDatabase();
        String equipSelection = "hero_id = ?";
        String [] equipSelectionArgs = {String.valueOf(hero_id)};
        Cursor cursor = db.query(Configuration.SKILL_TABLE_NAME, null, equipSelection, equipSelectionArgs, null, null, null);
        List<Skill> skillList = new ArrayList<>();
        while(cursor.moveToNext()) {
            skillList.add(new Skill(
                    cursor.getInt(0),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7)
            ));
            //System.out.println("**************"+skillList.get(skillList.size()-1).getEffectUrl());
        }
        cursor.close();
        return skillList;
    }

    public Skill getSkillBySkillId(int skill_id) {
        SQLiteDatabase db = getReadableDatabase();
        String equipSelection = "skill_id = ?";
        String [] equipSelectionArgs = {String.valueOf(skill_id)};
        Cursor cursor = db.query(Configuration.SKILL_TABLE_NAME, null, equipSelection, equipSelectionArgs, null, null, null);
        Skill skill = null;
        if(cursor.moveToNext()) {
            skill = new Skill(
                    cursor.getInt(0),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7)
            );
        }
        cursor.close();
        return skill;
    }

    /**
     *  int hero_id;
     *  String name;
     *  String img_url;
     * @param hero_id
     * @return
     */
    public List<Skin> getSkinByHeroId(int hero_id) {
        SQLiteDatabase db = getReadableDatabase();
        String skinSelection = "hero_id = ?";
        String [] skinSelectionArgs = {String.valueOf(hero_id)};
        Cursor cursor = db.query(Configuration.SKIN_TABLE_NAME, null, skinSelection, skinSelectionArgs, null, null, null);
        List<Skin> skinList = new ArrayList<>();
        while(cursor.moveToNext()){
            skinList.add(new Skin(
                hero_id,
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4)
            ));
        }
        cursor.close();
        return skinList;
    }

    private List<Hero> getNotPresentHeros(boolean limit) {
        SQLiteDatabase db =getReadableDatabase();
        String sql = "select hero.name from hero where not exists(select 1 from present where hero.hero_id = present.hero_id);";
        if(limit) {
            sql += "order by random() limit 10";
        }
        Cursor cursor = db.rawQuery(sql, null);
        List<Hero>heroList = new ArrayList<>();
        Hero hero = null;
        System.out.println("Count: " + cursor.getCount());
        while(cursor.moveToNext()){
            hero = getHeroInfoByName(cursor.getString(0));
            heroList.add(hero);
        }
        return heroList;
    }

    public List<Hero> getAllNotPresentHeros() {
        return getNotPresentHeros(false);
    }

    public List<Hero> getSomeNotPresentHeros() {
        return getNotPresentHeros(true);
    }

    public List<Hero> getAllPresentHeros() {
        SQLiteDatabase db =getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from present", null);
        List<Hero>heroList = new ArrayList<>();
        Hero hero = null;
        while(cursor.moveToNext()){
            hero = getHeroInfoByName(cursor.getString(1));
            heroList.add(hero);
        }
        return heroList;
    }

    public long insertPresentHero(Hero hero) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hero_id", hero.getHero_id());
        contentValues.put("name", hero.getName());
        contentValues.put("hero_type", hero.getHero_type());
        // insert返回插入条目的位置，即ID值
        long rid = 0;
        try {
            rid = db.insert(Configuration.PRESENT_TABLE_NAME, null, contentValues);
        } catch(Exception e) {
        }
        return rid;
    }

    // 返回删除数据的数量
    public long deletePresentHero(Hero hero) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = "hero_id = ?";
        String[] selectionArgs = { String.valueOf(hero.getHero_id())};
        int count = db.delete(Configuration.PRESENT_TABLE_NAME, selection, selectionArgs);
        return count;
    }

    // 更新hero
    public int updateHero(Hero hero) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = "hero_id = ?";
        String[] selectionArgs = { String.valueOf(hero.getHero_id()) };
        ContentValues contentValues = new ContentValues();
        contentValues.put("live", hero.getLive());
        contentValues.put("attack", hero.getAttack());
        contentValues.put("attack", hero.getDifficulty());
        int count = db.update(Configuration.HERO_TABLE_NAME, contentValues, selection, selectionArgs);
        return count;
    }

    // 根据类别获取英雄
    public List<String> getHerosNameByType(String type) {
        SQLiteDatabase db = getReadableDatabase();
        String []columns = {"name"};
        String selection = "hero_type = ?";
        String[] selectionArgs = { type };
        System.out.println();
        Cursor cursor = db.query(Configuration.PRESENT_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        List<String> herosName = new ArrayList<>();
        while(cursor.moveToNext()){
            herosName.add(cursor.getString(0));
        }
        return herosName;
    }

    /**
     *  int equip_id;
     *  String name;
     *  String equip_type;
     *  int selling_price;
     *  int purchase_price;
     *  String description;
     *  String passive;
     *  String img_url;
     * @param type
     * @return
     */
    // 根据类别获取装备
    public List<Equip> getEquipsByType(String type) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = "equip_type = ?";
        String[] selectionArgs = { type };
        System.out.println();
        Cursor cursor = db.query(Configuration.EQUIP_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        List<Equip> equips = new ArrayList<>();
        while(cursor.moveToNext()){
            equips.add(new Equip(
                cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7)
            ));
        }
        return equips;
    }
    /**
     *  int hero_id;
     *  String name;
     *  String img_url;
     *  String avatar_url;
     */
    // 根据皮肤名称获取皮肤
    public Skin getSkinByName(String name) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = "name = ?";
        String[] selectionArgs = { name };
        System.out.println();
        Cursor cursor = db.query(Configuration.SKIN_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        Skin skin = null;
        while(cursor.moveToNext()){
            skin = new Skin(
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
        }
        return skin;
    }

    /**
     * int hero_id;
     * String name;
     * String partner1;
     * String partner1description;
     * String partner2;
     * String partner2description;
     * String repress1;
     * String repress1description;
     * String repress2;
     * String repress2description;
     * String repressed1;
     * String repressed1description;
     * String repressed2;
     * String repressed2description;
     */
    public Relation getRelationsByHeroName(String name){
        SQLiteDatabase db = getReadableDatabase();
        String selection = "name = ?";
        String []selectionArgs = {name};
        Relation relation = null;
        Cursor cursor = db.query(Configuration.RELATION_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if(cursor.moveToNext()){
            relation = new Relation(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getString(11),
                    cursor.getString(12),
                    cursor.getString(13)
            );
        }
        return relation;
    }

    public Relation getRelationsByHeroId(int hero_id){
        SQLiteDatabase db =getReadableDatabase();
        String[] columns = {"name"};
        String selection = "hero_id = ?";
        String[] selectionArgs = {String.valueOf(hero_id)};
        Cursor cursor = db.query(Configuration.HERO_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        Relation relation = null;
        if(cursor.moveToNext()){
            relation = getRelationsByHeroName(cursor.getString(0));
        }
        return relation;
    }
}