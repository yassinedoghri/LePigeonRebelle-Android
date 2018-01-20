package com.lepigeonrebelle.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = Expense.TABLE_NAME_EXPENSE)
public class Expense {

    public static final String TABLE_NAME_EXPENSE = "Expense";

    public static final String FIELD_NAME_ID = "idExpense";
    public static final String FIELD_NAME_DESCRIPTION = "description";
    public static final String FIELD_NAME_DATE = "date";
    public static final String FIELD_NAME_COMMENT = "comment";

    public static final String FIELD_NAME_GROUP = "idGroup";
    public static final String FIELD_NAME_CATEGORY = "idCategory";
    public static final String FIELD_NAME_TYPE = "idType";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAME_DESCRIPTION)
    private String name;

    @DatabaseField(columnName = FIELD_NAME_DATE)
    private Date date;

    @DatabaseField(columnName = FIELD_NAME_COMMENT)
    private String comment;

    @DatabaseField(columnName = FIELD_NAME_GROUP, foreign = true, foreignAutoRefresh = true)
    private Group group;

    @DatabaseField(columnName = FIELD_NAME_CATEGORY, foreign = true, foreignAutoRefresh = true)
    private ExpenseCategory category;

    @DatabaseField(columnName = FIELD_NAME_TYPE, foreign = true, foreignAutoRefresh = true)
    private ExpenseType type;

    public Expense() {
    }

}
