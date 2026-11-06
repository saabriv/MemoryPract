package com.example.memorypract.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.memorypract.data.modelos.MetricaEntity;

import java.util.List;

@Dao
public interface MetricaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertarMetrica(MetricaEntity metrica);

    @Query("SELECT * FROM tabla_metricas WHERE usuario_correo = :correo ORDER BY fecha_timestamp DESC")
    LiveData<List<MetricaEntity>> obtenerPorUsuarioLiveData(String correo);

    @Query("SELECT COALESCE(SUM(aciertos), 0) FROM tabla_metricas WHERE usuario_correo = :correo AND categoria = :categoria AND fecha_timestamp >= :desdeTimestamp AND fecha_timestamp <= :hastaTimestamp")
    int obtenerAciertosPorCategoriaYFecha(String correo, String categoria, long desdeTimestamp, long hastaTimestamp);

    @Query("SELECT COUNT(*) FROM tabla_metricas WHERE usuario_correo = :correo AND categoria = 'SERIE_COMPLETA' AND fecha_timestamp >= :desdeTimestamp AND fecha_timestamp <= :hastaTimestamp")
    int obtenerSeriesPorFecha(String correo, long desdeTimestamp, long hastaTimestamp);

    @Query("SELECT COALESCE(SUM(aciertos), 0) FROM tabla_metricas WHERE usuario_correo = :correo AND fecha_timestamp >= :desdeTimestamp AND fecha_timestamp <= :hastaTimestamp")
    int obtenerAciertosTotalesPorFecha(String correo, long desdeTimestamp, long hastaTimestamp);

    @Query("DELETE FROM tabla_metricas WHERE usuario_correo = :correo")
    void borrarPorUsuario(String correo);
}
