package com.aahho.anno.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by souvikdas on 13/9/17.
 */


@IgnoreExtraProperties
public class Users implements Serializable{

    private String id;
    private String username;
    private String password;
    private String status;
    private String userCode;
    private String anonymousName;
    private String fcmToken;
    private String dpId;
    private ArrayList<String> interestedIn;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Users(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnonymousName() {
        return anonymousName;
    }

    public void setAnonymousName(String anonymousName) {
        this.anonymousName = anonymousName;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getDpId() {
        return dpId;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    public ArrayList<String> getInterestedIn() {
        return interestedIn;
    }

    public void setInterestedIn(ArrayList<String> interestedIn) {
        this.interestedIn = interestedIn;
    }
}
