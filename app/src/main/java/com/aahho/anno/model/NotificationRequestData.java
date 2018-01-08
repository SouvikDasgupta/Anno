package com.aahho.anno.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by souvikdas on 30/10/17.
 */

public class NotificationRequestData implements Serializable{

    @SerializedName("messageTitle")
    private String messageTitle;

    @SerializedName("messageBody")
    private String messageBody;

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
}
