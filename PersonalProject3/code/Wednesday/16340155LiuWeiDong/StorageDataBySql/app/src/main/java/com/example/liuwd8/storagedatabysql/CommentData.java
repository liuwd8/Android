package com.example.liuwd8.storagedatabysql;

import android.graphics.Bitmap;

public class CommentData {
    private int _id;
    private String username;
    private String comment;
    private String date;
    private Bitmap avatar;
    private int star;

    public CommentData(String _username, String _comment, String _date, Bitmap _avatar, int _star, int __id) {
        _id = __id;
        username = _username;
        comment = _comment;
        avatar = _avatar;
        star = _star;
        date = _date;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public int getStar() {
        return star;
    }

    public String getDate() {
        return date;
    }

    public int get_id() {
        return _id;
    }
}
