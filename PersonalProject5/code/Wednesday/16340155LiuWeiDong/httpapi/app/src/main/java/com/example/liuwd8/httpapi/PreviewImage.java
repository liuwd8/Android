package com.example.liuwd8.httpapi;

public class PreviewImage {
    private String pvdata;
    private int img_x_len;
    private int img_y_len;
    private int img_x_size;
    private int img_y_size;
    private String[] image;
    private int[] index;

    public void setImage(String[] image) {
        this.image = image;
    }

    public void setIndex(int[] index) {
        this.index = index;
    }

    public void setPvdata(String pvdata) {
        this.pvdata = pvdata;
    }

    public void setImg_x_len(int img_x_len) {
        this.img_x_len = img_x_len;
    }

    public void setImg_x_size(int img_x_size) {
        this.img_x_size = img_x_size;
    }

    public void setImg_y_len(int img_y_len) {
        this.img_y_len = img_y_len;
    }

    public void setImg_y_size(int img_y_size) {
        this.img_y_size = img_y_size;
    }

    public int[] getIndex() {
        return index;
    }

    public int getImg_x_len() {
        return img_x_len;
    }

    public int getImg_x_size() {
        return img_x_size;
    }

    public int getImg_y_len() {
        return img_y_len;
    }

    public int getImg_y_size() {
        return img_y_size;
    }

    public String getPvdata() {
        return pvdata;
    }

    public String[] getImage() {
        return image;
    }
}
