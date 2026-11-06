package com.example.memorypract.ui.controladores;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.memorypract.R;
import com.example.memorypract.utils.ThemeUtils;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class ConfiguracionActivity extends BaseActivity {

    private static final String TAG = "ConfiguracionActivity";

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
        Log.d(TAG, "onCreate: Inicializando ConfiguracionActivity");
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
        Log.d(TAG, "Inicializando vistas y cargando preferencias guardadas");
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

        Log.i(TAG, "Preferencias cargadas: TEXT_SIZE=" + selectedTextSize + ", MODO_OSCURO=" + modoOscuro + ", RECORDATORIOS=" + recordatorios);

        switchModoOscuro.setChecked(modoOscuro);
        switchRecordatorios.setChecked(recordatorios);

        updateTextSizeSelection(selectedTextSize);
        updateDarkModeIcon(modoOscuro);
    }

    private void setupListeners() {
        findViewById(R.id.btnBack).setOnClickListener(v -> {
            Log.i(TAG, "Botón Volver presionado en Configuración");
            finish();
        });

        btnTextPequeno.setOnClickListener(v -> updateTextSizeSelection("Pequeño"));
        btnTextMediano.setOnClickListener(v -> updateTextSizeSelection("Mediano"));
        btnTextGrande.setOnClickListener(v -> updateTextSizeSelection("Grande"));
        btnTextExtra.setOnClickListener(v -> updateTextSizeSelection("Extra"));

        switchModoOscuro.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.i(TAG, "Switch Modo Oscuro cambiado a: " + isChecked);
            updateDarkModeIcon(isChecked);
        });

        findViewById(R.id.btnGuardar).setOnClickListener(v -> {
            boolean modoOscuroVal = switchModoOscuro.isChecked();
            boolean recordatoriosVal = switchRecordatorios.isChecked();

            Log.i(TAG, "Guardando nuevas preferencias: TEXT_SIZE=" + selectedTextSize + ", MODO_OSCURO=" + modoOscuroVal + ", RECORDATORIOS=" + recordatoriosVal);

            SharedPreferences prefs = getSharedPreferences("MemoryAppPrefs", MODE_PRIVATE);
            prefs.edit()
                    .putString("TEXT_SIZE", selectedTextSize)
                    .putBoolean("MODO_OSCURO", modoOscuroVal)
                    .putBoolean("RECORDATORIOS", recordatoriosVal)
                    .apply();

            ThemeUtils.setDarkMode(modoOscuroVal);

            Toast.makeText(ConfiguracionActivity.this, "Configuración guardada y aplicada", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void updateDarkModeIcon(boolean isDarkMode) {
        Log.d(TAG, "Actualizando icono de modo oscuro a: " + isDarkMode);
        if (isDarkMode) {
            iconMoon.setImageResource(R.drawable.ic_sun);
            iconMoon.setBackgroundResource(R.drawable.bg_icon_yellow);
        } else {
            iconMoon.setImageResource(R.drawable.ic_moon);
            iconMoon.setBackgroundResource(R.drawable.bg_icon_purple);
        }
    }

    private void updateTextSizeSelection(String option) {
        Log.d(TAG, "Seleccionado tamaño de texto: " + option);
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
