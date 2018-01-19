package com.lepigeonrebelle.model;

public class GroupType {

    private int id;
    private String wording;
    private String icon;

    public GroupType(int id, String wording, String icon) {
        this.id = id;
        this.wording = wording;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWording() {
        return wording;
    }

    public void setWording(String wording) {
        this.wording = wording;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
