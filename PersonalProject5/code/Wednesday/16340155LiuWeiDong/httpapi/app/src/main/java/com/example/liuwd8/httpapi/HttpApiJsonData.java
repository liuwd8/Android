package com.example.liuwd8.httpapi;

public class HttpApiJsonData {
    private MidResponseData midResponseData;
    private AidResponseData aidResponseData;

    public void setAidResponseData(AidResponseData aidResponseData) {
        this.aidResponseData = aidResponseData;
    }

    public void setMidResponseData(MidResponseData midResponseData) {
        this.midResponseData = midResponseData;
    }

    public AidResponseData getAidResponseData() {
        return aidResponseData;
    }

    public MidResponseData getMidResponseData() {
        return midResponseData;
    }
}
