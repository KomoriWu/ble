package com.example.txtledbluetooth.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;

/**
 * Created by KomoriWu
 * on 2017-04-21.
 */

public class SharedPreferenceUtils {
    public static final String AUDIO_PROMPTS_MODEL_NAME = "audio_prompts_model_name";
    public static final String AUDIO_PROMPTS_MODEL_KEY = "audio_prompts_model_key";

    public static void saveAudioPromptsModel(Context context, String model) {
        SharedPreferenceUtils.saveSharedPreference(context, AUDIO_PROMPTS_MODEL_NAME,
                AUDIO_PROMPTS_MODEL_KEY, model);
    }

    public static String getAudioPromptsModel(Context context) {
        return SharedPreferenceUtils.getSharedPreferenceString(context, AUDIO_PROMPTS_MODEL_NAME,
                AUDIO_PROMPTS_MODEL_KEY, Utils.AUDIO_PROMPTS_DEFAULT_MODEL);
    }

    public static boolean saveSharedPreference(Context context, String name, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static boolean saveSharedPreference(Context context, String name, String key, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static boolean saveSharedPreference(Context context, String name, String key, long value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public static boolean saveSharedPreference(Context context, String name, String key, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static String getSharedPreferenceString(Context context, String name, String key) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return savedPreference.getString(key, null);
    }

    public static String getSharedPreferenceString(Context context, String name, String key, String defaultValue) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return savedPreference.getString(key, defaultValue);
    }


    public static int getSharedPreferenceInt(Context context, String name, String key) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return savedPreference.getInt(key, -1);
    }

    public static int getSharedPreferenceInt(Context context, String name, String key, int defaultValue) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return savedPreference.getInt(key, defaultValue);
    }

    public static long getSharedPreferenceLong(Context context, String name, String key) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        try {
            return savedPreference.getLong(key, -1);
        } catch (ClassCastException e) {
            return -1;
        }
    }

    public static long getSharedPreferenceLong(Context context, String name, String key, long defaultValue) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        try {
            return savedPreference.getLong(key, defaultValue);
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public static boolean getSharedPreferenceBoolean(Context context, String name, String key) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return savedPreference.getBoolean(key, false);
    }

    public static boolean getSharedPreferenceBoolean(Context context, String name, String key, boolean defaultValue) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return savedPreference.getBoolean(key, defaultValue);
    }

    public static boolean removeSharedPreference(Context context, String name, String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.remove(key);
        return editor.commit();
    }

    public static boolean existSharedPreferenceKey(Context context, String name, String key) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return savedPreference.contains(key);
    }

    public static int getSharedPreferenceSize(Context context, String name) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return savedPreference.getAll().size();
    }

    public static Map<String, ?> getSharedPreferenceAll(Context context, String name) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return savedPreference.getAll();
    }

    public static void setSharedPreferencesFlag(Context context, String title, boolean content) {
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pre.edit();
        editor.putBoolean(title, content);
        editor.apply();
    }
}
