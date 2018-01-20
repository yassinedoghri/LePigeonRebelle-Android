package com.lepigeonrebelle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.DatabaseTableConfigUtil;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;
import com.lepigeonrebelle.models.Debt;
import com.lepigeonrebelle.models.Expense;
import com.lepigeonrebelle.models.ExpenseCategory;
import com.lepigeonrebelle.models.ExpenseType;
import com.lepigeonrebelle.models.Group;
import com.lepigeonrebelle.models.GroupType;
import com.lepigeonrebelle.models.User;
import com.lepigeonrebelle.models.UserGroup;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.sql.SQLException;

public class DatabaseHelper extends SQLiteAssetHelper {

    protected AndroidConnectionSource connectionSource = new AndroidConnectionSource(this);

    private static final String DATABASE_NAME = "lepigeonrebelle.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Debt, Integer> debtDao = null;
    private Dao<Expense, Integer> expenseDao = null;
    private Dao<ExpenseCategory, Integer> expenseCategoryDao = null;
    private Dao<ExpenseType, Integer> expenseTypeDao = null;
    private Dao<Group, Integer> groupDao = null;
    private Dao<GroupType, Integer> groupTypeDao = null;
    private Dao<User, Integer> userDao = null;
    private Dao<UserGroup, Integer> userGroupDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void close() {
        debtDao = null;
        expenseDao = null;
        expenseCategoryDao = null;
        expenseTypeDao = null;
        groupDao = null;
        groupTypeDao = null;
        userDao = null;
        userGroupDao = null;

        super.close();
    }

    /* Debt */
    public Dao<Debt, Integer> getDebtDao() throws SQLException {
        if (debtDao == null) {
            debtDao = getDao(Debt.class);
        }

        return debtDao;
    }

    /* Expense */
    public Dao<Expense, Integer> getExpenseDao() throws SQLException {
        if (expenseDao == null) {
            expenseDao = getDao(Expense.class);
        }

        return expenseDao;
    }

    /* ExpenseCategory */
    public Dao<ExpenseCategory, Integer> getExpenseCategoryDao() throws SQLException {
        if (expenseCategoryDao == null) {
            expenseCategoryDao = getDao(ExpenseCategory.class);
        }

        return expenseCategoryDao;
    }

    /* ExpenseType */
    public Dao<ExpenseType, Integer> getExpenseTypeDao() throws SQLException {
        if (expenseTypeDao == null) {
            expenseTypeDao = getDao(ExpenseType.class);
        }

        return expenseTypeDao;
    }

    /* Group */
    public Dao<Group, Integer> getGroupDao() throws SQLException {
        if (groupDao == null) {
            groupDao = getDao(Group.class);
        }

        return groupDao;
    }

    /* GroupType */
    public Dao<GroupType, Integer> getGroupTypeDao() throws SQLException {
        if (groupTypeDao == null) {
            groupTypeDao = getDao(GroupType.class);
        }

        return groupTypeDao;
    }

    /* User */
    public Dao<User, Integer> getUserDao() throws SQLException {
        if (userDao == null) {
            userDao = getDao(User.class);
        }

        return userDao;
    }

    /* User */
    public Dao<UserGroup, Integer> getUserGroupDao() throws SQLException {
        if (userGroupDao == null) {
            userGroupDao = getDao(UserGroup.class);
        }

        return userGroupDao;
    }

    /**
     * Lifted off of https://github.com/j256/ormlite-examples/blob/master/android/HelloAndroidNoHelper/src/com/example/hellonohelper/DatabaseHelper.java
     *
     * @param clazz
     * @param <D>
     * @param <T>
     * @return
     * @throws java.sql.SQLException
     */
    private <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws SQLException {
        // lookup the dao, possibly invoking the cached database config
        Dao<T, ?> dao = DaoManager.lookupDao(connectionSource, clazz);
        if (dao == null) {
            // try to use our new reflection magic
            DatabaseTableConfig<T> tableConfig = DatabaseTableConfigUtil.fromClass(connectionSource, clazz);
            if (tableConfig == null) {
                /**
                 * TODO: we have to do this to get to see if they are using the deprecated annotations like
                 * {@link DatabaseFieldSimple}.
                 */
                dao = (Dao<T, ?>) DaoManager.createDao(connectionSource, clazz);
            } else {
                dao = (Dao<T, ?>) DaoManager.createDao(connectionSource, tableConfig);
            }
        }

        @SuppressWarnings("unchecked")
        D castDao = (D) dao;
        return castDao;
    }
}
