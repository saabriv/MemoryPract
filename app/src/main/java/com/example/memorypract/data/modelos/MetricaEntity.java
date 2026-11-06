package com.example.memorypract.data.modelos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabla_metricas")
public class MetricaEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "usuario_correo")
    private String usuarioCorreo;

    @ColumnInfo(name = "categoria")
    private String categoria;

    @ColumnInfo(name = "aciertos")
    private int aciertos;

    @ColumnInfo(name = "fallos")
    private int fallos;

    @ColumnInfo(name = "fecha_timestamp")
    private long fechaTimestamp;

    public MetricaEntity() {}

    public MetricaEntity(String usuarioCorreo, String categoria, int aciertos, int fallos, long fechaTimestamp) {
        this.usuarioCorreo = usuarioCorreo;
        this.categoria = categoria;
        this.aciertos = aciertos;
        this.fallos = fallos;
        this.fechaTimestamp = fechaTimestamp;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsuarioCorreo() { return usuarioCorreo; }
    public void setUsuarioCorreo(String usuarioCorreo) { this.usuarioCorreo = usuarioCorreo; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getAciertos() { return aciertos; }
    public void setAciertos(int aciertos) { this.aciertos = aciertos; }

    public int getFallos() { return fallos; }
    public void setFallos(int fallos) { this.fallos = fallos; }

    public long getFechaTimestamp() { return fechaTimestamp; }
    public void setFechaTimestamp(long fechaTimestamp) { this.fechaTimestamp = fechaTimestamp; }
}
