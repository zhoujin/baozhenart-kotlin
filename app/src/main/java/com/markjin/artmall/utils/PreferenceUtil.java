package com.markjin.artmall.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.markjin.artmall.AppContext;
import com.markjin.artmall.db.bean.User;

/**
 * Preference
 */
public class PreferenceUtil {

    public static final String PREFERENCE_NAME = "mark";
    public static final String PREFERENCE_DEVICE = "mark_Device";

    public static void putString(String key, String value) {
        SharedPreferences settings = AppContext.Companion.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void putString(String key, String value, String preference_name) {
        SharedPreferences settings = AppContext.Companion.getInstance().getSharedPreferences(preference_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String key) {
        SharedPreferences settings = AppContext.Companion.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, "");
    }

    public static String getString(String key, String perference_name) {
        SharedPreferences settings = AppContext.Companion.getInstance().getSharedPreferences(perference_name, Context.MODE_PRIVATE);
        return settings.getString(key, "");
    }

    public static void putInt(String key, int value) {
        SharedPreferences settings = AppContext.Companion.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void putInt(String key, int value, String perference_name) {
        SharedPreferences settings = AppContext.Companion.getInstance().getSharedPreferences(perference_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(String key) {
        return getInt(key, -1);
    }

    public static int getInt(String key, String perference_name) {
        SharedPreferences settings = AppContext.Companion.getInstance().getSharedPreferences(perference_name, Context.MODE_PRIVATE);
        return settings.getInt(key, -1);
    }

    public static int getInt(String key, int defaultValue) {
        SharedPreferences settings = AppContext.Companion.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }

    public static void putLong(String key, long value) {
        SharedPreferences settings = AppContext.Companion.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLong(String key) {
        return getLong(key, -1);
    }

    public static long getLong(String key, long defaultValue) {
        SharedPreferences settings = AppContext.Companion.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }

    public static void putFloat(String key, float value) {
        SharedPreferences settings = AppContext.Companion.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static float getFloat(String key) {
        return getFloat(key, -1);
    }

    public static float getFloat(String key, float defaultValue) {
        SharedPreferences settings = AppContext.Companion.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getFloat(key, defaultValue);
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences settings = AppContext.Companion.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences settings = AppContext.Companion.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }

    public static User getUserInfo() {
        User user = new User();
        SharedPreferences settings = AppContext.Companion.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        user.gender = settings.getString("gender", "0");
        user.user_name = settings.getString("user_name", "");
        user.email = settings.getString("email", "");
        user.user_id = settings.getString("user_id", "");
        user.bind_phone = settings.getString("bind_phone", "");
        return user;
    }

    public static void saveUser(User user) {
        SharedPreferences settings = AppContext.Companion.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("gender", user.gender);
        editor.putString("user_name", user.user_name);
        editor.putString("user_id", user.user_id);
        editor.putString("email", user.email);
        editor.putString("bind_phone", user.bind_phone);
        editor.commit();
    }

    /**
     * 判断区分是否登录
     *
     * @return true:登录，false:未登录
     */
    public static boolean isLogin() {
        SharedPreferences settings = AppContext.Companion.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (settings.contains("user_id") && !TextUtils.isEmpty(settings.getString("user_id", ""))) {
            return true;
        }
        return false;
    }

    public static void clearUser() {
        SharedPreferences settings = AppContext.Companion.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }
}
