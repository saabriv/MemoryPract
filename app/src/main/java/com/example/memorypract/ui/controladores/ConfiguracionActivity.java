package com.example.memorypract.ui.controladores;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.memorypract.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class ConfiguracionActivity extends AppCompatActivity {

    private TextView btnTextPequeno;
    private TextView btnTextMediano;
    private TextView btnTextGrande;
    private TextView btnTextExtra;
    private TextView tvTextPreview;

    private ImageView iconMoon;
    private SwitchMaterial switchModoOscuro;
    private SwitchMaterial switchRecordatorios;

    private String selectedTextSize = "Mediano";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_configuracion);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnTextPequeno = findViewById(R.id.btnTextPequeno);
        btnTextMediano = findViewById(R.id.btnTextMediano);
        btnTextGrande = findViewById(R.id.btnTextGrande);
        btnTextExtra = findViewById(R.id.btnTextExtra);
        tvTextPreview = findViewById(R.id.tvTextPreview);

        iconMoon = findViewById(R.id.iconMoon);
        switchModoOscuro = findViewById(R.id.switchModoOscuro);
        switchRecordatorios = findViewById(R.id.switchRecordatorios);

        SharedPreferences prefs = getSharedPreferences("MemoryAppPrefs", MODE_PRIVATE);
        selectedTextSize = prefs.getString("TEXT_SIZE", "Mediano");
        boolean modoOscuro = prefs.getBoolean("MODO_OSCURO", false);
        boolean recordatorios = prefs.getBoolean("RECORDATORIOS", true);

        switchModoOscuro.setChecked(modoOscuro);
        switchRecordatorios.setChecked(recordatorios);

        updateTextSizeSelection(selectedTextSize);
        updateDarkModeIcon(modoOscuro);
    }

    private void setupListeners() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        btnTextPequeno.setOnClickListener(v -> updateTextSizeSelection("Pequeño"));
        btnTextMediano.setOnClickListener(v -> updateTextSizeSelection("Mediano"));
        btnTextGrande.setOnClickListener(v -> updateTextSizeSelection("Grande"));
        btnTextExtra.setOnClickListener(v -> updateTextSizeSelection("Extra"));

        switchModoOscuro.setOnCheckedChangeListener((buttonView, isChecked) -> updateDarkModeIcon(isChecked));

        findViewById(R.id.btnGuardar).setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("MemoryAppPrefs", MODE_PRIVATE);
            prefs.edit()
                    .putString("TEXT_SIZE", selectedTextSize)
                    .putBoolean("MODO_OSCURO", switchModoOscuro.isChecked())
                    .putBoolean("RECORDATORIOS", switchRecordatorios.isChecked())
                    .apply();

            Toast.makeText(ConfiguracionActivity.this, "Configuración guardada y aplicada", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void updateDarkModeIcon(boolean isDarkMode) {
        if (isDarkMode) {
            iconMoon.setImageResource(R.drawable.ic_sun);
            iconMoon.setBackgroundResource(R.drawable.bg_icon_yellow);
        } else {
            iconMoon.setImageResource(R.drawable.ic_moon);
            iconMoon.setBackgroundResource(R.drawable.bg_icon_purple);
        }
    }

    private void updateTextSizeSelection(String option) {
        selectedTextSize = option;

        btnTextPequeno.setBackgroundResource(R.drawable.bg_option_unselected);
        btnTextPequeno.setTextColor(Color.parseColor("#374151"));

        btnTextMediano.setBackgroundResource(R.drawable.bg_option_unselected);
        btnTextMediano.setTextColor(Color.parseColor("#374151"));

        btnTextGrande.setBackgroundResource(R.drawable.bg_option_unselected);
        btnTextGrande.setTextColor(Color.parseColor("#374151"));

        btnTextExtra.setBackgroundResource(R.drawable.bg_option_unselected);
        btnTextExtra.setTextColor(Color.parseColor("#374151"));

        switch (option) {
            case "Pequeño":
                btnTextPequeno.setBackgroundResource(R.drawable.bg_option_selected);
                btnTextPequeno.setTextColor(Color.parseColor("#EC407A"));
                tvTextPreview.setTextSize(13);
                break;
            case "Grande":
                btnTextGrande.setBackgroundResource(R.drawable.bg_option_selected);
                btnTextGrande.setTextColor(Color.parseColor("#EC407A"));
                tvTextPreview.setTextSize(20);
                break;
            case "Extra":
                btnTextExtra.setBackgroundResource(R.drawable.bg_option_selected);
                btnTextExtra.setTextColor(Color.parseColor("#EC407A"));
                tvTextPreview.setTextSize(24);
                break;
            case "Mediano":
            default:
                btnTextMediano.setBackgroundResource(R.drawable.bg_option_selected);
                btnTextMediano.setTextColor(Color.parseColor("#EC407A"));
                tvTextPreview.setTextSize(16);
                break;
        }
    }
}
