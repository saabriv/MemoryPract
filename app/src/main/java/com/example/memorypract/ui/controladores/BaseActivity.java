package com.example.memorypract.ui.controladores;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memorypract.utils.FontScaleUtils;

public class BaseActivity extends AppCompatActivity {

    private float currentAppliedFontScale = -1f;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(FontScaleUtils.applyFontScale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        com.example.memorypract.utils.ThemeUtils.applyTheme(this);
        super.onCreate(savedInstanceState);
        currentAppliedFontScale = getResources().getConfiguration().fontScale;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("MemoryAppPrefs", MODE_PRIVATE);
        String option = prefs.getString("TEXT_SIZE", "Mediano");
        float targetFontScale = FontScaleUtils.getFontScaleForOption(option);

        if (currentAppliedFontScale > 0 && Math.abs(currentAppliedFontScale - targetFontScale) > 0.01f) {
            recreate();
        }
    }
}
