package com.aahho.anno.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by souvikdas on 31/10/17.
 */

@IgnoreExtraProperties
public class Upload {
    private String contentType;
    private String extension;
    private String id;
    private String name;
    private String originalName;
    private String path;
    private String selfLink;
    private int size;
    private String tempLink;
    private String title;
    private WebViewLinks webViewLinks;

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

    public String getOriginal_name() {
        return originalName;
    }

    public void setOriginal_name(String original_name) {
        this.originalName = original_name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSelf_link() {
        return selfLink;
    }

    public void setSelf_link(String self_link) {
        this.selfLink = self_link;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getTemp_link() {
        return tempLink;
    }

    public void setTemp_link(String temp_link) {
        this.tempLink = temp_link;
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
}
