package com.lepigeonrebelle;

import android.app.Application;

import com.lepigeonrebelle.models.Group;
import com.lepigeonrebelle.models.User;

import java.util.List;

public class MyApplication extends Application {

    private User defaultUser;
    private Group currentGroup;
    private List<User> currentGroupUsers;

    public User getDefaultUser() {
        return defaultUser;
    }

    public void setDefaultUser(User defaultUser) {
        this.defaultUser = defaultUser;
    }

    public Group getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(Group currentGroup) {
        this.currentGroup = currentGroup;
    }

    public List<User> getCurrentGroupUsers() {
        return currentGroupUsers;
    }

    public void setCurrentGroupUsers(List<User> currentGroupUsers) {
        this.currentGroupUsers = currentGroupUsers;
    }
}
