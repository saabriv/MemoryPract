package com.example.memorypract.data.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.memorypract.data.local.AppDatabase;
import com.example.memorypract.data.local.UsuarioDao;
import com.example.memorypract.data.modelos.Usuario;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsuarioRepository {

    private static final String TAG = "UsuarioRepository";

    private final UsuarioDao usuarioDao;
    private final LiveData<List<Usuario>> todosLosUsuarios;
    private final ExecutorService executorService;
    private final Handler mainHandler;

    public interface OnUsuarioCallback {
        void onResult(Usuario usuario);
    }

    public interface OnInsertCallback {
        void onResult(long id);
    }

    public UsuarioRepository(Application application) {
        Log.d(TAG, "Inicializando UsuarioRepository");
        AppDatabase db = AppDatabase.getDatabase(application);
        usuarioDao = db.usuarioDao();
        todosLosUsuarios = usuarioDao.obtenerTodos();
        executorService = Executors.newFixedThreadPool(2);
        mainHandler = new Handler(Looper.getMainLooper());
        Log.i(TAG, "UsuarioRepository inicializado correctamente");
    }

    public LiveData<List<Usuario>> getTodosLosUsuarios() {
        Log.d(TAG, "Solicitando LiveData de todos los usuarios");
        return todosLosUsuarios;
    }

    public void registrarUsuario(Usuario usuario, OnInsertCallback callback) {
        Log.d(TAG, "Iniciando registro de nuevo usuario: " + (usuario != null ? usuario.getCorreo() : "null"));
        executorService.execute(() -> {
            try {
                long id = usuarioDao.insertar(usuario);
                Log.i(TAG, "Usuario insertado en base de datos con ID=" + id);
                if (callback != null) {
                    mainHandler.post(() -> callback.onResult(id));
                }
            } catch (Exception e) {
                Log.e(TAG, "Excepción al registrar usuario en la base de datos", e);
                if (callback != null) {
                    mainHandler.post(() -> callback.onResult(-1)); // Indica fallo de inserción
                }
            }
        });
    }

    public void buscarPorCorreo(String correo, OnUsuarioCallback callback) {
        Log.d(TAG, "Iniciando búsqueda de usuario en segundo plano para el correo: " + correo);
        executorService.execute(() -> {
            try {
                Usuario usuario = usuarioDao.obtenerPorCorreo(correo);
                if (usuario != null) {
                    Log.i(TAG, "Usuario encontrado exitosamente: ID=" + usuario.getId() + ", Nombre=" + usuario.getNombre());
                } else {
                    Log.w(TAG, "No se encontró ningún usuario registrado con el correo: " + correo);
                }
                if (callback != null) {
                    mainHandler.post(() -> callback.onResult(usuario));
                }
            } catch (Exception e) {
                Log.e(TAG, "Excepción al consultar usuario en la base de datos por correo: " + correo, e);
                if (callback != null) {
                    mainHandler.post(() -> callback.onResult(null)); // Retorna null en caso de excepción
                }
            }
        });
    }

    public void cerrarEjecutor() {
        if (executorService != null && !executorService.isShutdown()) {
            Log.d(TAG, "Cerrando ExecutorService de UsuarioRepository");
            executorService.shutdownNow();
        }
    }
}
