package com.aahho.anno.model;

import com.aahho.anno.api.ServiceAPI;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by souvikdas on 30/10/17.
 */

public class NotificationResponse implements Serializable {

    @SerializedName("data")
    private NotificationResponseData data;

    public NotificationResponseData getData() {
        return data;
    }

    public void setData(NotificationResponseData data) {
        this.data = data;
    }
}
