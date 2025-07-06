package com.example.mosque_management_system.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mosque_management_system.models.Mosque;

public class PreferenceHelper {

    private static final String USER_PREFS = "UserPrefs";
    private static final String MOSQUE_PREFS = "MosquePrefs";

    // ------------------- USER PREFS -------------------
    public static void saveToken(Context context, String token) {
        SharedPreferences prefs = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        prefs.edit().putString("jwt_token", token).apply();
    }

    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        return prefs.getString("jwt_token", null);
    }

    public static void saveFullName(Context context, String name) {
        SharedPreferences prefs = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        prefs.edit().putString("fullName", name).apply();
    }

    public static String getFullName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        return prefs.getString("fullName", "");
    }

    public static String getMosqueId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MOSQUE_PREFS, Context.MODE_PRIVATE);
        return prefs.getString("mosqueId", null);
    }

    public static String getMosqueName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MOSQUE_PREFS, Context.MODE_PRIVATE);
        return prefs.getString("mosqueName", "");
    }

    public static void clearMosquePrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MOSQUE_PREFS, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    public static void clearUserPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
    public static void saveIsAdmin(Context context, boolean isAdmin) {
        SharedPreferences prefs = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        prefs.edit().putBoolean("isAdmin", isAdmin).apply();
    }

    public static boolean isAdmin(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        return prefs.getBoolean("isAdmin", false);
    }
    public static void setLoggedIn(Context context, boolean isLoggedIn) {
        SharedPreferences prefs = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        prefs.edit().putBoolean("isLoggedIn", isLoggedIn).apply();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        return prefs.getBoolean("isLoggedIn", false);
    }
    public static void setMosqueInfo(Context context, String mosqueId, String mosqueName) {
        SharedPreferences prefs = context.getSharedPreferences("MosquePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("mosqueId", mosqueId);
        editor.putString("mosqueName", mosqueName);
        editor.apply();
    }
    // Save full admin mosque details
    public static void saveMyMosqueDetails(Context context, Mosque mosque) {
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("myMosqueId", mosque.getId());
        editor.putString("myMosqueName", mosque.getName());
        editor.putString("myMosqueAddress", mosque.getAddress());
        editor.putString("myMosqueVillage", mosque.getVillage());
        editor.putString("myMosqueUnion", mosque.getUnionName());
        editor.putString("myMosqueUpazila", mosque.getUpazila());
        editor.putString("myMosqueZilla", mosque.getZilla());
        editor.putString("myMosqueImamName", mosque.getImamName());
        editor.apply();
    }



}
