package com.example.memorypract.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

public class FontScaleUtils {

    public static float getFontScaleForOption(String option) {
        if (option == null) return 1.0f;
        switch (option) {
            case "Pequeño":
                return 0.85f;
            case "Grande":
                return 1.15f;
            case "Extra":
                return 1.30f;
            case "Mediano":
            default:
                return 1.0f;
        }
    }

    public static Context applyFontScale(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MemoryAppPrefs", Context.MODE_PRIVATE);
        if (!prefs.contains("TEXT_SIZE")) {
            prefs.edit().putString("TEXT_SIZE", "Mediano").apply();
        }
        String option = prefs.getString("TEXT_SIZE", "Mediano");
        float fontScale = getFontScaleForOption(option);

        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.fontScale = fontScale;
        return context.createConfigurationContext(config);
    }
}
