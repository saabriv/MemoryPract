package com.example.memorypract.ui.controladores;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.memorypract.R;
import com.example.memorypract.ui.viewmodels.LoginViewModel;

public class login extends AppCompatActivity {

    private LoginViewModel viewModel;
    private EditText etNombre;
    private EditText etCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);

        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            String nombre = etNombre.getText() != null ? etNombre.getText().toString() : "";
            String correo = etCorreo.getText() != null ? etCorreo.getText().toString() : "";
            viewModel.iniciarSesionOCrear(nombre, correo);
        });

        viewModel.getLoginResult().observe(this, mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getIsSuccess().observe(this, isSuccess -> {
            if (Boolean.TRUE.equals(isSuccess)) {
                Intent intent = new Intent(login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
