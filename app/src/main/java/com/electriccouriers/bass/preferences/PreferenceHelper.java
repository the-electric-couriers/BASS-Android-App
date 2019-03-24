package com.electriccouriers.bass.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.electriccouriers.bass.data.Globals;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Thomas Hopstaken on 28/09/18
 */
public class PreferenceHelper {

    public static void save(SharedPreferences preferences, String key, String value) {
        SharedPreferences.Editor editor = getEditor(preferences);
        editor.putString(key, encrypt(value)).apply();
    }

    public static void save(Context context, String key, String value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(key, encrypt(value)).apply();
    }

    public static void save(SharedPreferences preferences, String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(preferences);
        editor.putBoolean(key, value).apply();
    }

    public static void save(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(key, value).apply();
    }

    public static String read(SharedPreferences preferences, String key, String defaultReturn) {
        String passEncrypted = preferences.getString(key, encrypt(defaultReturn));
        return decrypt(passEncrypted);
    }

    public static String read(Context context, String key, String defaultReturn) {
        SharedPreferences preferences = getPreferences(context);
        String passEncrypted = preferences.getString(key, encrypt(defaultReturn));
        return decrypt(passEncrypted);
    }

    public static String read(Context context, String key) {
        SharedPreferences preferences = getPreferences(context);
        String passEncrypted = preferences.getString(key, encrypt(""));
        return decrypt(passEncrypted);
    }

    public static Boolean read(SharedPreferences preferences, String key, Boolean defaultReturn) {
        return preferences.getBoolean(key, defaultReturn);
    }

    public static Boolean read(Context context, String key, Boolean defaultReturn) {
        SharedPreferences preferences = getPreferences(context);
        return preferences.getBoolean(key, defaultReturn);
    }

    public static void remove(SharedPreferences preferences, String key) {
        SharedPreferences.Editor editor = getEditor(preferences);
        editor.remove(key);
        editor.commit();
    }

    public static void remove(Context context, String key) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.remove(key);
        editor.commit();
    }

    public static void clearAll(SharedPreferences preferences) {
        SharedPreferences.Editor editor = getEditor(preferences);
        editor.clear();
        editor.commit();
    }

    public static void clearAll(Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.clear();
        editor.commit();
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(Globals.DEFAULT_PREFERENCE_SET, MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences preferences = getPreferences(context);
        return preferences.edit();
    }
    private static SharedPreferences.Editor getEditor(SharedPreferences sharedPreferences) {
        return sharedPreferences.edit();
    }

    private static String encrypt(String input) {
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    private static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }
}
