package com.example.memorypract.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.memorypract.data.local.AppDatabase;
import com.example.memorypract.data.local.MetricaDao;
import com.example.memorypract.data.modelos.MetricaEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MetricaRepository {

    private static final String TAG = "MetricaRepository";

    private final MetricaDao metricaDao;
    private final ExecutorService executor;

    public interface MetricaCallback<T> {
        void onResult(T result);
    }

    public MetricaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        metricaDao = db.metricaDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public void registrarAcierto(String usuarioCorreo, String categoria, int aciertos, int fallos) {
        executor.execute(() -> {
            String correoValido = (usuarioCorreo != null && !usuarioCorreo.trim().isEmpty()) ? usuarioCorreo.trim() : "invitado@memory.com";
            MetricaEntity entity = new MetricaEntity(
                    correoValido,
                    categoria,
                    aciertos,
                    fallos,
                    System.currentTimeMillis()
            );
            long id = metricaDao.insertarMetrica(entity);
            Log.i(TAG, "Registrada métrica en SQLite id=" + id + ", correo=" + correoValido + ", cat=" + categoria + ", aciertos=" + aciertos);
        });
    }

    public LiveData<List<MetricaEntity>> obtenerPorUsuarioLiveData(String usuarioCorreo) {
        String correoValido = (usuarioCorreo != null && !usuarioCorreo.trim().isEmpty()) ? usuarioCorreo.trim() : "invitado@memory.com";
        return metricaDao.obtenerPorUsuarioLiveData(correoValido);
    }

    public void cargarMetricasSemana(String usuarioCorreo, long desdeMs, long hastaMs, MetricaCallback<MetricaCalculada> callback) {
        executor.execute(() -> {
            String correoValido = (usuarioCorreo != null && !usuarioCorreo.trim().isEmpty()) ? usuarioCorreo.trim() : "invitado@memory.com";
            int series = metricaDao.obtenerSeriesPorFecha(correoValido, desdeMs, hastaMs);
            int colores = metricaDao.obtenerAciertosPorCategoriaYFecha(correoValido, "COLORES", desdeMs, hastaMs);
            int animales = metricaDao.obtenerAciertosPorCategoriaYFecha(correoValido, "ANIMALES", desdeMs, hastaMs);
            int objetos = metricaDao.obtenerAciertosPorCategoriaYFecha(correoValido, "OBJETOS", desdeMs, hastaMs);
            int aciertosTotales = metricaDao.obtenerAciertosTotalesPorFecha(correoValido, desdeMs, hastaMs);

            MetricaCalculada res = new MetricaCalculada(series, colores, animales, objetos, aciertosTotales);
            if (callback != null) {
                callback.onResult(res);
            }
        });
    }

    public void borrarMetricasPorUsuario(String usuarioCorreo, Runnable onComplete) {
        executor.execute(() -> {
            String correoValido = (usuarioCorreo != null && !usuarioCorreo.trim().isEmpty()) ? usuarioCorreo.trim() : "invitado@memory.com";
            metricaDao.borrarPorUsuario(correoValido);
            Log.w(TAG, "Borradas métricas de SQLite para usuario: " + correoValido);
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    public static class MetricaCalculada {
        public final int series;
        public final int colores;
        public final int animales;
        public final int objetos;
        public final int aciertosTotales;

        public MetricaCalculada(int series, int colores, int animales, int objetos, int aciertosTotales) {
            this.series = series;
            this.colores = colores;
            this.animales = animales;
            this.objetos = objetos;
            this.aciertosTotales = aciertosTotales;
        }
    }
}
