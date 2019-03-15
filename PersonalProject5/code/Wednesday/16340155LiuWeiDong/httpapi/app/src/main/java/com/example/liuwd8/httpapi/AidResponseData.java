package com.example.liuwd8.httpapi;

public class AidResponseData {
    private int code;
    private String message;
    private int ttl;
    private PreviewImage data;

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public void setPreviewImage(PreviewImage previewImage) {
        this.data = previewImage;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public PreviewImage getPreviewImage() {
        return data;
    }

    public int getTtl() {
        return ttl;
    }

    public int getCode() {
        return code;
    }
}
