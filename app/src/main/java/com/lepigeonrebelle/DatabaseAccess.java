package com.lepigeonrebelle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lepigeonrebelle.model.ExpenseCategory;
import com.lepigeonrebelle.model.ExpenseType;
import com.lepigeonrebelle.model.Group;
import com.lepigeonrebelle.model.GroupType;
import com.lepigeonrebelle.model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    /**
     * Private constructor to avoid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all Expense Types from the database.
     *
     * @return a List of quotes
     */
    public List<ExpenseType> getExpenseTypes() {
        List<ExpenseType> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM ExpenseType", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String wording = cursor.getString(1);

            list.add(new ExpenseType(id, wording));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    /**
     * Read all Expense Categories from the database.
     *
     * @return a List of quotes
     */
    public List<ExpenseCategory> getExpenseCategories() {
        List<ExpenseCategory> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM ExpenseCategory", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String wording = cursor.getString(1);
            String icon = cursor.getString(2);

            list.add(new ExpenseCategory(id, wording, icon));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    /**
     * Read all Group Types from the database.
     *
     * @return a List of quotes
     */
    public List<GroupType> getGroupTypes() {
        List<GroupType> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM GroupType", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String wording = cursor.getString(1);
            String icon = cursor.getString(2);

            list.add(new GroupType(id, wording, icon));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    /**
     * Inserts a user with a given name
     *
     * @param name user name
     * @return id of generated row
     */
    public long addUser(String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        return database.insert("User", null, values);
    }

    public void dropUser(int idUser) {
        String deleteQuery = "DELETE FROM User where idUser='" + idUser + "'";
        Log.d("query", deleteQuery);
        database.execSQL(deleteQuery);
    }

    public List<User> getUsers() {
        List<User> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM User", null);

        int idDefaultUser = getDefaultUser().getId();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);

            list.add(new User(id, name, idDefaultUser == id));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public long setDefaultUser(int newUserId, int currentUserId) {
        if (currentUserId == -1) {
            User oldUser = getDefaultUser();
            if (oldUser.getId() == -1) {
                // no user has been set as default yet. Insert.
                ContentValues values = new ContentValues();
                values.put("idDefaultUser", newUserId);
                return database.insert("DefaultUser", null, values);
            } else {
                currentUserId = oldUser.getId();
            }
        }
        ContentValues cv = new ContentValues();
        cv.put("idDefaultUser", newUserId);
        return database.update("DefaultUser", cv, "idDefaultUser=" + currentUserId, null);
    }

    public User getDefaultUser() {
        int idDefaultUser = -1;
        Cursor cursor = database.rawQuery("SELECT idDefaultUser FROM DefaultUser", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            idDefaultUser = cursor.getInt(cursor.getColumnIndex("idDefaultUser"));
            Cursor cursor2 = database.rawQuery("SELECT * FROM User WHERE idUser=" + idDefaultUser, null);
            if (cursor2.getCount() > 0) {
                cursor2.moveToFirst();
                User defaultUser = new User(cursor2.getInt(0), cursor2.getString(1), true);
                cursor.close();
                cursor2.close();
                return defaultUser;
            }
        }
        cursor.close();
        return new User(-1, "LePigeonRebelle", true);
    }

    public long createGroup(Group group) {
        ContentValues values = new ContentValues();
        values.put("name", group.getName());
        values.put("idType", group.getGroupType().getId());
        long newGroupId = database.insert("\"Group\"", null, values);

        if (newGroupId != -1) {
            // if group has been inserted successfully, add members
            for (User user : group.getGroupMembers()) {
                ContentValues valuesGM = new ContentValues();
                valuesGM.put("idUser", user.getId());
                valuesGM.put("idGroup", newGroupId);
                valuesGM.put("budget", 100);
                database.insert("User_Group", null, valuesGM);
            }
        }

        return newGroupId;
    }

}
