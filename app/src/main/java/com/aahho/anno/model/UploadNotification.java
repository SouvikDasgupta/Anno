package com.aahho.anno.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by souvikdas on 27/10/17.
 */

public class UploadNotification implements Serializable {

    @SerializedName("hint")
    private String hint;

    @SerializedName("message")
    private String message;

    @SerializedName("responseCode")
    private int responseCode;

    @SerializedName("type")
    private String type;

    //Getters and Setters


    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
