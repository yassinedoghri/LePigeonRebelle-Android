package com.lepigeonrebelle.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

@DatabaseTable(tableName = Group.TABLE_NAME_GROUP)
public class Group {

    public static final String TABLE_NAME_GROUP = "Group";

    public static final String FIELD_NAME_ID = "idGroup";
    public static final String FIELD_NAME_NAME = "name";
    public static final String FIELD_NAME_TYPE = "idType";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAME_NAME)
    private String name;

    @DatabaseField(columnName = FIELD_NAME_TYPE, foreign = true, foreignAutoRefresh = true)
    private GroupType type;

    private List<UserGroup> groupMembers;

    public Group() {
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

    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) {
        this.type = type;
    }

    public List<UserGroup> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<UserGroup> groupMembers) {
        this.groupMembers = groupMembers;
    }
}
