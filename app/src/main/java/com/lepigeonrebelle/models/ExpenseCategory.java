package com.lepigeonrebelle.models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = ExpenseCategory.TABLE_NAME_EXPENSE_CATEGORY)
public class ExpenseCategory {

    public static final String TABLE_NAME_EXPENSE_CATEGORY = "ExpenseCategory";

    public static final String FIELD_NAME_ID = "idExpenseCategory";
    public static final String FIELD_NAME_WORDING = "wording";
    public static final String FIELD_NAME_ICON = "icon";
    public static final String FIELD_NAME_EXPENSES = "expenses";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAME_WORDING)
    private String wording;

    @DatabaseField(columnName = FIELD_NAME_ICON)
    private String icon;

    // One-to-many
    @ForeignCollectionField(columnName = FIELD_NAME_EXPENSES, eager = true)
    private ForeignCollection<Expense> expenses;

    public ExpenseCategory() {
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

    public ForeignCollection<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(ForeignCollection<Expense> expenses) {
        this.expenses = expenses;
    }
}
