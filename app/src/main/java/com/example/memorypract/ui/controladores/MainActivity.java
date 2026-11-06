package com.example.memorypract.ui.controladores;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.memorypract.R;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Inicializando MainActivity");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnSettings).setOnClickListener(v -> {
            Log.i(TAG, "Navegando a pantalla de Configuración (ConfiguracionActivity)");
            Intent intent = new Intent(MainActivity.this, ConfiguracionActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnColores).setOnClickListener(v -> {
            Log.i(TAG, "Seleccionada categoría Colores. Navegando a ColoresActivity");
            Intent intent = new Intent(MainActivity.this, ColoresActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnAnimales).setOnClickListener(v -> {
            Log.i(TAG, "Seleccionada categoría Animales. Navegando a AnimalesActivity");
            Intent intent = new Intent(MainActivity.this, AnimalesActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnObjetos).setOnClickListener(v -> {
            Log.i(TAG, "Seleccionada categoría Objetos. Navegando a PantallaobjetosActivity");
            Intent intent = new Intent(MainActivity.this, PantallaobjetosActivity.class);
            startActivity(intent);
        });

        View btnSeguimiento = findViewById(R.id.btnSeguimiento);
        if (btnSeguimiento != null) {
            btnSeguimiento.setOnClickListener(v -> {
                Log.i(TAG, "Seleccionada categoría Seguimiento. Navegando a MetricasActivity");
                Intent intent = new Intent(MainActivity.this, MetricasActivity.class);
                startActivity(intent);
            });
        }
    }
}
