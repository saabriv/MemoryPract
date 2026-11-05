package com.example.memorypract.ui.controladores;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    private static final String TAG = "login";

    private LoginViewModel viewModel;
    private EditText etEmail;
    private View btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Inicializando pantalla de login");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        etEmail = findViewById(R.id.etEmail);
        btnLogin = findViewById(R.id.btnLogin);

        View btnSettings = findViewById(R.id.btnSettings);
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                Log.i(TAG, "Clic en botón de Configuración desde Login. Navegando a ConfiguracionActivity");
                Intent intent = new Intent(login.this, ConfiguracionActivity.class);
                startActivity(intent);
            });
        }

        btnLogin.setOnClickListener(v -> {
            String correo = etEmail.getText() != null ? etEmail.getText().toString() : "";
            Log.i(TAG, "Clic en botón Login con correo: " + correo);
            viewModel.iniciarSesionOCrear(correo, correo);
        });

        viewModel.getLoginResult().observe(this, mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Log.d(TAG, "Resultado de Login recibido: " + mensaje);
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getIsSuccess().observe(this, isSuccess -> {
            if (Boolean.TRUE.equals(isSuccess)) {
                Log.i(TAG, "Login exitoso. Navegando a MainActivity");
                Intent intent = new Intent(login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        viewModel.getEstado().observe(this, estado -> {
            if (estado == null) return;
            Log.d(TAG, "Estado de Login cambiado: " + estado);
            switch (estado) {
                case CARGANDO:
                    btnLogin.setEnabled(false);
                    break;
                case EXITO:
                    btnLogin.setEnabled(true);
                    Log.i(TAG, "Estado EXITO: Redirigiendo a MainActivity");
                    Intent intent = new Intent(login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case ERROR_CORREO_INVALIDO:
                    btnLogin.setEnabled(true);
                    Log.w(TAG, "Estado ERROR_CORREO_INVALIDO: Correo no válido");
                    Toast.makeText(this, "Ingrese un correo válido", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}
