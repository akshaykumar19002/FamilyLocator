package com.kumar.akshay.familylocator.MessageClasses;

public class GroupMessage {

    String groupid, groupname, groupAdmin, usersInGroup;

    public GroupMessage(){}

    public GroupMessage(String groupid, String groupname, String groupAdmin, String usersInGroup) {
        this.groupid = groupid;
        this.groupname = groupname;
        this.groupAdmin = groupAdmin;
        this.usersInGroup = usersInGroup;
    }

    public GroupMessage(String groupname, String groupAdmin, String usersInGroup) {
        this.groupname = groupname;
        this.groupAdmin = groupAdmin;
        this.usersInGroup = usersInGroup;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getGroupAdmin() {
        return groupAdmin;
    }

    public void setGroupAdmin(String groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    public String getUsersInGroup() {
        return usersInGroup;
    }

    public void setUsersInGroup(String usersInGroup) {
        this.usersInGroup = usersInGroup;
    }
}
