package com.example.memorypract.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.memorypract.data.local.AppDatabase;
import com.example.memorypract.data.local.UsuarioDao;
import com.example.memorypract.data.modelos.Usuario;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsuarioRepository {

    private final UsuarioDao usuarioDao;
    private final LiveData<List<Usuario>> todosLosUsuarios;
    private final ExecutorService executorService;

    public interface OnUsuarioCallback {
        void onResult(Usuario usuario);
    }

    public interface OnInsertCallback {
        void onResult(long id);
    }

    public UsuarioRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        usuarioDao = db.usuarioDao();
        todosLosUsuarios = usuarioDao.obtenerTodos();
        executorService = Executors.newFixedThreadPool(2);
    }

    public LiveData<List<Usuario>> getTodosLosUsuarios() {
        return todosLosUsuarios;
    }

    public void registrarUsuario(Usuario usuario, OnInsertCallback callback) {
        executorService.execute(() -> {
            long id = usuarioDao.insertar(usuario);
            if (callback != null) {
                callback.onResult(id);
            }
        });
    }

    public void buscarPorCorreo(String correo, OnUsuarioCallback callback) {
        executorService.execute(() -> {
            Usuario usuario = usuarioDao.obtenerPorCorreo(correo);
            if (callback != null) {
                callback.onResult(usuario);
            }
        });
    }
}
