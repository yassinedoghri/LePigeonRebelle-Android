package com.lepigeonrebelle;

import android.content.Context;

import com.lepigeonrebelle.model.GroupType;

import java.util.List;

public class Helper {

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
