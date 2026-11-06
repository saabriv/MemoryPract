package com.example.memorypract.ui.controladores;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.memorypract.R;

public class PantallaObservarActivity extends BaseActivity {

    private static final String TAG = "PantallaObservarActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Inicializando PantallaObservarActivity");
        EdgeToEdge.enable(this);
        setContentView(R.layout.pantallaobservar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Botón volver
        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                Log.i(TAG, "Botón Volver presionado. Finalizando PantallaObservarActivity");
                finish();
            });
        }

        // Botón Borrar (rosado)
        Button btnDelete = findViewById(R.id.btnDelete);
        if (btnDelete != null) {
            btnDelete.setOnClickListener(v -> {
                Log.d(TAG, "Botón Borrar presionado");
                // Acción de borrar
            });
        }

        // Botón Comprobar (violeta)
        Button btnCheck = findViewById(R.id.btnCheck);
        if (btnCheck != null) {
            btnCheck.setOnClickListener(v -> {
                Log.d(TAG, "Botón Comprobar presionado");
                // Acción de comprobar
            });
        }
    }
}
