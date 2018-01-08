package com.aahho.anno.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by souvikdas on 1/11/17.
 */

public class WebViewLinks implements Serializable {

    @SerializedName("large")
    private String large;

    @SerializedName("medium")
    private String medium;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("thumbnail")
    private String thumbnail;

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
