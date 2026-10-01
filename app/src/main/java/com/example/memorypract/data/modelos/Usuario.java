package com.example.memorypract.data.modelos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabla_clientes")
public class Usuario {
    @PrimaryKey(autoGenerate = true)
    private int id;


    // 3. LAS COLUMNAS
    // Obligamos a que la columna en SQLite se llame distinto a la variable de Java.
    // Esto nos salva la vida si mañana queremos cambiar el nombre de la variable en Java sin romper la base de datos.
    @ColumnInfo(name = "nombre_completo")
    private String nombre;

    @ColumnInfo(name = "correo_contacto")
    private String correo;

    // ==============================================================
    // LOS CONSTRUCTORES (La regla de oro de Room)
    // ==============================================================

    // Constructor A: El que usa Room.
    // El motor necesita un constructor vacío para instanciar la clase y luego usar los "setters" para rellenarla.
    public Usuario() {
    }

    // Constructor B: El que usamos nosotros en la Actividad/ViewModel.
    // Como no sabemos el "id" todavía (porque la BD lo va a generar), le pasamos solo nombre y correo.
    // Le ponemos @Ignore para decirle a Room: "¡Eh! Este constructor es mío, tú no lo mires que te confundes".
    @Ignore
    public Usuario(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
    }

    // ==============================================================
    // GETTERS Y SETTERS (La llave de acceso)
    // Room necesita absolutamente de todos ellos para poder leer y escribir los atributos privados.
    // ==============================================================

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}