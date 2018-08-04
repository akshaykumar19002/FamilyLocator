package com.kumar.akshay.familylocator.MessageClasses;

public class UserMessage {

    private String username, uid, groupsToWhichUserBelongs, userEmail, userPass;

    public UserMessage(String username, String uid, String groupsToWhichUserBelongs, String userEmail) {
        this.username = username;
        this.uid = uid;
        this.groupsToWhichUserBelongs = groupsToWhichUserBelongs;
        this.userEmail = userEmail;
    }

    public UserMessage(String uid, String username, String userEmail) {
        this.username = username;
        this.uid = uid;
        this.userEmail = userEmail;
    }

    public UserMessage() {
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGroupsToWhichUserBelongs() {
        return groupsToWhichUserBelongs;
    }

    public void setGroupsToWhichUserBelongs(String groupsToWhichUserBelongs) {
        this.groupsToWhichUserBelongs = groupsToWhichUserBelongs;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
