package com.example.liuwd8.storagedatabysql;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class UserData {
    private String username;
    private String password;
    private Bitmap avatar;
    private ArrayList<Integer> arrayList;
    public static UserData current = null;

    public UserData(String _username, String _password, Bitmap _avatar, ArrayList<Integer> _arrayList) {
        username = _username;
        password = _password;
        avatar = _avatar;
        if (_arrayList == null) {
            arrayList = new ArrayList<>();
        } else {
            arrayList = _arrayList;
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setArrayList(ArrayList<Integer> arrayList) {
        this.arrayList = arrayList;
    }

    public String getUsername() {
        return username;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Integer> getArrayList() {
        return arrayList;
    }
}
