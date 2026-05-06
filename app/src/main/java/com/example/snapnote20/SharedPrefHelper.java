package com.example.snapnote20;

import android.content.Context;
import android.content.SharedPreferences;

// Helper class to manage all SharedPreferences operations
public class SharedPrefHelper {

    private static final String PREF_NAME = "SnapNotePrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FULL_NAME = "fullName";
    private static final String KEY_DARK_MODE = "darkMode";
    private static final String KEY_PROFILE_IMAGE = "profileImage";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPrefHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Save login status
    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Save username
    public void setUsername(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    // Get username
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "User");
    }

    // Save email
    public void setEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    // Get email
    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    // Save full name
    public void setFullName(String fullName) {
        editor.putString(KEY_FULL_NAME, fullName);
        editor.apply();
    }

    // Get full name
    public String getFullName() {
        return sharedPreferences.getString(KEY_FULL_NAME, "");
    }

    // Save dark mode preference
    public void setDarkMode(boolean isDarkMode) {
        editor.putBoolean(KEY_DARK_MODE, isDarkMode);
        editor.apply();
    }

    // Get dark mode preference
    public boolean isDarkMode() {
        return sharedPreferences.getBoolean(KEY_DARK_MODE, false);
    }

    // Save profile image path
    public void setProfileImage(String path) {
        editor.putString(KEY_PROFILE_IMAGE, path);
        editor.apply();
    }

    // Get profile image path
    public String getProfileImage() {
        return sharedPreferences.getString(KEY_PROFILE_IMAGE, "");
    }

    // Logout: clear login status
    public void logout() {
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.apply();
    }

    // Clear all preferences (full reset)
    public void clearAll() {
        editor.clear();
        editor.apply();
    }
}
