package com.example.memorypract.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtils {

    public static void applyTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MemoryAppPrefs", Context.MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("MODO_OSCURO", false);
        setDarkMode(isDarkMode);
    }

    public static void setDarkMode(boolean enableDarkMode) {
        if (enableDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
