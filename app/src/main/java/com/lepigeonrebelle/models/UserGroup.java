package com.lepigeonrebelle.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = UserGroup.TABLE_NAME_USER_GROUP)
public class UserGroup {

    public static final String TABLE_NAME_USER_GROUP = "User_Group";

    public static final String FIELD_NAME_USER_ID = "idUser";
    public static final String FIELD_NAME_GROUP_ID = "idGroup";
    public static final String FIELD_NAME_BUDGET = "budget";

    @DatabaseField(foreign = true, columnName = FIELD_NAME_USER_ID)
    private User user;

    @DatabaseField(foreign = true, columnName = FIELD_NAME_GROUP_ID)
    private Group group;

    @DatabaseField(columnName = FIELD_NAME_BUDGET)
    private Double budget;

    public UserGroup() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }
}
