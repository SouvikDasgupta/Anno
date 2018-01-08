package com.aahho.anno.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by souvikdas on 30/10/17.
 */

public class NotificationRequest {
    @SerializedName("data")
    private NotificationRequestData data;

    @SerializedName("messageTitle")
    private String messageTitle;

    @SerializedName("messageBody")
    private String messageBody;

    @SerializedName("deviceIds")
    private ArrayList<String> deviceIds;

    public NotificationRequestData getData() {
        return data;
    }

    public void setData(NotificationRequestData data) {
        this.data = data;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public ArrayList<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(ArrayList<String> deviceIds) {
        this.deviceIds = deviceIds;
    }
}
