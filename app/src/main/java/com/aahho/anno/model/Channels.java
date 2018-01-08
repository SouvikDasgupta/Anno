package com.aahho.anno.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by souvikdas on 25/9/17.
 */

public class Channels implements Serializable {
    private String channelId;
    private String initiator;
    private ArrayList<Users> involved = new ArrayList<>();
    private ArrayList<Message> messages = new ArrayList<>();
    private long lastMessageTime;


    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public ArrayList<Users> getInvolved() {
        return involved;
    }

    public void setInvolved(ArrayList<Users> involved) {
        this.involved = involved;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}
