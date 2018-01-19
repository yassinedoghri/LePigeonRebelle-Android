package com.lepigeonrebelle;

import android.app.Application;

import com.lepigeonrebelle.model.User;

public class MyApplication extends Application {

    private User defaultUser;

    public User getDefaultUser() {
        return defaultUser;
    }

    public void setDefaultUser(User defaultUser) {
        this.defaultUser = defaultUser;
    }
}
