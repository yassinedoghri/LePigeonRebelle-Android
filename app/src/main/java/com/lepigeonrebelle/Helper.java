package com.lepigeonrebelle;

import android.content.Context;

import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.lepigeonrebelle.models.Debt;
import com.lepigeonrebelle.models.ExpenseCategory;
import com.lepigeonrebelle.models.GroupType;
import com.lepigeonrebelle.models.User;
import com.lepigeonrebelle.models.UserGroup;

import java.util.List;

public class Helper {


    public static Boolean isFriendInMembers(List<UserGroup> members, User friend) {
        for (UserGroup member : members) {
            if (member.getUser().getId() == friend.getId()) {
                return true;
            }
        }
        return false;
    }

    public static Boolean isFriendAlreadyOwing(List<Debt> debts, User friend) {
        for (Debt debt : debts) {
            if (debt.getUserOwing().getId() == friend.getId()) {
                return true;
            }
        }
        return false;
    }

    public static Boolean isItemInList(List<Integer> list, MaterialSimpleListItem item) {
        for (Integer i : list) {
            if (i == item.getId()) {
                return true;
            }
        }
        return false;
    }

    public static GroupType getGroupTypeById(List<GroupType> groupTypes, int id) {
        for (GroupType groupType : groupTypes) {
            if (groupType.getId() == id) {
                return groupType;
            }
        }
        return null;
    }

    public static ExpenseCategory getExpenseCategoryById(List<ExpenseCategory> expenseCategories, int id) {
        for (ExpenseCategory expenseCategory : expenseCategories) {
            if (expenseCategory.getId() == id) {
                return expenseCategory;
            }
        }
        return null;
    }

    public static int getDrawableId(Context context, String ImageName) {
        return context.getResources().getIdentifier(ImageName, "drawable", context.getPackageName());
    }
}
