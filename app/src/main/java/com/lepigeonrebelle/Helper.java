package com.lepigeonrebelle;

import android.content.Context;

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

    public static GroupType getGroupTypeById(List<GroupType> groupTypes, int id) {
        for (GroupType groupType : groupTypes) {
            // Set default group type
            if (groupType.getId() == id) {
                return groupType;
            }
        }
        return null;
    }

    public static int getDrawableId(Context context, String ImageName) {
        return context.getResources().getIdentifier(ImageName, "drawable", context.getPackageName());
    }
}
