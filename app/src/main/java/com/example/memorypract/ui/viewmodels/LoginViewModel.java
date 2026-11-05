package com.example.memorypract.ui.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.memorypract.data.modelos.Usuario;
import com.example.memorypract.data.repository.UsuarioRepository;

public class LoginViewModel extends AndroidViewModel {

    private static final String TAG = "LoginViewModel";

    private final UsuarioRepository repository;

    private final MutableLiveData<String> _loginResult = new MutableLiveData<>();
    public LiveData<String> getLoginResult() { return _loginResult; }

    private final MutableLiveData<Boolean> _isSuccess = new MutableLiveData<>();
    public LiveData<Boolean> getIsSuccess() { return _isSuccess; }

    public enum Estado {
        CARGANDO,
        EXITO,
        ERROR_CORREO_INVALIDO
    }

    private final MutableLiveData<Estado> _estado = new MutableLiveData<>();
    public LiveData<Estado> getEstado() {
        return _estado;
    }

    public LoginViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "Creando LoginViewModel");
        repository = new UsuarioRepository(application);
    }

    public void iniciarSesionOCrear(String nombre, String correo) {
        Log.d(TAG, "Solicitando inicio de sesión o creación de usuario para correo=" + correo);
        if (correo == null || correo.trim().isEmpty()) {
            Log.w(TAG, "Intento de inicio de sesión fallido: Correo vacío o nulo");
            _loginResult.postValue("Por favor ingresa un correo válido");
            _isSuccess.postValue(false);
            _estado.postValue(Estado.ERROR_CORREO_INVALIDO);
            return;
        }

        _estado.postValue(Estado.CARGANDO);

        String correoLimpio = correo.trim();
        String nombreLimpio = (nombre != null && !nombre.trim().isEmpty()) ? nombre.trim() : "Usuario";

        Log.i(TAG, "Buscando usuario existente con correo: " + correoLimpio);
        repository.buscarPorCorreo(correoLimpio, usuarioExistente -> {
            if (usuarioExistente != null) {
                Log.i(TAG, "Inicio de sesión exitoso para usuario existente: " + usuarioExistente.getNombre());
                _loginResult.setValue("¡Bienvenido de nuevo, " + usuarioExistente.getNombre() + "!");
                _isSuccess.setValue(true);
                _estado.setValue(Estado.EXITO);
            } else {
                Log.i(TAG, "No existe usuario con correo " + correoLimpio + ". Registrando nuevo usuario: " + nombreLimpio);
                Usuario nuevoUsuario = new Usuario(nombreLimpio, correoLimpio);
                repository.registrarUsuario(nuevoUsuario, id -> {
                    if (id > 0) {
                        Log.i(TAG, "Nuevo usuario registrado exitosamente con ID=" + id);
                        _loginResult.setValue("Usuario registrado exitosamente");
                        _isSuccess.setValue(true);
                        _estado.setValue(Estado.EXITO);
                    } else {
                        Log.e(TAG, "Error al registrar usuario en la base de datos Room. ID retornado: " + id);
                        _loginResult.setValue("Error de base de datos al registrar usuario");
                        _isSuccess.setValue(false);
                        _estado.setValue(Estado.ERROR_CORREO_INVALIDO);
                    }
                });
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: Liberando recursos y cerrando ejecutores de UsuarioRepository");
        if (repository != null) {
            repository.cerrarEjecutor();
        }
    }
}
