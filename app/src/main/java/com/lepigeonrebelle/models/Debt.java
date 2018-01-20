package com.lepigeonrebelle.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Debt.TABLE_NAME_DEBT)
public class Debt {

    public static final String TABLE_NAME_DEBT = "Debt";

    public static final String FIELD_NAME_EXPENSE   = "idExpense";
    public static final String FIELD_NAME_ID_USER_PAYING = "idUserPaying";
    public static final String FIELD_NAME_ID_USER_OWING = "idUserOwing";
    public static final String FIELD_NAME_AMOUNT = "amount";

    @DatabaseField(columnName = FIELD_NAME_EXPENSE, foreign = true, foreignAutoRefresh = true)
    private Expense expense;

    @DatabaseField(foreign = true, columnName = FIELD_NAME_ID_USER_PAYING)
    private User userPaying;

    @DatabaseField(foreign = true, columnName = FIELD_NAME_ID_USER_OWING)
    private User userOwing;

    @DatabaseField(columnName = FIELD_NAME_AMOUNT)
    private Double amount;

    public Debt() {
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public User getUserPaying() {
        return userPaying;
    }

    public void setUserPaying(User userPaying) {
        this.userPaying = userPaying;
    }

    public User getUserOwing() {
        return userOwing;
    }

    public void setUserOwing(User userOwing) {
        this.userOwing = userOwing;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
