package com.aahho.anno.model;

import java.io.Serializable;

/**
 * Created by souvikdas on 28/9/17.
 */

public class ChatList implements Serializable {

    private String lastMessage;
    private int unreadMessageCount;
    private Users otherUser;
    private Users testUser;


    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }


    public int getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public Users getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(Users otherUser) {
        this.otherUser = otherUser;
    }

    public Users getTestUser() {
        return testUser;
    }

    public void setTestUser(Users testUser) {
        this.testUser = testUser;
    }

    @Override
    public String toString() {
        return "ChatList{" +
                "lastMessage='" + lastMessage + '\'' +
                ", unreadMessageCount=" + unreadMessageCount +
                ", otherUser=" + testUser.getUsername() +
                '}';
    }
}
