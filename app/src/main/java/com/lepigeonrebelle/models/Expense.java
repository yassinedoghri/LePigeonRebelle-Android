package com.lepigeonrebelle.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.List;

@DatabaseTable(tableName = Expense.TABLE_NAME_EXPENSE)
public class Expense {

    public static final String TABLE_NAME_EXPENSE = "Expense";

    public static final String FIELD_NAME_ID = "idExpense";
    public static final String FIELD_NAME_DESCRIPTION = "description";
    public static final String FIELD_NAME_DATE = "date";
    public static final String FIELD_NAME_COMMENT = "comment";
    public static final String FIELD_NAME_IS_TODO = "isTodo";

    public static final String FIELD_NAME_GROUP = "idGroup";
    public static final String FIELD_NAME_CATEGORY = "idCategory";
    public static final String FIELD_NAME_TYPE = "idType";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAME_DESCRIPTION)
    private String description;

    @DatabaseField(columnName = FIELD_NAME_DATE)
    private Date date;

    @DatabaseField(columnName = FIELD_NAME_COMMENT)
    private String comment;

    @DatabaseField(columnName = FIELD_NAME_IS_TODO)
    private int isTodo;

    @DatabaseField(columnName = FIELD_NAME_GROUP, foreign = true, foreignAutoRefresh = true)
    private Group group;

    @DatabaseField(columnName = FIELD_NAME_CATEGORY, foreign = true, foreignAutoRefresh = true)
    private ExpenseCategory category;

    @DatabaseField(columnName = FIELD_NAME_TYPE, foreign = true, foreignAutoRefresh = true)
    private ExpenseType type;

    private List<Debt> debts;

    public Expense() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int isTodo() {
        return isTodo;
    }

    public void setTodo(int isTodo) {
        this.isTodo = isTodo;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public ExpenseType getType() {
        return type;
    }

    public void setType(ExpenseType type) {
        this.type = type;
    }

    public List<Debt> getDebts() {
        return debts;
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }
}
