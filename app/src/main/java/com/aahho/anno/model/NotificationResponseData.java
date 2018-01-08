package com.aahho.anno.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by souvikdas on 30/10/17.
 */

public class NotificationResponseData {
    @SerializedName("canonical_ids")
    @Expose
    private int canonicalIds;

    @SerializedName("failure")
    private int failure;

    @SerializedName("multicast_ids")
    @Expose
    private ArrayList<String> multicatIds;


    public int getCanonicalIds() {
        return canonicalIds;
    }

    public void setCanonicalIds(int canonicalIds) {
        this.canonicalIds = canonicalIds;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public ArrayList<String> getMulticatIds() {
        return multicatIds;
    }

    public void setMulticatIds(ArrayList<String> multicatIds) {
        this.multicatIds = multicatIds;
    }
}
