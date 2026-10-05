package com.example.memorypract.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.memorypract.data.modelos.Usuario;
import com.example.memorypract.data.repository.UsuarioRepository;

public class LoginViewModel extends AndroidViewModel {

    private final UsuarioRepository repository;

    private final MutableLiveData<String> _loginResult = new MutableLiveData<>();
    public LiveData<String> getLoginResult() { return _loginResult; }

    private final MutableLiveData<Boolean> _isSuccess = new MutableLiveData<>();
    public LiveData<Boolean> getIsSuccess() { return _isSuccess; }

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new UsuarioRepository(application);
    }

    public void iniciarSesionOCrear(String nombre, String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            _loginResult.postValue("Por favor ingresa un correo válido");
            _isSuccess.postValue(false);
            return;
        }

        String correoLimpio = correo.trim();
        String nombreLimpio = (nombre != null && !nombre.trim().isEmpty()) ? nombre.trim() : "Usuario";

        repository.buscarPorCorreo(correoLimpio, usuarioExistente -> {
            if (usuarioExistente != null) {
                _loginResult.postValue("¡Bienvenido de nuevo, " + usuarioExistente.getNombre() + "!");
                _isSuccess.postValue(true);
            } else {
                Usuario nuevoUsuario = new Usuario(nombreLimpio, correoLimpio);
                repository.registrarUsuario(nuevoUsuario, id -> {
                    _loginResult.postValue("Usuario registrado exitosamente");
                    _isSuccess.postValue(true);
                });
            }
        });
    }
}
