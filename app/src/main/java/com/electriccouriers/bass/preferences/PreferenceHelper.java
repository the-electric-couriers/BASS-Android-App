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

    /**
     * Save String via SharedPreferences object
     * @param preferences
     * @param key
     * @param value
     */
    public static void save(SharedPreferences preferences, String key, String value) {
        SharedPreferences.Editor editor = getEditor(preferences);
        editor.putString(key, encrypt(value)).apply();
    }

    /**
     * Save String via Context object
     * @param context
     * @param key
     * @param value
     */
    public static void save(Context context, String key, String value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(key, encrypt(value)).apply();
    }

    /**
     * Save Boolean via SharedPreferences object
     * @param preferences
     * @param key
     * @param value
     */
    public static void save(SharedPreferences preferences, String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(preferences);
        editor.putBoolean(key, value).apply();
    }

    /**
     * Save Boolean via Context object
     * @param context
     * @param key
     * @param value
     */
    public static void save(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(key, value).apply();
    }

    /**
     * Save int via SharedPreferences object
     * @param preferences
     * @param key
     * @param value
     */
    public static void save(SharedPreferences preferences, String key, int value) {
        SharedPreferences.Editor editor = getEditor(preferences);
        editor.putInt(key, value).apply();
    }

    /**
     * Save int via Context object
     * @param context
     * @param key
     * @param value
     */
    public static void save(Context context, String key, int value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt(key, value).apply();
    }



    /**
     * Get String via SharedPreferences object
     * @param preferences
     * @param key
     * @param defaultReturn
     * @return
     */
    public static String read(SharedPreferences preferences, String key, String defaultReturn) {
        String passEncrypted = preferences.getString(key, encrypt(defaultReturn));
        return decrypt(passEncrypted);
    }

    /**
     * Get String via Context object
     * @param context
     * @param key
     * @param defaultReturn
     * @return
     */
    public static String read(Context context, String key, String defaultReturn) {
        SharedPreferences preferences = getPreferences(context);
        return read(preferences, key, defaultReturn);
    }

    /**
     * Get String via Context object
     * @param context
     * @param key
     * @return
     */
    public static String read(Context context, String key) {
        SharedPreferences preferences = getPreferences(context);
        return read(preferences, key, "");
    }

    /**
     * Get Boolean via SharedPreferences object
     * @param preferences
     * @param key
     * @param defaultReturn
     * @return
     */
    public static Boolean read(SharedPreferences preferences, String key, Boolean defaultReturn) {
        return preferences.getBoolean(key, defaultReturn);
    }

    /**
     * Get Boolean via Context object
     * @param context
     * @param key
     * @param defaultReturn
     * @return
     */
    public static Boolean read(Context context, String key, Boolean defaultReturn) {
        SharedPreferences preferences = getPreferences(context);
        return preferences.getBoolean(key, defaultReturn);
    }

    /**
     * Get Int via SharedPreferences object
     * @param preferences
     * @param key
     * @param defaultReturn
     * @return
     */
    public static int read(SharedPreferences preferences, String key, int defaultReturn) {
        return preferences.getInt(key, defaultReturn);
    }

    /**
     * Get Int via Context object
     * @param context
     * @param key
     * @param defaultReturn
     * @return
     */
    public static int read(Context context, String key, int defaultReturn) {
        SharedPreferences preferences = getPreferences(context);
        return preferences.getInt(key, defaultReturn);
    }



    /**
     * Remove key via SharedPreferences object
     * @param preferences
     * @param key
     */
    public static void remove(SharedPreferences preferences, String key) {
        SharedPreferences.Editor editor = getEditor(preferences);
        editor.remove(key);
        editor.commit();
    }

    /**
     * Remove key via Context object
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.remove(key);
        editor.commit();
    }

    /**
     * Remove all keys via SharedPreferences object
     * @param preferences
     */
    public static void clearAll(SharedPreferences preferences) {
        SharedPreferences.Editor editor = getEditor(preferences);
        editor.clear();
        editor.commit();
    }

    /**
     * Remove all keys via Context object
     * @param context
     */
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
