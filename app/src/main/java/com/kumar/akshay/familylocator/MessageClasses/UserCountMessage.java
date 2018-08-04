package com.kumar.akshay.familylocator.MessageClasses;

/**
 * Created by LENOVO on 10-07-2017.
 */

public class UserCountMessage {

    String user_count;

    public UserCountMessage(String user_count) {
        this.user_count = user_count;
    }

    public UserCountMessage(){}

    public String getUser_count() {
        return user_count;
    }

    public void setUser_count(String user_count) {
        this.user_count = user_count;
    }
}
