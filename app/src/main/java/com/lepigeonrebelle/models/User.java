package com.lepigeonrebelle.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = User.TABLE_NAME_USER)
public class User {

    public static final String TABLE_NAME_USER = "User";

    public static final String FIELD_NAME_ID = "idUser";
    public static final String FIELD_NAME_NAME = "name";
    public static final String FIELD_NAME_IS_DEFAULT_USER = "isDefaultUser";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAME_NAME)
    private String name;

    @DatabaseField(columnName = FIELD_NAME_IS_DEFAULT_USER)
    private int isDefaultUser;

    public User() {
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

    public int isDefaultUser() {
        return isDefaultUser;
    }

    public void setDefaultUser(int defaultUser) {
        isDefaultUser = defaultUser;
    }
}
