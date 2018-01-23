package com.lepigeonrebelle.models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = ExpenseType.TABLE_NAME_EXPENSE_TYPE)
public class ExpenseType {

    public static final String TABLE_NAME_EXPENSE_TYPE = "ExpenseType";

    public static final String FIELD_NAME_ID = "idExpenseType";
    public static final String FIELD_NAME_WORDING = "wording";
    public static final String FIELD_NAME_EXPENSES = "expenses";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAME_WORDING)
    private String wording;

    // One-to-many
    @ForeignCollectionField(columnName = FIELD_NAME_EXPENSES, eager = true)
    private ForeignCollection<Expense> expenses;

    public ExpenseType() {
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

    public ForeignCollection<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(ForeignCollection<Expense> expenses) {
        this.expenses = expenses;
    }

    @Override
    public String toString() {
        return wording;
    }
}
