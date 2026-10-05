package com.example.memorypract.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.memorypract.data.modelos.Usuario;

import java.util.List;

@Dao
public interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(Usuario usuario);

    @Update
    void actualizar(Usuario usuario);

    @Delete
    void eliminar(Usuario usuario);

    @Query("SELECT * FROM tabla_clientes WHERE correo_contacto = :correo LIMIT 1")
    Usuario obtenerPorCorreo(String correo);

    @Query("SELECT * FROM tabla_clientes")
    LiveData<List<Usuario>> obtenerTodos();
}
