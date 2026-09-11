package com.example.memorypract.Controladores;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.memorypract.R;

public class AnimalesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_animales);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.btnBack).setOnClickListener(v -> {
            Intent intent = new Intent(AnimalesActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        findViewById(R.id.btnColores).setOnClickListener(v -> {
            Intent intent = new Intent(AnimalesActivity.this, ColoresActivity.class);
            startActivity(intent);
        });
    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        // La actividad está a punto de hacerse visible para el usuario
    }

    @Override
    protected void onPause() {
        super.onPause();
        // La actividad pierde el foco (por ejemplo, se abre un diálogo o se minimiza parcialmente)
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // La actividad va a ser destruida por completo (liberación de memoria)
    }*/
}