package com.lepigeonrebelle.model;

public class ExpenseType {

    private int id;
    private String wording;

    public ExpenseType(int id, String wording) {
        this.id = id;
        this.wording = wording;
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
}
