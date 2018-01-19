package com.lepigeonrebelle.model;

import java.util.List;

public class Group {
    private int id;
    private String name;
    private GroupType groupType;
    private List<User> groupMembers;

    public Group(int id, String name, GroupType groupType, List<User> groupMembers) {
        this.id = id;
        this.name = name;
        this.groupType = groupType;
        this.groupMembers = groupMembers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }

    public List<User> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<User> groupMembers) {
        this.groupMembers = groupMembers;
    }
}
