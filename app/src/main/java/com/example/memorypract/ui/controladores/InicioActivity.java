package com.example.memorypract.ui.controladores;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;

import com.example.memorypract.R;

public class InicioActivity extends AppCompatActivity {

    private static final String TAG = "InicioActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Inicializando InicioActivity");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnColores).setOnClickListener(v -> {
            Log.i(TAG, "Clic en categoría Colores. Navegando a ColoresActivity");
            Intent intent = new Intent(InicioActivity.this, ColoresActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnAnimales).setOnClickListener(v -> {
            Log.i(TAG, "Clic en categoría Animales. Navegando a AnimalesActivity");
            Intent intent = new Intent(InicioActivity.this, AnimalesActivity.class);
            startActivity(intent);
        });
    }
}
