package com.lepigeonrebelle.models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = GroupType.TABLE_NAME_GROUP_TYPE)
public class GroupType {

    public static final String TABLE_NAME_GROUP_TYPE = "GroupType";

    public static final String FIELD_NAME_ID = "idGroupType";
    public static final String FIELD_NAME_WORDING = "wording";
    public static final String FIELD_NAME_ICON = "icon";
    public static final String FIELD_NAME_GROUPS = "groups";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAME_WORDING)
    private String wording;

    @DatabaseField(columnName = FIELD_NAME_ICON)
    private String icon;

    // One-to-many
    @ForeignCollectionField(columnName = FIELD_NAME_GROUPS, eager = true)
    private ForeignCollection<Group> groups;

    public GroupType() {
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

    public ForeignCollection<Group> getGroups() {
        return groups;
    }

    public void setGroups(ForeignCollection<Group> groups) {
        this.groups = groups;
    }
}
