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
    public static final String MAC_ADDRESS_NAME = "mac_address_name";
    public static final String MAC_ADDRESS_KEY = "mac_address_key";
    public static final String RECEIVE_SERVICE_NAME = "receive_service_name";
    public static final String RECEIVE_SERVICE_KEY = "receive_service_key";
    public static final String RECEIVE_CHARACTER_NAME = "receive_character_name";
    public static final String RECEIVE_CHARACTER_KEY = "receive_character_key";
    public static final String SEND_SERVICE_NAME = "send_service_name";
    public static final String SEND_SERVICE_KEY = "send_service_key";
    public static final String SEND_CHARACTER_NAME = "send_character_name";
    public static final String SEND_CHARACTER_KEY = "send_character_key";
    public static final String CLICK_ITEM_POSITION_NAME = "click_item_position_name";
    public static final String CLICK_ITEM_POSITION_KEY = "click_item_position_key";
    public static final String LAST_PLAY_POSITION_NAME = "last_play_position_name";
    public static final String LAST_PLAY_POSITION_KEY = "last_play_position_key";


    public static void saveLastPlayPosition(Context context, int position) {
        SharedPreferenceUtils.saveSharedPreference(context, LAST_PLAY_POSITION_NAME,
                LAST_PLAY_POSITION_KEY, position);
    }

    public static int getLastPlayPosition(Context context) {
        return SharedPreferenceUtils.getSharedPreferenceInt(context, LAST_PLAY_POSITION_NAME,
                LAST_PLAY_POSITION_KEY, -1);
    }

    public static void saveClickPosition(Context context, int position) {
        SharedPreferenceUtils.saveSharedPreference(context, CLICK_ITEM_POSITION_NAME,
                CLICK_ITEM_POSITION_KEY, position);
    }

    public static int getClickPosition(Context context) {
        return SharedPreferenceUtils.getSharedPreferenceInt(context, CLICK_ITEM_POSITION_NAME,
                CLICK_ITEM_POSITION_KEY, 0);
    }

    public static void saveMacAddress(Context context, String macAddress) {
        SharedPreferenceUtils.saveSharedPreference(context, MAC_ADDRESS_NAME,
                MAC_ADDRESS_KEY, macAddress);
    }

    public static String getMacAddress(Context context) {
        return SharedPreferenceUtils.getSharedPreferenceString(context, MAC_ADDRESS_NAME,
                MAC_ADDRESS_KEY);
    }

    public static void saveSendService(Context context, String serviceUUID) {
        SharedPreferenceUtils.saveSharedPreference(context, SEND_SERVICE_NAME,
                SEND_SERVICE_KEY, serviceUUID);
    }

    public static String getSendService(Context context) {
        return SharedPreferenceUtils.getSharedPreferenceString(context, SEND_SERVICE_NAME,
                SEND_SERVICE_KEY);
    }

    public static void saveSendCharacter(Context context, String characterUUID) {
        SharedPreferenceUtils.saveSharedPreference(context, SEND_CHARACTER_NAME,
                SEND_CHARACTER_KEY, characterUUID);
    }

    public static String getSendCharacter(Context context) {
        return SharedPreferenceUtils.getSharedPreferenceString(context, SEND_CHARACTER_NAME,
                SEND_CHARACTER_KEY);
    }

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
