package com.lepigeonrebelle.model;

public class User {

    private int id;
    private String name;
    private Boolean isDefaultUser;

    public User(int id, String name, Boolean isDefaultUser) {
        this.id = id;
        this.name = name;
        this.isDefaultUser = isDefaultUser;
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

    public Boolean isDefaultUser() {
        return isDefaultUser;
    }

    public void setDefaultUser(Boolean defaultUser) {
        isDefaultUser = defaultUser;
    }
}
