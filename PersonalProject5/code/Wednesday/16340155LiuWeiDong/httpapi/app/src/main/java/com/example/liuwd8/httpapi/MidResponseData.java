package com.example.liuwd8.httpapi;

public class MidResponseData {
    private Boolean status;
    private Data data;

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public Boolean getStatus() {
        return status;
    }
}
