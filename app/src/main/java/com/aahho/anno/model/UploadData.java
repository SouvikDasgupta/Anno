package com.aahho.anno.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by souvikdas on 27/10/17.
 */
public class UploadData implements Serializable {

    @SerializedName("contentType")
    @Expose
    private String contentType;

    @SerializedName("extension")
    private String extension;

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("originalName")
    @Expose
    private String originalName;

    @SerializedName("path")
    private String path;

    @SerializedName("selfLink")
    private String selfLink;

    @SerializedName("size")
    private int size;

    @SerializedName("tempLink")
    @Expose
    private String tempLink;

    @SerializedName("title")
    private String title;

    @SerializedName("webViewLinks")
    private WebViewLinks webViewLinks;


    //Getters and Setters


    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getTempLink() {
        return tempLink;
    }

    public void setTempLink(String tempLink) {
        this.tempLink = tempLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public WebViewLinks getWebViewLinks() {
        return webViewLinks;
    }

    public void setWebViewLinks(WebViewLinks webViewLinks) {
        this.webViewLinks = webViewLinks;
    }
}
