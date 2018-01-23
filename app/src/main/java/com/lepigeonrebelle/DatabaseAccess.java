package com.lepigeonrebelle;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.ColumnArg;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.lepigeonrebelle.models.Debt;
import com.lepigeonrebelle.models.Expense;
import com.lepigeonrebelle.models.ExpenseCategory;
import com.lepigeonrebelle.models.ExpenseType;
import com.lepigeonrebelle.models.Group;
import com.lepigeonrebelle.models.GroupType;
import com.lepigeonrebelle.models.User;
import com.lepigeonrebelle.models.UserGroup;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private DatabaseHelper helper;
    private static DatabaseAccess instance;

    Dao<Debt, Integer> debtDao = null;
    Dao<Expense, Integer> expenseDao = null;
    Dao<ExpenseCategory, Integer> expenseCategoryDao = null;
    Dao<ExpenseType, Integer> expenseTypeDao = null;
    Dao<Group, Integer> groupDao = null;
    Dao<GroupType, Integer> groupTypeDao = null;
    Dao<User, Integer> userDao = null;
    Dao<UserGroup, Integer> userGroupDao = null;

    /**
     * Private constructor to avoid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.helper = new DatabaseHelper(context);
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
     * Close the database connection.
     */
    public void close() {
        if (instance != null) {
            this.helper.close();
        }
    }

    /**
     * Read all Expense Types from the database.
     *
     * @return a List of quotes
     */
    public List<ExpenseType> getExpenseTypes() {
        List<ExpenseType> list = new ArrayList<>();
        try {
            expenseTypeDao = this.helper.getExpenseTypeDao();
            list = expenseTypeDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Read all Expense Categories from the database.
     *
     * @return a List of quotes
     */
    public List<ExpenseCategory> getExpenseCategories() {
        List<ExpenseCategory> list = new ArrayList<>();
        try {
            expenseCategoryDao = helper.getExpenseCategoryDao();
            list = expenseCategoryDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Read all Group Types from the database.
     *
     * @return a List of quotes
     */
    public List<GroupType> getGroupTypes() {
        List<GroupType> list = new ArrayList<>();
        try {
            groupTypeDao = helper.getGroupTypeDao();
            list = groupTypeDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Inserts a user with a given name
     *
     * @param name user name
     * @return id of generated row
     */
    public User newUser(String name) {
        try {
            userDao = helper.getUserDao();
            User user = new User();
            user.setName(name);
            userDao.create(user);
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads all Users from the database.
     *
     * @return List<User>
     */
    public List<User> getUsers() {
        List<User> list = new ArrayList<>();
        try {
            userDao = helper.getUserDao();
            list = userDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Reads all Users from the database.
     *
     * @return List<User>
     */
    public List<User> getFriends() {
        List<User> list = new ArrayList<>();
        try {
            userDao = helper.getUserDao();
            list = userDao.queryBuilder()
                    .where()
                    .eq(User.FIELD_NAME_IS_DEFAULT_USER, 0)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void setDefaultUser(User newDefaultUser) {
        try {
            userDao = helper.getUserDao();
            List<User> users = getUsers();
            newDefaultUser.setDefaultUser(1);
            userDao.update(newDefaultUser);
            for (User user : users) {
                if (user.isDefaultUser() == 1) {
                    userDao.update(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getDefaultUser() {
        try {
            userDao = helper.getUserDao();
            QueryBuilder<User, Integer> queryBuilder = userDao.queryBuilder();
            Where<User, Integer> where = queryBuilder.where();
            where.eq(User.FIELD_NAME_IS_DEFAULT_USER, 1);
            return userDao.queryForFirst(queryBuilder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserById(int id) {
        try {
            userDao = helper.getUserDao();
            return userDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Group createGroup(Group group) {
        try {
            groupDao = helper.getGroupDao();
            groupDao.create(group);

            userGroupDao = helper.getUserGroupDao();
            int index = 0;
            for (UserGroup member : group.getGroupMembers()) {
                member.setGroup(group);
                userGroupDao.create(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return group;
    }

    public List<Group> getGroups() {
        List<Group> list = new ArrayList<>();
        try {
            groupDao = helper.getGroupDao();
            list = groupDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Group getGroupById(Integer id) {
        try {
            groupDao = helper.getGroupDao();
            return groupDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<UserGroup> getGroupMembers(Group group) {
        List<UserGroup> list = new ArrayList<>();
        try {
            userGroupDao = helper.getUserGroupDao();
            list = userGroupDao.queryBuilder()
                    .where()
                    .eq(UserGroup.FIELD_NAME_GROUP_ID, group.getId())
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<User> getGroupUsers(Group group) {
        List<User> list = new ArrayList<>();
        try {
            userDao = helper.getUserDao();
            userGroupDao = helper.getUserGroupDao();
            List<UserGroup> members = userGroupDao.queryBuilder()
                    .where()
                    .eq(UserGroup.FIELD_NAME_GROUP_ID, group.getId())
                    .query();
            for (UserGroup member : members) {
                list.add(this.getUserById(member.getUser().getId()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Expense> getGroupExpenses(Group group) {
        List<Expense> list = new ArrayList<>();
        try {
            expenseDao = helper.getExpenseDao();
            list = expenseDao.queryBuilder()
                    .where()
                    .eq(Expense.FIELD_NAME_GROUP, group.getId())
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Expense createExpense(Expense expense) {
        try {
            expenseDao = helper.getExpenseDao();
            expenseDao.create(expense);

            debtDao = helper.getDebtDao();
            for (Debt debt : expense.getDebts()) {
                debt.setExpense(expense);
                debtDao.create(debt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expense;
    }

    public List<Debt> getExpenseDebts(Expense expense) {
        List<Debt> list = new ArrayList<>();
        try {
            debtDao = helper.getDebtDao();
            list = debtDao.queryBuilder()
                    .where()
                    .eq(Debt.FIELD_NAME_EXPENSE, expense.getId())
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Debt> getGroupDebts(Group group) {
        List<Debt> debtslist = new ArrayList<>();
        List<Expense> list = new ArrayList<>();
        list = getGroupExpenses(group);
        try {
            for (Expense expense : list) {
                debtDao = helper.getDebtDao();
                QueryBuilder<Debt, Integer> queryBuilder = debtDao.queryBuilder();
                Where<Debt, Integer> where = queryBuilder.where();
                where.ne(Debt.FIELD_NAME_ID_USER_OWING, new ColumnArg(Debt.FIELD_NAME_ID_USER_PAYING));
                where.and();
                where.eq(Debt.FIELD_NAME_EXPENSE, expense.getId());
                debtslist.addAll(debtDao.query(queryBuilder.prepare()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return debtslist;
    }

    public List<Debt> getAllGroupDebts(Group group) {
        List<Debt> debtslist = new ArrayList<>();
        List<Expense> list = new ArrayList<>();
        list = getGroupExpenses(group);
        try {
            for (Expense expense : list) {
                debtDao = helper.getDebtDao();
                QueryBuilder<Debt, Integer> queryBuilder = debtDao.queryBuilder();
                Where<Debt, Integer> where = queryBuilder.where();
                where.eq(Debt.FIELD_NAME_EXPENSE, expense.getId());
                debtslist.addAll(debtDao.query(queryBuilder.prepare()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return debtslist;
    }

    public List<Debt> getDebts() {
        List<Debt> list = new ArrayList<>();
        try {
            debtDao = helper.getDebtDao();
            QueryBuilder<Debt, Integer> queryBuilder = debtDao.queryBuilder();
            Where<Debt, Integer> where = queryBuilder.where();
            where.ne(Debt.FIELD_NAME_ID_USER_OWING, new ColumnArg(Debt.FIELD_NAME_ID_USER_PAYING));
            return debtDao.query(queryBuilder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
