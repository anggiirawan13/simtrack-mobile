package com.simple.tracking;

import android.content.Context;

public class AdminChecker {

    public static boolean isAdmin(Context context, int id) {
        PreferenceManager preferenceManager = new PreferenceManager(context);
        int userId = preferenceManager.getUserId();
        String role = preferenceManager.getUserRole();

        if (userId == id) return false;

        return role.equalsIgnoreCase("admin");
    }

}
