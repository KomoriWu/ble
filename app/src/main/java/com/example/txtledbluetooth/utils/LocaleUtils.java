package com.example.txtledbluetooth.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LocaleUtils {

    private static final String LANGUAGE_NAME = "language_name";
    private static final String LANGUAGE_KEY = "language_key";
    public static final String LANGUAGE_EN = "en";
    public static final String LANGUAGE_ZH = "zh";
    private static final int LANGUAGE_EN_INDEX = 0;
    private static final int LANGUAGE_ZH_INDEX = 1;


    public static void saveLanguage(Context context, int language) {
        SharedPreferenceUtils.saveSharedPreference(context, LANGUAGE_NAME, LANGUAGE_KEY, language);
    }

    public static int getLanguage(Context context) {
        return SharedPreferenceUtils.getSharedPreferenceInt(context, LANGUAGE_NAME, LANGUAGE_KEY,
                0);
    }

    public static void updateLocale(Context context) {
        String language = LANGUAGE_EN;
        int index = getLanguage(context);
        switch (index) {
            case LANGUAGE_EN_INDEX:
                language = LANGUAGE_EN;
                break;
            case LANGUAGE_ZH_INDEX:
                language = LANGUAGE_ZH;
                break;
        }

        if (index != 0 || !(language.equals(Locale.getDefault().getLanguage()))) {
            Resources res = context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = new Locale(language);
            res.updateConfiguration(conf, dm);
        }
    }

    public static void setAutoLanguage(Context context) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = Locale.getDefault();
        res.updateConfiguration(conf, dm);
    }

    public static void initLocale(Context context) {
        String language = Locale.getDefault().getLanguage().toLowerCase();
        int index = LANGUAGE_EN_INDEX;
        switch (language) {
            case LANGUAGE_EN:
                index = LANGUAGE_EN_INDEX;
                break;
            case LANGUAGE_ZH:
                index = LANGUAGE_ZH_INDEX;
                break;
        }

        LocaleUtils.saveLanguage(context, index);
    }

    public static int getCurrentLanguage(Context context) {
        int index = getLanguage(context);
        String language = Locale.getDefault().getLanguage().toLowerCase();

        if (index < 0) {
            if (language.startsWith(LANGUAGE_EN)) {
                index = LANGUAGE_EN_INDEX;
            } else if (language.startsWith(LANGUAGE_ZH)) {
                index = LANGUAGE_ZH_INDEX;
            } else {
                index = LANGUAGE_EN_INDEX;
            }
        }

        return index;
    }

    public static Locale getCurrentLocale(Context context) {
        int index = getLanguage(context);
        String language = Locale.getDefault().getLanguage().toLowerCase();

        if (index <= 0) {
            if (language.startsWith(LANGUAGE_EN)) {
                language = LANGUAGE_EN;
            }else if (language.startsWith(LANGUAGE_ZH)) {
                language = LANGUAGE_ZH;
            } else {
                language = LANGUAGE_EN;
            }
        } else {
            switch (index) {
                case LANGUAGE_EN_INDEX:
                    language = LANGUAGE_EN;
                    break;
                case LANGUAGE_ZH_INDEX:
                    language = LANGUAGE_ZH;
                    break;
            }
        }

        return new Locale(language);
    }
}
