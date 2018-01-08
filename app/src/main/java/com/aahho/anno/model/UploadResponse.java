package com.aahho.anno.model;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by souvikdas on 27/10/17.
 */

public class UploadResponse implements Serializable {
    @SerializedName("data")
    private ArrayList<UploadData> data;

    @SerializedName("notification")
    private UploadNotification notification;

    public ArrayList<UploadData> getData() {
        return data;
    }

    public void setData(ArrayList<UploadData> data) {
        this.data = data;
    }

    public UploadNotification getNotification() {
        return notification;
    }

    public void setNotification(UploadNotification notification) {
        this.notification = notification;
    }
}
