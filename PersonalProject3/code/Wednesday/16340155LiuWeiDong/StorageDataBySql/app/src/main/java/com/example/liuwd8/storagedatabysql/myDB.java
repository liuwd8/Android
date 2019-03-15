package com.example.liuwd8.storagedatabysql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;

public class myDB extends SQLiteOpenHelper {
    private static final int DB_VRESION = 1;
    private static final String TABLE_NAME = "data";
    public static final String CREATE_TABLE = "CREATE TABLE if not exists " + TABLE_NAME + " (_id integer primary key autoincrement, username text not null, comment text not null, date text not null, star integer default 0)";

    public myDB(Context c) {
        super(c, myUserDB.DB_NAME, null, DB_VRESION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(myUserDB.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int ii) {

    }

    public long save(CommentData commentData) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", commentData.getUsername());
        contentValues.put("comment", commentData.getComment());
        contentValues.put("date", commentData.getDate());
        contentValues.put("star", commentData.getStar());
        long rid = database.insert(TABLE_NAME, null, contentValues);
        database.close();
        return rid;
    }

    public ArrayList<CommentData> query() {
        SQLiteDatabase database = getWritableDatabase();
        String select_sql = "select f._id, f.username, f.comment, f.date, f.star, s.image from " + TABLE_NAME + " as f, " + myUserDB.TABLE_NAME + " as s where f.username = s.username";
        Cursor cursor = database.rawQuery(select_sql, new String[]{});
        ArrayList<CommentData> arrayList = new ArrayList<>();
        int usernameIndex = cursor.getColumnIndex("username");
        int commentIndex = cursor.getColumnIndex("comment");
        int dateIndex = cursor.getColumnIndex("date");
        int starIndex = cursor.getColumnIndex("star");
        int imageIndex = cursor.getColumnIndex("image");
        int idIndex = cursor.getColumnIndex("_id");
        while (cursor.moveToNext()) {
            byte[] bytes = cursor.getBlob(imageIndex);
            arrayList.add(new CommentData(cursor.getString(usernameIndex), cursor.getString(commentIndex), cursor.getString(dateIndex),BitmapFactory.decodeByteArray(bytes, 0, bytes.length), cursor.getInt(starIndex), cursor.getInt(idIndex)));
        }
        cursor.close();
        database.close();
        return arrayList;
    }

    public boolean updateStar(CommentData data) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("star", data.getStar());
        String whereClause = "_id = ?";
        String[] whereArgs = {String.valueOf(data.get_id())};
        int rowNum = sqLiteDatabase.update(TABLE_NAME, cv, whereClause, whereArgs);
        sqLiteDatabase.close();
        return rowNum > 0;
    }

    public boolean deleteComment(CommentData data) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = "_id = ?";
        String[] whereArgs={String.valueOf(data.get_id())};
        int rid = sqLiteDatabase.delete(TABLE_NAME, whereClause, whereArgs);
        sqLiteDatabase.close();
        return rid > 0;
    }
}
