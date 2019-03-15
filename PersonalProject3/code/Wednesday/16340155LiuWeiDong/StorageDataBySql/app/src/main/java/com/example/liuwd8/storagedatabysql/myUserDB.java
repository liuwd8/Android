package com.example.liuwd8.storagedatabysql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class myUserDB extends SQLiteOpenHelper {
    public static final String DB_NAME = "data.db";
    private static final int DB_VRESION = 1;
    public static final String TABLE_NAME = "user";
    public static final String CREATE_TABLE = "CREATE TABLE if not exists " + TABLE_NAME + " (username text primary key not null, password text not null, likeList BLOB default null,image BLOB default null)";

    public myUserDB(Context c) {
        super(c, DB_NAME, null, DB_VRESION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(myDB.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int ii) {

    }

    public long save(UserData userData) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", userData.getUsername());
        contentValues.put("password", userData.getPassword());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        userData.getAvatar().compress(Bitmap.CompressFormat.PNG, 100,byteArrayOutputStream);
        contentValues.put("image", byteArrayOutputStream.toByteArray());
        long rid = database.insert(TABLE_NAME, null, contentValues);
        database.close();
        return rid;
    }

    public UserData queryByUsername(String username) {
        UserData userData = null;
        SQLiteDatabase database = getReadableDatabase();
        String selection = "username = ?";
        String[] selectionArgs = {username};
        Cursor c = database.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if (c.moveToNext()) {
            byte[] bytes = c.getBlob(c.getColumnIndex("image"));
            byte[] arrayListBytes = c.getBlob(c.getColumnIndex("likeList"));
            ArrayList<Integer> arrayList = new ArrayList<>();
            if (arrayListBytes != null) {
                try (ByteArrayInputStream bais = new ByteArrayInputStream(arrayListBytes);
                     DataInputStream in = new DataInputStream(bais)){
                    while (in.available() > 0) {
                        arrayList.add(in.readInt());
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
                Log.i(TAG, "queryByUsername: " + arrayListBytes.length);
            }
            userData = new UserData(c.getString(c.getColumnIndex("username")), c.getString(c.getColumnIndex("password")), BitmapFactory.decodeByteArray(bytes, 0, bytes.length), arrayList);
        }
        c.close();
        database.close();
        return userData;
    }

    public int updateLikeList(String username, ArrayList<Integer> array) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)) {
            for (Integer i : array) {
                dataOutputStream.writeInt(i);
            }
            contentValues.put("likeList", byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            return 0;
        }
        String whereClause = "username = ?";
        String[] whereArgs = {username};
        int rowNums = sqLiteDatabase.update(TABLE_NAME, contentValues, whereClause, whereArgs);
        sqLiteDatabase.close();
        return rowNums;
    }
}
